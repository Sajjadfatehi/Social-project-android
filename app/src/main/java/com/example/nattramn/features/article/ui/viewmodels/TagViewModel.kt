package com.example.nattramn.features.article.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.features.article.data.ArticleRepository
import com.example.nattramn.features.article.ui.ArticleView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagViewModel : ViewModel() {

    private val articleRepository = ArticleRepository.getInstance()

    private var _tagArticlesResult = MutableLiveData<Resource<List<ArticleView>>>()
    val tagArticlesResult: LiveData<Resource<List<ArticleView>>> get() = _tagArticlesResult

    private var _singleArticleResult = MutableLiveData<Resource<ArticleView>>()
    val singleArticleResult: LiveData<Resource<ArticleView>> get() = _singleArticleResult

    private var _bookmarkResult = MutableLiveData<Resource<ArticleView>>()
    val bookmarkResult: LiveData<Resource<ArticleView>> get() = _bookmarkResult

    private var _removeBookmark = MutableLiveData<Resource<Unit>>()
    val removeBookmark: LiveData<Resource<Unit>> get() = _removeBookmark

    fun getSingleArticleDb(tag: String) = articleRepository.getSingleArticleDb(tag)

    fun getTagArticlesDb(tag: String) = articleRepository.getTagArticlesDb(tag)

    fun getTagArticles(tag: String) {

        _tagArticlesResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _tagArticlesResult.postValue(articleRepository.getTagArticles(tag))
        }
    }

    fun getSingleArticle(slug: String) {

        _singleArticleResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _singleArticleResult.postValue(articleRepository.getSingleArticle(slug))
        }
    }

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
}