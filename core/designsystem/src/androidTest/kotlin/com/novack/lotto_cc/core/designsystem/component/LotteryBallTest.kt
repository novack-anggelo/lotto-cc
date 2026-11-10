package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.Density
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

    @Test
    fun lotto6aus49_rendersSixRegularBallsAndOneSuperzahl() {
        composeRule.setContent {
            LottoTheme {
                DrawnNumbers(listOf(4, 19, 24, 43, 44, 45), listOf(7), BallStyle.Superzahl)
            }
        }

        val regular = composeRule.onAllNodes(hasBallStyle(BallStyle.Regular))
        regular.assertCountEquals(6)
        listOf("4", "19", "24", "43", "44", "45").forEachIndexed { index, text ->
            regular[index].assertTextEquals(text)
        }
        composeRule.onAllNodes(hasBallStyle(BallStyle.Superzahl)).assertCountEquals(1)[0].assertTextEquals("7")
        composeRule.onAllNodes(hasBallStyle(BallStyle.Eurozahl)).assertCountEquals(0)
    }

    @Test
    fun eurojackpot_rendersFiveRegularBallsAndTwoEurozahlen() {
        composeRule.setContent {
            LottoTheme {
                DrawnNumbers(listOf(1, 12, 15, 31, 47), listOf(9, 10), BallStyle.Eurozahl)
            }
        }

        composeRule.onAllNodes(hasBallStyle(BallStyle.Regular)).assertCountEquals(5)
        val euro = composeRule.onAllNodes(hasBallStyle(BallStyle.Eurozahl)).assertCountEquals(2)
        euro[0].assertTextEquals("9")
        euro[1].assertTextEquals("10")
        composeRule.onAllNodes(hasBallStyle(BallStyle.Superzahl)).assertCountEquals(0)
    }

    @Test
    fun sameNumberInMainAndBonus_isRenderedTwiceWithDifferentStyles() {
        composeRule.setContent {
            LottoTheme { DrawnNumbers(listOf(7), listOf(7), BallStyle.Superzahl) }
        }

        composeRule.onAllNodesWithText("7").assertCountEquals(2)
        composeRule.onAllNodes(hasBallStyle(BallStyle.Regular)).assertCountEquals(1)
        composeRule.onAllNodes(hasBallStyle(BallStyle.Superzahl)).assertCountEquals(1)
    }

    @Test
    fun ball_growsWithLargeFontScale_andStaysRound() {
        composeRule.setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale = 2f)) {
                LottoTheme { LotteryBall(number = 42) }
            }
        }

        val node = composeRule.onNodeWithText("42").assertIsDisplayed()
        val bounds = node.getUnclippedBoundsInRoot()
        assertTrue("ball should grow beyond 32dp, was ${bounds.width}", bounds.width > 32.dp)
        assertEquals(bounds.width.value, bounds.height.value, 0.5f)
        assertEquals(
            BallStyle.Regular,
            node.fetchSemanticsNode().config[BallStyleKey],
        )
        assertTrue(node.fetchSemanticsNode().config.contains(SemanticsProperties.Text))
    }
}
