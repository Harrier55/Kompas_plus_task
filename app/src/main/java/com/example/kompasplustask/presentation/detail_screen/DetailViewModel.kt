package com.example.kompasplustask.presentation.detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kompasplustask.data.FaqRepository
import com.example.kompasplustask.domain.FaqItem
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

class DetailViewModel: ViewModel() {

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
                // Загрузка в фоновом потоке
                val foundItem = withContext(dispatcher) {
                    FaqRepository.getFaqData().find { it.code == faqCode }
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




   fun getFaqItemById(faqCode: String): FaqItem?{
       val allFaqItems = FaqRepository.getFaqData()
       return allFaqItems.find { it.code == faqCode }
   }
}