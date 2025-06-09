package com.example

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import org.slf4j.event.Level
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

val tasks = mutableListOf(
    Task(1,"Call Mom",false),
    Task(2,"Call Friends",false)

)
fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureRouting()




    routing {
        get("/tasks"){
            call.respond(tasks)
        }

        post("/tasks") {
            val newTask = call.receive<Task>() // receive task from  client
            tasks.add(newTask) // add to the list
            call.respond(mapOf("message" to "Task added", "task" to newTask)) // send back confirmation
        }


        put("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(mapOf("error" to "Invalid ID"))
                return@put
            }

            val index = tasks.indexOfFirst { it.id == id }

            if (index == -1) {
                call.respond(mapOf("error" to "Task not found"))
                return@put
            }

            val updatedTask = call.receive<Task>()

            tasks[index] = updatedTask

            call.respond(mapOf("message" to "Task updated", "task" to updatedTask))
        }


    }

}

