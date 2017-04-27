package utils.parsertools.combinators.token

import utils.parsertools.ast.AstLeaf
import utils.parsertools.ast.Token

/**
 * Created by liufengkai on 2017/4/24.
 */
class IdToken(clazz: Class<out AstLeaf>, var reserved: Set<String> = hashSetOf())
	: AbstractToken(clazz) {

	override fun tokenTest(token: Token): Boolean {
		return token.isIdentifier() && !reserved.contains(token.text)
	}
}