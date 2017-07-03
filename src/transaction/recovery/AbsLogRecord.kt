package transaction.recovery

import core.JustDB
import core.JustDBService
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

abstract class AbsLogRecord {

	val logManager: LogManager = JustDB[JustDBService.LOGGER_MANAGER] as LogManager

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
	 * Returns the transaction id stored with
	 * the log record.
	 * @return the log record's transaction id
	 */
	abstract fun transactionNumber(): Int

	/**
	 * Undoes the operation encoded by this log record.
	 * The only log record types for which this method
	 * does anything interesting are SETINT and SETSTRING.
	 * @param transaction the id of the transaction that is performing the undo.
	 */
	abstract fun undo(transaction: Int)
}

class AbsLogRecordIterator : Iterator<AbsLogRecord> {
	private val iterator = (JustDB[JustDBService.LOGGER_MANAGER] as LogManager).iterator()

	override fun hasNext(): Boolean {
		return iterator.hasNext()
	}

	override fun next(): AbsLogRecord {
		val rec = iterator.next()
		val op = rec.nextInt()
		when (op) {
			LogType.CHECKPOINT.value -> return CheckPointRecord(rec)
			LogType.START.value -> return StartRecord(rec)
			LogType.COMMIT.value -> return CommitRecord(rec)
			LogType.ROLLBACK.value -> return RollBackRecord(rec)
			LogType.SETINT.value -> return SetIntLogRecord(rec)
			LogType.SETSTRING.value -> return SetStringLogRecord(rec)
			else -> throw CanNotFindLogRecord()
		}
	}
}