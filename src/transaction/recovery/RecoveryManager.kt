package transaction.recovery

import buffer.Buffer
import core.BufferManager
import core.JustDB
import core.LogManager
import storage.Block
import transaction.record.*
import java.util.*

/**
 * Created by liufengkai on 2017/5/1.
 */
class RecoveryManager(val justDB: JustDB, val transactionID: Int) : Iterable<AbsLogRecord> {

	private val bufferManager = justDB.BufferManager()
	private val loggerManager = justDB.LogManager()

	init {
		StartRecord(justDB, transactionID).writeToLog()
	}

	fun commit() {
		bufferManager.flushAll(transactionID)
		val lsn = CommitRecord(justDB, transactionID).writeToLog()
		loggerManager.flush(lsn)
	}

	fun rollback() {
		// do rollback
		this.filter { logRecord -> logRecord.transactionNumber() == transactionID }
				.forEach { logRecord ->
					if (logRecord.op() == LogType.START)
						return
					logRecord.undo(transactionID)
				}
		// ----
		bufferManager.flushAll(transactionID)
		loggerManager.flush(RollBackRecord(justDB, transactionID).writeToLog())
	}

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

	fun setInt(buff: Buffer, offset: Int, newval: Int): Int {
		val oldVal = buff.getInt(offset)
		val blk = buff.block()
		blk?.let {
			if (isTempBlock(blk)) {
				return -1
			} else {
				return SetIntLogRecord(justDB, transactionID, blk, offset, oldVal).writeToLog()
			}
		}
		return -1
	}

	fun setString(buff: Buffer, offset: Int, newval: String): Int {
		val oldVal = buff.getString(offset)
		val blk = buff.block()
		blk?.let {
			if (isTempBlock(blk)) {
				return -1
			} else {
				return SetStringLogRecord(justDB, transactionID, blk, offset, oldVal).writeToLog()
			}
		}
		return -1
	}

	private fun isTempBlock(blk: Block): Boolean = blk.fileName.startsWith("just-temp")

	override fun iterator(): Iterator<AbsLogRecord> = AbsLogRecordIterator(justDB)
}