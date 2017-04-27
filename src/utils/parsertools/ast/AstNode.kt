package utils.parsertools.ast

import sql.eval.Env
import sql.eval.Eval
import sql.token.SqlToken
import java.util.*
import java.util.function.Consumer


/**
 * Created by liufengkai on 2017/4/24.
 */

open abstract class AstNode(open val tag: Int = SqlToken.EMPTY)
	: Iterable<AstNode>, Eval {

	abstract fun childCount(): Int
	abstract fun location(): String
	abstract fun children(): Iterator<AstNode>

	override fun iterator(): Iterator<AstNode> {
		return children()
	}

	override fun forEach(action: Consumer<in AstNode>?) {
		Objects.requireNonNull(action)
		for (t in this) {
			action?.accept(t)
		}
	}

	override fun eval(env: Env): Any {
		return this
	}
}