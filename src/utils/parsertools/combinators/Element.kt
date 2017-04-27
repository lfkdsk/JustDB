package utils.parsertools.combinators

import utils.parsertools.exception.ParseException
import utils.parsertools.lex.Lexer
import utils.parsertools.ast.AstNode

/**
 * Created by liufengkai on 2017/4/24.
 *
 * @author lfkdsk
 * @author ice1000
 */

interface Element {
	/**
	 * 语法分析

	 * @param lexer 语法分析器
	 * *
	 * @param nodes 节点
	 * *
	 * @throws ParseException
	 */
	fun parse(lexer: Lexer, nodes: MutableList<AstNode>)

	/**
	 * 匹配

	 * @param lexer 语法分析器
	 * *
	 * @return tof?
	 * *
	 * @throws ParseException
	 */
	fun match(lexer: Lexer): Boolean
}