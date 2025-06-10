package com.example

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object DatabaseFactory {
    private val logger = LoggerFactory.getLogger(DatabaseFactory::class.java)

    fun init(environment: ApplicationEnvironment) {
        try {
            val config = environment.config.config("database")
            val hikariConfig = HikariConfig().apply {
                jdbcUrl = config.property("url").getString()
                driverClassName = config.property("driver").getString()
                username = config.property("user").getString()
                password = config.property("password").getString()
                maximumPoolSize = 10
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"

                // Additional recommended settings
                connectionTimeout = 30000
                idleTimeout = 600000
                maxLifetime = 1800000
            }

            Database.connect(HikariDataSource(hikariConfig))
            logger.info("Database connection initialized successfully")

        } catch (e: Exception) {
            logger.error("Failed to initialize database connection", e)
            throw e
        }
        transaction {
            SchemaUtils.create(Tasks) // <-- Creates the table if not exists
        }
    }


}