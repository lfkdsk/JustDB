package transaction.recovery

import logger.LogRecord

/**
 * Created by liufengkai on 2017/5/1.
 */
class StartRecord(val transaction: Int) : AbsLogRecord() {
	constructor(logRecord: LogRecord) : this(logRecord.nextInt())

	override fun writeToLog(): Int {
		val rec = arrayOf(LogType.START, transaction)
		return logManager.append(rec)
	}

	override fun op(): LogType {
		return LogType.START
	}

	override fun transactionNumber(): Int {
		return transaction
	}

	override fun undo(transaction: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun toString(): String {
		return "StartRecord(transaction=$transaction)"
	}

}