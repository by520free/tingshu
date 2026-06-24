package com.example.tingshu.di

import com.example.tingshu.data.repository.BookRepositoryImpl
import com.example.tingshu.data.repository.ShelfRepositoryImpl
import com.example.tingshu.domain.repository.BookRepository
import com.example.tingshu.domain.repository.ShelfRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBookRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ): BookRepository

    @Binds
    abstract fun bindShelfRepository(
        shelfRepositoryImpl: ShelfRepositoryImpl
    ): ShelfRepository
}
