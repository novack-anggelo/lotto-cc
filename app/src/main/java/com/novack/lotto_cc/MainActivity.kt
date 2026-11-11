package com.novack.lotto_cc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme
import com.novack.lotto_cc.feature.drawresults.presentation.ui.DrawResultsRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LottoTheme { DrawResultsRoute() }
        }
    }
}
