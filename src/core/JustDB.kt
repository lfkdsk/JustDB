package core

import storage.Block
import storage.FileManager
import storage.FileManagerImpl
import java.nio.ByteBuffer

/**
 * Created by liufengkai on 2017/4/30.
 */

interface SystemService {

	/**
	 * File Manager Interface
	 */
	fun read(block: Block, byteBuffer: ByteBuffer)

	fun write(block: Block, byteBuffer: ByteBuffer)

	fun append(filename: String, byteBuffer: ByteBuffer): Block
}

object JustDB {
	const val FILE_MANAGER = "FILE_MANAGER"

	private val systemServersSet: MutableMap<String, SystemService> = HashMap()

	private var dataBaseName = "just-db"

	fun init(dataBaseName: String) {
		this.dataBaseName = dataBaseName
		initFileManager(dataBaseName)
	}

	private fun initFileManager(dataBaseName: String): FileManager {
		val fileManager: FileManager = FileManagerImpl(dataBaseName)
		systemServersSet.put(FILE_MANAGER, fileManager)
		return fileManager
	}

	fun getService(serName: String)
			= systemServersSet[serName] ?: initFileManager(dataBaseName)

	operator fun get(serName: String) = getService(serName)

}