package com.test.memoapp.ui.addeditmemo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.test.Event
import com.test.memoapp.data.Memo

class AddEditMemoViewModel : ViewModel(){

    // Two-way databinding, exposing MutableLiveData
    val title = MutableLiveData<String>()

    // Two-way databinding, exposing MutableLiveData
    val description = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarText

    private val _taskUpdated = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdated
}