package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.novack.lotto_cc.core.designsystem.theme.Spacing
import com.novack.lotto_cc.feature.drawresults.presentation.DrawResultsViewModel
import com.novack.lotto_cc.feature.drawresults.presentation.R
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawCardUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawResultsUiState
import com.novack.lotto_cc.feature.drawresults.presentation.model.ErrorReason
import org.koin.compose.viewmodel.koinViewModel

/** Entry point of the draw results screen: connects [DrawResultsScreen] to its ViewModel. */
@Composable
fun DrawResultsRoute(modifier: Modifier = Modifier, viewModel: DrawResultsViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DrawResultsScreen(uiState, onRetry = viewModel::retry, modifier = modifier)
}

@Composable
internal fun DrawResultsScreen(uiState: DrawResultsUiState, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) { padding ->
        when (uiState) {
            DrawResultsUiState.Loading -> Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                CircularProgressIndicator()
            }
            is DrawResultsUiState.Content -> DrawCards(uiState.cards, padding)
            is DrawResultsUiState.Error -> ErrorMessage(uiState.reason, onRetry, Modifier.padding(padding))
        }
    }
}

/** Where a tap on any card leads. */
internal const val Lotto24Url = "https://www.lotto24.de"

@Composable
private fun DrawCards(cards: List<DrawCardUiModel>, padding: PaddingValues) {
    val direction = LocalLayoutDirection.current
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        // Scaffold insets go into contentPadding, so cards scroll behind the system bars.
        contentPadding = PaddingValues(
            start = padding.calculateStartPadding(direction) + Spacing.l,
            top = padding.calculateTopPadding() + Spacing.l,
            end = padding.calculateEndPadding(direction) + Spacing.l,
            bottom = padding.calculateBottomPadding() + Spacing.l,
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.l),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(cards) { DrawCard(it, onClick = { uriHandler.openUri(Lotto24Url) }) }
    }
}

@Composable
private fun ErrorMessage(reason: ErrorReason, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.l, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(Spacing.xl),
    ) {
        Text(
            text = stringResource(reason.messageRes),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) { Text(stringResource(R.string.retry)) }
    }
}
