package com.example.kompasplustask.domain

import androidx.compose.runtime.Immutable

@Immutable
data class FaqCategory(
    val title: String,
    val items: List<FaqItem>
)
