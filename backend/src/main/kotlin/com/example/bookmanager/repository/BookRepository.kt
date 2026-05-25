package com.example.bookmanager.repository

import com.example.bookmanager.entity.Book
import com.example.bookmanager.entity.BookStatus
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long> {
    fun findByStatus(status: BookStatus): List<Book>
    fun findByTitleContainingIgnoreCase(title: String): List<Book>
}
