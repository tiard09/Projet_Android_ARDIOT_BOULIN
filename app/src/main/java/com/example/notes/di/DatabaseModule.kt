package com.example.notes.di

import android.content.Context
import androidx.room.Room
import com.example.notes.data.local.NoteDao
import com.example.notes.data.local.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(
        @ApplicationContext context: Context,
    ): NoteDatabase = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        "notes_database",
    ).build()

    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()
}
