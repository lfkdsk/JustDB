package sql.ast

import sql.token.SqlToken.default.CONSTANT_LIST
import utils.parsertools.ast.AstList
import utils.parsertools.ast.AstNode

/**
 * Created by liufengkai on 2017/4/27.
 */
class ConstantList(override val children: List<AstNode>)
	: AstList(children, CONSTANT_LIST) {

}