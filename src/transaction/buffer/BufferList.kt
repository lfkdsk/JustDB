package transaction.buffer

import buffer.Buffer
import buffer.PageFormatter
import core.BufferManager
import core.JustDB
import storage.Block
import java.util.*

/**
 * Created by liufengkai on 2017/7/4.
 */

class BufferList(justDB: JustDB) {
	private val buffers = HashMap<Block, Buffer>()
	private val pins = ArrayList<Block>()
	private val bufferManager = justDB.BufferManager()

	fun getBuffer(blk: Block): Buffer = buffers[blk]
			?: throw IllegalAccessException("index of buffer list out of range $blk")

	fun pin(block: Block) {
		val buffer = bufferManager.pin(block)
		buffer?.run {
			buffers.put(block, buffer)
			pins.add(block)
		}
	}

	fun pinNew(filename: String, pageFormatter: PageFormatter): Block {
		val buffer = bufferManager.pinNew(filename, pageFormatter)
		buffer?.run {
			val block = buffer.block()
			block?.run {
				buffers.put(block, buffer)
				pins.add(block)
				return block
			}
		}
		throw IllegalAccessException("pin New block but this block is null")
	}

	fun unpin(block: Block) {
		val buffer = buffers[block]
		buffer?.run {
			bufferManager.unpin(buffer)
			pins.remove(block)
			if (!pins.contains(block))
				buffers.remove(block)

		}
	}

	fun unpinAll() {
		pins.map {
			buffers[it]
		}.forEach {
			it?.run {
				bufferManager.unpin(it)
			}
		}
		buffers.clear()
		pins.clear()
	}
}