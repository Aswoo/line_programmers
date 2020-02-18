package com.test.memoapp.ui.addeditmemo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.memoapp.Event
import com.test.memoapp.R
import com.test.memoapp.data.Memo
import com.test.memoapp.data.source.MemosRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddEditMemoViewModel @Inject constructor(private val repository: MemosRepository) :
    ViewModel() {

    // Two-way databinding, exposing MutableLiveData
    val title = MutableLiveData<String>()

    // Two-way databinding, exposing MutableLiveData
    val description = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarText

    private val _imagePathList = MutableLiveData<MutableList<String>>()
    val imagePathList : LiveData<MutableList<String>> = _imagePathList

    protected val compositeDisposable = CompositeDisposable()

    fun saveMemo(memo: Memo) {

        val currentTitle = title.value
        val currentDescription = description.value
        val currentImagePathList = imagePathList.value

        if (currentTitle == null || currentDescription == null) {
            _snackbarText.value =
                Event(R.string.empty_memo_message)
            return
        }
        if (Memo(currentTitle, currentDescription).isEmpty) {
            _snackbarText.value =
                Event(R.string.empty_memo_message)
            return
        }

        compositeDisposable.add(repository.saveTask(memo).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _snackbarText.value =
                Event(R.string.create_memo_message)
            })
    }

    fun addImagePath(uri : Uri?){
        _imagePathList.value?.add(uri.toString())
    }
}