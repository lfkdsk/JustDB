package core

import buffer.BufferManagerImpl
import logger.LogManagerImpl
import metadata.MetadataManagerImpl
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


class JustDB(dataBaseName: String = "just-db") {
	val bufferSize = 8

//	private val systemServersSet: MutableMap<String, SystemService> = HashMap()

	companion object Factory {
		private lateinit var systemServersSet: Map<String, SystemService>

		lateinit var logFileName: String

		operator fun invoke(dataBaseName: String): JustDB {
			return JustDB(dataBaseName)
		}

		fun create(): JustDB = JustDB()

		fun getService(serName: String): SystemService {
			return systemServersSet[serName] ?: throw CanNotFindService("can not find service: $serName")
		}

		operator fun get(serName: String) = getService(serName)
	}

	init {
//		initFileManager(dataBaseName)
//		initLogManager(logFileName)
//		initBufferManager(bufferSize)
//		initMetadataManager()
		logFileName = dataBaseName + "-log.log"

		systemServersSet = mapOf(
				JustDBService.FILE_MANAGER to FileManagerImpl(dataBaseName),
				JustDBService.LOGGER_MANAGER to LogManagerImpl(logFileName),
				JustDBService.BUFFER_MANAGER to BufferManagerImpl(bufferSize),
				JustDBService.METADATA_MANAGER to MetadataManagerImpl()
		)
	}

//	private fun initFileManager(dataBaseName: String): FileManager {
//		val fileManager: FileManager = FileManagerImpl(dataBaseName)
//		systemServersSet.put(FILE_MANAGER, fileManager)
//		return fileManager
//	}
//
//	private fun initLogManager(logFile: String): LogManager {
//		val logManager: LogManager = LogManagerImpl(logFile)
//		systemServersSet.put(LOGGER_MANAGER, logManager)
//		return logManager
//	}
//
//	private fun initBufferManager(bufferSize: Int): BufferManager {
//		val bufferManager: BufferManager = BufferManagerImpl(bufferSize)
//		systemServersSet.put(BUFFER_MANAGER, bufferManager)
//		return bufferManager
//	}
//
//	private fun initMetadataManager(): MetadataManager {
//		val metadataManager: MetadataManager = MetadataManagerImpl()
//		systemServersSet.put(BUFFER_MANAGER, metadataManager)
//		return metadataManager
//	}
}