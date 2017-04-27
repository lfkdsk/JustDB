package storage

import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

/**
 * Created by liufengkai on 2017/4/24.
 */
class FileManager(databaseName: String, homeDir: String) {

	private val dataBaseDir: File = File(homeDir, databaseName)

	private var isNewDataBase: Boolean

	private var openFiles: MutableMap<String, FileChannel> = HashMap()

	init {
		// create database file
		this.isNewDataBase = !dataBaseDir.exists()

		// test isCan create dir
		if (isNewDataBase && !dataBaseDir.mkdir()) {
			throw RuntimeException("cannot create database : $databaseName")
		}

		// delete temp files
		dataBaseDir.list()
				.filter { it.startsWith("just-temp") }
				.forEach { File(dataBaseDir, it).delete() }
	}

	constructor(databaseName: String) : this(databaseName, System.getProperty("user.home"))

	fun read(block: Block, byteBuffer: ByteBuffer) {
		byteBuffer.clear()
		var fileChannel: FileChannel = getFile(block.fileName)
	}

	/**
	 * create new table
	 * @param fileName tableName
	 */
	private fun createNewTableChannel(fileName: String): FileChannel {
		val newTable = File(dataBaseDir, fileName)
		val randomAccess: RandomAccessFile = RandomAccessFile(newTable, "rws")
		return randomAccess.channel
	}

	private fun getFile(fileName: String): FileChannel {
		return openFiles[fileName] ?: createNewTableChannel(fileName)
	}
}