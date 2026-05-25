package com.example.bookmanager.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 255)
    var title: String = "",

    @Column(nullable = false, length = 255)
    var author: String = "",

    @Column(length = 20)
    var isbn: String? = null,

    @Column(length = 500)
    var memo: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: BookStatus = BookStatus.UNREAD,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class BookStatus {
    UNREAD,    // 未読
    READING,   // 読書中
    COMPLETED  // 読了
}
