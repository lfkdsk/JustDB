package utils.parsertools.exception

import utils.parsertools.ast.Token

/**
 * Created by liufengkai on 16/7/11.
 * ParseException 编译错误
 *
 * @author liufengkai
 * @author ice1000
 */
class ParseException(message: String) : Exception(message) {

	constructor(message: String, token: Token)
			: this("syntax error around \"${token.getText()}\"" +
			" at line ${token.lineNumber} message : $message")
}