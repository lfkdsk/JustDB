package buffer

import core.JustDB
import storage.Block

/**
 * Buffer Abort
 * Created by liufengkai on 2017/4/30.
 */
class BufferAbortException : RuntimeException()

/**
 * Buffer-Manager Impl
 * Created by liufengkai on 2017/4/30.
 */
class BufferManagerImpl(justDB: JustDB, bufferNumber: Int) : BufferManager {
	/**
	 * max-wait-time
	 */
	private val MAX_TIME: Long = 10000 // 10 seconds

	private val bufferPoolManager = BufferPoolManagerImpl(justDB, bufferNumber)

	/**
	 * sync - lock - object
	 */
	private val lock = java.lang.Object()

	/**
	 * Buffer pin block
	 * @param block => block
	 */
	@Throws(BufferAbortException::class)
	override
	fun pin(block: Block): Buffer = synchronized(lock) {
		try {
			val timestamp = System.currentTimeMillis()
			var buff = bufferPoolManager.pin(block)

			while (buff == null && !waitingTooLong(timestamp)) {
				lock.wait(MAX_TIME)
				buff = bufferPoolManager.pin(block)
			}

			if (buff == null)
				throw BufferAbortException()
			return buff
		} catch (e: InterruptedException) {
			throw BufferAbortException()
		}
	}

	/**
	 * Pin new fileName Block
	 * @param fileName
	 * @param pageFormatter
	 */
	@Throws(BufferAbortException::class)
	override
	fun pinNew(fileName: String, pageFormatter: PageFormatter): Buffer = synchronized(lock) {
		try {
			val timestamp = System.currentTimeMillis()
			var buff = bufferPoolManager.pinNew(fileName, pageFormatter)

			while (buff == null && !waitingTooLong(timestamp)) {
				lock.wait(MAX_TIME)
				// get new block
				buff = bufferPoolManager.pinNew(fileName, pageFormatter)
			}

			if (buff == null)
				throw BufferAbortException()

			return buff
		} catch (e: InterruptedException) {
			throw BufferAbortException()
		}
	}

	/**
	 * unpin buffer from block
	 * @param buffer this buffer
	 */
	override
	fun unpin(buffer: Buffer) = synchronized(lock) {
		bufferPoolManager.unpin(buffer)
		// unpin all -> lock
		if (!buffer.isPinned())
			lock.notifyAll()
	}

	override
	fun flushAll(transaction: Int) {
		bufferPoolManager.flushAll(transaction)
	}

	override
	fun available(): Int {
		return bufferPoolManager.available()
	}

	private fun waitingTooLong(startTime: Long) = System.currentTimeMillis() - startTime > MAX_TIME
}

