package com.example.notes.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object NoteListRoute

@Serializable
data class NoteEditRoute(
    val noteId: Long = -1L,
)
