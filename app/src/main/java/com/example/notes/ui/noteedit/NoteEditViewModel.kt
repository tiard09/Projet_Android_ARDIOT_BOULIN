package com.example.notes.ui.noteedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.notes.data.local.NoteEntity
import com.example.notes.data.repository.NoteRepository
import com.example.notes.ui.navigation.NoteEditRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val noteId: Long = savedStateHandle.toRoute<NoteEditRoute>().noteId
    private var existingNote: NoteEntity? = null

    val isEditing: Boolean = noteId != -1L

    var title by mutableStateOf("")
        private set

    var content by mutableStateOf("")
        private set

    var category by mutableStateOf("Personnel")
        private set

    val canSave: Boolean
        get() = title.isNotBlank()

    init {
        if (isEditing) {
            viewModelScope.launch {
                repository.getNoteById(noteId)?.let { note ->
                    existingNote = note
                    title = note.title
                    content = note.content
                    category = note.category
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        title = value
    }

    fun onContentChange(value: String) {
        content = value
    }

    fun onCategoryChange(value: String) {
        category = value
    }

    fun save(onSaved: () -> Unit) {
        if (!canSave) return

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val cleanCategory = category.ifBlank { "Personnel" }

            if (isEditing) {
                val note = existingNote ?: repository.getNoteById(noteId) ?: return@launch
                repository.updateNote(
                    note.copy(
                        title = title.trim(),
                        content = content.trim(),
                        category = cleanCategory.trim(),
                        updatedAt = now,
                    )
                )
            } else {
                repository.insertNote(
                    NoteEntity(
                        title = title.trim(),
                        content = content.trim(),
                        category = cleanCategory.trim(),
                        createdAt = now,
                        updatedAt = now,
                    )
                )
            }

            onSaved()
        }
    }
}
