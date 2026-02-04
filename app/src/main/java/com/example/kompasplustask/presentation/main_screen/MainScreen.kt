package com.example.kompasplustask.presentation.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kompasplustask.R
import com.example.kompasplustask.domain.models.FaqCategory
import com.example.kompasplustask.presentation.common_components.AppSearchBar1
import com.example.kompasplustask.presentation.common_components.LoadingState
import com.example.kompasplustask.ui.theme.KompasPlusTaskTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (String) -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Поисковая строка
        AppSearchBar1(
            query = state.searchQuery,
            onQueryChange = { query ->
                viewModel.onSearchQueryChanged(query)
            },
            onClearClick = {
                viewModel.clearSearch()
                focusManager.clearFocus()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )

        // Контент в зависимости от состояния
        when {
            state.isLoading -> {
                LoadingState(
                    modifier = Modifier.fillMaxSize()
                )
            }

            state.error != null -> {
                ErrorState(
                    errorMessage = state.error!!,
                    onRetry = { viewModel.loadFaqData() },
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                MainContent(
                    state = state,
                    onToggleSection = { title -> viewModel.toggleSection(title) },
                    onQuestionClick = { faqCode ->
                        onNavigateToDetail(faqCode)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    state: MainScreenState,
    onToggleSection: (String) -> Unit,
    onQuestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    when{
        // Если есть категории - показываем список
        state.categories.isNotEmpty() -> {
            FaqCategoriesList(
                categories = state.categories,
                expandedSections = state.expandedSections,
                onToggleSection = onToggleSection,
                onQuestionClick = onQuestionClick,
                modifier = modifier
            )
        }

        // Если нет категорий при активном поиске
        state.searchQuery.isNotBlank() -> {
            EmptySearchState(
                searchQuery = state.searchQuery,
                modifier = modifier
            )
        }

        // Если загрузились данные, но категорий нет
        else -> {
            EmptyState(
                modifier = modifier
            )
        }
    }
}

@Composable
private fun FaqCategoriesList(
    categories: List<FaqCategory>,
    expandedSections: Set<String>,
    onToggleSection: (String) -> Unit,
    onQuestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = categories,
            key = { it.title }
        ) { category ->
            FaqCategorySection(
                category = category,
                isExpanded = expandedSections.contains(category.title),
                onToggle = { onToggleSection(category.title) },
                onQuestionClick = onQuestionClick
            )
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    KompasPlusTaskTheme {
        MainScreen()
    }
}

