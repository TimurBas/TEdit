package lexer

import token.Token
import token.TokenType
import token.TokenType.*

class Lexer(private val source: String) {
    private var error: Boolean = false
    var tokens: MutableList<Token> = mutableListOf()
    private var startChar: Int = 0
    private var currentChar: Int = 0
    private var line: Int = 1
    private val alphaRegex: Regex = Regex("[a-zA-Z_]")
    private var keywords: Map<String, TokenType> = hashMapOf(
        "and"     to     AND,
        "class"   to     CLASS,
        "else"    to     ELSE,
        "false"   to     FALSE,
        "for"     to     FOR,
        "fun"     to     FUN,
        "if"      to     IF,
        "null"     to    NULL,
        "or"      to     OR,
        "print"   to     PRINT,
        "return"  to     RETURN,
        "true"    to     TRUE,
        "var"     to     VAR,
        "while"   to     WHILE
    )

    fun scanTokens(): List<Token> {
        while (!isEOF()) {
            // We are at the beginning of the next lexeme.
            startChar = currentChar
            scanNextToken()
        }
        tokens.add(Token(EOF, "", null, line))
        return tokens
    }

    private fun scanNextToken() {
        val character: Char = advanceToNextCharacter()
        when (character) {
            '(' -> addToken(LEFT_PARENTHESES)
            ')' -> addToken(RIGHT_PARENTHESES)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '!' -> addToken(if (match('=')) NEGATION_EQUAL else NEGATION)
            '=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
            '<' -> addToken(if (match('=')) LESS_EQUAL else LESS)
            '>' -> addToken(if (match('=')) GREATER_EQUAL else GREATER)
            '/' ->
                if (match('/')) {
                    val character: Char = peekCharacter()
                    while (character != '\n' && !isEOF())
                        advanceToNextCharacter()
                } else
                    addToken(SLASH)
            '\n' -> line++
            ' ', '\r', '\t' -> {}
            '"' -> string()
            else -> when {
                character.isDigit() -> number()
                isAlpha(character) -> identifier()
                else -> error(line, "Unexpected character $character")
            }
        }
    }

    private fun isEOF(): Boolean {
        return currentChar >= source.length
    }

    private fun peekCharacter(): Char {
        if (isEOF()) return '\u+000'
        return source[currentChar]
    }

    private fun peekNextCharacter(): Char {
        if (currentChar + 1 >= source.length) return '\u+000'
        return source[currentChar + 1]
    }

    private fun advanceToNextCharacter(): Char {
        currentChar++
        return source[currentChar - 1]
    }

    private fun isAlphaNumeric(character: Char): Boolean {
        return isAlpha(character) || character.isDigit()
    }

    private fun isAlpha(character: Char): Boolean {
        return alphaRegex matches character.toString()
    }

    private fun match(expected: Char): Boolean {
        if (isEOF()) return false

        val isCharacterTheExpected: Boolean = source[currentChar] != expected
        if (isCharacterTheExpected) return false

        currentChar++
        return true
    }

    private fun identifier() {
        while (isAlphaNumeric(peekCharacter())) advanceToNextCharacter()

        val text = source.substring(startChar, currentChar)
        var type = keywords[text]
        if (type == null) type = IDENTIFIER
        addToken(type)
    }

    private fun number() {
        val peekedCharacter: Char = peekCharacter()
        while (peekedCharacter.isDigit()) advanceToNextCharacter()

        val nextPeekedCharacter: Char = peekNextCharacter()
        if (peekCharacter() == '.' && nextPeekedCharacter.isDigit()) {
            advanceToNextCharacter()

            val peekedCharacter: Char = peekCharacter()
            while (peekedCharacter.isDigit()) advanceToNextCharacter()
        }

        addToken(NUMBER, source.substring(startChar, currentChar).toDouble())
    }

    private fun string() {
        while (peekCharacter() != '"' && !isEOF()) {
            if (peekCharacter() == '\n') line++
            advanceToNextCharacter()
        }

        if (isEOF()) {
            error(line, "Unterminated string.")
            return
        }

        // The closing "
        advanceToNextCharacter()

        // Trim the surrounding quotes.
        val value = source.substring(startChar + 1, currentChar - 1)
        addToken(STRING, value)
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?) {
        val text = source.substring(startChar, currentChar)
        tokens.add(Token(type, text, literal, line))
    }

    private fun error(line: Int, message: String) {
        report(line, "", message)
    }

    private fun report(line: Int, where: String, message: String) {
        println("[Line $line] Error $where: $message")
        error = true
    }
}
