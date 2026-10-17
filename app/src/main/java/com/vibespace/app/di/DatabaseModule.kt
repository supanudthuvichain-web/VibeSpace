package com.vibespace.app.di

import android.content.Context
import androidx.room.Room
import com.vibespace.app.data.db.VibeSpaceDatabase
import com.vibespace.app.data.db.dao.PlaybackStateDao
import com.vibespace.app.data.db.dao.PlaylistDao
import com.vibespace.app.data.db.dao.QueueDao
import com.vibespace.app.data.db.dao.RecentlyPlayedDao
import com.vibespace.app.data.db.dao.TrackDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing the Room database and all DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): VibeSpaceDatabase {
        return Room.databaseBuilder(
            context,
            VibeSpaceDatabase::class.java,
            "vibespace_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTrackDao(db: VibeSpaceDatabase): TrackDao = db.trackDao()

    @Provides
    fun providePlaylistDao(db: VibeSpaceDatabase): PlaylistDao = db.playlistDao()

    @Provides
    fun provideQueueDao(db: VibeSpaceDatabase): QueueDao = db.queueDao()

    @Provides
    fun providePlaybackStateDao(db: VibeSpaceDatabase): PlaybackStateDao = db.playbackStateDao()

    @Provides
    fun provideRecentlyPlayedDao(db: VibeSpaceDatabase): RecentlyPlayedDao = db.recentlyPlayedDao()
}
