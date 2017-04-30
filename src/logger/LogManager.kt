package logger

import core.SystemService

/**
 * Created by liufengkai on 2017/4/30.
 */
interface LogManager : SystemService {
	fun flush(recordStored: Int)

	fun append(rec: Array<Any>): Int
}