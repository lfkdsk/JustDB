package sql.parser

import sql.lexer.SqlLexer
import sql.token.SqlToken
import java.io.FileReader

/**
 * Created by liufengkai on 2017/4/28.
 */
fun main(args: Array<String>) {
	val lexer = SqlLexer(FileReader("./tests/sql/parser/sqltext"))

	val parser = SqlParser()

	while (lexer.tokenAt(0) != SqlToken.EOF) {
		val node = parser.parse(lexer)
		println(" ====> " + node.toString())
	}
}
