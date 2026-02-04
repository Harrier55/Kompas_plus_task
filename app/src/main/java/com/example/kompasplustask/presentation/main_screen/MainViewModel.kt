package com.example.kompasplustask.presentation.main_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kompasplustask.domain.models.FaqCategory
import com.example.kompasplustask.domain.models.FaqItem
import com.example.kompasplustask.domain.repository.FaqRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainScreenState(
    val categories: List<FaqCategory> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
    val expandedSections: Set<String> = emptySet()
)

class MainViewModel(
    private val repository: FaqRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    private var allFaqItems: List<FaqItem> = emptyList()
    private val dispatcher = Dispatchers.IO

    init {
        Log.d(TAG, "Init")
        loadFaqData()
    }

    fun loadFaqData() {
        viewModelScope.launch(dispatcher) {
            try {
                allFaqItems = repository.getFaqData()
                Log.d(TAG, "list: ${allFaqItems.size}")
                Log.d(TAG, "list: ${allFaqItems}")
                val categories = groupFaqByCategory(allFaqItems)

                _state.update { currentState ->
                    currentState.copy(
                        categories = categories,
                        isLoading = false,
                        error = null,
                        // По умолчанию все секции развернуты
                        expandedSections = categories.map { it.title }.toSet()
                    )
                }
            } catch (e: Exception) {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "Failed to load FAQ data"
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { currentState ->
            val categories = if (query.isBlank()) {
                groupFaqByCategory(allFaqItems)
            } else {
                searchFaqItems(allFaqItems, query)
            }

            currentState.copy(
                searchQuery = query,
                categories = categories,
                // При поиске автоматически разворачиваем все найденные секции
                expandedSections = if (query.isNotBlank()) {
                    categories.map { it.title }.toSet()
                } else {
                    currentState.expandedSections
                }
            )
        }
    }

    fun toggleSection(title: String) {
        _state.update { currentState ->
            val newExpandedSections = if (currentState.expandedSections.contains(title)) {
                currentState.expandedSections - title
            } else {
                currentState.expandedSections + title
            }

            currentState.copy(
                expandedSections = newExpandedSections
            )
        }
    }

    fun clearSearch() {
        _state.update { currentState ->
            val categories = groupFaqByCategory(faqItems = allFaqItems)

            // При очистке разворачиваем ВСЕ категории
            currentState.copy(
                searchQuery = "",
                categories = categories,
                expandedSections = categories.map { it.title }.toSet()
            )
        }
    }

    private fun searchFaqItems(
        faqItems: List<FaqItem>,
        query: String
    ): List<FaqCategory> {
        if (query.isBlank()) {
            return groupFaqByCategory(faqItems)
        }

        val trimmedQuery = query.trim()

        val filteredItems = faqItems.filter { item ->
            // Поиск по вопросу
            val matchesQuestion = item.question.contains(trimmedQuery, ignoreCase = true)

            // Поиск по ответу
            val normalizedAnswer = item.answer.replace("\n", " ")
            val matchesAnswer = normalizedAnswer.contains(trimmedQuery, ignoreCase = true)

            matchesQuestion || matchesAnswer
        }
        return groupFaqByCategory(filteredItems)
    }

    private fun groupFaqByCategory(faqItems: List<FaqItem>): List<FaqCategory> {
        // Группируем по категориям, элементы без категории попадут в "General"
        val grouped = faqItems.groupBy { it.subject ?: "General" }

        return grouped.map { (subject, items) ->
            FaqCategory(
                title = subject,
                items = items
            )
        }
    }

    fun expandAllSections() {
        _state.update { currentState ->
            currentState.copy(
                expandedSections = currentState.categories.map { it.title }.toSet()
            )
        }
    }

    fun collapseAllSections() {
        _state.update { currentState ->
            currentState.copy(
                expandedSections = emptySet()
            )
        }
    }

    companion object {
        const val TAG = "App"
    }
}