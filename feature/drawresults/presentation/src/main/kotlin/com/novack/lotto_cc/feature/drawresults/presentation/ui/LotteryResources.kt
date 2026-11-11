package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.annotation.StringRes
import com.novack.lotto_cc.core.designsystem.theme.LotteryColors
import com.novack.lotto_cc.feature.drawresults.presentation.R
import com.novack.lotto_cc.feature.drawresults.presentation.model.ErrorReason
import com.novack.lotto_cc.feature.drawresults.presentation.model.Lottery

@get:StringRes
internal val Lottery.nameRes: Int
    get() = when (this) {
        Lottery.Lotto6aus49 -> R.string.lottery_lotto6aus49
        Lottery.Eurojackpot -> R.string.lottery_eurojackpot
    }

/** Spoken before each bonus ball, e.g. "Superzahl 7". */
@get:StringRes
internal val Lottery.bonusLabelRes: Int
    get() = when (this) {
        Lottery.Lotto6aus49 -> R.string.bonus_superzahl
        Lottery.Eurojackpot -> R.string.bonus_eurozahl
    }

internal val Lottery.colors: LotteryColors
    get() = when (this) {
        Lottery.Lotto6aus49 -> LotteryColors.Lotto6aus49
        Lottery.Eurojackpot -> LotteryColors.Eurojackpot
    }

@get:StringRes
internal val ErrorReason.messageRes: Int
    get() = when (this) {
        ErrorReason.NoConnection -> R.string.error_no_connection
        ErrorReason.Server -> R.string.error_server
        ErrorReason.Unknown -> R.string.error_unknown
    }
