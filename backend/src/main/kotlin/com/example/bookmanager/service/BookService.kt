package com.example.bookmanager.service

import com.example.bookmanager.entity.Book
import com.example.bookmanager.entity.BookStatus
import com.example.bookmanager.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class BookService(private val bookRepository: BookRepository) {

    fun findAll(): List<Book> = bookRepository.findAll()

    fun findById(id: Long): Book =
        bookRepository.findById(id).orElseThrow { NoSuchElementException("書籍が見つかりません: id=$id") }

    fun findByStatus(status: BookStatus): List<Book> = bookRepository.findByStatus(status)

    fun search(query: String): List<Book> = bookRepository.findByTitleContainingIgnoreCase(query)

    fun create(book: Book): Book = bookRepository.save(book)

    fun update(id: Long, request: Book): Book {
        val existing = findById(id)
        existing.title = request.title
        existing.author = request.author
        existing.isbn = request.isbn
        existing.memo = request.memo
        existing.status = request.status
        existing.updatedAt = LocalDateTime.now()
        return bookRepository.save(existing)
    }

    fun delete(id: Long) {
        val book = findById(id)
        bookRepository.delete(book)
    }
}
