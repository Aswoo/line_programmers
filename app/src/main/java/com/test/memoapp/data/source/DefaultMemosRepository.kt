package com.test.memoapp.data.source

import com.test.memoapp.data.Memo
import com.test.memoapp.data.Result
import com.test.memoapp.data.source.local.MemosDao
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class DefaultMemosRepository @Inject constructor(private val memosDao: MemosDao) : MemosRepository {

    override fun getTasks(forceUpdate: Boolean): Single<List<Memo>> {
        return memosDao.getTasks()
    }

    override fun getTask(memoId: String, forceUpdate: Boolean): Single<Memo> {
        return memosDao.getMemoById(memoId)
    }

    override fun saveTask(memo: Memo): Completable {
        return memosDao.insertMemo(memo)
    }

    override fun deleteAllTasks() {
        return memosDao.deleteMemos()
    }

    override fun deleteTask(memoId: String) : Completable{
        return memosDao.deleteTaskById(memoId)
    }

}