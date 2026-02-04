package com.example.kompasplustask.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class FaqCategory(
    val title: String,
    val items: List<FaqItem>
)
