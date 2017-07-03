package transaction.recovery

import buffer.BufferManager
import core.JustDB
import core.JustDBService
import logger.LogRecord
import storage.Block

/**
 * Created by liufengkai on 2017/5/1.
 */
class SetIntLogRecord(val transaction: Int,
                      val block: Block,
                      val offset: Int,
                      val value: Int) : AbsLogRecord() {


	constructor(logRecord: LogRecord)
			: this(logRecord.nextInt(),
			Block(logRecord.nextString(), logRecord.nextInt()),
			logRecord.nextInt(),
			logRecord.nextInt())

	override fun writeToLog(): Int {
		val rec = arrayOf(LogType.SETINT, transaction, block.fileName, block.blockNumber, offset, value)
		return logManager.append(rec)
	}

	override fun op(): LogType {
		return LogType.SETINT
	}

	override fun transactionNumber(): Int {
		return transaction
	}

	override fun undo(transaction: Int) {
		val bufferManager = JustDB[JustDBService.BUFFER_MANAGER] as BufferManager
		val buff = bufferManager.pin(block)
		buff?.let {
			buff.setInt(offset, value, transaction, -1)
			bufferManager.unpin(it)
		}
	}

	override fun toString(): String {
		return "SetIntLogRecord(transaction=$transaction, block=$block, offset=$offset, value=$value)"
	}

}