package com.example.nattramn.features.user.ui

interface OnBottomSheetItemsClick {

    fun onShareArticle(slug: String)

    fun onDeleteArticle(slug: String, position: Int)

    fun onEditArticle(slug: String)

}