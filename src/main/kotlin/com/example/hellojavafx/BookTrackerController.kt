package com.example.hellojavafx

import com.example.hellojavafx.dao.BookDaoExposed
import com.example.hellojavafx.model.Book
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.Label



class BookTrackerController  {
    private val dao = BookDaoExposed()

    @FXML lateinit var tableView: TableView<Book>
    @FXML lateinit var titleColumn: TableColumn<Book, String>
    @FXML lateinit var authorColumn: TableColumn<Book, String>

    @FXML lateinit var btnAdd: Button
    @FXML lateinit var btnEdit: Button
    @FXML lateinit var btnDelete: Button
    @FXML lateinit var btnBackup: Button
    @FXML lateinit var btnRestore: Button

    @FXML
    fun initialize() {
        // Bind columns
        titleColumn.setCellValueFactory { SimpleStringProperty(it.value.title) }
        authorColumn.setCellValueFactory { SimpleStringProperty(it.value.author) }

        // Load data
        refreshTable()
    }
    private fun refreshTable() {
        val books = dao.getAll() // fetch from DB
        tableView.items.setAll(books)
    }

    @FXML
    fun onAddClicked() {
        val titleDialog = javafx.scene.control.TextInputDialog()
        titleDialog.title = "Add Book"
        titleDialog.headerText = "Enter book title"
        val titleResult = titleDialog.showAndWait()

        if (titleResult.isPresent) {
            val authorDialog = javafx.scene.control.TextInputDialog()
            authorDialog.title = "Add Book"
            authorDialog.headerText = "Enter book author"
            val authorResult = authorDialog.showAndWait()

            if (authorResult.isPresent) {
                val newBook = Book(
                    id = 0, // DB generates this
                    title = titleResult.get(),
                    author = authorResult.get()
                )

                dao.insert(newBook) // Save to DB
                refreshTable()   // Refresh table
            }
        }
    }

}
