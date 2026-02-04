package com.example.kompasplustask.data

import android.util.Log
import com.example.kompasplustask.domain.FaqCategory
import com.example.kompasplustask.domain.FaqItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FaqJsonItem(
    val Code: String,
    val Subject: String?,
    val Question: String,
    val Answer: String
)

object FaqRepository {

    private const val TAG = "App"

    fun getFaqData(jsonString: String = DataSource.FAQ_JSON_STRING): List<FaqItem> {
        return try {
            val json = Json { ignoreUnknownKeys = true }
            val jsonItems: List<FaqJsonItem> = json.decodeFromString(jsonString)

            jsonItems.map { jsonItem ->
                FaqItem(
                    code = jsonItem.Code,
                    subject = jsonItem.Subject.replaceAppNameOrNull(),
                    question = jsonItem.Question.replaceAppName(),
                    answer = jsonItem.Answer.replaceAppName()
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "FaqRepository getFaqData ", e)
            emptyList()
        }
    }

    fun groupFaqByCategory(faqItems: List<FaqItem>): List<FaqCategory> {
        // Группируем по категориям, элементы без категории попадут в "General"
        val grouped = faqItems.groupBy { it.subject ?: "General" }

        return grouped.map { (subject, items) ->
            FaqCategory(
                title = subject,
                items = items
            )
        }
    }

    fun searchFaqItems(
        faqItems: List<FaqItem>,
        query: String
    ): List<FaqCategory> {
        if (query.isBlank()) {
            return groupFaqByCategory(faqItems)
        }

        val filteredItems = faqItems.filter { item ->
            item.question.contains(query, ignoreCase = true) ||
                    item.answer.contains(query, ignoreCase = true)
        }

        return groupFaqByCategory(filteredItems)
    }

    private fun String.replaceAppName(): String {
        return this.replace("%SUBST_APPLICATION_NAME%", "Платежи Мобикеш")//TODO вынести в ресурсы
    }

    // Для nullable строк
    private fun String?.replaceAppNameOrNull(): String? {
        return this?.replace("%SUBST_APPLICATION_NAME%", "Платежи Мобикеш")
    }
}