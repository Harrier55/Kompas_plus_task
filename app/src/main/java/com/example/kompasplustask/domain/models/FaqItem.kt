package com.example.kompasplustask.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class FaqItem(
    val code: String,
    val subject: String?,
    val question: String,
    val answer: String
)