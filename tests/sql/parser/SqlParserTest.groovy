package sql.parser

import sql.lexer.SqlLexer
import sql.token.SqlToken
import utils.parsertools.ast.AstNode
import utils.parsertools.lex.Lexer

import java.util.logging.Logger

/**
 * Created by liufengkai on 2017/4/28.
 */
class SqlParserTest extends GroovyTestCase {
    void testParse() {
        Lexer lexer = new SqlLexer(new FileReader("./tests/sql/parser/sqltext"))

        SqlParser parser = new SqlParser()

        while (lexer.tokenAt(0) != SqlToken.EOF) {
            AstNode node = parser.parse(lexer);
            Logger.println(" => " + node.toString() + "  ");
        }
    }
}
