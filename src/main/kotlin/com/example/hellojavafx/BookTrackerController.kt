package com.example.hellojavafx

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.Label
import java.awt.print.Book
data class Book(val title: String, val author: String)
class BookTrackerController  {
    @FXML lateinit var tableView: TableView<Book>
    @FXML lateinit var titleColumn: TableColumn<Book, String>
    @FXML lateinit var authorColumn: TableColumn<Book, String>

    @FXML lateinit var btnAdd: Button
    @FXML lateinit var btnEdit: Button
    @FXML lateinit var btnDelete: Button
    @FXML lateinit var btnBackup: Button
    @FXML lateinit var btnRestore: Button

}