package com.example



import org.jetbrains.exposed.sql.Table

object Tasks : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 100)
    val description = varchar("description", 500)
    val isDone = bool("is_done").default(false)

    override val primaryKey = PrimaryKey(id)
}