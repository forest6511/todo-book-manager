package com.example.bookmanager.controller

import com.example.bookmanager.entity.Book
import com.example.bookmanager.entity.BookStatus
import com.example.bookmanager.service.BookService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest(BookController::class)
class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @MockitoBean
    private lateinit var bookService: BookService

    private val sampleBook = Book(
        id = 1,
        title = "リーダブルコード",
        author = "Dustin Boswell",
        isbn = "978-4873115658",
        memo = "コードの読みやすさに関する名著",
        status = BookStatus.COMPLETED,
        createdAt = LocalDateTime.of(2026, 1, 1, 0, 0),
        updatedAt = LocalDateTime.of(2026, 1, 1, 0, 0)
    )

    @Test
    fun `GET api books - 全件取得`() {
        whenever(bookService.findAll()).thenReturn(listOf(sampleBook))

        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].title").value("リーダブルコード"))
            .andExpect(jsonPath("$[0].author").value("Dustin Boswell"))
            .andExpect(jsonPath("$[0].status").value("COMPLETED"))
    }

    @Test
    fun `GET api books id - ID で取得`() {
        whenever(bookService.findById(1)).thenReturn(sampleBook)

        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("リーダブルコード"))
            .andExpect(jsonPath("$.isbn").value("978-4873115658"))
    }

    @Test
    fun `GET api books id - 存在しない ID で 404`() {
        whenever(bookService.findById(999)).thenThrow(NoSuchElementException("書籍が見つかりません: id=999"))

        mockMvc.perform(get("/api/books/999"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("書籍が見つかりません: id=999"))
    }

    @Test
    fun `GET api books status - ステータスでフィルター`() {
        whenever(bookService.findByStatus(BookStatus.READING)).thenReturn(
            listOf(sampleBook.copy(id = 2, title = "Clean Architecture", status = BookStatus.READING))
        )

        mockMvc.perform(get("/api/books/status/READING"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].status").value("READING"))
    }

    @Test
    fun `GET api books search - タイトル検索`() {
        whenever(bookService.search("コード")).thenReturn(listOf(sampleBook))

        mockMvc.perform(get("/api/books/search").param("q", "コード"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].title").value("リーダブルコード"))
    }

    @Test
    fun `POST api books - 新規登録`() {
        val request = BookRequest(
            title = "Effective Java",
            author = "Joshua Bloch",
            isbn = "978-4621303252",
            status = BookStatus.UNREAD,
            memo = null
        )
        whenever(bookService.create(any())).thenReturn(
            Book(id = 4, title = request.title, author = request.author, isbn = request.isbn, status = request.status)
        )

        mockMvc.perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(4))
            .andExpect(jsonPath("$.title").value("Effective Java"))
    }

    @Test
    fun `PUT api books id - 更新`() {
        val request = BookRequest(
            title = "リーダブルコード 改訂版",
            author = "Dustin Boswell",
            isbn = "978-4873115658",
            status = BookStatus.COMPLETED,
            memo = "再読了"
        )
        whenever(bookService.update(any(), any())).thenReturn(
            sampleBook.copy(title = request.title, memo = request.memo)
        )

        mockMvc.perform(
            put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("リーダブルコード 改訂版"))
            .andExpect(jsonPath("$.memo").value("再読了"))
    }

    @Test
    fun `DELETE api books id - 削除`() {
        doNothing().whenever(bookService).delete(1)

        mockMvc.perform(delete("/api/books/1"))
            .andExpect(status().isNoContent)
    }
}
