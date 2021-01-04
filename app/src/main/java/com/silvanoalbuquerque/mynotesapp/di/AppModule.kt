package com.silvanoalbuquerque.mynotesapp.di

import android.content.Context
import androidx.room.Room
import com.silvanoalbuquerque.mynotesapp.db.NotesDatabase
import com.silvanoalbuquerque.mynotesapp.other.Constants.NOTES_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNotesDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        NotesDatabase::class.java,
        NOTES_DATABASE_NAME,
    ).build()

    @Singleton
    @Provides
    fun provideNotesDao(db: NotesDatabase) = db.getNotesDao()
}