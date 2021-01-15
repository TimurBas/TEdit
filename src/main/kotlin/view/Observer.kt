package view

import token.Token

interface Observer {
    fun updateTextArea(tokens: List<Token>)
    fun saveSuccessful()
    fun updateSaveStatus()
}
