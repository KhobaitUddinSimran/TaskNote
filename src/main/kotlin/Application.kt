package com.example

        import io.ktor.serialization.kotlinx.json.json
        import io.ktor.server.application.*
        import io.ktor.server.engine.embeddedServer
        import io.ktor.server.netty.Netty
        import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

val tasks = mutableListOf(
            Task(1, "Call Mom", false),
            Task(2, "Call Friends", false)
        )

  // In your Application.kt file
  fun Application.configureContentNegotiation() {
      install(ContentNegotiation) {
          json()
      }
  }

  // Make sure to call this in your main function:
  fun main() {
      embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
          configureContentNegotiation() // Add this line
          configureRouting()
          // other configurations...
      }.start(wait = true)
  }
        fun Application.module() {
            configureSerialization()
            configureMonitoring()
            configureRouting()
        }