package com.example.nattramn.features.article.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.features.article.data.ArticleRepository
import com.example.nattramn.features.article.data.models.CommentRequest
import com.example.nattramn.features.article.data.models.SingleCommentRequest
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.article.ui.CommentView
import com.example.nattramn.features.user.data.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    private val articleRepository = ArticleRepository.getInstance()
    private val profileRepository = ProfileRepository.getInstance()

    private var _bookmarkResult = MutableLiveData<Resource<ArticleView>>()
    val bookmarkResult: LiveData<Resource<ArticleView>> get() = _bookmarkResult

    private var _removeBookmark = MutableLiveData<Resource<Unit>>()
    val removeBookmark: LiveData<Resource<Unit>> get() = _removeBookmark

    private var _sendCommentResult = MutableLiveData<Resource<Unit>>()
    val sendCommentResult: LiveData<Resource<Unit>> get() = _sendCommentResult

    private var _articleCommentsResult = MutableLiveData<Resource<List<CommentView>>>()
    val articleCommentsResult: LiveData<Resource<List<CommentView>>> get() = _articleCommentsResult

    private var _tagArticlesResult = MutableLiveData<Resource<List<ArticleView>>>()
    val tagArticlesResult: LiveData<Resource<List<ArticleView>>> get() = _tagArticlesResult

    private var _singleArticleResult = MutableLiveData<Resource<ArticleView>>()
    val singleArticleResult: LiveData<Resource<ArticleView>> get() = _singleArticleResult

    private var _userArticlesResult = MutableLiveData<Resource<List<ArticleView>>>()
    val userArticlesResult: LiveData<Resource<List<ArticleView>>> get() = _userArticlesResult

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

    fun sendComment(slug: String, comment: String) {

        val commentRequest = CommentRequest(SingleCommentRequest(comment))

        _sendCommentResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _sendCommentResult.postValue(articleRepository.sendComment(slug, commentRequest))
        }
    }

    fun applyLike(slug: String) = articleRepository.applyLike(slug)

    fun getLikedArticles() = articleRepository.getLikedArticles()

    fun getArticleComments(slug: String) {

        _articleCommentsResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _articleCommentsResult.postValue(articleRepository.getArticleComments(slug))
        }
    }

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

    fun getUserArticles(username: String) {

        _userArticlesResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _userArticlesResult.postValue(profileRepository.getUserArticles(username))
        }
    }

}