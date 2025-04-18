package com.isteam.movieappaz.domain.useCase

import com.google.ai.client.generativeai.type.Content
import com.isteam.movieappaz.data.repository.AiRepository
import javax.inject.Inject

class AiUseCase @Inject constructor(private val repo: AiRepository) {

    suspend fun generateContent(content: String, history: List<Content>) =
        repo.generateContent(content, history)

}