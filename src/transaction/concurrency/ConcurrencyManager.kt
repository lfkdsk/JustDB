package transaction.concurrency

import storage.Block
import java.util.*

/**
 * Created by liufengkai on 2017/5/1.
 */
class ConcurrencyManager {

	val lockTable: LockTable = LockTable
	private val locks = HashMap<Block, String>()

	/**
	 * add read lock
	 * @param blk block
	 */
	fun readLock(blk: Block) {
		if (locks[blk] == null) {
			lockTable.readLock(blk)
			locks.put(blk, "S")
		}
	}

	/**
	 * add write lock
	 * 1.add read lock
	 * 2.writeLock => block
	 * 3.upgrade read-lock to write-lock
	 */
	fun writeLock(blk: Block) {
		if (!hasXLock(blk)) {
			readLock(blk)
			lockTable.writeLock(blk)
			locks.put(blk, "X")
		}
	}

	fun release() {
		for (blk in locks.keys)
			lockTable.unlock(blk)
		locks.clear()
	}

	private fun hasXLock(blk: Block): Boolean {
		val lockType = locks[blk]
		return lockType != null && lockType == "X"
	}
}