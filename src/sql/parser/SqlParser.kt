package sql.parser

import sql.ast.*
import sql.literal.EnumLeaf
import sql.literal.IdLeaf
import sql.literal.NumLeaf
import sql.literal.StrLeaf
import sql.token.SqlToken
import utils.parsertools.ast.AstNode
import utils.parsertools.combinators.Bnf
import utils.parsertools.exception.ParseException
import utils.parsertools.lex.Lexer


/**
 * Created by liufengkai on 2017/4/25.
 */

class SqlParser {
	// word set
	var reserved: HashSet<String> = HashSet()

	var typeDefSet: HashSet<String> = HashSet()

	val number = Bnf.rule().number(NumLeaf::class.java)

	val string = Bnf.rule().string(StrLeaf::class.java)

	var id = Bnf.rule().identifier(reserved, IdLeaf::class.java)

	val enum = Bnf.rule().enum(typeDefSet, EnumLeaf::class.java)

	val constant = Bnf.rule().or(number, string)

	val constantList = Bnf.rule(ConstantList::class.java)
			.ast(constant)
			.repeat(Bnf.rule().sep(",").ast(constant))

	val field = id

	val expr = Bnf.rule().or(field, constant)

	val term = Bnf.rule(Term::class.java)
			.ast(expr).sep("=").ast(expr)

	val fieldList = Bnf.rule()
			.identifier(reserved)
			.repeat(Bnf.rule().sep(",").identifier(reserved))

//	val typeDef = Bnf.rule().enum(reserved)

	val fieldDef = Bnf.rule(FieldList::class.java).identifier(reserved)
			.ast(enum)

	val fieldDefList = Bnf.rule(FieldDefList::class.java)
			.ast(fieldDef)
			.repeat(Bnf.rule().sep(",").ast(fieldDef))

	val createTable = Bnf.rule(CreateTable::class.java)
			.sep("create")
			.sep("table")
			.identifier(reserved)
			.sep("(")
			.ast(fieldDefList)
			.sep(")")

	val insert = Bnf.rule(Insert::class.java)
			.sep("insert", "into")
			.identifier(reserved)
			.sep("(")
			.ast(fieldList)
			.sep(")")
			.sep("values")
			.sep("(")
			.ast(constantList)
			.sep(")")

	val predicate = Bnf.rule(Predicate::class.java)
			.ast(term)
			.repeat(Bnf.rule().sep("and").ast(term))

	val tableNameList = Bnf.rule(TableNameList::class.java)
			.identifier(reserved)
			.repeat(Bnf.rule().sep(",").identifier(reserved))

	val selectList = Bnf.rule(SelectList::class.java)
			.ast(field)
			.repeat(Bnf.rule().sep(",").ast(field))

	val query = Bnf.rule(Query::class.java)
			.sep("select")
			.ast(selectList)
			.sep("from")
			.ast(tableNameList)
			.maybe(Bnf.rule().sep("where").ast(predicate))

	val createView = Bnf.rule(CreateView::class.java)
			.sep("create", "view")
			.identifier(reserved)
			.sep("as")
			.ast(query)

	val createIndex = Bnf.rule(CreateIndex::class.java)
			.sep("create", "index")
			.identifier(reserved)
			.sep("on")
			.identifier(reserved)
			.sep("(")
			.ast(field) // TODO fields?
			.sep(")")

	val delete = Bnf.rule(Delete::class.java)
			.sep("delete", "from")
			.identifier(reserved)
			.maybe(Bnf.rule().sep("where").ast(predicate))

	val update = Bnf.rule(Update::class.java)
			.sep("update")
			.identifier(reserved)
			.sep("set")
			.ast(field)
			.sep("=")
			.ast(expr)
			.maybe(Bnf.rule().sep("where").ast(predicate))

	val create = Bnf.rule().or(
			createTable.sep(";"),
			createIndex.sep(";"),
			createView.sep(";")
	)

	val modify = Bnf.rule().or(
			insert.sep(";"),
			delete.sep(";"),
			update.sep(";"),
			query.sep(";")
	)

	val singleCommand = Bnf.rule().or(create, modify)

	val program = Bnf.rule().ast(singleCommand).sep(SqlToken.EOL)

	init {
		// init type def params
		typeDefSet.add("VARCHAR")
		typeDefSet.add("INT")
	}

	@Throws(ParseException::class)
	fun parse(lexer: Lexer): AstNode {
		return program.parse(lexer)
	}


}