package transaction.recovery

import buffer.Buffer
import core.BufferManager
import core.JustDB
import core.LogManager
import storage.Block
import transaction.record.*
import java.util.*

/**
 * Recovery Manager
 * use recovery-manager - to control log-record-msg
 *
 * @param justDB database
 * @param transactionID transaction - ID
 *
 * @see logger.LogManagerImpl control log-manager
 * @see buffer.BufferManagerImpl control buffer-manager
 * @sample transaction.recovery.RecoveryManagerImplTest test recovery-manager
 * Created by liufengkai on 2017/5/1.
 */
class RecoveryManager(val justDB: JustDB, val transactionID: Int) : Iterable<AbsLogRecord> {

	private val bufferManager = justDB.BufferManager()

	/**
	 * log-manager
	 */
	private val loggerManager = justDB.LogManager()

	init {
		// start-record write to file
		StartRecord(justDB, transactionID).writeToLog()
	}

	/**
	 * Commit Record
	 * 1.commit flush all buffers
	 * 2.add commit record
	 * 3.flush
	 */
	fun commit() {
		bufferManager.flushAll(transactionID)
		val lsn = CommitRecord(justDB, transactionID).writeToLog()
		loggerManager.flush(lsn)
	}

	/**
	 * RollBack Record
	 * 1.roll bind transaction
	 * 2.for-each => undo
	 * 3.flush buffers & logger
	 */
	fun rollback() {
		// do rollback
		this.filter { logRecord ->
			logRecord.transactionNumber() == transactionID
		}.forEach { logRecord ->
			if (logRecord.op() == LogType.START)
				return
			logRecord.undo(transactionID)
		}.apply {
			bufferManager.flushAll(transactionID)
			loggerManager.flush(RollBackRecord(justDB, transactionID).writeToLog())
		}
	}

	/**
	 * recover
	 */
	fun recover() {
		val finishedTransactions = ArrayList<Int>()
		this.forEach {
			when (it.op()) {
				LogType.CHECKPOINT -> return
				LogType.COMMIT -> finishedTransactions.add(it.transactionNumber())
				LogType.ROLLBACK -> finishedTransactions.add(it.transactionNumber())
				else -> {
					if (!finishedTransactions.contains(it.transactionNumber())) {
						it.undo(it.transactionNumber())
					}
				}
			}
		}.apply {
			bufferManager.flushAll(transactionID)
			loggerManager.flush(CheckPointRecord(justDB, transactionID).writeToLog())
		}
	}

	/**
	 * write int to file
	 * @param buffer
	 * @param offset to current-pointer
	 * @param newVal new int value
	 */
	fun setInt(buffer: Buffer, offset: Int, newVal: Int): Int {
//		val oldVal = buffer.getInt(offset)
		val block = buffer.block()
		block?.let {
			if (isTempBlock(block)) {
				return -1
			} else {
				return SetIntLogRecord(justDB, transactionID, block, offset, newVal).writeToLog()
			}
		}

		return -1
	}

	/**
	 * write string to file
	 * @param buffer
	 * @param offset offset to current-pointer
	 * @param newVal new String value
	 */
	fun setString(buffer: Buffer, offset: Int, newVal: String): Int {
//		val oldVal = buffer.getString(offset)
		val block = buffer.block()
		block?.let {
			if (isTempBlock(block)) {
				return -1
			} else {
				return SetStringLogRecord(justDB, transactionID, block, offset, newVal).writeToLog()
			}
		}
		return -1
	}

	/**
	 * is temp block
	 */
	private fun isTempBlock(blk: Block): Boolean = blk.fileName.startsWith("just-temp")

	override fun iterator(): Iterator<AbsLogRecord> = AbsLogRecordIterator(justDB)
}