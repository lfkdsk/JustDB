package core

import buffer.BufferManager
import buffer.BufferManagerImpl
import logger.LogManager
import logger.LogManagerImpl
import storage.FileManager
import storage.FileManagerImpl

/**
 * Created by liufengkai on 2017/4/30.
 */

/**
 * system basic interface
 */
interface SystemService

/**
 * cannot find exception
 */
class CanNotFindService(message: String?) : Exception(message)

object JustDB {
	const val FILE_MANAGER = "FILE_MANAGER"
	const val LOGGER_MANAGER = "LOGGER_MANAGER"
	const val BUFFER_MANAGER = "BUFFER_MANAGER"

	const val bufferSize = 8
	const val logFileName = "just-log.log"

	private val systemServersSet: MutableMap<String, SystemService> = HashMap()

	private var dataBaseName = "just-db"


	fun init(dataBaseName: String) {
		this.dataBaseName = dataBaseName

		initFileManager(dataBaseName)
		initLogManager(logFileName)
		initBufferManager(bufferSize)
	}

	private fun initFileManager(dataBaseName: String): FileManager {
		val fileManager: FileManager = FileManagerImpl(dataBaseName)
		systemServersSet.put(FILE_MANAGER, fileManager)
		return fileManager
	}

	private fun initLogManager(logFile: String): LogManager {
		val logManager: LogManager = LogManagerImpl(logFile)
		systemServersSet.put(LOGGER_MANAGER, logManager)
		return logManager
	}

	private fun initBufferManager(bufferSize: Int): BufferManager {
		val bufferManager: BufferManager = BufferManagerImpl(bufferSize)
		systemServersSet.put(BUFFER_MANAGER, bufferManager)
		return bufferManager
	}

	fun getService(serName: String): SystemService {
		return systemServersSet[serName] ?:
				when (serName) {
					FILE_MANAGER -> initFileManager(dataBaseName)
					LOGGER_MANAGER -> initLogManager(logFileName)
					BUFFER_MANAGER -> initBufferManager(bufferSize)
					else -> {
						throw CanNotFindService("can not find service: $serName")
					}
				}
	}

	operator fun get(serName: String) = getService(serName)
}