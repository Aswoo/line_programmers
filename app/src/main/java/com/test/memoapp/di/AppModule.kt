/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.memoapp.di

import android.app.Application
import androidx.room.Room

import com.test.memoapp.data.source.DefaultMemosRepository
import com.test.memoapp.data.source.MemosRepository
import com.test.memoapp.data.source.local.MemoDb
import com.test.memoapp.data.source.local.MemosDao
import dagger.Binds

import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module(includes = [ViewModelModule::class,ApplicationModuleBinds::class])
class AppModule {


    @Singleton
    @Provides
    fun provideDb(app: Application): MemoDb {
        return Room
            .databaseBuilder(app, MemoDb::class.java, "Memos.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMemoDao(db: MemoDb): MemosDao {
        return db.memoDao()
    }

}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: DefaultMemosRepository): MemosRepository
}

