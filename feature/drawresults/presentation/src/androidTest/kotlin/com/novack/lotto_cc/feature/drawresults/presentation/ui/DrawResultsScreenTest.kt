package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme
import com.novack.lotto_cc.feature.drawresults.domain.DrawFixtures
import com.novack.lotto_cc.feature.drawresults.domain.model.NextDraw
import com.novack.lotto_cc.feature.drawresults.presentation.R
import com.novack.lotto_cc.feature.drawresults.presentation.format.toDrawDateText
import com.novack.lotto_cc.feature.drawresults.presentation.format.toJackpotText
import com.novack.lotto_cc.feature.drawresults.presentation.mapper.toUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawResultsUiState
import com.novack.lotto_cc.feature.drawresults.presentation.model.ErrorReason
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.time.ZoneId

@RunWith(AndroidJUnit4::class)
class DrawResultsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val locale = context.resources.configuration.locales[0]

    private fun string(@StringRes id: Int, vararg args: Any) = context.getString(id, *args)
    private fun Instant.text() = toDrawDateText(ZoneId.systemDefault(), locale)

    private val lotto = DrawFixtures.lotto6aus49()
    private val eurojackpot = DrawFixtures.eurojackpot(
        nextDraw = NextDraw(Instant.parse("2023-01-24T18:00:00Z"), jackpot = null),
    )

    private fun setContent(uiState: DrawResultsUiState, onRetry: () -> Unit = {}) = composeRule.setContent {
        LottoTheme { DrawResultsScreen(uiState, onRetry) }
    }

    private fun showContent() =
        setContent(DrawResultsUiState.Content(listOf(lotto.toUiModel(), eurojackpot.toUiModel())))

    @Test
    fun content_showsEachLotteryWithItsLastDrawDateAndNumbers() {
        showContent()

        composeRule.onNodeWithText(string(R.string.lottery_lotto6aus49)).assertIsDisplayed()
        composeRule.onNodeWithText(lotto.lastDrawTime.text()).assertIsDisplayed()
        composeRule.onNodeWithText("43").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("${string(R.string.bonus_superzahl)} 7").assertIsDisplayed()

        composeRule.onNodeWithText(string(R.string.lottery_eurojackpot)).assertIsDisplayed()
        composeRule.onNodeWithText(eurojackpot.lastDrawTime.text()).assertIsDisplayed()
        composeRule.onNodeWithContentDescription("${string(R.string.bonus_eurozahl)} 9").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("${string(R.string.bonus_eurozahl)} 10").assertIsDisplayed()
    }

    @Test
    fun content_showsTheNextDrawDateOfEachLottery() {
        showContent()

        composeRule.onAllNodesWithText(string(R.string.next_draw)).assertCountEquals(2)
        composeRule.onNodeWithText(lotto.nextDraw.time.text()).assertIsDisplayed()
        composeRule.onNodeWithText(eurojackpot.nextDraw.time.text()).assertIsDisplayed()
    }

    @Test
    fun jackpot_isShownWhenKnownAndHiddenWhenUnknown() {
        showContent()

        val jackpot = lotto.nextDraw.jackpot!!.toJackpotText(locale)
        composeRule.onNodeWithContentDescription(string(R.string.jackpot_description, jackpot.fullAmount))
            .assertIsDisplayed()
        composeRule.onAllNodesWithContentDescription(string(R.string.jackpot_description, ""), substring = true)
            .assertCountEquals(1)
    }

    @Test
    fun loading_showsAProgressIndicator() {
        setContent(DrawResultsUiState.Loading)

        composeRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun error_showsTheReasonAndRetries() {
        var retries = 0
        setContent(DrawResultsUiState.Error(ErrorReason.NoConnection), onRetry = { retries++ })

        composeRule.onNodeWithText(string(R.string.error_no_connection)).assertIsDisplayed()
        composeRule.onNodeWithText(string(R.string.retry)).performClick()

        assertEquals(1, retries)
    }
}
