package buffer

import core.JustDB
import storage.Block

/**
 * Created by liufengkai on 2017/4/30.
 */
class BufferManagerImpl(justDB: JustDB, bufferNumber: Int) : BufferManager {
	private val MAX_TIME: Long = 10000 // 10 seconds

	private val simpleBufferManager = SimpleBufferManagerImpl(justDB, bufferNumber)

	private val lock = Object()

	override fun pin(block: Block): Buffer? {
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

	@Synchronized override
	fun pinNew(fileName: String, pageFormatter: PageFormatter): Buffer? {
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

	override fun unpin(buffer: Buffer) {
		simpleBufferManager.unpin(buffer)
		if (!buffer.isPinned())
			lock.notifyAll()
	}

	override fun flushAll(transaction: Int) {
		simpleBufferManager.flushAll(transaction)
	}

	override fun available(): Int {
		return simpleBufferManager.available()
	}

	private fun waitingTooLong(startTime: Long): Boolean {
		return System.currentTimeMillis() - startTime > MAX_TIME
	}
}

