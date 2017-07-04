package transaction.concurrency

import storage.Block

/**
 * S -- sync(read lock)
 * X -- mutually(write lock)
 * Created by liufengkai on 2017/5/1.
 */

class LockAbortException : RuntimeException()

/**
 * Lock Table
 * Concurrency control add lock to block
 * @see ConcurrencyManager more control concurrency function
 */
class LockTable {

	/**
	 * max wait time
	 */
	private val MAX_TIME: Long = 10000 // 10 seconds
	/**
	 * mutable locks - control
	 */
	private val locks: MutableMap<Block, Int> = HashMap()
	/**
	 * 	lock object
	 */
	private var lockObject = java.lang.Object()

	/**
	 * add writeLock
	 * @param block add lock to block
	 */
	@Throws(LockAbortException::class)
	fun writeLock(block: Block) = synchronized(lockObject) {
		val timeStamp: Long = System.currentTimeMillis()
		// hasOtherReadLock => this block is watching by other read lock
		// or waiting not too long => wait write lock
		while (hasOtherReadLocks(block) && !waitingTooLong(timeStamp)) {
			println("block : $block wait write lock startTime: $timeStamp")
			// thread sleep wait 10s until lock is notify
			lockObject.wait(MAX_TIME)
		}

		if (hasOtherReadLocks(block))
			throw LockAbortException()

		// add lock
		locks.put(block, -1)
		println("blomeck $block startTime: $timeStamp get write lock")
	}

	/**
	 * add read lock
	 * @param block add read lock to block
	 */
	@Throws(LockAbortException::class)
	fun readLock(block: Block) = synchronized(lockObject) {
		val timeStamp = System.currentTimeMillis()
		// if don't have write lock , can add read lock to it
		while (hasWriteLock(block) && !waitingTooLong(timeStamp)) {
			println("blomeck $block startTime: $timeStamp wait read lock")

			lockObject.wait(MAX_TIME)
		}

		if (hasWriteLock(block))
			throw LockAbortException()

		locks.put(block, this[block] + 1)

		println("blomeck $block startTime: $timeStamp get read lock")
	}

	/**
	 * remove all lock on block
	 * @param block unlock-block
	 */
	fun unlock(block: Block) = synchronized(lockObject) {
		println("unlock $block")

		val value = this[block]
		if (value > 1) {
			locks.put(block, value - 1)
		} else {
			locks.remove(block)
			lockObject.notifyAll()
		}
	}

	/**
	 * if lock number < 0 => has write lock
	 */
	private fun hasWriteLock(block: Block): Boolean = getLockVal(block) < 0

	/**
	 * if lock number > 1 => has read lock
	 * > 1 ==> has not write value & at least one read lock
	 */
	private fun hasOtherReadLocks(block: Block): Boolean = getLockVal(block) > 1

	/**
	 * judge wait time
	 */
	private fun waitingTooLong(startTime: Long): Boolean = System.currentTimeMillis() - startTime > MAX_TIME

	/**
	 * get lock value
	 * block's lock number
	 * @param block
	 */
	private fun getLockVal(block: Block): Int {
		val requestValue = locks[block]
		return requestValue ?: 0
	}

	operator fun get(block: Block) = getLockVal(block)
}

