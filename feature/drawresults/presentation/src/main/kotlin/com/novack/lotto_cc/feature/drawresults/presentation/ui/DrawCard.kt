package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novack.lotto_cc.core.designsystem.component.DrawnNumbers
import com.novack.lotto_cc.core.designsystem.theme.LotteryColors
import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import com.novack.lotto_cc.feature.drawresults.presentation.R
import com.novack.lotto_cc.feature.drawresults.presentation.format.JackpotText
import com.novack.lotto_cc.feature.drawresults.presentation.format.toDrawDateText
import com.novack.lotto_cc.feature.drawresults.presentation.format.toJackpotText
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawCardUiModel
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

/** One lottery: jackpot strip on the left; name, last draw, numbers and next draw on the right. */
@Composable
internal fun DrawCard(card: DrawCardUiModel, modifier: Modifier = Modifier) {
    val colors = card.lottery.colors
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            // Grows with the font (up to 1.5x, so the numbers keep their room); same width on every card.
            val stripWidth = 56.dp * LocalDensity.current.fontScale.coerceAtMost(1.5f)
            JackpotStrip(card.jackpot, colors, Modifier.width(stripWidth).fillMaxHeight())
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f).padding(12.dp),
            ) {
                Column {
                    Text(
                        text = stringResource(card.lottery.nameRes),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = rememberDrawDate(card.lastDrawTime),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                DrawnNumbers(
                    numbers = card.numbers,
                    bonusNumbers = card.bonusNumbers,
                    bonusLabel = stringResource(card.lottery.bonusLabelRes),
                )
                NextDraw(card.nextDrawTime, colors, Modifier.align(Alignment.End))
            }
        }
    }
}

/** The lottery's accent colour as a metal sheen, with the next jackpot at the bottom when it is known. */
@Composable
private fun JackpotStrip(jackpot: Money?, colors: LotteryColors, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(colors.accentSheen).padding(4.dp),
    ) {
        if (jackpot == null) return@Column
        val text = rememberJackpotText(jackpot)
        val description = stringResource(R.string.jackpot_description, text.fullAmount)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clearAndSetSemantics { contentDescription = description },
        ) {
            if (text.millions == null) {
                JackpotLine(text.fullAmount, colors.onAccent, MaterialTheme.typography.labelMedium)
            } else {
                JackpotLine(text.millions, colors.onAccent, MaterialTheme.typography.headlineSmall)
                JackpotLine(stringResource(R.string.jackpot_unit_millions), colors.onAccent, MaterialTheme.typography.titleSmall)
            }
        }
    }
}

/** One bold line of the strip; long values ("53,3") shrink to fit instead of wrapping. */
@Composable
private fun JackpotLine(text: String, color: Color, style: TextStyle) {
    Text(
        text = text,
        color = color,
        style = style,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        maxLines = 1,
        autoSize = TextAutoSize.StepBased(minFontSize = 10.sp, maxFontSize = style.fontSize),
    )
}

@Composable
private fun NextDraw(time: Instant, colors: LotteryColors, modifier: Modifier = Modifier) {
    // Wraps the badge under the label when both don't fit (narrow screens, large font).
    FlowRow(
        itemVerticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.next_draw),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = rememberDrawDate(time),
            color = colors.onBadge,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(colors.badge, BadgeShape)
                .padding(start = 12.dp, end = 6.dp, top = 2.dp, bottom = 2.dp),
        )
    }
}

/** A badge with a slanted left edge, like the mockup. The slant stays within the badge's start padding. */
private object BadgeShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val slant = with(density) { 8.dp.toPx() }
        return Outline.Generic(
            Path().apply {
                moveTo(slant, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            },
        )
    }
}

@Composable
private fun currentLocale(): Locale = LocalConfiguration.current.locales[0]

@Composable
private fun rememberDrawDate(time: Instant): String {
    val locale = currentLocale()
    return remember(time, locale) { time.toDrawDateText(ZoneId.systemDefault(), locale) }
}

@Composable
private fun rememberJackpotText(jackpot: Money): JackpotText {
    val locale = currentLocale()
    return remember(jackpot, locale) { jackpot.toJackpotText(locale) }
}
