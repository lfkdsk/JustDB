package storage

import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

/**
 * Created by liufengkai on 2017/4/24.
 */
class FileManager {
	private val dataBaseDir: File

	private var isNewDataBase: Boolean

	private var openFiles: MutableMap<String, FileChannel> = HashMap()

	constructor(databaseName: String) {
		val homeDir = System.getProperty("user.home")
		// create database file
		this.dataBaseDir = File(homeDir, databaseName)
		this.isNewDataBase = !dataBaseDir.exists()

		// test isCan create dir
		if (isNewDataBase && !dataBaseDir.mkdir()) {
			throw RuntimeException("cannot create database : $databaseName")
		}

		// delete temp files
		dataBaseDir.list().forEach {
			if (it.startsWith("just-temp")) {
				File(dataBaseDir, it).delete()
			}
		}
	}

	fun read(block: Block, byteBuffer: ByteBuffer) {
		byteBuffer.clear()
		var fileChannel: FileChannel = getFile(block.fileName)
	}

	private fun getFile(fileName: String): FileChannel {
		var fileChannel: FileChannel? = openFiles[fileName]
		return fileChannel ?: fileChannel.apply {
			val newTable = File(dataBaseDir, fileName)
			val randomAccess: RandomAccessFile = RandomAccessFile(newTable, "rws")
			fileChannel = randomAccess.channel
			openFiles.put(fileName, fileChannel as FileChannel)
		} as FileChannel
	}
}