package utils.parsertools.combinators.token

import utils.parsertools.ast.AstLeaf
import utils.parsertools.ast.Token

/**
 * Created by liufengkai on 2017/4/27.
 */
class EnumToken(clazz: Class<out AstLeaf>,
                var enumSet: Set<String> = hashSetOf()) : AbstractToken(clazz) {
	override fun tokenTest(token: Token): Boolean {
		return token.isIdentifier() && enumSet.contains(token.text)
	}
}