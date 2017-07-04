package logger

import core.FileManager
import core.JustDB
import storage.Block
import storage.ExPage
import storage.ExPage.DEFAULT.BLOCK_SIZE
import storage.ExPage.DEFAULT.INT_SIZE
import storage.ExPage.DEFAULT.strSize
import utils.logger.Logger
import utils.standard.If

/**
 * Logger Manager
 * | LAST_POS | Record |
 * @see JustDB/art/logger-file-structure.png to get more message about logger-structure
 * Created by liufengkai on 2017/4/30.
 */
class LogManagerImpl(val justDB: JustDB, val logFile: String = justDB.logFileName) : LogManager {

	/**
	 * current page
	 */
	private val page = ExPage(justDB)

	/**
	 * current position
	 */
	private var currentPos = 0

	/**
	 * current block
	 */
	private var currentBlock: Block =
			justDB.FileManager()
					.blockNumber(logFile) // get block size
					.If({ logFileSize -> logFileSize == 0 }, {
						// init log file
						// save last record position
						setLastRecordPos(0)
						// reset pos
						currentPos = INT_SIZE
						// append to log page
						return@If page.append(logFile)
					}, { logFileSize ->
						currentBlock = Block(logFile, logFileSize - 1)
						page.read(currentBlock)
						currentPos = getLastPos() + INT_SIZE
						return@If currentBlock
					})

//	private fun initBlock(): Block {
//		val currentBlock: Block
//		val fileManager: FileManager = justDB.FileManager()
//		val logFileSize: Int = fileManager.blockNumber(logFile)
//		if (logFileSize == 0) {
//			// save last record position
//			setLastRecordPos(0)
//			// reset pos
//			currentPos = INT_SIZE
//			// append to log page
//			currentBlock = page.append(logFile)
//		} else {
//			currentBlock = Block(logFile, logFileSize - 1)
//			page.read(currentBlock)
//			currentPos = getLastPos() + INT_SIZE
//		}
//		return currentBlock
//	}

	companion object {
		// last record pos | position wont change
		var LAST_POS = 0
	}

	/**
	 * We save lastPosition-Pointer in first 4 byte(called LAST_POS)
	 * Every Element be saved into loggerFile LAST_POS will be updated
	 */
	private fun getLastPos(): Int {
		return page.getInt(LAST_POS)
	}

	/**
	 * set LAST_POS in log file in position
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

	/**
	 * append structure message to LoggerFile
	 * @see transaction.record Support some Record method to save Logger
	 * { RecordType , otherMessage }
	 * @see transaction.recovery.RecoveryManager Use RecoveryManager to control Logger
	 */
	@Synchronized
	override fun append(rec: List<Any>): Int {
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

		Logger.d("pos save to local block $currentBlock \n" +
				"write to file | ${getLastPos()} | at position $currentPos")

		setLastRecordPos(currentPos)
		currentPos += INT_SIZE
	}

	private fun appendVal(value: Any) {
		if (value is String)
			page.setString(currentPos, value)
		else if (value is Int)
			page.setInt(currentPos, value)

		Logger.d("isString: ${value is String} \n" +
				"isInt: ${value is Int}  \n" +
				"val save to local block $currentBlock \n" +
				"write to file | $value | at position $currentPos")

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

	private fun currentBlockNumber() = currentBlock.blockNumber

	@Synchronized
	override operator fun iterator(): Iterator<LogRecord> {
		flush()
		return LogIterator(justDB, currentBlock)
	}

	class LogIterator(justDB: JustDB, var currentBlock: Block) : Iterator<LogRecord> {
		private val page = ExPage(justDB)

		private var currentRecord: Int

		init {
			page.read(currentBlock)
			currentRecord = page.getInt(LogManagerImpl.LAST_POS)
			println("init currentRecord $currentRecord")
		}

		override operator fun next(): LogRecord {
			if (currentRecord == 0)
				moveToNextBlock()
			currentRecord = page.getInt(currentRecord)
			println("currentRecord $currentRecord")
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
			println("check to next block ${currentBlock.fileName} : ${currentBlock.blockNumber} ")
			page.read(currentBlock)
			currentRecord = page.getInt(LogManagerImpl.LAST_POS)
			println("current record $currentRecord")
		}
	}
}
