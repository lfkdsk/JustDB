package transaction.record

import core.JustDB
import logger.LogRecord

/**
 * CommitRecord | JUST FLAG
 * Created by liufengkai on 2017/5/1.
 */
class CommitRecord(justDB: JustDB, val transaction: Int) : AbsLogRecord(justDB) {

	constructor(justDB: JustDB, logRecord: LogRecord) : this(justDB, logRecord.nextInt())

	override fun writeToLog(): Int {
		val rec = listOf(LogType.COMMIT.value, transaction)
		return logManager.append(rec)
	}

	override fun op(): LogType {
		return LogType.COMMIT
	}

	override fun transactionNumber(): Int {
		return transaction
	}

	override fun undo(transaction: Int) {}

	override fun toString(): String {
		return "<CommitRecord(transactionID=$transaction)>"
	}
}