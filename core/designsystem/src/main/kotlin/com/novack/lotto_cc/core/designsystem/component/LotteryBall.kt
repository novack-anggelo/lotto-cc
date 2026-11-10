package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novack.lotto_cc.core.designsystem.theme.BallColors
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme

/** How a drawn number is rendered. Superzahl and Eurozahlen must look different from regular numbers. */
enum class BallStyle {
    /** Main numbers of any lottery. */
    Regular,

    /** The single LOTTO 6aus49 Superzahl: outlined ball. */
    Superzahl,

    /** The two Eurojackpot Eurozahlen: darker, bordered ball. */
    Eurozahl,
}

/** Exposes a ball's [BallStyle] to UI tests. */
val BallStyleKey = SemanticsPropertyKey<BallStyle>("BallStyle")
private var SemanticsPropertyReceiver.ballStyle by BallStyleKey

@Composable
fun LotteryBall(
    number: Int,
    modifier: Modifier = Modifier,
    style: BallStyle = BallStyle.Regular,
) {
    val (background, borderWidth) = when (style) {
        BallStyle.Regular -> BallColors.RegularBackground to 0.dp
        BallStyle.Superzahl -> BallColors.SuperzahlBackground to 2.dp
        BallStyle.Eurozahl -> BallColors.EurozahlBackground to 1.dp
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .semantics(mergeDescendants = true) { ballStyle = style }
            // Grows with the font scale instead of clipping the number.
            .defaultMinSize(minWidth = 32.dp, minHeight = 32.dp)
            .aspectRatio(1f)
            .background(background, CircleShape)
            .border(borderWidth, BallColors.Border, CircleShape)
            .padding(4.dp),
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = BallColors.Content,
        )
    }
}

@Preview
@Composable
private fun LotteryBallStylesPreview() {
    LottoTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BallStyle.entries.forEach { LotteryBall(number = 42, style = it) }
        }
    }
}
