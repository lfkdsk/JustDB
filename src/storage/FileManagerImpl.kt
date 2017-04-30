package storage

import storage.ExPage.DEFAULT.BLOCK_SIZE
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

/**
 * Created by liufengkai on 2017/4/24.
 */
class FileManagerImpl(databaseName: String, homeDir: String)
	: FileManager {

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

	constructor(databaseName: String)
			: this(databaseName, "./tests/databasetemp/")
//	constructor(databaseName: String)
//			: this(databaseName, System.getProperty("user.home"))

	override fun read(block: Block, byteBuffer: ByteBuffer) {
		try {
			byteBuffer.clear()
			val fileChannel: FileChannel = getFile(block.fileName)
			fileChannel.read(byteBuffer, (block.blockNumber * ExPage.BLOCK_SIZE).toLong())
		} catch (e: IOException) {
			throw RuntimeException("cannot read block : $block")
		}
	}

	@Synchronized override
	fun write(block: Block, byteBuffer: ByteBuffer) {
		try {
			byteBuffer.rewind()
			val fileChannel = getFile(block.fileName)
			fileChannel.write(byteBuffer, (block.blockNumber * BLOCK_SIZE).toLong())
		} catch (e: IOException) {
			throw RuntimeException("cannot write block $block")
		}
	}

	@Synchronized override
	fun append(filename: String, byteBuffer: ByteBuffer): Block {
		val newBlockNumber = blockNumber(filename)
		val blk = Block(filename, newBlockNumber)
		write(blk, byteBuffer)
		return blk
	}

	@Synchronized override
	fun blockNumber(filename: String): Int {
		try {
			val fc = getFile(filename)
			return (fc.size() / BLOCK_SIZE).toInt()
		} catch (e: IOException) {
			throw RuntimeException("cannot access " + filename)
		}
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