package com.example.nattramn.features.article.ui

interface OnArticleListener {

    fun onCardClick(slug: String)

    fun onArticleTitleClick(slug: String)

    fun onArticleSaveClick(slug: String, isBookmarked: Boolean, position: Int, item: String)

    fun onAuthorNameClick(username: String)

    fun onAuthorIconClick(username: String)

}