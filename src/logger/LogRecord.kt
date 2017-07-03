package logger

import storage.ExPage
import storage.ExPage.DEFAULT.INT_SIZE
import storage.ExPage.DEFAULT.strSize

/**
 * Simple Line Logger Record
 * Created by liufengkai on 2017/4/30.
 */

class LogRecord(private val page: ExPage, private var pos: Int) {
	/**
	 * Returns the next value of the current log record,
	 * assuming it is an integer.
	 * @return the next value of the current log record
	 */
	fun nextInt(): Int {
		val result = page.getInt(pos)
		pos += INT_SIZE
		return result
	}

	/**
	 * Returns the next value of the current log record,
	 * assuming it is a string.
	 * @return the next value of the current log record
	 */
	fun nextString(): String {
		val result = page.getString(pos)
		pos += strSize(result.length)
		return result
	}

	override fun toString(): String {
		return "LogRecord(page=$page, pos=$pos)"
	}
}