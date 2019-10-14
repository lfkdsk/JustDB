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

import bnfgenast.bnf.BnfCom;
import bnfgenast.lexer.Lexer;
import com.lfkdsk.justdb.parser.lexer.SQLLexer;
import com.lfkdsk.justdb.parser.literal.IDLiteral;
import com.lfkdsk.justdb.parser.literal.NumberLiteral;
import com.lfkdsk.justdb.parser.literal.StringLiteral;
import com.lfkdsk.justdb.parser.model.DataType;

import java.util.HashSet;
import java.util.Set;

import static bnfgenast.bnf.BnfCom.leaf;
import static bnfgenast.bnf.BnfCom.rule;

public class SQLParser {
    private static final Lexer lexer = new SQLLexer("");
    public static final BnfCom<NumberLiteral> number = leaf(NumberLiteral::new).number();
    public static final BnfCom<StringLiteral> string = leaf(StringLiteral::new).string();
    public static final BnfCom<IDLiteral> id = leaf(IDLiteral::new).identifier(lexer.getReservedToken());
    public static final BnfCom<DataType> enumType = rule(DataType::new).or(
            rule().token("int"),
            rule().token("varchar").sep("(").ast(number).sep(")")
    );
}
