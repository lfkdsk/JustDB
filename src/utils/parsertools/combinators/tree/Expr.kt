package utils.parsertools.combinators.tree

import combinators.word.Operators
import combinators.word.Precedence
import utils.parsertools.ast.AstLeaf
import utils.parsertools.ast.AstNode
import utils.parsertools.combinators.Bnf
import utils.parsertools.combinators.Element
import utils.parsertools.combinators.TokenFactory
import utils.parsertools.lex.Lexer
import java.text.ParseException


/**
 * Created by liufengkai on 2017/4/24.
 */

/**
 * 表达式子树
 */
open class Expr(
		clazz: Class<out AstNode>?,
		protected var ops: Operators,
		protected var factor: Bnf) : Element {
	protected var factory = TokenFactory.getForAstList(clazz)

	override fun parse(lexer: Lexer, nodes: MutableList<AstNode>) {
		var right: AstNode = factor.parse(lexer)

		var prec = nextOperator(lexer)

		while (prec != null) {
			right = doShift(lexer, right, prec.value)
			prec = nextOperator(lexer)
		}

		nodes.add(right)
	}

	override fun match(lexer: Lexer): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun doShift(lexer: Lexer, left: AstNode, prec: Int): AstNode {
		val list = arrayListOf<AstNode>()

		list.add(left)
		// 读取一个符号
		list.add(AstLeaf(lexer.nextToken()))
		// 返回节点放在右子树
		var right = factor.parse(lexer)

		var next = nextOperator(lexer)
		// 子树向右拓展

		while (null != next && rightIsExpr(prec, next)) {
			right = doShift(lexer, right, next.value)
			next = nextOperator(lexer)
		}

		list.add(right)

		return factory.make(list)
	}

	/**
	 * 那取下一个符号

	 * @param lexer 词法
	 * *
	 * @return 符号
	 * *
	 * @throws ParseException
	 */
	private fun nextOperator(lexer: Lexer): Precedence? {
		val token = lexer[0]

		if (token.isIdentifier()) {
			// 从符号表里找对应的符号
			return ops[token.text]
		} else {
			return null
		}
	}

	/**
	 * 比较和右侧符号的结合性
	 *
	 * @param prec     优先级
	 * @param nextPrec 下一个符号的优先级
	 * @return tof?
	 */
	private fun rightIsExpr(prec: Int, nextPrec: Precedence): Boolean {
		if (nextPrec.leftAssoc) return prec > nextPrec.value
		else return prec >= nextPrec.value
	}

}
