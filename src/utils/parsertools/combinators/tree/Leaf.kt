package utils.parsertools.combinators.tree

import utils.parsertools.ast.AstLeaf
import utils.parsertools.ast.AstNode
import utils.parsertools.ast.Token
import utils.parsertools.combinators.Element
import utils.parsertools.exception.ParseException
import utils.parsertools.lex.Lexer


/**
 * Created by liufengkai on 2017/4/24.
 *
 * @author ice1000
 * @author lfkdsk
 */

open class Leaf(val tokens: List<String>) : Element {

	override fun parse(lexer: Lexer, nodes: MutableList<AstNode>) {
		val token = lexer.nextToken()

		if (token.isIdentifier()) {
			tokens.filter { it == token.text }
					.forEach {
						find(nodes, token)
						return
					}
		}

		if (tokens.count() > 0) {
			throw ParseException(tokens[0] + " expected. ", token)
		} else {
			throw ParseException("", token)
		}
	}

	/**
	 * 添加终结符

	 * @param list  list
	 * *
	 * @param token 终结符对应token
	 */
	protected open fun find(list: MutableList<AstNode>, token: Token) {
		list.add(AstLeaf(token))
	}

	override fun match(lexer: Lexer): Boolean {
		val token = lexer[0]
		return token.isIdentifier() && tokens.any { it == token.text }
	}
}