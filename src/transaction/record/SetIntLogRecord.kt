package transaction.record

import core.BufferManager
import core.JustDB
import logger.LogRecord
import storage.Block

/**
 * write int => generateIntLogRecord
 * Created by liufengkai on 2017/5/1.
 */
class SetIntLogRecord(val justDB: JustDB,
                      val transaction: Int,
                      val block: Block,
                      val offset: Int,
                      val value: Int) : AbsLogRecord(justDB) {


	constructor(justDB: JustDB, logRecord: LogRecord)
			: this(
			justDB,
			logRecord.nextInt(),
			Block(logRecord.nextString(), logRecord.nextInt()),
			logRecord.nextInt(),
			logRecord.nextInt())

	override fun writeToLog(): Int {
		val rec = listOf(LogType.SETINT.value, transaction, block.fileName, block.blockNumber, offset, value)
		return logManager.append(rec)
	}

	override fun op(): LogType {
		return LogType.SETINT
	}

	override fun transactionNumber(): Int {
		return transaction
	}

	/**
	 * undo transaction => write int
	 * @param transaction transaction ID
	 */
	override fun undo(transaction: Int) {
		val bufferManager = justDB.BufferManager()
		// pin block to buffer
		val buffer = bufferManager.pin(block)
		buffer?.let { block ->
			buffer.setInt(offset, value, transaction, -1)
			bufferManager.unpin(block)
		}
	}

	override fun toString(): String {
		return "<SetIntLogRecord(transactionID=$transaction, block=$block, offset=$offset, value=$value)>"
	}

}