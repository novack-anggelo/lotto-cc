package com.novack.lotto_cc.feature.drawresults.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.novack.lotto_cc.feature.drawresults.presentation.DrawsViewModel
import com.novack.lotto_cc.feature.drawresults.presentation.models.DrawsUiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DrawsInfoRoute(
    modifier: Modifier = Modifier,
    viewModel: DrawsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DrawsInfoScreen(state = state)
}

@Composable
fun DrawsInfoScreen(
    state: DrawsUiState,
    modifier: Modifier = Modifier,
) {
    when(state) {
        is DrawsUiState.Error -> {

        }
        DrawsUiState.Loading -> {

        }
        is DrawsUiState.Loaded -> {

        }
    }
}