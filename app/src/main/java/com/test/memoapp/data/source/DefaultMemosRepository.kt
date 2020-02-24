package com.test.memoapp.data.source

import com.test.memoapp.data.Memo
import com.test.memoapp.data.source.local.MemosDao
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class DefaultMemosRepository @Inject constructor(private val memosDao: MemosDao) : MemosRepository {

    override fun getMemos(): Single<List<Memo>> {
        return memosDao.getMemos()
    }

    override fun getMemo(memoId: String): Single<Memo> {
        return memosDao.getMemoById(memoId)
    }

    override fun saveMemo(memo: Memo): Completable {
        return memosDao.insertMemo(memo)
    }

    override fun deleteAllMemos() {
        return memosDao.deleteMemos()
    }

    override fun deleteMemo(memoId: String) : Completable{
        return memosDao.deleteMemoById(memoId)
    }

}