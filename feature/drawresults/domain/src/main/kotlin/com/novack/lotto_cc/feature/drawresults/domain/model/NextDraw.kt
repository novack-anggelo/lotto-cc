package com.novack.lotto_cc.feature.drawresults.domain.model

import java.time.OffsetDateTime

/** The upcoming draw. [jackpot] is null when it is not known yet (e.g. right after a draw). */
data class NextDraw(
    val date: OffsetDateTime,
    val jackpot: Money?,
)
