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

	private fun createNewTableChannel(fileName: String): FileChannel {
		val newTable = File(dataBaseDir, fileName)
		val randomAccess: RandomAccessFile = RandomAccessFile(newTable, "rws")
		return randomAccess.channel
	}

	private fun getFile(fileName: String): FileChannel {
		return openFiles[fileName] ?: createNewTableChannel(fileName)
	}
}