package com.novack.lotto_cc.feature.drawresults.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novack.lotto_cc.core.designsystem.theme.BallColors
import com.novack.lotto_cc.core.designsystem.theme.LotteryColors
import com.novack.lotto_cc.core.designsystem.theme.Spacing
import com.novack.lotto_cc.core.domain.models.Money
import com.novack.lotto_cc.feature.drawresults.presentation.R
import com.novack.lotto_cc.feature.drawresults.presentation.formatters.toDrawDate
import com.novack.lotto_cc.feature.drawresults.presentation.formatters.toFullAmount
import com.novack.lotto_cc.feature.drawresults.presentation.formatters.toMillionsOrNull
import com.novack.lotto_cc.feature.drawresults.presentation.models.AdditionalGameUi
import com.novack.lotto_cc.feature.drawresults.presentation.models.DrawCardUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.models.Lottery
import java.util.Locale
import kotlin.time.Instant

private val JackpotStripWidth = 56.dp
private val BallSize = 28.dp
private val SuperNumberRing = 2.dp
private val NumbersRowBackground = Color(0xFFE0E0E0)
private val AdditionalNumbersBackground = Color(0xFF9E9E9E)

@Composable
fun DrawInfoCard(card: DrawCardUiModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = card.lottery.colors
    ElevatedCard(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            JackpotStrip(
                jackpot = card.jackpotValue,
                colors = colors,
                modifier = Modifier
                    .width(JackpotStripWidth)
                    .fillMaxHeight(),
            )
            Column(Modifier.weight(1f)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.s),
                    modifier = Modifier.padding(start = Spacing.m, top = Spacing.m, end = Spacing.m, bottom = Spacing.s),
                ) {
                    Row {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = stringResource(card.lottery.nameRes),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(Modifier.height(Spacing.xs))
                            Text(
                                text = rememberDrawDate(card.date),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        card.additionalGames?.let { AdditionalGames(it) }
                    }
                    WinningNumbers(card.numbers, card.additionalNumbers, card.lottery)
                }
                Spacer(Modifier.height(Spacing.xl))
                NextDraw(card.nextDrawDate, colors, Modifier.align(Alignment.End))
            }
        }
    }
}

@Composable
private fun JackpotStrip(jackpot: Money?, colors: LotteryColors, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(colors.accent)
            .padding(Spacing.xs),
    ) {
        if (jackpot == null) return@Column
        val locale = currentLocale()
        val millions = remember(jackpot, locale) { jackpot.toMillionsOrNull(locale) }
        if (millions != null) {
            JackpotLine(millions, colors.onAccent, MaterialTheme.typography.headlineSmall)
            JackpotLine(stringResource(R.string.jackpot_millions), colors.onAccent, MaterialTheme.typography.titleSmall)
        } else {
            val amount = remember(jackpot, locale) { jackpot.toFullAmount(locale) }
            JackpotLine(amount, colors.onAccent, MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun JackpotLine(text: String, color: Color, style: TextStyle) {
    Text(
        text = text,
        color = color,
        style = style,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        autoSize = TextAutoSize.StepBased(minFontSize = 8.sp, maxFontSize = style.fontSize),
    )
}

@Composable
private fun AdditionalGames(games: List<AdditionalGameUi>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        games.forEach { game ->
            Text(
                text = buildAnnotatedString {
                    append(stringResource(game.nameRes))
                    append(": ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(game.number)
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun WinningNumbers(
    numbers: List<Int>,
    additionalNumbers: List<Int>,
    lottery: Lottery,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(NumbersRowBackground, CircleShape)
            .padding(Spacing.xxs),
    ) {
        numbers.forEach { Ball(it) }
        when (lottery) {
            Lottery.Lotto6aus49 -> AdditionalNumbersRow(additionalNumbers)
            Lottery.EuroJackpot -> AdditionalNumbersRow(additionalNumbers)
        }
    }
}

@Composable
private fun AdditionalNumbersRow(additionalNumbers: List<Int>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
        modifier = Modifier
            .background(AdditionalNumbersBackground, CircleShape)
            .padding(Spacing.xxs),
    ) {
        additionalNumbers.forEach { Ball(it) }
    }
}

@Composable
private fun Ball(number: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(BallSize)
            .background(BallColors.RegularBackground, CircleShape),
    ) {
        Text(
            text = number.toString(),
            color = BallColors.Content,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun NextDraw(date: Instant, colors: LotteryColors, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.next_draw),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = rememberDrawDate(date),
            color = colors.onBadge,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(colors.badge, RoundedCornerShape(topStart = Spacing.s))
                .padding(horizontal = Spacing.s, vertical = Spacing.xs),
        )
    }
}

private val Lottery.colors: LotteryColors
    get() = when (this) {
        Lottery.Lotto6aus49 -> LotteryColors.Lotto6aus49
        Lottery.EuroJackpot -> LotteryColors.Eurojackpot
    }

@get:StringRes
private val Lottery.nameRes: Int
    get() = when (this) {
        Lottery.Lotto6aus49 -> R.string.lotto6aus49
        Lottery.EuroJackpot -> R.string.eurojackpot
    }

@get:StringRes
private val AdditionalGameUi.nameRes: Int
    get() = when (this) {
        is AdditionalGameUi.Spiel77 -> R.string.spiel77
        is AdditionalGameUi.Super6 -> R.string.super6
    }

@Composable
private fun currentLocale(): Locale = LocalConfiguration.current.locales[0]

@Composable
private fun rememberDrawDate(date: Instant): String {
    val locale = currentLocale()
    return remember(date, locale) { date.toDrawDate(locale) }
}
