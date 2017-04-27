package utils.parsertools.combinators.tree

import utils.parsertools.combinators.Bnf
import utils.parsertools.combinators.Element
import utils.parsertools.lex.Lexer
import utils.parsertools.ast.AstList
import utils.parsertools.ast.AstNode

/**
 * Created by liufengkai on 2017/4/24.
 */

class OneOrMore(private val parser: Bnf, private val onlyOne: Boolean) : Element {

	override fun parse(lexer: Lexer, nodes: MutableList<AstNode>) {
		while (parser.match(lexer)) {
			val node = parser.parse(lexer)
			// leaf or list
			if (node !is AstList || node.childCount() > 0) {
				nodes.add(node)
			}

			if (onlyOne)
				break
		}
	}

	override fun match(lexer: Lexer): Boolean {
		return parser.match(lexer)
	}
}