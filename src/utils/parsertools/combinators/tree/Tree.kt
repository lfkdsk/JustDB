package utils.parsertools.combinators.tree

import utils.parsertools.combinators.Bnf
import utils.parsertools.combinators.Element
import utils.parsertools.lex.Lexer
import utils.parsertools.ast.AstNode

/**
 * Created by liufengkai on 2017/4/24.
 */


class Tree(private var bnf: Bnf) : Element {
	override fun parse(lexer: Lexer, nodes: MutableList<AstNode>) {
		nodes.add(bnf.parse(lexer))
	}

	override fun match(lexer: Lexer): Boolean {
		return bnf.match(lexer)
	}
}