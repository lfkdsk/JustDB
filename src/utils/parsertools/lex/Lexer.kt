package utils.parsertools.lex

import utils.parsertools.ast.Token

/**
 * Created by liufengkai on 2017/4/24.
 *
 * @author lfkdsk
 * @author ice1000
 */

interface Lexer {

	fun nextToken(): Token = tokenAt(0)

	fun tokenAt(num: Int): Token

	operator fun get(num: Int) = tokenAt(num)
}