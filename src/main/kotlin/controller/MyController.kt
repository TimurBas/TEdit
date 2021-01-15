package controller

import javafx.event.ActionEvent
import javafx.scene.input.KeyEvent
import model.TextEditor
import tornadofx.Controller
import view.MyView

class MyController : Controller() {
    private val view: MyView by inject()
    private val editor: TextEditor = TextEditor(mutableListOf(view))

    fun open() {
        editor.open()
    }

    fun save(content: String) {
        editor.save(content)
    }
}