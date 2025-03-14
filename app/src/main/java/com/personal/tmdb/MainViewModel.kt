package com.personal.tmdb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.tmdb.auth.data.models.AccessTokenBody
import com.personal.tmdb.auth.data.models.RedirectToBody
import com.personal.tmdb.auth.data.models.RequestTokenBody
import com.personal.tmdb.auth.domain.repository.AuthRepository
import com.personal.tmdb.core.domain.models.LogoutRequestBody
import com.personal.tmdb.core.domain.models.User
import com.personal.tmdb.core.domain.repository.LocalCache
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.repository.UserRepository
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.DialogAction
import com.personal.tmdb.core.domain.util.DialogEvent
import com.personal.tmdb.core.domain.util.SnackbarController
import com.personal.tmdb.core.domain.util.SnackbarEvent
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.core.presentation.PreferencesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val localCache: LocalCache
): ViewModel() {

    var holdSplash by mutableStateOf(true)
        private set

    private val _preferencesState = MutableStateFlow(PreferencesState())
    val preferencesState: StateFlow<PreferencesState> = _preferencesState.asStateFlow()

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    var showDialog by mutableStateOf(false)
        private set

    private val _dialogState = MutableStateFlow(
        DialogEvent(
            message = UiText.StringResource(R.string.empty),
            confirmAction = DialogAction(
                name = UiText.StringResource(R.string.ok),
                action = { showDialog = false}
            )
        )
    )
    val dialogState = _dialogState.asStateFlow()

    init {
        observePreferences()
        getUser()
    }

    private fun observePreferences() {
        viewModelScope.launch {
            preferencesRepository.getPreferences().collect { preferences ->
                _preferencesState.update {
                    it.copy(
                        darkTheme = preferences.darkTheme,
                        language = preferences.language,
                        showTitle = preferences.showTitle,
                        showVoteAverage = preferences.showVoteAverage,
                        additionalNavigationItem = preferences.additionalNavigationItem
                    )
                }
                holdSplash = false
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            userRepository.getUser()?.let { user ->
                _userState.update { it.copy(user = user) }
                getUserDetails(user.sessionId ?: "")
            } ?: _userState.update { it.copy(user = null) }
        }
    }

    private fun createRequestToken() {
        viewModelScope.launch {
            _userState.update { it.copy(loading = true) }

            authRepository.createRequestToken(RedirectToBody(C.REDIRECT_URL))
                .onError { error ->
                    _userState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    if (result.success) {
                        _userState.update {
                            it.copy(
                                loading = false,
                                requestToken = result.requestToken
                            )
                        }
                        localCache.saveRequestToken(result.requestToken)
                    } else {
                        _userState.update {
                            it.copy(
                                loading = false,
                                errorMessage = UiText.DynamicString(result.statusMessage)
                            )
                        }
                    }
                }
        }
    }

    private fun signInUser() {
        viewModelScope.launch {
            _userState.update { it.copy(loading = true) }
            authRepository.createAccessToken(RequestTokenBody(localCache.getRequestToken()))
                .onError { error ->
                    _userState.update { it.copy(errorMessage = error.toUiText()) }
                }
                .onSuccess { accessToken ->
                    if (accessToken.success && accessToken.accessToken != null && accessToken.accountId != null) {
                        authRepository.createSession(AccessTokenBody(accessToken.accessToken))
                            .onError { error ->
                                _userState.update { it.copy(loading = false) }
                                SnackbarController.sendEvent(
                                    event = SnackbarEvent(
                                        message = error.toUiText()
                                    )
                                )
                            }
                            .onSuccess { session ->
                                if (session.success && session.sessionId != null) {
                                    _userState.update { state ->
                                        state.copy(
                                            user = User(
                                                accessToken = accessToken.accessToken,
                                                accountObjectId = accessToken.accountId,
                                                sessionId = session.sessionId
                                            )
                                        )
                                    }
                                    getUserDetails(session.sessionId)
                                    localCache.clearRequestToken()
                                } else {
                                    SnackbarController.sendEvent(
                                        event = SnackbarEvent(
                                            message = UiText.StringResource(R.string.error_session_id)
                                        )
                                    )
                                }
                            }
                    } else {
                        SnackbarController.sendEvent(
                            event = SnackbarEvent(
                                message = UiText.DynamicString(accessToken.statusMessage)
                            )
                        )
                    }
                }
        }
    }

    private fun getUserDetails(sessionId: String) {
        if (sessionId.isBlank()) return
        viewModelScope.launch {
            authRepository.getUserDetails(sessionId)
                .onError { error ->
                    println(error.toUiText())
                    _userState.update { it.copy(loading = false) }
                    _userState.value.user?.let { userRepository.saveUser(it) }
                }
                .onSuccess { result ->
                    _userState.update { state ->
                        val user = state.user?.copy(
                            accountId = result.accountId,
                            gravatarAvatarPath = result.gravatarAvatarPath,
                            tmdbAvatarPath = result.tmdbAvatarPath,
                            iso6391 = result.iso6391,
                            iso31661 = result.iso31661,
                            name = result.name,
                            includeAdult = result.includeAdult,
                            username = result.username
                        )
                        user?.let { userRepository.saveUser(it) }
                        state.copy(
                            loading = false,
                            user = user
                        )
                    }
                }
        }
    }

    private fun singOut(user: User?) {
        viewModelScope.launch {
            user?.let { user ->
                _userState.update { it.copy(loading = true) }
                val request = LogoutRequestBody(
                    accessToken = user.accessToken ?: "",
                    sessionId = user.sessionId ?: ""
                )
                authRepository.logout(request)
                    .onError { error ->
                        println(error.toUiText())
                        _userState.update { it.copy(loading = false) }
                    }
                    .onSuccess {
                        println("Logout Success")
                        _userState.update { it.copy(loading = false) }
                    }
                userRepository.removeUser(user)
                _userState.update { it.copy(user = null) }

                SnackbarController.sendEvent(
                    event = SnackbarEvent(
                        message = UiText.StringResource(R.string.logout_successfully)
                    )
                )
            } ?: SnackbarController.sendEvent(
                event = SnackbarEvent(
                    message = UiText.StringResource(R.string.wait_what)
                )
            )
        }
    }

    fun uiEvent(event: UiEvent) {
        when (event) {
            UiEvent.CreateRequestToken -> {
                createRequestToken()
            }
            UiEvent.DropRequestToken -> {
                _userState.update { it.copy(requestToken = null) }
            }
            UiEvent.SignInUser -> {
                signInUser()
            }
            is UiEvent.SignOut -> {
                singOut(event.user)
            }
            is UiEvent.ShowDialog -> {
                showDialog = event.show
                event.content?.let { content ->
                    _dialogState.value = content
                }
            }
        }
    }
}