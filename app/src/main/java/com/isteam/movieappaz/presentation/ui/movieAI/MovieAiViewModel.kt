package com.isteam.movieappaz.presentation.ui.movieAI

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.utils.AiChatEnum
import com.isteam.movieappaz.common.utils.AiRole
import com.isteam.movieappaz.common.utils.Genres
import com.isteam.movieappaz.common.utils.ManualResourceProvider
import com.isteam.movieappaz.common.utils.getCurrentTime
import com.isteam.movieappaz.domain.model.AiUiModel
import com.isteam.movieappaz.domain.model.MessageUiModel
import com.isteam.movieappaz.domain.useCase.AiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieAiViewModel @Inject constructor(
    private val aiUseCase: AiUseCase,
    private val spManager: SharedPreferencesManager
) : BaseViewModel<MovieAiUiState>() {

    private val _chatHistory = MutableLiveData<List<Content>>(emptyList())
    private val _messageList = arrayListOf<AiUiModel>()
    private var isInitial = true

    private var messageEnum = AiChatEnum.START

    private val messagesList = arrayListOf<MessageUiModel>()

    private var isGenreSelected = false

    private val genreMessages = arrayListOf<MessageUiModel>()

    private lateinit var resourcesProvider: ManualResourceProvider

    fun setManualResourceProvider(manualResourceProvider: ManualResourceProvider) {
        resourcesProvider = manualResourceProvider
        viewModelScope.launch {
            messagesList.clear()
            genreMessages.clear()
            messagesList.addAll(
                listOf(
                    MessageUiModel(
                        1, resourcesProvider.getString(R.string.ai_message_welcome_1),
                    ),
                    MessageUiModel(
                        2, resourcesProvider.getString(R.string.ai_message_welcome_2),
                    )
                )
            )
            genreMessages.addAll(
                Genres.entries.map {
                    MessageUiModel(it.id, resourcesProvider.getString(it.displayName))
                }
            )
        }
        if (isInitial) {
            initialCall()
            isInitial = false
        }
    }

    private fun initialCall() {
        if (!spManager.getIsStoryFinished()) {
            setState(MovieAiUiState.StoryShow)
        } else {
            startConversation()
        }
    }

    fun getChat() = _messageList

    fun storyDone() {
        spManager.setIsStoryFinished(true)
        setState(MovieAiUiState.StoryDone)
    }

    fun generateContent(chat: String) {
        if (chat == messagesList[1].message) {
            messageEnum = AiChatEnum.SELECT_GENRE
            selectGenreConversation()
        } else {
            viewModelScope.launch {
                val oldHistory = ArrayList(_chatHistory.value.orEmpty())

                val newChat = if (isGenreSelected && messageEnum == AiChatEnum.SELECT_GENRE)
                    "${resourcesProvider.getString(R.string.ai_suggest_movie_in_genre)} $chat"
                else chat

                _messageList.add(
                    AiUiModel(
                        _messageList.size + 1, AiRole.USER, newChat, getCurrentTime()
                    )
                )

                setState(MovieAiUiState.UserMessage(_messageList))

                oldHistory.add(
                    content(role = AiRole.USER) { text(newChat) }
                )

                aiUseCase.generateContent(newChat, _chatHistory.value.orEmpty()).handleResult(
                    onComplete = {
                        _messageList.add(
                            AiUiModel(
                                _messageList.size + 1,
                                AiRole.MODEL,
                                it,
                                getCurrentTime()
                            )
                        )

                        oldHistory.add(content(role = AiRole.MODEL) { text(it) })

                        _chatHistory.value = oldHistory

                        setState(MovieAiUiState.AiMessage(_messageList))
                        setState(MovieAiUiState.MessageComplete)

                    },
                    onError = {
                        setState(MovieAiUiState.Error(it.localizedMessage as String))
                    },
                    onLoading = {
                        setState(MovieAiUiState.Loading)
                    }
                )
            }
        }
    }

    fun changeMessageList() {
        when (messageEnum) {
            AiChatEnum.START -> {
                setState(MovieAiUiState.MessageList(messagesList))
                return
            }

            AiChatEnum.SELECT_GENRE -> {
                isGenreSelected = true
                setState(MovieAiUiState.MessageList(genreMessages))
                return
            }

            AiChatEnum.CHANGE_GENRE -> {
                setState(MovieAiUiState.MessageList(messagesList))
                return
            }
        }
    }

    fun startConversation() {
        viewModelScope.launch {
            delay(400)
            _messageList.add(
                AiUiModel(
                    _messageList.size + 1,
                    aiRole = AiRole.MODEL,
                    resourcesProvider.getString(R.string.ai_show_case_1),
                    getCurrentTime()
                )
            )
            setState(MovieAiUiState.AiMessage(_messageList))
            setState(MovieAiUiState.Loading)
            delay(1000)
            _messageList.add(
                AiUiModel(
                    _messageList.size + 1,
                    aiRole = AiRole.MODEL,
                    resourcesProvider.getString(R.string.ai_message_4),
                    getCurrentTime()
                )
            )
            setState(MovieAiUiState.AiMessage(_messageList))
            delay(400)
            setState(MovieAiUiState.MessageComplete)
        }
    }

    private fun selectGenreConversation() {
        viewModelScope.launch {
            _messageList.add(
                AiUiModel(
                    _messageList.size + 1,
                    aiRole = AiRole.USER,
                    resourcesProvider.getString(R.string.ai_message_welcome_2),
                    getCurrentTime()
                )
            )
            delay(400)
            _messageList.add(
                AiUiModel(
                    _messageList.size + 1,
                    aiRole = AiRole.MODEL,
                    resourcesProvider.getString(R.string.ai_message_5),
                    getCurrentTime()
                )
            )
            setState(MovieAiUiState.AiMessage(_messageList))
            setState(MovieAiUiState.Loading)
            delay(500)
            _messageList.add(
                AiUiModel(
                    _messageList.size + 1,
                    aiRole = AiRole.MODEL,
                    resourcesProvider.getString(R.string.ai_message_6),
                    getCurrentTime()
                )
            )
            isGenreSelected = true
            setState(MovieAiUiState.AiMessage(_messageList))
            delay(400)
            setState(MovieAiUiState.MessageComplete)
        }
    }

}

sealed class MovieAiUiState : State {

    data object Loading : MovieAiUiState()

    data class Error(val message: String) : MovieAiUiState()

    data object MessageComplete : MovieAiUiState()

    data object StoryShow : MovieAiUiState()

    data object StoryDone : MovieAiUiState()

    data class MessageList(val messages: List<MessageUiModel>) : MovieAiUiState()

    data class AiMessage(val data: List<AiUiModel>) : MovieAiUiState()

    data class UserMessage(val data: List<AiUiModel>) : MovieAiUiState()

}