package core

import buffer.BufferManagerImpl
import logger.LogManagerImpl
import metadata.MetadataManagerImpl
import storage.FileManagerImpl
import utils.logger.Logger

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

class JustDB(val dataBaseName: String = "just-db",
             val logFileName: String = dataBaseName + "-log.log") {
	val bufferSize = 8

	private val systemServersSet: MutableMap<String, SystemService> =
			mutableMapOf()

	init {
		Logger.init(dataBaseName)
		/**
		 * load path: file -> metadata -> logger -> buffer
		 */
		systemServersSet.put(JustDBService.FILE_MANAGER, FileManagerImpl(dataBaseName))
		systemServersSet.put(JustDBService.METADATA_MANAGER, MetadataManagerImpl())
		systemServersSet.put(JustDBService.LOGGER_MANAGER, LogManagerImpl(this@JustDB, logFileName))
		systemServersSet.put(JustDBService.BUFFER_MANAGER, BufferManagerImpl(this@JustDB, bufferSize))
	}

	fun getService(serName: String): SystemService {
		return systemServersSet[serName] ?: throw CanNotFindService("can not find service: $serName")
	}

	operator fun get(serName: String) = getService(serName)

	override fun toString(): String {
		return "\n <JustDB message> database $dataBaseName \n " +
				"database-name: $dataBaseName \n " +
				"database-log-name: $logFileName \n " +
				"database-provide-services: ${systemServersSet.keys}"
	}
}