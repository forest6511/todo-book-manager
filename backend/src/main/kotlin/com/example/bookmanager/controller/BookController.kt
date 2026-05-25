package com.example.bookmanager.controller

import com.example.bookmanager.entity.Book
import com.example.bookmanager.entity.BookStatus
import com.example.bookmanager.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController(private val bookService: BookService) {

    @GetMapping
    fun findAll(): List<Book> = bookService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Book = bookService.findById(id)

    @GetMapping("/status/{status}")
    fun findByStatus(@PathVariable status: BookStatus): List<Book> = bookService.findByStatus(status)

    @GetMapping("/search")
    fun search(@RequestParam q: String): List<Book> = bookService.search(q)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: BookRequest): Book {
        val book = Book(
            title = request.title,
            author = request.author,
            isbn = request.isbn,
            memo = request.memo,
            status = request.status
        )
        return bookService.create(book)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: BookRequest): Book {
        val book = Book(
            title = request.title,
            author = request.author,
            isbn = request.isbn,
            memo = request.memo,
            status = request.status
        )
        return bookService.update(id, book)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = bookService.delete(id)
}
