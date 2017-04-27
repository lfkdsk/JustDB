package utils.parsertools.ast

/**
 * Created by liufengkai on 2017/4/24.
 */

open class AstList(open val children: List<AstNode>, override val tag: Int) : AstNode(tag) {

	override fun childCount(): Int {
		return children.size
	}

	override fun location(): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun children(): Iterator<AstNode> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}