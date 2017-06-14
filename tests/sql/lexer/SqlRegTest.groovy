package sql.lexer
/**
 * Created by liufengkai on 2017/4/27.
 */
class SqlRegTest extends GroovyTestCase {

    static final String output =
            "\\s*((//.*)|(([0-9]+)(\\.[0-9]+)?([eE][-+]?[0-9]+)?)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|!=|&&|\\|\\||true|false|null|\\p{Punct})?"

    void testGetHobbyReg() {
        // sql
        SqlReg sqlReg = new SqlReg()
        println(sqlReg.hobbyReg)
        assertEquals(sqlReg.hobbyReg, output)
    }
}
