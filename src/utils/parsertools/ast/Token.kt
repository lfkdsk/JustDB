package utils.parsertools.ast

/**
 * Created by liufengkai on 2017/4/24.
 */

interface Token {

	val lineNumber: Int

	var tag: Int

	fun isIdentifier(): Boolean

	fun isNumber(): Boolean

	fun isString(): Boolean

	fun isNull(): Boolean

	fun getText(): String

	fun isType(): Boolean

	fun isBool(): Boolean
}