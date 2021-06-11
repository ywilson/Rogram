package com.android.rogram.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.rogram.data.NetworkState
import com.android.rogram.data.RoData
import com.android.rogram.data.RoRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class RoViewModel : ViewModel() {

    private val repository: RoRepository = RoRepository()

    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _roData = MutableLiveData<NetworkState<List<RoData>>>()
    val roData: LiveData<NetworkState<List<RoData>>>
        get() = _roData

    init {
        loadDataFromRepo()
    }

    private fun loadDataFromRepo() {
        viewModelScope.launch {
            try {
                _spinner.postValue(true)
                _roData.postValue(NetworkState.Loading())
                val res = repository.fetchRoData()
                _roData.postValue(NetworkState.Success(res))
            } catch (error: Exception) {
                _roData.postValue(NetworkState.Error(error))
            } finally {
                _spinner.postValue(false)
            }
        }
    }
}