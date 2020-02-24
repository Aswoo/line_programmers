package com.test.memoapp.ui.memos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.test.memoapp.Event
import com.test.memoapp.data.Memo
import com.test.memoapp.data.source.MemosRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MemosViewModel @Inject constructor(private val repository: MemosRepository) : ViewModel() {

    private val _items = MutableLiveData<List<Memo>>().apply { value = emptyList() }
    val items: LiveData<List<Memo>> = _items

    private val _forceUpdate = MutableLiveData<Boolean>()
    val forceUpdate : LiveData<Boolean> = _forceUpdate

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarText


    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openTaskEvent: LiveData<Event<String>> = _openTaskEvent

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    protected val compositeDisposable = CompositeDisposable()


    init {
        loadMemos()
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    fun loadMemos() {
        _dataLoading.value = true

        compositeDisposable.add(
            repository.getMemos().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _dataLoading.value = false
                    _items.value = it
                },
                    { error ->
                        _dataLoading.value = false
                        _items.value = emptyList()
                        Log.e("TAG", "Unable to get memos", error)
                    })
        )
    }

    fun convert(memo : Memo){


    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
    fun refresh() {
        loadMemos()
    }
    fun updateForce(boolean: Boolean){
        _forceUpdate.value = boolean
    }
}