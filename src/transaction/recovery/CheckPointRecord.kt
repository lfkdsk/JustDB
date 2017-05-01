package transaction.recovery

import logger.LogRecord

/**
 * Created by liufengkai on 2017/5/1.
 */
class CheckPointRecord : AbsLogRecord {

	constructor(logRecord: LogRecord)

	constructor(transaction: Int)

	override fun writeToLog(): Int {
		val rec = arrayOf<Any>(LogType.CHECKPOINT)
		return logManager.append(rec)
	}

	override fun op(): LogType {
		return LogType.CHECKPOINT
	}

	override fun transactionNumber(): Int {
		return -1 // dummy value
	}

	override fun undo(transaction: Int) {}

	override fun toString(): String {
		return "CheckPointRecord()"
	}
}