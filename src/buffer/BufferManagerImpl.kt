package buffer

import core.JustDB
import storage.Block

/**
 * Buffer-Manager Impl
 * Created by liufengkai on 2017/4/30.
 */
class BufferManagerImpl(justDB: JustDB, bufferNumber: Int) : BufferManager {
	/**
	 * max-wait-time
	 */
	private val MAX_TIME: Long = 10000 // 10 seconds

	private val simpleBufferManager = BufferPoolManagerImpl(justDB, bufferNumber)

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
			var buff = simpleBufferManager.pin(block)

			while (buff == null && !waitingTooLong(timestamp)) {
				lock.wait(MAX_TIME)
				buff = simpleBufferManager.pin(block)
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
			var buff = simpleBufferManager.pinNew(fileName, pageFormatter)

			while (buff == null && !waitingTooLong(timestamp)) {
				lock.wait(MAX_TIME)
				buff = simpleBufferManager.pinNew(fileName, pageFormatter)
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
		simpleBufferManager.unpin(buffer)
		// unpin all -> lock
		if (!buffer.isPinned())
			lock.notifyAll()
	}

	override
	fun flushAll(transaction: Int) {
		simpleBufferManager.flushAll(transaction)
	}

	override
	fun available(): Int {
		return simpleBufferManager.available()
	}

	private fun waitingTooLong(startTime: Long) = System.currentTimeMillis() - startTime > MAX_TIME
}

