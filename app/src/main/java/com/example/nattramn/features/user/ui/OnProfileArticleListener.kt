package com.example.nattramn.features.user.ui

interface OnProfileArticleListener {

    fun onProfileArticleCardClick(slug: String)

    fun onBookmarkClick(slug: String, isBookmarked: Boolean, position: Int, item: String)

    fun onMoreOptionsClick(slug: String, position: Int)

    fun onAuthorIconClick(username: String)

    fun onAuthorNameClick(username: String)

    fun onArticleCommentsClick(position: Int)

    fun onArticleTitleClick(slug: String)

    fun onArticleDescriptionClick(slug: String)

}