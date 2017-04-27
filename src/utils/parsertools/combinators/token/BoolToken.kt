package utils.parsertools.combinators.token

import utils.parsertools.ast.AstLeaf
import utils.parsertools.ast.Token


/**
 * Created by liufengkai on 2017/4/24.
 */

class BoolToken(clazz: Class<out AstLeaf>) : AbstractToken(clazz) {
	override fun tokenTest(token: Token) = token.isBool()
}