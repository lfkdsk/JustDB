package transaction.record

import core.JustDB
import logger.LogRecord

/**
 * RollBack FileRecord | JUST FLAG
 * Created by liufengkai on 2017/5/1.
 */
class RollBackRecord(justDB: JustDB, val transaction: Int) : AbsLogRecord(justDB) {

	constructor(justDB: JustDB, logRecord: LogRecord) : this(justDB, logRecord.nextInt())

	override fun writeToLog(): Int {
		val rec = listOf(LogType.ROLLBACK.value, transaction)
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
		return "<RollBackRecord(transactionID=$transaction)>"
	}

}