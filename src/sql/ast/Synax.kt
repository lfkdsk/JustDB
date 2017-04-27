package sql.ast

import sql.token.SqlToken.default.CREATE_INDEX
import sql.token.SqlToken.default.CREATE_TABLE
import sql.token.SqlToken.default.CREATE_VIEW
import sql.token.SqlToken.default.DELETE
import sql.token.SqlToken.default.INSERT
import sql.token.SqlToken.default.QUERY
import sql.token.SqlToken.default.UPDATE
import utils.parsertools.ast.AstList
import utils.parsertools.ast.AstNode

/**
 * Created by liufengkai on 2017/4/27.
 */
class CreateTable(override val children: List<AstNode>)
	: AstList(children, CREATE_TABLE) {

}

class CreateView(override val children: List<AstNode>)
	: AstList(children, CREATE_VIEW) {

}

class CreateIndex(override val children: List<AstNode>)
	: AstList(children, CREATE_INDEX) {

}

class Insert(override val children: List<AstNode>)
	: AstList(children, INSERT) {

}

class Query(override val children: List<AstNode>)
	: AstList(children, QUERY) {

}


class Delete(override val children: List<AstNode>)
	: AstList(children, DELETE) {}

class Update(override val children: List<AstNode>)
	: AstList(children, UPDATE) {}