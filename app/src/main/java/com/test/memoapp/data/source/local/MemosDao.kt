/*
 * Copyright (C) 2019 The Android Open Source Project
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

package com.test.memoapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.memoapp.data.Memo
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Data Access Object for the momes table.
 */
@Dao
interface MemosDao {

    /**
     * Select all memos from the tasks table.
     *
     * @return all memos.
     */
    @Query("SELECT * FROM Memos")
    fun getTasks(): Single<List<Memo>>

    /**
     * Select a memo by id.
     *
     * @param memoId the memo id.
     * @return the memo with memoId.
     */
    @Query("SELECT * FROM Memos WHERE entryid = :memoId")
    fun getMemoById(memoId: String): Single<Memo>

    /**
     * Insert a memo in the database. If the task already exists, replace it.
     *
     * @param task the memo to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(memo: Memo) : Completable

    /**
     * Update a task.
     *
     * @param memo task to be updated
     * @return the number of tasks updated. This should always be 1.
     */
    @Update
    fun updateMemo(memo: Memo): Completable

    /**
     * Delete a task by id.
     *
     * @return the number of tasks deleted. This should always be 1.
     */
    @Query("DELETE FROM Memos WHERE entryid = :memoId")
    fun deleteTaskById(memoId: String): Completable

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM Memos")
    fun deleteMemos()
}
