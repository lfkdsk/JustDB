package sql.token


/**
 * Created by liufengkai on 2017/4/27.
 */
class SqlNumToken(override val lineNumber: Int,
                  override var tag: Int,
                  val number: Number)
	: SqlToken(lineNumber, tag) {

	fun getInt(): Int {
		return number.toInt()
	}

	fun getFloat(): Float {
		return number.toFloat()
	}

	override fun isNumber(): Boolean {
		return true
	}
}