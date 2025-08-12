package com.example.hellojavafx.dao




import com.example.hellojavafx.db.Books
import com.example.hellojavafx.model.Book
import com.example.hellojavafx.db.DatabaseFactory
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class BookDaoExposed {



    fun getAll(): List<Book> = transaction {
        Books.selectAll().map {
            Book(it[Books.id].value, it[Books.title], it[Books.author])
        }
    }

    fun insert(book: Book): Int = transaction {
        Books.insertAndGetId {
            it[title] = book.title
            it[author] = book.author
        }.value
    }

    fun update(book: Book) {
        require(book.id != null)
        transaction {
            Books.update({ Books.id eq book.id }) {
                it[title] = book.title
                it[author] = book.author
            }
        }
    }

    fun delete(id: Int) = transaction {
        Books.deleteWhere { Books.id eq id }
    }


}
