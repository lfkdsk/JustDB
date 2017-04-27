package utils.parsertools.ast

/**
 * Created by liufengkai on 2017/4/24.
 */

interface Token {

	val lineNumber: Int

	var tag: Int

	var text: String

	fun isIdentifier(): Boolean

	fun isNumber(): Boolean

	fun isString(): Boolean

	fun isNull(): Boolean

	fun isType(): Boolean

	fun isBool(): Boolean
}