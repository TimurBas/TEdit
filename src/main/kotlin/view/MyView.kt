package view

import controller.MyController
import javafx.scene.control.TextArea
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import token.Token
import tornadofx.*
import kotlin.system.exitProcess

class MyView : View("My View"), Observer {
    private val regex: Regex = """[\x00-\x7F]""".toRegex()

    private val textArea: TextArea by fxid("textArea")
    private val savedStatus: Rectangle by fxid("savedStatus")
    private val controller: MyController by inject()
    override val root: BorderPane by fxml()

    fun open() {
        controller.open()
    }

    fun save() {
        val content: String = textArea.text
        controller.save(content)
    }

    fun exit() {
        exitProcess(0)
    }

    fun keyPressed(event: KeyEvent) {
        val character = event.text
        when {
            event.isControlDown && character == "s" -> save()
            regex.matches(character) -> updateSaveStatus()
            else -> {}
        }
    }

    fun showAbout() {

    }

    override fun updateTextArea(tokens: List<Token>) {
        val content = createContent(tokens)
        textArea.text = content
    }

    private fun createContent(tokens: List<Token>): String {
        return "hello"
    }

/*    private fun getLexemes(content: List<Token>): List<String> {
        val lexemes: MutableList<String> = mutableListOf()
        for (token in content) lexemes.add(token.lexeme)
        return lexemes
    }

    private fun getTokenTypes(content: List<Token>): List<TokenType> {
        val tokenTypes: MutableList<TokenType> = mutableListOf()
        for (token in content) tokenTypes.add(token.type)
        return tokenTypes
    }*/

    override fun saveSuccessful() {
        savedStatus.fill = Paint.valueOf("#23751f")
    }

    override fun updateSaveStatus() {
        savedStatus.fill = Paint.valueOf("#751f1f")
    }
}
