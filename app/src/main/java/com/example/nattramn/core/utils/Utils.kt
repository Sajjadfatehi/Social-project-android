package com.example.nattramn.core.utils

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nattramn.R
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.core.resource.Status
import com.example.nattramn.features.article.data.entities.ArticleEntity
import com.example.nattramn.features.article.data.entities.CommentEntity
import com.example.nattramn.features.article.data.entities.TagEntity
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.user.data.UserEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

fun snackMaker(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
}

suspend inline fun <T> safeApiCall(responseFunction: suspend () -> T): Resource<T> {
    return try {
        Resource.success(responseFunction.invoke())
    } catch (e: HttpException) {
        Resource.error(e.code().toString(), null)
    } catch (e: Exception) {
        Resource.error("vpn disconnected", null)
    }
}

fun <T, A> performGetOperation(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {

        emit(Resource.loading(null))
        val source = databaseQuery.invoke().map { Resource.success<T>(it) }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)

        } else if (responseStatus.status == Status.ERROR) {
            emit(Resource.error(responseStatus.message!!, null))
            emitSource(source)
        }
    }

fun toArticleView(
    userEntity: UserEntity?,
    articleEntity: ArticleEntity?,
    tagsEntity: List<TagEntity>?,
    commentsEntity: List<CommentEntity>?
): ArticleView {
    return ArticleView(
        userView = userEntity!!.toUserView(),
        date = articleEntity!!.date,
        title = articleEntity.title,
        body = articleEntity.body,
        likes = articleEntity.likes.toInt(),
        tags = tagsEntity!!.map { it.toTagString() },
        commentViews = commentsEntity?.map { it.toCommentView() }!!,
        commentsNumber = commentsEntity.size,
        bookmarked = articleEntity.bookmarked,
        slug = articleEntity.slug
    )
}

fun load(view: ImageView, image: Int) {
    Glide.with(view.context)
        .load(image)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_bookmark_article_fragment)
        )
        .into(view)
}

fun View.setOnSingleClickListener(l: View.OnClickListener) {
    setOnClickListener(OnSingleClickListener(l))
}

fun View.setOnSingleClickListener(l: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(l))
}

val faNumbers =
    arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")