package sql.lexer

/**
 * Created by liufengkai on 2017/4/27.
 */
class SqlReg {
	/**
	 * 匹配注释
	 * 即注释线之后+任意数量的字符
	 */
	val annotationReg = "(//.*)"

	/**
	 * 数字类型的正则
	 * unused
	 */
	val numberIntReg = "([0-9]+)"

	/**
	 * 其余的全部数字类型
	 */
	val numberFloatReg = "(([0-9]+)(\\.[0-9]+)?([eE][-+]?[0-9]+)?)"

	/**
	 * 变量名的正则
	 */
	val variableReg = "[A-Z_a-z][A-Z_a-z0-9]*"

	/**
	 * 布尔表达式的正则
	 */
	val booleanReg = "==|<=|>=|!=|&&|\\|\\|"

	/**
	 * 任意符号的正则匹配
	 */
	val symbolReg = "\\p{Punct}"

	val bool = "true|false|null"

	/**
	 * string 类型的正则
	 * string 中是在两个双引号中的多个匹配模式
	 * 支持 \" \n \\ 和任意一种不是"的符号匹配
	 * 遇到\\"这种试图二度转译的应当会先与\\匹配
	 * 使转译停止
	 */
	val stringReg = "(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"

	/**
	 * 定义了所有Token识别的基础类型
	 */
	val tokenReg = "$variableReg|$booleanReg|$bool|$symbolReg"

	val hobbyUnFormat = "\\s*(%1\$s|%2\$s|%3\$s|%4\$s)?"

	val hobbyReg = String.format(hobbyUnFormat,
			annotationReg, numberFloatReg, stringReg, tokenReg)

	/**
	 * 各部分Token在正则中的位置
	 * matcher.group的选取是看括号的
	 * 前套来的首先匹配的是全局,
	 * 然后缩紧一次括号位置
	 */
	enum class RegType(id: Int) {
		NOT_EMPTY_INDEX(1),
		ANNOTATION_INDEX(2),
		FLOAT_NUMBER_INDEX(3),
		STRING_INDEX(8);

		val regId = id
	}
}