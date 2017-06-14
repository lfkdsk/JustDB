package sql.lexer
/**
 * Created by liufengkai on 2017/4/27.
 */
class SqlLexerTest extends GroovyTestCase {

    static SqlLexer lexer= new SqlLexer(new FileReader("./tests/sql/lexer/sqltext"))

    void testTokenAt() {
        String lfkdsk = lexer.tokenAt(2).text
        println("get token at " + lfkdsk)
        assertEquals("lfkdsk", lfkdsk)
    }

    void testNextToken() {
        String select = lexer.nextToken().text;
        println("get next token at " + select)
        assertEquals("select", select)
    }

    void testGet() {

    }
}
