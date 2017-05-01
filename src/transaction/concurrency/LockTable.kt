package transaction.concurrency

import storage.Block

/**
 * S -- sync(read lock)
 * X -- mutually(write lock)
 * Created by liufengkai on 2017/5/1.
 */

class LockAbortException : RuntimeException()

object LockTable {
	private val MAX_TIME: Long = 10000 // 10 seconds

	private val locks: MutableMap<Block, Int> = HashMap()
	// common queue
	private var lockObject = java.lang.Object()

	fun writeLock(block: Block) = synchronized(lockObject) {
		try {
			val timeStamp: Long = System.currentTimeMillis()
			while (hasOtherSLocks(block) && !waitingTooLong(timeStamp)) {
				println("blomeck $block startTime: $timeStamp wait write lock")
				// thread sleep
				lockObject.wait(MAX_TIME)
			}

			if (hasOtherSLocks(block))
				throw LockAbortException()

			// add lock
			locks.put(block, -1)
			println("blomeck $block startTime: $timeStamp get write lock")
		} catch(e: InterruptedException) {
			throw LockAbortException()
		}
	}

	fun readLock(block: Block) = synchronized(lockObject) {
		try {
			val timeStamp = System.currentTimeMillis()
			// if don't have x lock , can add read lock to it
			while (hasXLock(block) && !waitingTooLong(timeStamp)) {
				println("blomeck $block startTime: $timeStamp wait read lock")
				lockObject.wait(MAX_TIME)
			}

			if (hasXLock(block))
				throw LockAbortException()

			locks.put(block, this[block] + 1)

			println("blomeck $block startTime: $timeStamp get read lock")
		} catch (e: InterruptedException) {
			throw LockAbortException()
		}
	}

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

	private fun hasXLock(blk: Block): Boolean {
		return getLockVal(blk) < 0
	}

	private fun hasOtherSLocks(block: Block): Boolean {
		return getLockVal(block) > 1
	}

	private fun waitingTooLong(startTime: Long): Boolean {
		return System.currentTimeMillis() - startTime > MAX_TIME
	}

	private fun getLockVal(block: Block): Int {
		val requestValue = locks[block]
		return requestValue ?: 0
	}

	operator fun get(block: Block) = getLockVal(block)
}

