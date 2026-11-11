package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
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

    /** A ball is a merged node whose text is a number. */
    private val isBall = SemanticsMatcher("is a ball") { node ->
        node.config.getOrNull(SemanticsProperties.Text)?.singleOrNull()?.text?.toIntOrNull() != null
    }
    private val inBonusGroup = hasAnyAncestor(hasTestTag(BonusNumbersTestTag))
    private val mainBalls get() = composeRule.onAllNodes(isBall and !inBonusGroup)
    private val bonusBalls get() = composeRule.onAllNodes(isBall and inBonusGroup)

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
        val balls = composeRule.onAllNodes(isBall).assertCountEquals(count)
        repeat(count) { index ->
            val bounds = balls[index].bounds()
            assertEquals("ball $index width", MinBallSize.value, bounds.width.value, 0.5f)
            assertEquals("ball $index height", MinBallSize.value, bounds.height.value, 0.5f)
        }
    }

    @Test
    fun lotto6aus49_rendersSixMainBallsAndTheSuperzahlInTheBonusGroup() {
        setContent { DrawnNumbers(listOf(4, 19, 24, 43, 44, 45), listOf(7)) }

        assertAllBallsAre32dpAndVisible(count = 7)
        val main = mainBalls.assertCountEquals(6)
        listOf("4", "19", "24", "43", "44", "45").forEachIndexed { index, text -> main[index].assertTextEquals(text) }
        bonusBalls.assertCountEquals(1)[0].assertTextEquals("7")
    }

    @Test
    fun eurojackpot_rendersFiveMainBallsAndTwoEurozahlenInTheBonusGroup() {
        setContent { DrawnNumbers(listOf(1, 12, 15, 31, 47), listOf(9, 10)) }

        assertAllBallsAre32dpAndVisible(count = 7)
        mainBalls.assertCountEquals(5)
        val euro = bonusBalls.assertCountEquals(2)
        euro[0].assertTextEquals("9")
        euro[1].assertTextEquals("10")
    }

    @Test
    fun sameNumberInMainAndBonus_isRenderedOnceInEachGroup() {
        setContent { DrawnNumbers(listOf(7), listOf(7)) }

        composeRule.onAllNodesWithText("7").assertCountEquals(2)
        mainBalls.assertCountEquals(1)
        bonusBalls.assertCountEquals(1)
    }

    @Test
    fun noBonusNumbers_rendersNoBonusGroup() {
        setContent { DrawnNumbers(listOf(1, 2, 3), emptyList()) }

        mainBalls.assertCountEquals(3)
        composeRule.onNodeWithTag(BonusNumbersTestTag).assertDoesNotExist()
    }

    @Test
    fun bonusLabel_isAnnouncedForBonusBallsOnly() {
        setContent { DrawnNumbers(listOf(4), listOf(7), bonusLabel = "Superzahl") }

        composeRule.onNodeWithContentDescription("Superzahl 7").assertIsDisplayed()
        val regular = mainBalls[0].fetchSemanticsNode()
        assertTrue(SemanticsProperties.ContentDescription !in regular.config)
    }

    @Test
    fun ball_growsWithLargeFontScale_butDoesNotFillItsParent() {
        setContent(fontScale = 2f) { LotteryBall(number = 42) }

        val bounds = composeRule.onNodeWithText("42").bounds()
        assertTrue("ball should grow beyond $MinBallSize, was ${bounds.width}", bounds.width > MinBallSize)
        assertTrue("ball should fit its number, was ${bounds.width}", bounds.width < 80.dp)
        assertEquals(bounds.width.value, bounds.height.value, 0.5f)
    }

    @Test
    fun row_wrapsOnNarrowScreensWithLargeFont_keepingEveryBallVisible() {
        setContent(fontScale = 2f) {
            Box(Modifier.width(200.dp)) {
                DrawnNumbers(listOf(1, 12, 15, 31, 47), listOf(9, 10))
            }
        }

        val balls = composeRule.onAllNodes(isBall).assertCountEquals(7)
        val bounds = (0 until 7).map { balls[it].bounds() }
        bounds.forEach { assertTrue("ball must stay inside the 200dp parent: $it", it.right <= 200.dp) }
        assertTrue("row should wrap onto a second line", bounds.map { it.top }.distinct().size > 1)
    }
}
