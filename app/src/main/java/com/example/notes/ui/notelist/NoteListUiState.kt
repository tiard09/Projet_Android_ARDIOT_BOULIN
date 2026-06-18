package com.example.notes.ui.notelist

import com.example.notes.data.local.NoteEntity

data class NoteListUiState(
    val notes: List<NoteEntity> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
