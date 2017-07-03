package transaction.record

import core.JustDB
import core.LogManager
import logger.LogManager

/**
 * Created by liufengkai on 2017/5/1.
 */
enum class LogType(val value: Int) {
	CHECKPOINT(0),
	START(1),
	COMMIT(2),
	ROLLBACK(3),
	SETINT(4),
	SETSTRING(5)
}

class CanNotFindLogRecord : Exception()

abstract class AbsLogRecord(justDB: JustDB) {

	val logManager: LogManager = justDB.LogManager()

	/**
	 * Writes the record to the log and returns its LSN.
	 * @return the LSN of the record in the log
	 */
	abstract fun writeToLog(): Int

	/**
	 * Returns the log record's type.
	 * @return the log record's type
	 */
	abstract fun op(): LogType

	/**
	 * Returns the transactionID id stored with
	 * the log record.
	 * @return the log record's transactionID id
	 */
	abstract fun transactionNumber(): Int

	/**
	 * Undoes the operation encoded by this log record.
	 * The only log record types for which this method
	 * does anything interesting are SETINT and SETSTRING.
	 * @param transaction the id of the transactionID that is performing the undo.
	 */
	abstract fun undo(transaction: Int)
}

class AbsLogRecordIterator(val justDB: JustDB) : Iterator<AbsLogRecord> {
	private val iterator = (justDB.LogManager()).iterator()

	override fun hasNext(): Boolean {
		return iterator.hasNext()
	}

	override fun next(): AbsLogRecord {
		val rec = iterator.next()
		val op = rec.nextInt()
		when (op) {
			LogType.CHECKPOINT.value -> return CheckPointRecord(justDB, rec)
			LogType.START.value -> return StartRecord(justDB, rec)
			LogType.COMMIT.value -> return CommitRecord(justDB, rec)
			LogType.ROLLBACK.value -> return RollBackRecord(justDB, rec)
			LogType.SETINT.value -> return SetIntLogRecord(justDB, rec)
			LogType.SETSTRING.value -> return SetStringLogRecord(justDB, rec)
			else -> throw CanNotFindLogRecord()
		}
	}
}