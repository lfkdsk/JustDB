package utils.parsertools.combinators.tree

import utils.parsertools.ast.AstNode
import utils.parsertools.combinators.Bnf
import utils.parsertools.combinators.Element
import utils.parsertools.exception.ParseException
import utils.parsertools.lex.Lexer

/**
 * Created by liufengkai on 2017/4/24.
 */

open class OrTree(private val bnfList: List<Bnf>) : Element {

	override fun parse(lexer: Lexer, nodes: MutableList<AstNode>) {
		val parser = choose(lexer)
		if (parser == null) {
			throw ParseException("or tree cannot find parser for" +
					" : ${lexer.tokenAt(0).text}" +
					" at : ${lexer.tokenAt(0).lineNumber} line")
		} else {
			nodes.add(parser.parse(lexer))
		}
	}

	override fun match(lexer: Lexer): Boolean {
		return choose(lexer) != null
	}

	protected fun choose(lexer: Lexer): Bnf? {
		bnfList.forEach {
			if (it.match(lexer)) {
				return it
			}
		}
		return null
	}


	/**
	 * 插入节点 插在了0

	 * @param parser BNF
	 */
	fun insert(parser: Bnf) {
		bnfList.toMutableList().add(0, parser)
	}
}