package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novack.lotto_cc.core.designsystem.component.DrawnNumbers
import com.novack.lotto_cc.core.designsystem.theme.LotteryColors
import com.novack.lotto_cc.core.designsystem.theme.Spacing
import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import com.novack.lotto_cc.feature.drawresults.presentation.R
import com.novack.lotto_cc.feature.drawresults.presentation.format.JackpotText
import com.novack.lotto_cc.feature.drawresults.presentation.format.toDrawDateText
import com.novack.lotto_cc.feature.drawresults.presentation.format.toJackpotText
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawCardUiModel
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

private val CardElevation = 4.dp

/** Strip width at font scale 1. */
private val JackpotStripWidth = 56.dp

/** Above this font scale the strip stops growing; long jackpot values shrink instead. */
private const val MaxStripFontScale = 1.5f

/** Smallest size a long jackpot value ("53,3") may shrink to. */
private val MinJackpotFontSize = 10.sp

/** Horizontal run of the badge's slanted start edge. */
private val BadgeSlant = 8.dp

/** One lottery: jackpot strip on the left; name, last draw, numbers and next draw on the right. */
@Composable
internal fun DrawCard(card: DrawCardUiModel, modifier: Modifier = Modifier) {
    val colors = card.lottery.colors
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = CardElevation),
        modifier = modifier.fillMaxWidth(),
    ) {
        // Grows with the font, capped so the numbers keep their room; same width on every card.
        val stripWidth = JackpotStripWidth * LocalDensity.current.fontScale.coerceAtMost(MaxStripFontScale)
        // The strip is drawn behind the row at the row's measured height (no intrinsic sizing, which
        // underestimates wrapped FlowRows and clipped the card at large font sizes).
        Row(Modifier.drawBehind { drawStrip(colors.accentSheen, stripWidth.toPx()) }) {
            JackpotStrip(card.jackpot, colors.onAccent, Modifier.width(stripWidth).align(Alignment.Bottom))
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
                modifier = Modifier.weight(1f).padding(Spacing.m),
            ) {
                // Spiel 77 / SUPER 6 sit next to the title, and wrap under it when there's no room.
                FlowRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
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
                    Column(horizontalAlignment = Alignment.End) {
                        card.spiel77?.let { SupplementaryNumber(stringResource(R.string.spiel77), it) }
                        card.super6?.let { SupplementaryNumber(stringResource(R.string.super6), it) }
                    }
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

/** "Spiel 77: 7312244": grey label, bold number, like the mockup. Read digit by digit. */
@Composable
private fun SupplementaryNumber(label: String, number: String) {
    val description = "$label: ${number.toList().joinToString(" ")}"
    Text(
        modifier = Modifier.semantics { contentDescription = description },
        text = buildAnnotatedString {
            append("$label: ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                append(number)
            }
        },
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** The strip at the start edge, full height. Drawn in an inset so the brush is sized to the strip. */
private fun DrawScope.drawStrip(brush: Brush, width: Float) {
    val rest = size.width - width
    if (layoutDirection == LayoutDirection.Ltr) {
        inset(left = 0f, top = 0f, right = rest, bottom = 0f) { drawRect(brush) }
    } else {
        inset(left = rest, top = 0f, right = 0f, bottom = 0f) { drawRect(brush) }
    }
}

/** The next jackpot, shown at the bottom of the strip when it is known. */
@Composable
private fun JackpotStrip(jackpot: Money?, contentColor: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(Spacing.xs),
    ) {
        if (jackpot == null) return@Column
        val text = rememberJackpotText(jackpot)
        val description = stringResource(R.string.jackpot_description, text.fullAmount)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clearAndSetSemantics { contentDescription = description },
        ) {
            if (text.millions == null) {
                JackpotLine(text.fullAmount, contentColor, MaterialTheme.typography.labelMedium)
            } else {
                JackpotLine(text.millions, contentColor, MaterialTheme.typography.headlineSmall)
                JackpotLine(stringResource(R.string.jackpot_unit_millions), contentColor, MaterialTheme.typography.titleSmall)
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
        autoSize = TextAutoSize.StepBased(minFontSize = MinJackpotFontSize, maxFontSize = style.fontSize),
    )
}

@Composable
private fun NextDraw(time: Instant, colors: LotteryColors, modifier: Modifier = Modifier) {
    // Wraps the badge under the label when both don't fit (narrow screens, large font).
    FlowRow(
        itemVerticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s, Alignment.End),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
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
                // Start padding clears the slant, so wrapped text never sits on it.
                .padding(start = BadgeSlant + Spacing.xs, end = Spacing.s, top = Spacing.xxs, bottom = Spacing.xxs),
        )
    }
}

/** A badge with a slanted start edge, like the mockup; mirrored in right-to-left layouts. */
private object BadgeShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val slant = with(density) { BadgeSlant.toPx() }
        val path = if (layoutDirection == LayoutDirection.Ltr) {
            Path().apply {
                moveTo(slant, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
            }
        } else {
            Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width - slant, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
            }
        }
        return Outline.Generic(path.apply { close() })
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
