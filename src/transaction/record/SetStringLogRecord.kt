package transaction.record

import core.BufferManager
import core.JustDB
import logger.LogRecord
import storage.Block

/**
 * write string to file => generateStringLogRecord
 * Created by liufengkai on 2017/5/1.
 */
class SetStringLogRecord(val justDB: JustDB,
                         val transaction: Int,
                         val block: Block,
                         val offset: Int,
                         val value: String) : AbsLogRecord(justDB) {

	constructor(justDB: JustDB, logRecord: LogRecord)
			: this(justDB,
			logRecord.nextInt(),
			Block(logRecord.nextString(), logRecord.nextInt()),
			logRecord.nextInt(),
			logRecord.nextString())


	override fun writeToLog(): Int {
		val rec = listOf(LogType.SETSTRING.value, transaction, block.fileName, block.blockNumber, offset, value)
		return logManager.append(rec)
	}

	override fun op(): LogType {
		return LogType.SETSTRING
	}

	override fun transactionNumber(): Int {
		return transaction
	}

	/**
	 * undo write string to file
	 * @param transaction transaction ID
	 */
	override fun undo(transaction: Int) {
		val bufferManager = justDB.BufferManager()
		val buffer = bufferManager.pin(block)
		buffer?.let { block ->
			buffer.setString(offset, value, transaction, -1)
			bufferManager.unpin(block)
		}
	}

	override fun toString(): String {
		return "<SetStringLogRecord(transactionID=$transaction, block=$block, offset=$offset, value='$value')>"
	}

}