package com.example.nattramn.features.home.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.features.home.data.ArticleHomeRepository
import com.example.nattramn.features.home.data.models.AllTagsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeyWordsViewModel : ViewModel() {

    private val homeRepository = ArticleHomeRepository.getInstance()

    private var _allTagsResult = MutableLiveData<Resource<AllTagsResponse>>()
    val allTagsResult: MutableLiveData<Resource<AllTagsResponse>> get() = _allTagsResult

    fun getAllTagsDb() = homeRepository.getAllTagsDb()

    fun getAllTags() {

        _allTagsResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _allTagsResult.postValue(homeRepository.getAllTags())
        }
    }

}