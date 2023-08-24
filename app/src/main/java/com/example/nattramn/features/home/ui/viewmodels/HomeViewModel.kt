package com.example.nattramn.features.home.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.features.article.data.ArticleRepository
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.home.data.ArticleHomeRepository
import com.example.nattramn.features.user.data.ProfileRepository
import com.example.nattramn.features.user.ui.UserView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val articleHomeRepository = ArticleHomeRepository.getInstance()
    private val articleRepository = ArticleRepository.getInstance()
    private val profileRepository = ProfileRepository.getInstance()

    private var _singleArticleResult = MutableLiveData<Resource<ArticleView>>()
    val singleArticleResult: LiveData<Resource<ArticleView>> get() = _singleArticleResult

    private var _feedResult = MutableLiveData<Resource<List<ArticleView>>>()
    val feedResult: LiveData<Resource<List<ArticleView>>> get() = _feedResult

    private var _latestArticlesResult = MutableLiveData<Resource<List<ArticleView>>>()
    val latestArticlesResult: LiveData<Resource<List<ArticleView>>> get() = _latestArticlesResult

    private var _bookmarkResult = MutableLiveData<Resource<ArticleView>>()
    val bookmarkResult: LiveData<Resource<ArticleView>> get() = _bookmarkResult

    private var _removeBookmark = MutableLiveData<Resource<Unit>>()
    val removeBookmark: LiveData<Resource<Unit>> get() = _removeBookmark

    private var _profileResult = MutableLiveData<Resource<UserView>>()
    val profileResult: LiveData<Resource<UserView>> get() = _profileResult

    fun setFeedArticles() {

        _feedResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _feedResult.postValue(articleHomeRepository.getFeedArticles())
        }
    }

    fun setLatestArticles() {

        _latestArticlesResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _latestArticlesResult.postValue(articleHomeRepository.getAllArticles())
        }
    }

    fun setLatestArticlesDb() = articleHomeRepository.getAllArticlesDb()

    fun setFeedArticlesDb() = articleHomeRepository.getFeedArticlesDb()

    fun getSingleArticle(slug: String) {

        _singleArticleResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _singleArticleResult.postValue(articleRepository.getSingleArticle(slug))
        }

    }

    fun getSingleArticleDb(slug: String) = articleRepository.getSingleArticleDb(slug)

    fun bookmarkArticle(slug: String) {

        _bookmarkResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _bookmarkResult.postValue(articleRepository.bookmarkArticle(slug))
        }
    }

    fun removeFromBookmarks(slug: String) {

        _removeBookmark.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _removeBookmark.postValue(articleRepository.removeFromBookmarks(slug))
        }
    }

    fun saveUserInfo(username: String) {

        _profileResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _profileResult.postValue(profileRepository.getProfile(username))
        }
    }

}