package utils.parsertools.combinators.token

import utils.parsertools.ast.AstLeaf
import utils.parsertools.ast.AstNode
import utils.parsertools.ast.Token
import utils.parsertools.combinators.Element
import utils.parsertools.combinators.TokenFactory
import utils.parsertools.exception.ParseException
import utils.parsertools.lex.Lexer

/**
 * Created by liufengkai on 2017/4/24.
 *
 * @author ice1000
 * @author lfkdsk
 */

abstract class AbstractToken(clazz: Class<out AstLeaf> = AstLeaf::class.java) : Element {

	private val factory = TokenFactory[clazz, Token::class.java]

	override fun parse(lexer: Lexer, nodes: MutableList<AstNode>) {
		val token = lexer.nextToken()

		if (tokenTest(token)) {
			val leaf = factory!!.make(token)
			nodes.add(leaf)
		} else throw ParseException(token.toString())
	}

	override fun match(lexer: Lexer) = tokenTest(lexer[0])

	/**
	 * 判断是否符合该类Token
	 * 标准的抽象方法

	 * @param token token
	 * *
	 * @return tof?
	 */
	protected abstract fun tokenTest(token: Token): Boolean

}