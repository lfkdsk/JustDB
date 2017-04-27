package sql.token

/**
 * Created by liufengkai on 2017/4/27.
 */
class SqlIdToken(override val lineNumber: Int,
                 override var text: String)
	: SqlToken(lineNumber, SqlToken.ID, text) {

	init {
		// init EOF Token
		if (text == SqlToken.EOL) {
			this.tag = SqlToken.EOF_TAG
		}
	}

	override fun isIdentifier(): Boolean {
		return true
	}
}