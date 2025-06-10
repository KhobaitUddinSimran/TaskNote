package com.example

        import io.ktor.http.HttpStatusCode
        import io.ktor.server.application.*
        import io.ktor.server.request.*
        import io.ktor.server.response.*
        import io.ktor.server.routing.*

        fun Application.configureRouting() {
            routing {
//                get("/") {
//                    call.respondText("Hello World!")
//                }

                get("/tasks") {
                    call.respond(tasks)
                }

                post("/tasks") {
                    val newTask = call.receive<Task>()
                    tasks.add(newTask)
                    call.respond(status = HttpStatusCode.Created, message = mapOf("message" to "Task added", "task" to newTask))
                }


                put("/tasks/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()

                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))
                        return@put
                    }

                    val index = tasks.indexOfFirst { it.id == id }

                    if (index == -1) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Task not found"))
                        return@put
                    }

                    val updatedTask = call.receive<Task>()
                    tasks[index] = updatedTask

                    call.respond(HttpStatusCode.OK, mapOf("message" to "Task updated", "task" to updatedTask))
                }

                delete("/tasks/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()

                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))
                        return@delete
                    }

                    val removed = tasks.removeIf { it.id == id }

                    if (removed) {
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Task with ID $id deleted"))
                    } else {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Task not found"))
                    }
                }


            }
        }