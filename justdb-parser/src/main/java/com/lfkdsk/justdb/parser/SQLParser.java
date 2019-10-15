package com.lfkdsk.justdb.parser;/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import bnfgenast.ast.base.AstList;
import bnfgenast.ast.base.AstNode;
import bnfgenast.bnf.BnfCom;
import com.lfkdsk.justdb.parser.lexer.SQLLexer;
import com.lfkdsk.justdb.parser.literal.IDLiteral;
import com.lfkdsk.justdb.parser.literal.NumberLiteral;
import com.lfkdsk.justdb.parser.literal.StringLiteral;
import com.lfkdsk.justdb.parser.model.*;

import java.io.LineNumberReader;
import java.io.StringReader;

import static bnfgenast.ast.token.Token.EOL;
import static bnfgenast.bnf.BnfCom.leaf;
import static bnfgenast.bnf.BnfCom.rule;

public class SQLParser {
    private static final SQLLexer lexer = new SQLLexer("");
    public static final BnfCom<NumberLiteral> number = leaf(NumberLiteral::new).number();
    public static final BnfCom<StringLiteral> string = leaf(StringLiteral::new).string();
    public static final BnfCom<IDLiteral> id = leaf(IDLiteral::new).identifier(lexer.getReservedToken());
    public static final BnfCom<DataType> enumType = rule(DataType::new).or(
            rule().token("int"),
            rule().token("varchar").sep("(").ast(number).sep(")")
    );
    public static final BnfCom<Constant> constant = rule(Constant::new).or(number, string);
    public static final BnfCom<ConstantList> constantList = rule(ConstantList::new)
            .ast(constant)
            .repeat(rule().sep(",").ast(constant));
    public static final BnfCom<IDLiteral> field = id;
    public static final BnfCom<Expr> expr = rule(Expr::new).or(field, constant);
    public static final BnfCom<Term> term = rule(Term::new).ast(expr).sep("=").ast(expr);
    public static final BnfCom<FieldList> fieldList = rule(FieldList::new).ast(id).repeat(rule().sep(",").ast(id));
    public static final BnfCom<FieldDef> fieldDef = rule(FieldDef::new).ast(id).then(enumType);
    public static final BnfCom<FieldDefList> fieldDefList = rule(FieldDefList::new).ast(fieldDef)
            .repeat(rule().sep(",").ast(fieldDef));
    public static final BnfCom<CreateTable> createTable = rule(CreateTable::new)
            .sep("create").sep("table")
            .ast(id)
            .sep("(").then(fieldDefList).sep(")");
    public static final BnfCom<Insert> insert = rule(Insert::new)
            .sep("insert").sep("into")
            .ast(id)
            .sep("(").ast(fieldList).sep(")")
            .sep("values")
            .sep("(").ast(constantList).sep(")");

    public static final BnfCom<Predicate> predicate = rule(Predicate::new)
            .ast(term)
            .repeat(rule().sep("and").ast(term));

    public static final BnfCom<TableNameList> tableNameList = rule(TableNameList::new)
            .ast(id)
            .repeat(rule().sep(",").ast(id));

    public static final BnfCom<SelectList> selectList = rule(SelectList::new)
            .ast(field)
            .repeat(rule().sep(",").ast(field));

    public static final BnfCom<Query> query = rule(Query::new)
            .sep("select")
            .ast(selectList)
            .sep("from")
            .ast(tableNameList)
            .maybe(rule().sep("where").ast(predicate));

    public static final BnfCom<CreateView> createView = rule(CreateView::new)
            .sep("create").sep("view")
            .ast(id)
            .sep("as").then(query);

    public static final BnfCom<CreateIndex> createIndex = rule(CreateIndex::new)
            .sep("create").sep("index")
            .ast(id)
            .sep("on")
            .ast(id)
            .sep("(").ast(field).sep(")");

    public static final BnfCom<Delete> delete = rule(Delete::new)
            .sep("delete").sep("from")
            .ast(id)
            .maybe(rule().sep("where").ast(predicate));

    public static final BnfCom<Update> update = rule(Update::new)
            .sep("update")
            .ast(id)
            .sep("set")
            .ast(field).sep("=").ast(expr)
            .maybe(rule().sep("where").ast(predicate));

    public static final BnfCom<Create> create = rule(Create::new)
            .or(createTable, createView, createIndex).sep(";");

    public static final BnfCom<Modify> modify = rule(Modify::new)
            .or(insert, delete, update, query).sep(";");

    public static final BnfCom<AstList> program = rule(AstList::new).or(create, modify).sep(EOL);

    public static AstNode parse(String input) {
        lexer.setReader(new LineNumberReader(new StringReader(input)));
        return program.parse(lexer.tokens());
    }
}
