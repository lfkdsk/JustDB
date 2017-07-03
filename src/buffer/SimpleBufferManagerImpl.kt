package buffer

import core.JustDB
import storage.Block

/**
 * Created by liufengkai on 2017/4/30.
 */
class SimpleBufferManagerImpl(justDB: JustDB, bufferNumber: Int) : BufferManager {

	private val bufferPool: Array<Buffer> = Array(bufferNumber, { Buffer(justDB) })
	private var numAvailable: Int = bufferNumber

	@Synchronized
	override fun flushAll(transaction: Int) {
		bufferPool.filter { it.isModifiedBy(transaction) }
				.forEach(Buffer::flush)
	}

	override fun available(): Int {
		return numAvailable
	}

	@Synchronized override
	fun pin(block: Block): Buffer? {
		var buffer = getExistBuffer(block)
		if (buffer == null) {
			buffer = chooseUnPinnedBuffer()
			if (buffer == null)
				return null
			buffer.assignToBlock(block)
		}
		if (!buffer.isPinned())
			numAvailable--
		buffer.pin()
		return buffer
	}

	@Synchronized override
	fun unpin(buffer: Buffer) {
		buffer.unpin()
		if (!buffer.isPinned())
			numAvailable++
	}

	@Synchronized override
	fun pinNew(fileName: String, pageFormatter: PageFormatter): Buffer? {
		val buff = chooseUnPinnedBuffer() ?: return null
		buff.assignToNew(fileName, pageFormatter)
		numAvailable--
		buff.pin()
		return buff
	}


	private fun getExistBuffer(block: Block): Buffer? {
		return bufferPool.firstOrNull { it.block() != null && it.block() == block }
	}

	private fun chooseUnPinnedBuffer(): Buffer? {
		return bufferPool.firstOrNull { !it.isPinned() }
	}
}