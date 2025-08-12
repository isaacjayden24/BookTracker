package com.example.hellojavafx

import com.example.hellojavafx.dao.BookDaoExposed
import com.example.hellojavafx.model.Book
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.*
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javafx.stage.FileChooser
import javafx.stage.Stage


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

        val titleDialog = TextInputDialog()
        titleDialog.title = "Add Book"
        titleDialog.headerText = "Enter book title"
        val titleResult = titleDialog.showAndWait()

        if (titleResult.isPresent) {
            val authorDialog = TextInputDialog()
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


    @FXML
    fun onEditClicked() {
        val selectedBook = tableView.selectionModel.selectedItem
        if (selectedBook != null) {
            // Edit title
            val titleDialog = TextInputDialog(selectedBook.title)
            titleDialog.title = "Edit Book"
            titleDialog.headerText = "Update book title"
            val newTitle = titleDialog.showAndWait()

            if (newTitle.isPresent) {
                // Edit author
                val authorDialog = TextInputDialog(selectedBook.author)
                authorDialog.title = "Edit Book"
                authorDialog.headerText = "Update book author"
                val newAuthor = authorDialog.showAndWait()

                if (newAuthor.isPresent) {
                    val updatedBook = selectedBook.copy(
                        title = newTitle.get(),
                        author = newAuthor.get()
                    )
                    dao.update(updatedBook)
                    refreshTable()
                }
            }
        } else {
            println("No book selected for editing.")
        }
    }


    @FXML
    fun onDeleteClicked() {
        val selectedBook = tableView.selectionModel.selectedItem
        if (selectedBook != null) {
            selectedBook.id?.let { dao.delete(it) }
            refreshTable()
        } else {
            println("No book selected for deletion.")
        }
    }




    @FXML
    fun onBackupClicked() {
        val dbFile = File("books.db") //load the database to back up

        if (!dbFile.exists()) {
            println("Database file not found!")
            return
        }

        val backupDir = File(System.getProperty("user.home"), "Documents/Backups")
        if (!backupDir.exists()) backupDir.mkdirs()

        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        val backupFile = File(backupDir, "books_backup_$timestamp.db")

        Files.copy(dbFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        println("Backup created: ${backupFile.absolutePath}")
    }
    @FXML
    fun onRestoreClicked(event: ActionEvent) {
        val stage = (event.source as Node).scene.window as Stage

        val fileChooser = FileChooser()
        fileChooser.title = "Select Backup File"
        fileChooser.initialDirectory = File(System.getProperty("user.home"), "Documents/Backups")
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Database Backup (*.db)", "*.db"))

        val selectedFile = fileChooser.showOpenDialog(stage)

        if (selectedFile != null) {
            val dbFile = File("books.db")
            Files.copy(selectedFile.toPath(), dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            println("Database restored from backup: ${selectedFile.name}")
            refreshTable()
        } else {
            println("Restore canceled.")
        }
    }


}
