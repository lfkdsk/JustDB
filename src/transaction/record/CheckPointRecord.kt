package transaction.record

import core.JustDB
import logger.LogRecord

/**
 * Created by liufengkai on 2017/5/1.
 */
class CheckPointRecord(justDB: JustDB) : AbsLogRecord(justDB) {

	constructor(justDB: JustDB, logRecord: LogRecord) : this(justDB)

	constructor(justDB: JustDB, transaction: Int) : this(justDB)

	override fun writeToLog(): Int {
		val rec = listOf<Any>(LogType.CHECKPOINT)
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