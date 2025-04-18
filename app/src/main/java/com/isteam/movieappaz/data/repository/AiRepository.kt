package com.isteam.movieappaz.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.isteam.movieappaz.common.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AiRepository @Inject constructor(private val generativeModel: GenerativeModel) {

    suspend fun generateContent(content: String, history: List<Content>): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading)
            try {
                generativeModel.startChat(history)
                val response = generativeModel.generateContent(content)
                emit(Resource.Success(response.text))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }

}