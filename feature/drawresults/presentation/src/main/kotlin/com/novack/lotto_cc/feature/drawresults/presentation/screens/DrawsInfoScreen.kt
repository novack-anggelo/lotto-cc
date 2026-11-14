package com.novack.lotto_cc.feature.drawresults.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.novack.lotto_cc.core.common.presentation.R
import com.novack.lotto_cc.core.designsystem.theme.Spacing
import com.novack.lotto_cc.feature.drawresults.presentation.DrawsViewModel
import com.novack.lotto_cc.feature.drawresults.presentation.models.DrawsUiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DrawsInfoRoute(
    modifier: Modifier = Modifier,
    viewModel: DrawsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DrawsInfoScreen(
        state = state,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onClick = {}
    )
}

@Composable
fun DrawsInfoScreen(
    state: DrawsUiState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onClick: () -> Unit,
) {
    Scaffold(modifier = modifier) { paddingValues ->
        when (state) {
            is DrawsUiState.Error -> {
                ErrorContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = Spacing.xl),
                    onRetry = onRetry
                )
            }

            DrawsUiState.Loading -> {
                LoadingContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is DrawsUiState.Loaded -> {

            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = stringResource(R.string.error_title), textAlign = TextAlign.Center)
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}