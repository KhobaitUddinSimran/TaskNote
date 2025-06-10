package com.example

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val repo = TaskRepository()

    routing {
        get("/tasks") {
            try {
                val tasks = repo.getAllTasks()
                if (tasks.isEmpty()) {
                    call.respond(HttpStatusCode.OK, MessageResponse("No tasks found"))
                } else {
                    call.respond(HttpStatusCode.OK, tasks)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Failed to retrieve tasks: ${e.message}"))
            }
        }

        post("/tasks") {
            try {
                val newTask = try {
                    call.receive<Task>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid task format"))
                    return@post
                }

                // Validate task
                if (newTask.title.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Title must not be empty"))
                    return@post
                }

                val savedTask = repo.addTask(newTask)
                call.respond(HttpStatusCode.Created, TaskResponse("Task added", savedTask))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Failed to add task: ${e.message}"))
            }
        }

        put("/tasks/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid ID format"))
                    return@put
                }

                val updatedTask = try {
                    call.receive<Task>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid task format"))
                    return@put
                }

                // Validate fields
                if (updatedTask.title.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Title must not be empty"))
                    return@put
                }

                val success = repo.updateTask(id, updatedTask)
                if (success) {
                    call.respond(HttpStatusCode.OK, TaskResponse("Task updated", updatedTask))
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Task with ID $id not found"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Failed to update task: ${e.message}"))
            }
        }

        delete("/tasks/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid ID format"))
                    return@delete
                }

                val success = repo.deleteTask(id)
                if (success) {
                    call.respond(HttpStatusCode.OK, MessageResponse("Task with ID $id deleted"))
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Task with ID $id not found"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Failed to delete task: ${e.message}"))
            }
        }
    }
}