package com.example.bookmanager.controller

import com.example.bookmanager.entity.BookStatus

data class BookRequest(
    val title: String,
    val author: String,
    val isbn: String? = null,
    val memo: String? = null,
    val status: BookStatus = BookStatus.UNREAD
)
