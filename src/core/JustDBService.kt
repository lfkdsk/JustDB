package core

import buffer.BufferManager
import logger.LogManager
import metadata.MetadataManager
import storage.FileManager

/**
 * Register Service Here!
 * Created by liufengkai on 2017/7/3.
 */

object JustDBService {
	const val FILE_MANAGER = "FILE_MANAGER"
	const val LOGGER_MANAGER = "LOGGER_MANAGER"
	const val BUFFER_MANAGER = "BUFFER_MANAGER"
	const val METADATA_MANAGER = "METADATA_MANAGER"
}

fun JustDB.FileManager(): FileManager {
	return this[JustDBService.FILE_MANAGER] as FileManager
}

fun JustDB.LogManager(): LogManager {
	return this[JustDBService.LOGGER_MANAGER] as LogManager
}

fun JustDB.BufferManager(): BufferManager {
	return this[JustDBService.BUFFER_MANAGER] as BufferManager
}

fun JustDB.MetaDataManager(): MetadataManager {
	return this[JustDBService.METADATA_MANAGER] as MetadataManager
}

