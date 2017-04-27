package sql.lexer

import java.io.StringReader

/**
 * Created by liufengkai on 2017/4/27.
 */
fun main(args: Array<String>) {
	val lexer: SqlLexer = SqlLexer(StringReader("lfkdsk fkkkkkk"))
	lexer.nextToken()
	lexer.nextToken()
	lexer.nextToken()
	lexer.nextToken()
	lexer.nextToken()
}