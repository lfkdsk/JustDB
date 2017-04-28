package utils.parsertools.combinators

import combinators.word.Operators
import utils.parsertools.ast.AstLeaf
import utils.parsertools.ast.AstNode
import utils.parsertools.combinators.token.*
import utils.parsertools.combinators.tree.*
import utils.parsertools.exception.ParseException
import utils.parsertools.lex.Lexer
import java.util.*

/**
 * Created by liufengkai on 2017/4/24.
 *
 * @author ice1000
 * @author lfkdsk
 */

open class Bnf {
	/**
	 * 存储全部的BNF表达式
	 */
	protected var elements = arrayListOf<Element>()

	/**
	 * 构建工厂类
	 */
	protected var factory: TokenFactory? = null

	constructor(clazz: Class<out AstNode>? = null) {
		reset(clazz)
	}

	constructor(parser: Bnf) {
		elements = parser.elements
		factory = parser.factory
	}

	/**
	 * 分析处理
	 *
	 * @param lexer 词法分析
	 * @return 节点
	 * @throws ParseException
	 */
	fun parse(lexer: Lexer): AstNode {
		val results = ArrayList<AstNode>()
		elements.forEach { it.parse(lexer, results) }
		return factory!!.make(results)
	}


	@Throws(ParseException::class)
	fun match(lexer: Lexer): Boolean {
		if (elements.isEmpty()) {
			return true
		} else {
			return elements[0].match(lexer)
		}
	}

	companion object {
		/**
		 * 初始化 / 新定义一个一条产生式
		 *
		 * @param clazz 类
		 * @return Ast
		 */
		fun rule(clazz: Class<out AstNode>? = null) = Bnf(clazz)

		val defaultLeaf = AstLeaf::class.java
	}

	fun reset(): Bnf {
		elements = ArrayList<Element>()
		return this
	}

	fun reset(clazz: Class<out AstNode>? = null): Bnf {
		elements = ArrayList<Element>()
		factory = TokenFactory.getForAstList(clazz)
		return this
	}

	///////////////////////////////////////////////////////////////////////////
	// 添加识别各种Token的方法
	///////////////////////////////////////////////////////////////////////////

	fun number(clazz: Class<out AstLeaf> = defaultLeaf): Bnf {
		elements.add(NumToken(clazz))
		return this
	}

	fun identifier(reserved: Set<String>, clazz: Class<out AstLeaf> = defaultLeaf): Bnf {
		elements.add(IdToken(clazz, reserved))
		return this
	}

	fun enum(enumSet: Set<String>, clazz: Class<out AstLeaf> = defaultLeaf): Bnf {
		elements.add(EnumToken(clazz, enumSet))
		return this
	}

	fun string(clazz: Class<out AstLeaf> = defaultLeaf): Bnf {
		elements.add(StrToken(clazz))
		return this
	}

	fun bool(clazz: Class<out AstLeaf> = defaultLeaf): Bnf {
		elements.add(BoolToken(clazz))
		return this
	}

	fun Null(clazz: Class<out AstLeaf> = defaultLeaf): Bnf {
		elements.add(NullToken(clazz))
		return this
	}

	fun type(clazz: Class<out AstLeaf> = defaultLeaf): Bnf {
		elements.add(TypeToken(clazz))
		return this
	}

	/**
	 * 添加非终结符
	 *
	 * @param pat
	 * @return
	 */
	fun token(vararg pat: String): Bnf {
		elements.add(Leaf(pat.toList()))
		return this
	}

	/**
	 * 插入符号
	 *
	 * @param pat 符号
	 * @return 这种格式的符号(跳
	 */
	fun sep(vararg pat: String): Bnf {
		elements.add(Skip(pat.toList()))
		return this
	}

	/**
	 * 插入一棵子树
	 *
	 * @param parser BNF
	 * @return BNF
	 */
	fun ast(parser: Bnf): Bnf {
		elements.add(Tree(parser))
		return this
	}

	/**
	 * 多个对象传入or树
	 *
	 * @param parsers BNF
	 * @return BNF
	 */
	fun or(vararg parsers: Bnf): Bnf {
		elements.add(OrTree(parsers.toList()))
		return this
	}

	fun maybe(parser: Bnf): Bnf {
		val parser1 = Bnf(parser)
		parser1.reset()
		elements.add(OrTree(listOf(parser, parser1).toMutableList()))
		return this
	}

	/**
	 * onlyOne 只重复一次
	 *
	 * @param parser BNF
	 * @return BNF
	 */
	fun option(parser: Bnf): Bnf {
		elements.add(OneOrMore(parser, true))
		return this
	}

	/**
	 * 重复多次的节点
	 *
	 * @param parser BNF
	 * @return BNF
	 */
	fun repeat(parser: Bnf): Bnf {
		elements.add(OneOrMore(parser, false))
		return this
	}

	fun expression(subExp: Bnf, operators: Operators): Bnf {
		elements.add(Expr(null, operators, subExp))
		return this
	}

	fun expression(
			clazz: Class<out AstNode>,
			subExp: Bnf,
			operators: Operators): Bnf {
		elements.add(Expr(clazz, operators, subExp))
		return this
	}

	fun insertChoice(parser: Bnf): Bnf {
		val e = elements[0]
		if (e is OrTree) {
			e.insert(parser)
		} else {
			val otherWise = Bnf(this)
			reset(null)
			or(parser, otherWise)
		}
		return this
	}
}