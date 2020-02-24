package com.test.memoapp.ui.addeditmemo

import android.util.Log
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

    private val _imagePathList = MutableLiveData<List<String>>()
    val imagePathList: LiveData<List<String>> = _imagePathList

    private val _memoUpdated = MutableLiveData<Event<Unit>>()
    val memoUpdatedEvent: LiveData<Event<Unit>> = _memoUpdated

    var items : MutableList<String> = mutableListOf()

    private var memoId: String? = null

    var isNewMemo: Boolean = false

    private var isDataLoaded = false

    protected val compositeDisposable = CompositeDisposable()

    fun start(memoId : String) {
        if (_dataLoading.value == true) {
            return
        }

        this.memoId = memoId
        if (memoId == "") {
            // No need to populate, it's a new memo
            isNewMemo = true
            return
        }
        if (isDataLoaded) {
            // No need to populate, already have data.
            return
        }

        isNewMemo = false
        _dataLoading.value = true

        compositeDisposable.add(
            repository.getMemo(memoId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onMemoLoaded(it)
                    _dataLoading.value = false
                },
                    { error ->
                        Log.e("TAG", "Unable to get memo :it is new Memo", error)
                        _dataLoading.value = false
                        //onMemoLoaded(Memo())
                    })
        )
    }

    fun saveMemo(memo: Memo) {

        val currentTitle = title.value
        val currentDescription = description.value
        val currentImagePathList = imagePathList.value!!

        memo.images = currentImagePathList

        if (currentTitle == null || currentDescription == null) {
            _snackbarText.value =
                Event(R.string.empty_memo_message)
            return
        }
        if (Memo(currentTitle, currentDescription,currentImagePathList).isEmpty) {
            _snackbarText.value =
                Event(R.string.empty_memo_message)
            return
        }

        val currentMemoId = memoId
        if (isNewMemo || currentMemoId == "") {
            createMemo(Memo(currentTitle, currentDescription,memo.images))
        } else {
            val memo = Memo(currentTitle, currentDescription,memo.images,currentMemoId!!)
            updateMemo(memo)
        }


    }

    private fun createMemo(newMemo: Memo)  {

        compositeDisposable.add(repository.saveMemo(newMemo).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _snackbarText.value =
                    Event(R.string.create_memo_message)
                _memoUpdated.value = Event(Unit)
            })
    }

    private fun updateMemo(memo: Memo) {
        if (isNewMemo) {
            throw RuntimeException("updateMemo() was called but memo is new.")
        }
        compositeDisposable.add(repository.saveMemo(memo).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _snackbarText.value =
                    Event(R.string.update_memo_message)
                _memoUpdated.value = Event(Unit)
            })
    }

    fun removeImagePath(position: Int) {

        items = _imagePathList.value!!.toMutableList()
        items.removeAt(position)
        _imagePathList.postValue(items)

    }
    fun onTempMemoLoad(memo : Memo){
        title.value = memo.title
        description.value = memo.description
        _imagePathList.value = memo.images

        _dataLoading.value = false
    }
    fun clearMemo(){
        onTempMemoLoad(Memo("","", emptyList(),""))
    }
    private fun onMemoLoaded(memo: Memo) {
        title.value = memo.title
        description.value = memo.description
        _dataLoading.value = false
        isDataLoaded = true
    }

    override fun onCleared() {

        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}