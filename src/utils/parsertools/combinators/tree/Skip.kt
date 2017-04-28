package utils.parsertools.combinators.tree

import utils.parsertools.ast.AstNode
import utils.parsertools.ast.Token


/**
 * Created by liufengkai on 2017/4/24.
 */

class Skip constructor(pat: List<String>) : Leaf(pat) {

	/**
	 * 所谓Skip 不添加节点
	 * 比如一些格式控制符号是不算做节点的

	 * @param list  list
	 * *
	 * @param token token
	 */
	override fun find(list: MutableList<AstNode>, token: Token) {

	}
}