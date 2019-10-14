package com.lfkdsk.justdb.parser.literal;


import bnfgenast.ast.token.Token;

/**
 * String Literal =>
 * - "lfkdsk"
 * - "\"lfkdsk\""
 *
 * @author liufengkai
 * Created by liufengkai on 2017/7/18.
 */
public class StringLiteral extends Literal {

    public StringLiteral(Token token) {
        super(token);
    }

    @Override
    public String value() {
        return token.getText();
    }
}
