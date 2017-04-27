package sql.ast

import sql.token.SqlToken.default.PREDICATE
import sql.token.SqlToken.default.SELECT_LIST
import sql.token.SqlToken.default.TABLE_LIST
import sql.token.SqlToken.default.TERM
import utils.parsertools.ast.AstList
import utils.parsertools.ast.AstNode

/**
 * Created by liufengkai on 2017/4/27.
 */
class Term(override val children: List<AstNode>)
	: AstList(children, TERM) {

}

class Predicate(override val children: List<AstNode>)
	: AstList(children, PREDICATE) {

}

class TableNameList(override val children: List<AstNode>)
	: AstList(children, TABLE_LIST) {

}

class SelectList(override val children: List<AstNode>)
	: AstList(children, SELECT_LIST) {}