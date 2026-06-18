package com.example.notes.ui.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.local.NoteEntity
import com.example.notes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: NoteRepository,
) : ViewModel() {
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState = repository.getAllNotes()
        .combine(errorMessage) { notes, error ->
            NoteListUiState(
                notes = notes,
                isLoading = false,
                errorMessage = error,
            )
        }
        .catch { throwable ->
            emit(
                NoteListUiState(
                    isLoading = false,
                    errorMessage = throwable.localizedMessage ?: "Impossible de charger les notes.",
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NoteListUiState(),
        )

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            try {
                repository.deleteNote(note)
            } catch (throwable: Throwable) {
                errorMessage.value = throwable.localizedMessage ?: "La suppression a echoue."
            }
        }
    }

    fun togglePin(note: NoteEntity) {
        viewModelScope.launch {
            try {
                repository.updateNote(
                    note.copy(
                        isPinned = !note.isPinned,
                        updatedAt = System.currentTimeMillis(),
                    )
                )
            } catch (throwable: Throwable) {
                errorMessage.value = throwable.localizedMessage ?: "La note n'a pas pu etre mise a jour."
            }
        }
    }

    fun clearError() {
        errorMessage.value = null
    }
}
