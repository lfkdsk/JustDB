package transaction.recovery

import buffer.BufferManager
import core.JustDB
import logger.LogRecord
import storage.Block

/**
 * Created by liufengkai on 2017/5/1.
 */
class SetStringLogRecord(val transaction: Int,
                         val block: Block,
                         val offset: Int,
                         val value: String) : AbsLogRecord() {

	constructor(logRecord: LogRecord)
			: this(logRecord.nextInt(),
			Block(logRecord.nextString(), logRecord.nextInt()),
			logRecord.nextInt(),
			logRecord.nextString())


	override fun writeToLog(): Int {
		val rec = arrayOf(LogType.SETSTRING, transaction, block.fileName, block.blockNumber, offset, value)
		return logManager.append(rec)
	}

	override fun op(): LogType {
		return LogType.SETSTRING
	}

	override fun transactionNumber(): Int {
		return transaction
	}

	override fun undo(transaction: Int) {
		val bufferManager = JustDB[JustDB.BUFFER_MANAGER] as BufferManager
		val buff = bufferManager.pin(block)
		buff?.let {
			buff.setString(offset, value, transaction, -1)
			bufferManager.unpin(it)
		}
	}

	override fun toString(): String {
		return "SetStringLogRecord(transaction=$transaction, block=$block, offset=$offset, value='$value')"
	}

}