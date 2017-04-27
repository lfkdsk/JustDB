package utils.parsertools.ast

import sql.eval.Env
import java.util.*


/**
 * Created by liufengkai on 2017/4/24.
 */

open class AstLeaf(val token: Token) : AstNode(token.tag) {

	override fun childCount(): Int = 0

	private val empty = ArrayList<AstNode>()

	override fun location(): String {
		throw IndexOutOfBoundsException()
	}

	override fun children(): Iterator<AstNode> {
		return empty.iterator()
	}

	override fun toString(): String {
		return token.text
	}

	override fun eval(env: Env): Any {
		throw Exception("can not eval : ${toString()} leaf : ${this}")
	}
}