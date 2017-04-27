package sql.lexer
/**
 * Created by liufengkai on 2017/4/27.
 */
class SqlLexerTest extends GroovyTestCase {

    static SqlLexer lexer= new SqlLexer(new FileReader("./tests/sql/lexer/sqltext"))

    void testTokenAt() {
        println("get token at " + lexer.tokenAt(2).text)
    }

    void testNextToken() {
        println("get next token at " + lexer.nextToken().text)
    }

    void testGet() {

    }
}
