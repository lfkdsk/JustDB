package transaction.recovery

import buffer.Buffer
import core.BufferManager
import core.JustDB
import core.LogManager
import storage.Block
import java.util.*

/**
 * Created by liufengkai on 2017/5/1.
 */
class RecoveryManager(val justDB: JustDB, val transaction: Int) : Iterable<AbsLogRecord> {

	private val bufferManager = justDB.BufferManager()
	private val loggerManager = justDB.LogManager()

	init {
		StartRecord(justDB, transaction).writeToLog()
	}

	fun commit() {
		bufferManager.flushAll(transaction)
		val lsn = CommitRecord(justDB, transaction).writeToLog()
		loggerManager.flush(lsn)
	}

	fun rollback() {
		// do rollback
		this.filter { it.transactionNumber() == transaction }
				.forEach {
					if (it.op() == LogType.START)
						return
					it.undo(transaction)
				}
		// ----
		bufferManager.flushAll(transaction)
		loggerManager.flush(RollBackRecord(justDB, transaction).writeToLog())
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
		}
		// ----
		bufferManager.flushAll(transaction)
		loggerManager.flush(CheckPointRecord(justDB, transaction).writeToLog())
	}

	fun setInt(buff: Buffer, offset: Int, newval: Int): Int {
		val oldVal = buff.getInt(offset)
		val blk = buff.block()
		blk?.let {
			if (isTempBlock(blk)) {
				return -1
			} else {
				return SetIntLogRecord(justDB, transaction, blk, offset, oldVal).writeToLog()
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
				return SetStringLogRecord(justDB, transaction, blk, offset, oldVal).writeToLog()
			}
		}
		return -1
	}

	private fun isTempBlock(blk: Block): Boolean {
		return blk.fileName.startsWith("just-temp")
	}

	override fun iterator(): Iterator<AbsLogRecord> {
		return AbsLogRecordIterator(justDB)
	}
}