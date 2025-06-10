package com.example

            import org.jetbrains.exposed.sql.*
            import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
            import org.jetbrains.exposed.sql.transactions.transaction

            class TaskRepository {

                fun getAllTasks(): List<Task> = transaction {
                    Tasks.selectAll().map {
                        Task(
                            id = it[Tasks.id],
                            title = it[Tasks.title],
                            description = it[Tasks.description],
                            isDone = it[Tasks.isDone]  // This should match the column name
                        )
                    }
                }

                fun addTask(task: Task): Task = transaction {
                    val id = Tasks.insert {
                        it[title] = task.title
                        it[description] = task.description
                        it[isDone] = task.isDone
                    } get Tasks.id

                    task.copy(id = id)
                }

                fun updateTask(id: Int, updatedTask: Task): Boolean = transaction {
                    Tasks.update({ Tasks.id eq id }) {
                        it[title] = updatedTask.title
                        it[description] = updatedTask.description
                        it[isDone] = updatedTask.isDone
                    } > 0
                }

                fun deleteTask(id: Int): Boolean = transaction {
                    Tasks.deleteWhere { Tasks.id eq id } > 0
                }

                fun getTaskById(id: Int): Task? = transaction {
                    Tasks.select { Tasks.id eq id }.map {
                        Task(
                            id = it[Tasks.id],
                            title = it[Tasks.title],
                            description = it[Tasks.description],
                            isDone = it[Tasks.isDone]
                        )
                    }.singleOrNull()
                }
            }