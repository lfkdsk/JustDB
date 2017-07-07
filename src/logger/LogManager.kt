package logger

import core.SystemService

/**
 * Created by liufengkai on 2017/4/30.
 */
interface LogManager : SystemService, Iterable<LogRecord> {
	fun flush(recordStored: Int)

	fun append(rec: List<Any>): Int

	override fun iterator(): Iterator<LogRecord>
}

