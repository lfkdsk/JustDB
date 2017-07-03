package logger

import core.JustDB
import core.LogManager
import groovy.util.GroovyTestCase

/**
 * Logger 单独测试没有问题
 * 但是 All Test 似乎有时序性问题
 * Created by liufengkai on 2017/4/30.
 */
class LogManagerImplTest : GroovyTestCase() {
	val justDB = JustDB()
	val manager = justDB.LogManager()

	fun testAppend() {
//		for (i in 0..10) {
		val list = mutableListOf<String>()
		repeat(10) {
			list.add("000 message")
		}
		manager.append(list)
		manager.flush(10)

		for (record: LogRecord in manager) {
			println("=====> ${record.nextString()}")
		}
	}

//	fun testIterator() {
//		for (record: LogRecord in manager) {
//			println("=====> ${record.nextString()}")
//		}
//	}

	fun testFlush() {
//        manager.flush(10)
	}
}
