
package com.lfkdsk.justdb.parser.literal;


import bnfgenast.ast.token.Token;

import static com.lfkdsk.justdb.parser.token.BoolToken.BooleanEnum.TRUE;
import static com.lfkdsk.justdb.parser.token.BoolToken.booleanValue;

/**
 * Boolean Literal => Support two Boolean Value.
 * - true
 * - false
 *
 * @author liufengkai
 * Created by liufengkai on 2017/7/26.
 */
public class BoolLiteral extends Literal {

    public BoolLiteral(Token token) {
        super(token);
    }

    @Override
    public Boolean value() {
        return booleanValue(token.getText()) == TRUE;
    }
}
