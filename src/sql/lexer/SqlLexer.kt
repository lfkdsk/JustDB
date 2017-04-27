package sql.lexer

import sql.token.SqlIdToken
import sql.token.SqlNumToken
import sql.token.SqlStrToken
import sql.token.SqlToken
import utils.parsertools.ast.Token
import utils.parsertools.exception.ParseException
import utils.parsertools.lex.Lexer
import java.io.LineNumberReader
import java.io.Reader
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created by liufengkai on 2017/4/27.
 */
class SqlLexer(reader: Reader) : Lexer {
	private var reader: LineNumberReader = LineNumberReader(reader)
	private val regPattern: Pattern
	private val queue = ArrayList<SqlToken>()
	private var hasMore: Boolean = true
	private val regReg: SqlReg = SqlReg()

	init {
		// init reg pattern
		regPattern = Pattern.compile(regReg.hobbyReg)
	}

	@Throws(ParseException::class)
	private fun readLine() {
		val line: String? = reader.readLine()

		if (line == null) {
			hasMore = false
			return
		}

		val lineNum = reader.lineNumber

		val matcher = regPattern.matcher(line)

		/*
		  1.透明边界:允许环视这样就能避免一些词素匹配混乱
		  2.匹配边界:不允许正则里面包含对边界的限定符
		 */
		matcher.useTransparentBounds(true)
				.useAnchoringBounds(false)

		var start = 0
		val end = line.count()

		if (end == 0) return

		while (start < end) {
			matcher.region(start, end)
			// 出现匹配
			if (matcher.lookingAt()) {
				addToken(lineNum, matcher)
				start = matcher.end()
			} else {
				throw ParseException("bad token at line $lineNum")
			}

		}

		queue.add(SqlIdToken(lineNum, SqlToken.EOL))
	}

	/**
	 * 填充队列
	 *
	 * @param index 指定num
	 * @return 返回状态
	 * @throws ParseException
	 */
	@Throws(ParseException::class)
	private fun fillQueue(index: Int): Boolean {
		while (index >= queue.count()) {
			if (hasMore) {
				readLine()
			} else {
				return false
			}
		}
		return true
	}

	override fun tokenAt(index: Int): Token {
		if (fillQueue(index)) {
			return queue[index]
		} else {
			return SqlToken.EOF
		}
	}

	override fun nextToken(): Token {
		if (fillQueue(0)) {
			return queue.removeAt(0)
		} else {
			return SqlToken.EOF
		}
	}

	/**
	 * 所谓字符串转译

	 * @param str 传入字符串
	 * *
	 * @return 返回字符串
	 */
	private fun toStringLiteral(str: String): String {
		val builder = StringBuilder()

		val length = str.length - 1

		var i = 1
		while (i < length) {
			var ch = str[i]

			// 发现需要转译的\
			if (ch == '\\' && i + 1 < length) {
				// 取下一个字符
				val ch2 = str[i + 1]
				// 手动跳过
				if (ch2 == '"' || ch2 == '\\') {
					ch = str[++i]
					// 手工转译嵌入\n
				} else if (ch2 == 'n') {
					++i
					ch = '\n'
				}
			}

			builder.append(ch)
			i++
		}
		return builder.toString()
	}

	/**
	 * 通过匹配模式判断词素类型

	 * @param lineNum 行号
	 * *
	 * @param matcher matcher
	 */
	private fun addToken(lineNum: Int, matcher: Matcher) {
		val first = matcher.group(SqlReg.RegType.NOT_EMPTY_INDEX.regId)

		if (first != null) {
			// 不是空格
			if (matcher.group(SqlReg.RegType.ANNOTATION_INDEX.regId) == null) {
				// 不是注释
				var token: SqlToken? = null
				// 是数字
				if (matcher.group(SqlReg.RegType.FLOAT_NUMBER_INDEX.regId) != null) {
					if (first.contains("e") || first.contains("E") || first.contains(".")) {
						token = SqlNumToken(lineNum, SqlToken.REAL, first.toDouble())
					} else {
						token = SqlNumToken(lineNum, SqlToken.NUM, first.toInt())
					}
					// handler "" 空字串
				} else if (first == "\"\"" || matcher.group(SqlReg.RegType.STRING_INDEX.regId) != null) {
					token = SqlStrToken(lineNum, toStringLiteral(first))
				} else {
//					when (first) {
//						BoolToken.TRUE -> token = BoolToken(lineNum, BoolToken.BoolType.TRUE)
//						BoolToken.FALSE -> token = BoolToken(lineNum, BoolToken.BoolType.FALSE)
//						NullToken.NULL -> token = NullToken(lineNum)
//						else -> token = IdToken(lineNum, first!!)
//					}
					token = SqlIdToken(lineNum, first)
				}
				queue.add(token)
			}
		}
	}


}