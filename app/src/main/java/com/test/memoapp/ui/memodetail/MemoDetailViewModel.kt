package com.test.memoapp.ui.memodetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.memoapp.data.Memo
import com.test.memoapp.data.source.MemosRepository
import javax.inject.Inject

import com.test.memoapp.Event
import com.test.memoapp.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MemoDetailViewModel @Inject constructor(private val repository: MemosRepository) :
    ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    private val _memo = MutableLiveData<Memo>()
    val memo: LiveData<Memo> = _memo

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _editTaskCommand = MutableLiveData<Event<Unit>>()
    val editTaskCommand: LiveData<Event<Unit>> = _editTaskCommand

    private val _deleteTaskCommand = MutableLiveData<Event<Unit>>()
    val deleteTaskCommand: LiveData<Event<Unit>> = _deleteTaskCommand

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarText

    private val _imagePathList = MutableLiveData<List<String>>()
    val imagePathList: LiveData<List<String>> = _imagePathList

    private val memoId: String
        get() = _memo.value!!.id

    fun deleteMemo() {
        compositeDisposable.add(repository.deleteMemo(memoId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _snackbarText.value =
                    Event(R.string.menu_delete_memo)
            })
    }

    fun load(memoId: String?) {


        if (_isDataAvailable.value == true || _dataLoading.value == true) {
            return
        }

        // Show loading indicator
        _dataLoading.value = true

        if (memoId != null) {

            compositeDisposable.add(
                repository.getMemo(memoId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        _dataLoading.value = false
                        _memo.value = it
                        _imagePathList.value = it.images
                    },
                        { error ->
                            Log.e("TAG", "Unable to get memo", error)
                            _dataLoading.value = false
                            _memo.value = Memo()
                        })
            )
        }


    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }


}