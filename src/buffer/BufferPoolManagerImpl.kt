package buffer

import core.JustDB
import storage.Block

/**
 * SimpleBuffer-Manager
 * control block <=> buffer
 * @param justDB DataBase
 * @param bufferNumber buffer-number
 * Created by liufengkai on 2017/4/30.
 */
class BufferPoolManagerImpl(justDB: JustDB, bufferNumber: Int) : BufferManager {
	/**
	 * initial buffer-pool
	 */
	private val bufferPool: Array<Buffer> = Array(bufferNumber, { Buffer(justDB) })

	/**
	 * available buffer in pool
	 */
	private var numAvailable: Int = bufferNumber

	/**
	 * flush all in bufferPool
	 * @param transaction transaction Num
	 */
	@Synchronized
	override fun flushAll(transaction: Int) {
		bufferPool.filter { buffer -> buffer.isModifiedBy(transaction) }.forEach(Buffer::flush)
	}

	override
	fun available(): Int = numAvailable

	/**
	 * pin block => buffer manager
	 * @param block block <=> buffer
	 */
	@Synchronized override
	fun pin(block: Block): Buffer? {
		// exist buffer
		var buffer = getExistBuffer(block)
		if (buffer == null) {
			// unused buffer
			buffer = chooseUnPinnedBuffer()
			// double-check
			if (buffer == null)
				return null
			buffer.assignToBlock(block)
		}

		if (!buffer.isPinned())
			numAvailable.inv()
		buffer.pin()

		return buffer
	}

	/**
	 * unpin buffer in pool
	 * @param buffer pin buffer
	 */
	@Synchronized override
	fun unpin(buffer: Buffer) {
		buffer.unpin()
		if (!buffer.isPinned())
			numAvailable.inc()
	}

	/**
	 * pin new —— get new block =>
	 */
	@Synchronized override
	fun pinNew(fileName: String, pageFormatter: PageFormatter): Buffer? {
		// get-buffer
		val buffer = chooseUnPinnedBuffer() ?: return null
		// assign to new
		buffer.assignToNew(fileName, pageFormatter)
		numAvailable.inv()
		buffer.pin()
		return buffer
	}

	/**
	 * Exist buffer
	 * @param block bind block
	 */
	private fun getExistBuffer(block: Block): Buffer? {
		return bufferPool.firstOrNull { buffer ->
			buffer.block() != null && buffer.block() == block
		}
	}

	/**
	 * choose unpin buffer
	 */
	private fun chooseUnPinnedBuffer(): Buffer? {
		return bufferPool.firstOrNull { buffer ->
			!buffer.isPinned()
		}
	}
}