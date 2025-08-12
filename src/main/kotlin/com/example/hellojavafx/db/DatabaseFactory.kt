package com.example.hellojavafx.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

//Table definition
object Books : IntIdTable("books") {
    val title = varchar("title", 255)
    val author = varchar("author", 255)
}

object DatabaseFactory {
    fun init(dbFilePath: String = "books.db") {
        val url = "jdbc:sqlite:$dbFilePath"

        //connect to SQlite
        Database.connect(url, driver = "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(Books) // creates table if missing
        }
    }
}