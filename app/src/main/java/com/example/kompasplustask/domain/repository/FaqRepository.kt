package com.example.kompasplustask.domain.repository

import com.example.kompasplustask.domain.models.FaqItem

interface FaqRepository {
    suspend fun getFaqData(): List<FaqItem>
}