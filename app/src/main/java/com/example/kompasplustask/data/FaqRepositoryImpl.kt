package com.example.kompasplustask.data

import android.content.Context
import android.util.Log
import com.example.kompasplustask.R
import com.example.kompasplustask.domain.models.FaqItem
import com.example.kompasplustask.domain.repository.FaqRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FaqJsonItem(
    val Code: String,
    val Subject: String?,
    val Question: String,
    val Answer: String
)


class FaqRepositoryImpl(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): FaqRepository {
    private val jsonString: String = DataSource.FAQ_JSON_STRING

    override suspend fun getFaqData(): List<FaqItem> = withContext(ioDispatcher){
        return@withContext try {
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
            Log.e("App", "FaqRepository getFaqData ", e)
            emptyList()
        }
    }


    private fun String.replaceAppName(): String {
        val appName = context.getString(R.string.company_name)
        return this.replace("%SUBST_APPLICATION_NAME%", appName)
    }

    // Для nullable строк
    private fun String?.replaceAppNameOrNull(): String? {
        return this?.replaceAppName()
    }

}