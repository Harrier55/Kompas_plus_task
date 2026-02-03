package com.example.kompasplustask.presentation.main_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kompasplustask.data.FaqRepository
import com.example.kompasplustask.domain.FaqCategory
import com.example.kompasplustask.domain.FaqItem
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

class MainViewModel : ViewModel() {

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
                allFaqItems = FaqRepository.getFaqData()
                Log.d(TAG, "list: ${allFaqItems.size}")
            //    Log.d(TAG, "list: ${allFaqItems}")
                val categories = FaqRepository.groupFaqByCategory(allFaqItems)

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
                FaqRepository.groupFaqByCategory(allFaqItems)
            } else {
                FaqRepository.searchFaqItems(allFaqItems, query)
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
            val categories = FaqRepository.groupFaqByCategory(faqItems = allFaqItems)

            // При очистке разворачиваем ВСЕ категории
            currentState.copy(
                searchQuery = "",
                categories = categories,
                expandedSections = categories.map { it.title }.toSet()
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