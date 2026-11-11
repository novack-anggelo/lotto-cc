package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@Composable
private fun DrawCards(cards: List<DrawCardUiModel>, padding: PaddingValues) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize().padding(padding),
    ) {
        items(cards, key = { it.lottery }) { DrawCard(it) }
    }
}

@Composable
private fun ErrorMessage(reason: ErrorReason, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(32.dp),
    ) {
        Text(
            text = stringResource(reason.messageRes),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) { Text(stringResource(R.string.retry)) }
    }
}
