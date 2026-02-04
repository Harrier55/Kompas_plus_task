package com.example.kompasplustask.presentation.detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kompasplustask.domain.models.FaqItem
import com.example.kompasplustask.domain.repository.FaqRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DetailScreenState(
    val faqItem: FaqItem? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isNotFound: Boolean = false
)

class DetailViewModel(
    private val repository: FaqRepository
): ViewModel() {

    private val _state = MutableStateFlow(DetailScreenState())
    val state: StateFlow<DetailScreenState> = _state

    private val dispatcher = Dispatchers.IO

    fun loadFaqItem(faqCode: String) {
        if (faqCode.isEmpty()) {
            _state.update {
                it.copy(
                    isLoading = false,
                    isNotFound = true,
                    error = "Не передан код вопроса"
                )
            }
            return
        }

        // Сброс состояния перед загрузкой
        _state.update {
            it.copy(
                isLoading = true,
                error = null,
                isNotFound = false,
                faqItem = null
            )
        }

        viewModelScope.launch {
            try {
                val foundItem = withContext(dispatcher) {
                    repository.getFaqData().find { it.code == faqCode }
                }

                _state.update {
                    if (foundItem != null) {
                        it.copy(
                            faqItem = foundItem,
                            isLoading = false,
                            error = null,
                            isNotFound = false
                        )
                    } else {
                        it.copy(
                            isLoading = false,
                            error = null,
                            isNotFound = true
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка загрузки: ${e.message ?: "Неизвестная ошибка"}",
                        isNotFound = false
                    )
                }
            }
        }
    }

    fun retry(faqCode: String) {
        loadFaqItem(faqCode)
    }
}