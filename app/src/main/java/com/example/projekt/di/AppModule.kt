package com.example.projekt.di

import android.content.Context
import androidx.room.Room
import com.example.projekt.data.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context ) = Room.databaseBuilder(context, NoteDatabase:: class.java, name = "NoteDatabase").fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideNoteDao(db: NoteDatabase) = db.noteDao()
}