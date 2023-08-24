package com.example.nattramn.features.user.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.features.article.data.ArticleRepository
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.user.data.ProfileRepository
import com.example.nattramn.features.user.ui.UserView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val profileRepository = ProfileRepository.getInstance()
    private val articleRepository = ArticleRepository.getInstance()

    private var _profileResult = MutableLiveData<Resource<UserView>>()
    val profileResult: LiveData<Resource<UserView>> get() = _profileResult

    private var _userArticlesResult = MutableLiveData<Resource<List<ArticleView>>>()
    val userArticlesResult: LiveData<Resource<List<ArticleView>>> get() = _userArticlesResult

    private var _profileBookmarkedArticlesResult = MutableLiveData<Resource<List<ArticleView>>>()
    val profileBookmarkedArticlesResult: LiveData<Resource<List<ArticleView>>> get() = _profileBookmarkedArticlesResult

    private var _bookmarkResult = MutableLiveData<Resource<ArticleView>>()
    val bookmarkResult: LiveData<Resource<ArticleView>> get() = _bookmarkResult

    private var _removeBookmark = MutableLiveData<Resource<Unit>>()
    val removeBookmark: LiveData<Resource<Unit>> get() = _removeBookmark

    private var _singleArticleResult = MutableLiveData<Resource<ArticleView>>()
    val singleArticleResult: LiveData<Resource<ArticleView>> get() = _singleArticleResult

    private var _deleteArticleResult = MutableLiveData<Resource<Response<Unit>?>>()
    val deleteArticleResult: LiveData<Resource<Response<Unit>?>> get() = _deleteArticleResult

    private var _followUserResult = MutableLiveData<Resource<UserView>>()
    val followUserResult: LiveData<Resource<UserView>> get() = _followUserResult

    private var _unFollowUserResult = MutableLiveData<Resource<UserView>>()
    val unFollowUserResult: LiveData<Resource<UserView>> get() = _unFollowUserResult

    private var _userArticles = MutableLiveData<List<ArticleView>>()
    val userArticles: LiveData<List<ArticleView>> get() = _userArticles

    private var _userBookmarks = MutableLiveData<List<ArticleView>>()
    val userBookmarks: LiveData<List<ArticleView>> get() = _userBookmarks

    fun logout() = articleRepository.logout()

    fun getUserDb(username: String) = profileRepository.getUserDb(username)

    fun getUserArticlesDb(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _userArticles.postValue(profileRepository.getUserArticlesDb(username))
        }
    }

    fun getBookmarkedArticlesDb() {
        viewModelScope.launch(Dispatchers.IO) {
            _userBookmarks.postValue(profileRepository.getBookmarkedArticlesDb())
        }
    }

    fun getSingleArticleDb(slug: String) = articleRepository.getSingleArticleDb(slug)

    fun getSingleArticle(slug: String) {

        _singleArticleResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _singleArticleResult.postValue(articleRepository.getSingleArticle(slug))
        }
    }

    fun deleteArticle(slug: String) {

        _deleteArticleResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _deleteArticleResult.postValue(profileRepository.deleteArticle(slug))
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

    fun getUserArticles(username: String) {

        _userArticlesResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _userArticlesResult.postValue(profileRepository.getUserArticles(username))
        }
    }

    fun setBookmarkedArticles(username: String) {

        _profileBookmarkedArticlesResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _profileBookmarkedArticlesResult.postValue(
                profileRepository.getBookmarkedArticles(username)
            )
        }
    }

    fun setProfile(username: String) {

        _profileResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _profileResult.postValue(profileRepository.getProfile(username))
        }
    }

    fun followUser(username: String) {

        _followUserResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _followUserResult.postValue(profileRepository.followUser(username))
        }
    }

    fun unFollowUser(username: String) {

        _unFollowUserResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _unFollowUserResult.postValue(profileRepository.unFollowUser(username))
        }
    }

}

