package com.example.hellojavafx

import com.sun.javafx.scene.control.skin.Utils.getResource
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import com.example.hellojavafx.db.DatabaseFactory

class BookTrackerApp : Application() {


    override fun start(stage: Stage) {
        DatabaseFactory.init()  // set up the DB once
        val fxml = javaClass.getResource("hello-view.fxml")
            ?: throw IllegalStateException("FXML file not found: hello-view.fxml")

        val loader = FXMLLoader(fxml)
        val root = loader.load<Parent>()

        stage.scene = Scene(root)
        stage.title = "Book Tracker"
        stage.show()
    }
}

fun main() {
    Application.launch(BookTrackerApp::class.java)
}
