package transaction.concurrency

import storage.Block
import java.util.*

/**
 * Concurrency Manager
 * @see LockTable base function depend on lockTable
 * Created by liufengkai on 2017/5/1.
 */
class ConcurrencyManager {

	/**
	 * lock-table
	 */
	val lockTable: LockTable = LockTable()

	/**
	 * bind < Block , Lock Type> message
	 */
	private val locks = HashMap<Block, String>()

	/**
	 * add read lock
	 * @param block block
	 */
	fun readLock(block: Block) {
		locks[block]?.run {
			lockTable.readLock(block)
			locks.put(block, "R")
			println("add read lock to $block")
		}
	}

	/**
	 * add write lock
	 * 1.add read lock
	 * 2.writeLock => block
	 * 3.upgrade read-lock to write-lock
	 */
	fun writeLock(block: Block) {
		if (!hasWriteLock(block)) {
			// add read lock
			readLock(block)
			// add write lock - lock-update
			lockTable.writeLock(block)
			locks.put(block, "W")
			println("add write lock to $block")
		}
	}

	fun release() {
		for (block in locks.keys)
			lockTable.unlock(block)
		locks.clear()
	}

	/**
	 * has write lock
	 * @param block lock-block
	 */
	private fun hasWriteLock(block: Block): Boolean {
		val lockType = locks[block]
		return lockType ?: lockType == "W"
	}
}