package transaction.recovery

import logger.LogRecord

/**
 * Created by liufengkai on 2017/5/1.
 */
class RollBackRecord(val transaction: Int) : AbsLogRecord() {

	constructor(logRecord: LogRecord) : this(logRecord.nextInt())

	override fun writeToLog(): Int {
		val rec = arrayOf(LogType.ROLLBACK, transaction)
		return logManager.append(rec)
	}

	override fun op(): LogType {
		return LogType.ROLLBACK
	}

	override fun transactionNumber(): Int {
		return transaction
	}

	override fun undo(transaction: Int) {}

	override fun toString(): String {
		return "RollBackRecord(transaction=$transaction)"
	}

}