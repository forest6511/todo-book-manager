package com.example.bookmanager.service

import com.example.bookmanager.entity.Book
import com.example.bookmanager.entity.BookStatus
import com.example.bookmanager.repository.BookRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
class BookServiceTest {

    @Mock
    private lateinit var bookRepository: BookRepository

    @InjectMocks
    private lateinit var bookService: BookService

    private val sampleBook = Book(
        id = 1,
        title = "リーダブルコード",
        author = "Dustin Boswell",
        isbn = "978-4873115658",
        status = BookStatus.COMPLETED
    )

    @Test
    fun `findAll - 全件取得`() {
        whenever(bookRepository.findAll()).thenReturn(listOf(sampleBook))

        val result = bookService.findAll()

        assertEquals(1, result.size)
        assertEquals("リーダブルコード", result[0].title)
    }

    @Test
    fun `findById - 存在する ID`() {
        whenever(bookRepository.findById(1)).thenReturn(Optional.of(sampleBook))

        val result = bookService.findById(1)

        assertEquals("リーダブルコード", result.title)
    }

    @Test
    fun `findById - 存在しない ID で例外`() {
        whenever(bookRepository.findById(999)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> {
            bookService.findById(999)
        }
        assertEquals("書籍が見つかりません: id=999", exception.message)
    }

    @Test
    fun `findByStatus - ステータスでフィルター`() {
        whenever(bookRepository.findByStatus(BookStatus.READING)).thenReturn(
            listOf(sampleBook.copy(status = BookStatus.READING))
        )

        val result = bookService.findByStatus(BookStatus.READING)

        assertEquals(1, result.size)
        assertEquals(BookStatus.READING, result[0].status)
    }

    @Test
    fun `search - タイトル部分一致検索`() {
        whenever(bookRepository.findByTitleContainingIgnoreCase("コード")).thenReturn(listOf(sampleBook))

        val result = bookService.search("コード")

        assertEquals(1, result.size)
    }

    @Test
    fun `create - 新規作成`() {
        whenever(bookRepository.save(any<Book>())).thenReturn(sampleBook)

        val result = bookService.create(sampleBook)

        assertEquals("リーダブルコード", result.title)
        verify(bookRepository).save(any<Book>())
    }

    @Test
    fun `update - 既存レコードの更新`() {
        val updateData = sampleBook.copy(title = "改訂版リーダブルコード", status = BookStatus.READING)
        whenever(bookRepository.findById(1)).thenReturn(Optional.of(sampleBook))
        whenever(bookRepository.save(any<Book>())).thenReturn(updateData)

        val result = bookService.update(1, updateData)

        assertEquals("改訂版リーダブルコード", result.title)
    }

    @Test
    fun `delete - 既存レコードの削除`() {
        whenever(bookRepository.findById(1)).thenReturn(Optional.of(sampleBook))

        bookService.delete(1)

        verify(bookRepository).delete(sampleBook)
    }

    @Test
    fun `delete - 存在しない ID で例外`() {
        whenever(bookRepository.findById(999)).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            bookService.delete(999)
        }
    }
}
