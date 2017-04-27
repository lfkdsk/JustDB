package sql.token

/**
 * Created by liufengkai on 2017/4/27.
 */
class SqlStrToken(override val lineNumber: Int,
                  var literalStr: String)
	: SqlToken(lineNumber, SqlToken.STRING) {

	override fun isString(): Boolean {
		return true
	}
}