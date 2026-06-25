package com.example.tingshu.di

import android.content.Context
import androidx.room.Room
import com.example.tingshu.data.local.TingshuDatabase
import com.example.tingshu.data.local.dao.BookDao
import com.example.tingshu.data.local.dao.ChapterDao
import com.example.tingshu.data.local.dao.FavoriteDao
import com.example.tingshu.data.local.dao.PlayHistoryDao
import com.example.tingshu.data.local.dao.PlayProgressDao
import com.example.tingshu.data.local.dao.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TingshuDatabase {
        return Room.databaseBuilder(
            context,
            TingshuDatabase::class.java,
            "tingshu_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideBookDao(database: TingshuDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    fun provideChapterDao(database: TingshuDatabase): ChapterDao {
        return database.chapterDao()
    }

    @Provides
    fun provideFavoriteDao(database: TingshuDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    fun providePlayHistoryDao(database: TingshuDatabase): PlayHistoryDao {
        return database.playHistoryDao()
    }

    @Provides
    fun provideSearchHistoryDao(database: TingshuDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }

    @Provides
    fun providePlayProgressDao(database: TingshuDatabase): PlayProgressDao {
        return database.playProgressDao()
    }
}
