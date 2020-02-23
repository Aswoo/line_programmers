package com.test.memoapp.data.source

import com.test.memoapp.data.Memo
import com.test.memoapp.data.Result
import com.test.memoapp.data.source.local.MemosDao
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class DefaultMemosRepository @Inject constructor(private val memosDao: MemosDao) : MemosRepository {

    override fun getMemos(forceUpdate: Boolean): Single<List<Memo>> {
        return memosDao.getTasks()
    }

    override fun getMemo(memoId: String, forceUpdate: Boolean): Single<Memo> {
        return memosDao.getMemoById(memoId)
    }

    override fun saveMemo(memo: Memo): Completable {
        return memosDao.insertMemo(memo)
    }

    override fun deleteAllMemos() {
        return memosDao.deleteMemos()
    }

    override fun deleteMemo(memoId: String) : Completable{
        return memosDao.deleteTaskById(memoId)
    }

}