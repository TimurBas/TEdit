package token

class Token(val type: TokenType, var lexeme: String, private val literal: Any?, val line: Int) {
    override fun toString(): String {
        return "$type $lexeme $literal"
    }
}
