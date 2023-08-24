package com.example.nattramn.features.article.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.features.article.data.ArticleRepository
import com.example.nattramn.features.article.data.models.ArticleEdit
import com.example.nattramn.features.article.data.models.EditArticleRequest
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.home.data.ArticleHomeRepository
import com.example.nattramn.features.home.data.models.CreateArticleModel
import com.example.nattramn.features.home.data.models.CreateArticleRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteViewModel : ViewModel() {

    private val articleHomeRepository = ArticleHomeRepository.getInstance()
    private val articleRepository = ArticleRepository.getInstance()

    private var _editArticleResult = MutableLiveData<Resource<ArticleView>>()
    val editArticleResult: LiveData<Resource<ArticleView>> get() = _editArticleResult

    private var _createArticleResult = MutableLiveData<Resource<ArticleView>>()
    val createArticleResult: LiveData<Resource<ArticleView>> get() = _createArticleResult

    private var _singleArticleResult = MutableLiveData<Resource<ArticleView>>()
    val singleArticleResult: LiveData<Resource<ArticleView>> get() = _singleArticleResult

    fun getSingleArticle(slug: String) {

        _singleArticleResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _singleArticleResult.postValue(articleRepository.getSingleArticle(slug))
        }

    }

    fun createArticle(body: String, title: String, tags: List<String>) {

        val createArticleRequest = CreateArticleRequest(
            CreateArticleModel(
                title = title,
                description = "",
                body = body,
                tagList = tags
            )
        )

        _createArticleResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _createArticleResult.postValue(articleHomeRepository.createArticle(createArticleRequest))
        }
    }

    fun editArticle(body: String, slug: String) {

        val editArticleRequest = EditArticleRequest(
            ArticleEdit(
                body = body
            )
        )

        _editArticleResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            _editArticleResult.postValue(
                articleHomeRepository.editArticle(
                    editArticleRequest,
                    slug
                )
            )
        }
    }

    fun saveDraft(title: String, body: String) = articleRepository.saveDraft(title, body)

    fun getTitleDraft() = articleRepository.getTitleDraft()

    fun getBodyDraft() = articleRepository.getBodyDraft()

}