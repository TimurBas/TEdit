package model

import javafx.stage.FileChooser
import lexer.Lexer
import token.Token
import view.Observer
import java.io.File

class TextEditor(private val observerList: MutableList<Observer> = mutableListOf()) {
    private var file: File? = null

    fun open() {
        val fileChooser = FileChooser()
        fileChooser.title = "Choose a file"
        file = fileChooser.showOpenDialog(null)
        val content: String = file!!.readText()
        val tokens: List<Token> = createTokens(content)
        observerList.forEach { it.updateTextArea(tokens) }
    }

    private fun createTokens(content: String): List<Token> {
        val lexer = Lexer(content)
        lexer.scanTokens()
        return lexer.tokens
    }

    fun save(content: String) {
        file?.writeText(content)
        observerList.forEach { it.saveSuccessful() }
    }
}