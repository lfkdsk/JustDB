package transaction.recovery

import buffer.Buffer
import buffer.BufferManager
import core.JustDB
import logger.LogManager
import storage.Block

/**
 * Created by liufengkai on 2017/5/1.
 */
class RecoveryManager(val transaction: Int) {
	private val bufferManager = JustDB[JustDB.BUFFER_MANAGER] as BufferManager
	private val loggerManager = JustDB[JustDB.BUFFER_MANAGER] as LogManager

	init {
		StartRecord(transaction).writeToLog()
	}

	fun commit() {
		bufferManager.flushAll(transaction)
		val lsn = CommitRecord(transaction).writeToLog()
		loggerManager.flush(lsn)
	}

	fun setInt(buff: Buffer, offset: Int, newval: Int): Int {
		val oldVal = buff.getInt(offset)
		val blk = buff.block()
		blk?.let {
			if (isTempBlock(blk)) {
				return -1
			} else {
				return SetIntLogRecord(transaction, blk, offset, oldVal).writeToLog()
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
				return SetStringLogRecord(transaction, blk, offset, oldVal).writeToLog()
			}
		}
		return -1
	}


	private fun isTempBlock(blk: Block): Boolean {
		return blk.fileName.startsWith("just-temp")
	}
}