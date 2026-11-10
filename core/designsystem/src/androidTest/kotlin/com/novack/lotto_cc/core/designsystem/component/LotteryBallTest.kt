package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LotteryBallTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun hasBallStyle(style: BallStyle) = SemanticsMatcher.expectValue(BallStyleKey, style)

    private fun SemanticsNodeInteraction.bounds(): DpRect = assertIsDisplayed().getUnclippedBoundsInRoot()

    private fun setContent(fontScale: Float = 1f, content: @Composable () -> Unit) {
        composeRule.setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale)) {
                LottoTheme(content = content)
            }
        }
    }

    private fun assertAllBallsAre32dpAndVisible(count: Int) {
        val balls = composeRule.onAllNodes(SemanticsMatcher.keyIsDefined(BallStyleKey)).assertCountEquals(count)
        repeat(count) { index ->
            val bounds = balls[index].bounds()
            assertEquals("ball $index width", 32f, bounds.width.value, 0.5f)
            assertEquals("ball $index height", 32f, bounds.height.value, 0.5f)
        }
    }

    @Test
    fun lotto6aus49_rendersSixRegularBallsAndOneSuperzahl() {
        setContent { DrawnNumbers(listOf(4, 19, 24, 43, 44, 45), listOf(7), BallStyle.Superzahl) }

        assertAllBallsAre32dpAndVisible(count = 7)
        val regular = composeRule.onAllNodes(hasBallStyle(BallStyle.Regular)).assertCountEquals(6)
        listOf("4", "19", "24", "43", "44", "45").forEachIndexed { index, text -> regular[index].assertTextEquals(text) }
        composeRule.onAllNodes(hasBallStyle(BallStyle.Superzahl)).assertCountEquals(1)[0].assertTextEquals("7")
        composeRule.onAllNodes(hasBallStyle(BallStyle.Eurozahl)).assertCountEquals(0)
    }

    @Test
    fun eurojackpot_rendersFiveRegularBallsAndTwoEurozahlen() {
        setContent { DrawnNumbers(listOf(1, 12, 15, 31, 47), listOf(9, 10), BallStyle.Eurozahl) }

        assertAllBallsAre32dpAndVisible(count = 7)
        composeRule.onAllNodes(hasBallStyle(BallStyle.Regular)).assertCountEquals(5)
        val euro = composeRule.onAllNodes(hasBallStyle(BallStyle.Eurozahl)).assertCountEquals(2)
        euro[0].assertTextEquals("9")
        euro[1].assertTextEquals("10")
        composeRule.onAllNodes(hasBallStyle(BallStyle.Superzahl)).assertCountEquals(0)
    }

    @Test
    fun sameNumberInMainAndBonus_isRenderedTwiceWithDifferentStyles() {
        setContent { DrawnNumbers(listOf(7), listOf(7), BallStyle.Superzahl) }

        composeRule.onAllNodesWithText("7").assertCountEquals(2)
        composeRule.onAllNodes(hasBallStyle(BallStyle.Regular)).assertCountEquals(1)
        composeRule.onAllNodes(hasBallStyle(BallStyle.Superzahl)).assertCountEquals(1)
    }

    @Test
    fun bonusLabel_isAnnouncedForBonusBallsOnly() {
        setContent { DrawnNumbers(listOf(4), listOf(7), BallStyle.Superzahl, bonusLabel = "Superzahl") }

        composeRule.onNodeWithContentDescription("Superzahl 7").assertIsDisplayed()
        val regular = composeRule.onAllNodes(hasBallStyle(BallStyle.Regular))[0].fetchSemanticsNode()
        assertTrue(SemanticsProperties.ContentDescription !in regular.config)
    }

    @Test
    fun ball_growsWithLargeFontScale_butDoesNotFillItsParent() {
        setContent(fontScale = 2f) { LotteryBall(number = 42) }

        val bounds = composeRule.onNodeWithText("42").bounds()
        assertTrue("ball should grow beyond 32dp, was ${bounds.width}", bounds.width > 32.dp)
        assertTrue("ball should fit its number, was ${bounds.width}", bounds.width < 80.dp)
        assertEquals(bounds.width.value, bounds.height.value, 0.5f)
    }

    @Test
    fun row_wrapsOnNarrowScreensWithLargeFont_keepingEveryBallVisible() {
        setContent(fontScale = 2f) {
            Box(Modifier.width(200.dp)) {
                DrawnNumbers(listOf(1, 12, 15, 31, 47), listOf(9, 10), BallStyle.Eurozahl)
            }
        }

        val balls = composeRule.onAllNodes(SemanticsMatcher.keyIsDefined(BallStyleKey)).assertCountEquals(7)
        val bounds = (0 until 7).map { balls[it].bounds() }
        bounds.forEach { assertTrue("ball must stay inside the 200dp parent: $it", it.right <= 200.dp) }
        assertTrue("row should wrap onto a second line", bounds.map { it.top }.distinct().size > 1)
    }
}
