package com.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.CallLogging

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/tasks") {
            val newTask = call.receive<Task>()
           tasks.add(newTask) // add to the list
            call.respond(mapOf("message" to "Task added", "task" to newTask))
        }
    }
}
