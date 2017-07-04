package transaction.record

import core.JustDB
import logger.LogRecord

/**
 * Creates a new start log record for the specified transaction.
 * Created by liufengkai on 2017/5/1.
 */
class StartRecord(justDB: JustDB, val transaction: Int) : AbsLogRecord(justDB) {

	constructor(justDB: JustDB, logRecord: LogRecord)
			: this(justDB, logRecord.nextInt())

	override fun writeToLog(): Int {
		val rec = listOf(LogType.START.value, transaction)
		return logManager.append(rec)
	}

	override fun op(): LogType {
		return LogType.START
	}

	override fun transactionNumber(): Int {
		return transaction
	}

	override fun undo(transaction: Int) {

	}

	override fun toString(): String {
		return "StartRecord(transactionID=$transaction)"
	}

}