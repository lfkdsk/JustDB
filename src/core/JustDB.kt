package core

import logger.LogManager
import logger.LogManagerImpl
import storage.FileManager
import storage.FileManagerImpl

/**
 * Created by liufengkai on 2017/4/30.
 */

interface SystemService {

//	/**
//	 * File Manager Interface
//	 */
//	fun read(block: Block, byteBuffer: ByteBuffer)
//
//	fun write(block: Block, byteBuffer: ByteBuffer)
//
//	fun append(filename: String, byteBuffer: ByteBuffer): Block
//
//	/**
//	 * Logger Manager Interface
//	 */
//	fun flush(recordStored: Int)
//
//	fun append(rec: Array<Any>): Int
}

class JustDB private constructor() {

	/**
	 * static object block
	 */
	private object Initializer {
		val singleTon = JustDB()
	}

	/**
	 * static function
	 */
	companion object {
		fun getInstance(): JustDB {
			return Initializer.singleTon
		}

		val FILE_MANAGER = "FILE_MANAGER"
		val LOGGER_MANAGER = "LOGGER_MANAGER"
	}

	private val systemServersSet: MutableMap<String, SystemService> = HashMap()

	private var dataBaseName = "just-db"

	private val logFileName = "just-log.log"

	fun init(dataBaseName: String) {
		this.dataBaseName = dataBaseName
		initFileManager(dataBaseName)
		initLogManager(dataBaseName)
	}

	private fun initFileManager(dataBaseName: String): FileManager {
		val fileManager: FileManager = FileManagerImpl(dataBaseName)
		systemServersSet.put(FILE_MANAGER, fileManager)
		return fileManager
	}

	private fun initLogManager(logFile: String): LogManager {
		val logManager: LogManager = LogManagerImpl(logFile)
		systemServersSet.put(FILE_MANAGER, logManager)
		return logManager
	}

	fun getService(serName: String): SystemService {
		return systemServersSet[serName] ?:
				when (serName) {
					FILE_MANAGER -> initFileManager(dataBaseName)
					LOGGER_MANAGER -> initLogManager(logFileName)
					else -> {
						throw Exception("can not find service: $serName")
					}
				}
	}

	operator fun get(serName: String) = getService(serName)

}