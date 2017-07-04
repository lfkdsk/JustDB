package transaction.record

import core.BufferManager
import core.JustDB
import logger.LogRecord
import storage.Block

/**
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
		val rec = listOf(LogType.SETSTRING, transaction, block.fileName, block.blockNumber, offset, value)
		return logManager.append(rec)
	}

	override fun op(): LogType {
		return LogType.SETSTRING
	}

	override fun transactionNumber(): Int {
		return transaction
	}

	override fun undo(transaction: Int) {
		val bufferManager = justDB.BufferManager()
		val buff = bufferManager.pin(block)
		buff?.let {
			buff.setString(offset, value, transaction, -1)
			bufferManager.unpin(it)
		}
	}

	override fun toString(): String {
		return "<SetStringLogRecord(transactionID=$transaction, block=$block, offset=$offset, value='$value')>"
	}

}