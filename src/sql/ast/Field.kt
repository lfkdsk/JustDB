package sql.ast

import sql.token.SqlToken.default.FIELD
import sql.token.SqlToken.default.FIELD_DEF
import sql.token.SqlToken.default.FIELD_DEF_LIST
import sql.token.SqlToken.default.FIELD_LIST
import utils.parsertools.ast.AstList
import utils.parsertools.ast.AstNode

/**
 * Created by liufengkai on 2017/4/27.
 */
class Field(override val children: List<AstNode>):AstList(children, FIELD)

class FieldList(override val children: List<AstNode>) : AstList(children, FIELD_LIST) {}

class FieldDef(override val children: List<AstNode>) : AstList(children, FIELD_DEF) {

}

class FieldDefList(override val children: List<AstNode>) : AstList(children, FIELD_DEF_LIST) {

}