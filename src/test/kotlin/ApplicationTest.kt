package com.example

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*


class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}
