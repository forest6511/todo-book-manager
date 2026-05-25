package com.example.bookmanager.controller

import com.example.bookmanager.entity.BookStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class BookRequest(
    @field:NotBlank
    @field:Size(max = 255)
    val title: String,

    @field:NotBlank
    @field:Size(max = 255)
    val author: String,

    @field:Size(max = 20)
    val isbn: String? = null,

    @field:Size(max = 500)
    val memo: String? = null,

    val status: BookStatus = BookStatus.UNREAD
)
