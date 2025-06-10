package com.example


import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val message: String,
    val tasks: List<Task> = emptyList()
)

@Serializable
data class ErrorResponse(
    val error: String
)

@Serializable
data class TaskResponse(
    val message: String,
    val task: Task
) {
}