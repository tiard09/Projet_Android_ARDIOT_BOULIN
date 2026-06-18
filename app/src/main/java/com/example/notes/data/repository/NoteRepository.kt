package com.example.notes.data.repository

import com.example.notes.data.local.NoteDao
import com.example.notes.data.local.NoteEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
) {
    fun getAllNotes() = noteDao.getAllNotes()

    suspend fun getNoteById(noteId: Long) = noteDao.getNoteById(noteId)

    suspend fun insertNote(note: NoteEntity) = noteDao.insertNote(note)

    suspend fun updateNote(note: NoteEntity) = noteDao.updateNote(note)

    suspend fun deleteNote(note: NoteEntity) = noteDao.deleteNote(note)

    fun searchNotes(query: String) = noteDao.searchNotes(query)
}
