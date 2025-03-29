package com.personal.tmdb.settings.presentation.languages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.settings.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val preferencesRepository: PreferencesRepository
): ViewModel() {

    private val _languagesState = MutableStateFlow(LanguagesState())
    val languagesState = _languagesState.asStateFlow()

    init {
        getLanguages()
    }

    private fun getLanguages() {
        viewModelScope.launch {
            _languagesState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }

            settingsRepository.getPrimaryTranslations()
                .onError { error ->
                    _languagesState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    val language = preferencesRepository.getLanguage()
                    _languagesState.update {
                        it.copy(
                            loading = false,
                            selectedLanguage = language,
                            languages = result,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun languagesUiEvent(event: LanguagesUiEvent) {
        when (event) {
            LanguagesUiEvent.OnNavigateBack -> Unit
            is LanguagesUiEvent.SetLanguage -> {
                viewModelScope.launch {
                    _languagesState.update {
                        it.copy(selectedLanguage = event.languageCode)
                    }
                    preferencesRepository.setLanguage(event.languageCode)
                }
            }
        }
    }
}