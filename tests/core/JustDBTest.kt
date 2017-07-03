package core

import buffer.BufferManager
import groovy.util.GroovyTestCase
import logger.LogManager
import storage.FileManager

/**
 * Created by liufengkai on 2017/7/3.
 */

class JustDBTest : GroovyTestCase() {

	override fun setUp() {
		super.setUp()
	}

	fun testInit() {
		val justDB: JustDB = JustDB()
		println("JustDB init ===> " + justDB.toString())
	}

	fun testGetService() {
		val justDB: JustDB = JustDB()
		println("JustDB getService ===> " + justDB.getService(JustDBService.FILE_MANAGER) as FileManager)
		println("JustDB getService ===> " + justDB.getService(JustDBService.LOGGER_MANAGER) as LogManager)
		println("JustDB getService ===> " + justDB.getService(JustDBService.BUFFER_MANAGER) as BufferManager)
	}

	fun testGet() {
		val justDB: JustDB = JustDB()

		println("JustDB getService ===> " + justDB[JustDBService.FILE_MANAGER] as FileManager)
		println("JustDB getService ===> " + justDB[JustDBService.LOGGER_MANAGER] as LogManager)
		println("JustDB getService ===> " + justDB[JustDBService.BUFFER_MANAGER] as BufferManager)
	}

	fun testMutiJustDB() {
		val firstDB = JustDB("first-db")
		println(firstDB.toString())
		println(firstDB[JustDBService.FILE_MANAGER] as FileManager)

		val secondDB = JustDB("second-db")
		println(secondDB.toString())
		println(secondDB[JustDBService.FILE_MANAGER] as FileManager)
	}
}