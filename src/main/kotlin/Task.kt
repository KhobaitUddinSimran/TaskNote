package com.example
import kotlinx.serialization.Serializable
import java.util.concurrent.atomic.AtomicInteger

val nextId = AtomicInteger(1) // starts from 1

@Serializable
data class Task(
    val id: Int? = null,
    val title: String = "",
    val description: String = "",
    val isDone: Boolean = false
)

