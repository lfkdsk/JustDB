package logger

import core.JustDB
import storage.Block
import storage.ExPage
import storage.ExPage.DEFAULT.BLOCK_SIZE
import storage.ExPage.DEFAULT.INT_SIZE
import storage.ExPage.DEFAULT.strSize
import storage.FileManager

/**
 * Created by liufengkai on 2017/4/30.
 */
class LogManagerImpl(val logFile: String = JustDB.logFileName) : LogManager {

	private val page = ExPage()
	private var currentBlock: Block
	private var currentPos = 0

	init {
		val fileManager: FileManager = JustDB[JustDB.FILE_MANAGER] as FileManager
		val logFileSize: Int = fileManager.blockNumber(logFile)
		if (logFileSize == 0) {
			// save last record position
			setLastRecordPos(0)
			// reset pos
			currentPos = INT_SIZE
			// append to log page
			currentBlock = page.append(logFile)
		} else {
			currentBlock = Block(logFile, logFileSize - 1)
			page.read(currentBlock)
		}
	}

	companion object {
		// last record pos
		var LAST_POS = 0
	}

	private fun getLastPos(): Int {
		return page.getInt(LAST_POS)
	}

	/**
	 * set LAST_POS in log file in postion
	 * @param pos in logfile
	 */
	private fun setLastRecordPos(pos: Int) {
		page.setInt(LAST_POS, pos)
	}

	/**
	 * save all the block which id is earlier than
	 * @param recordStored
	 */
	override fun flush(recordStored: Int) {
		// write current block to log file
		if (recordStored >= currentBlockNumber())
			flush()
	}

	/**
	 * save all the block
	 */
	private fun flush() {
		page.write(currentBlock)
	}

	@Synchronized
	override fun append(rec: Array<Any>): Int {
		// 4 bytes for the integer that points to the previous log record
		val recordSize = INT_SIZE + rec.sumBy { sizeOfObject(it) }
		// the log record doesn't fit,
		if (currentPos + recordSize >= BLOCK_SIZE) {
			flush()
			// so move to the next block.
			createNewBlock()
		}
		rec.forEach { appendVal(it) }
		finalizeRecord()
		return currentBlockNumber()
	}

	private fun finalizeRecord() {
		page.setInt(currentPos, getLastPos())
		setLastRecordPos(currentPos)
		currentPos += INT_SIZE
	}

	private fun appendVal(value: Any) {
		if (value is String)
			page.setString(currentPos, value)
		else if (value is Int)
			page.setInt(currentPos, value)
		currentPos += sizeOfObject(value)
	}


	private fun createNewBlock() {
		// save last record position
		setLastRecordPos(0)
		// reset pos
		currentPos = INT_SIZE
		// append to log page
		currentBlock = page.append(logFile)
	}

	/**
	 * Calculates the sizeOfObject of the specified integer or string.
	 * @param value the value
	 * *
	 * @return the sizeOfObject of the value, in bytes
	 */
	private fun sizeOfObject(value: Any): Int {
		if (value is String) {
			return strSize(value.length)
		} else
			return INT_SIZE
	}

	private fun currentBlockNumber(): Int {
		return currentBlock.blockNumber
	}

	@Synchronized
	override operator fun iterator(): Iterator<LogRecord> {
		flush()
		return LogIterator(currentBlock)
	}

	class LogIterator(var currentBlock: Block) : Iterator<LogRecord> {
		private val page = ExPage()

		private var currentRecord: Int

		init {
			page.read(currentBlock)
			currentRecord = page.getInt(LogManagerImpl.LAST_POS)
		}

		override operator fun next(): LogRecord {
			if (currentRecord == 0)
				moveToNextBlock()
			currentRecord = page.getInt(currentRecord)
			return LogRecord(page, currentRecord + INT_SIZE)
		}

		override operator fun hasNext()
				= currentRecord > 0 || currentBlock.blockNumber > 0

		/**
		 * Moves to the next log block in reverse order,
		 * and positions it after the last record in that block.
		 */
		private fun moveToNextBlock() {
			currentBlock = Block(currentBlock.fileName, currentBlock.blockNumber - 1)
			page.read(currentBlock)
			currentRecord = page.getInt(LogManagerImpl.LAST_POS)
		}
	}


}