package com.aredruss.mangatana.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.repo.DatabaseRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    var homeState = MutableLiveData<HomeState>(HomeState.Empty)

    fun getRecentManga(type: String) = viewModelScope.launch {
        databaseRepository.getRecentEntries(type).catch { e ->
            homeState.value = HomeState.Empty
        }.collect { recent ->
            homeState.postValue(
                if (recent.isEmpty()) {
                    HomeState.Empty
                } else {
                    HomeState.Success(recent as ArrayList<MediaDb>)
                }
            )
        }
    }
}
