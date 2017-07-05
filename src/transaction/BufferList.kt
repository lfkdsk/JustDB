package transaction

import buffer.Buffer
import buffer.PageFormatter
import core.BufferManager
import core.JustDB
import storage.Block
import java.util.*

/**
 * Buffer List - Bind to a Transaction
 * @see transaction.Transaction transaction => buffers
 * @param justDB DataBase
 * Created by liufengkai on 2017/7/4.
 */
class BufferList(justDB: JustDB) {
	/**
	 * Block <=> Buffer
	 * Transaction's Block & Buffer
	 */
	private val buffers = HashMap<Block, Buffer>()

	/**
	 * pins => block
	 * Transaction's Pin Block
	 */
	private val pins = ArrayList<Block>()

	/**
	 * use buffer-manager to control buffer
	 */
	private val bufferManager = justDB.BufferManager()

	/**
	 * get block-buffer
	 * @param block control block's buffer
	 */
	fun getBuffer(block: Block): Buffer = buffers[block]
			?: throw IllegalAccessException("index of buffer list out of range $block")

	/**
	 * pin block
	 * @param block pin block
	 */
	fun pin(block: Block) {
		// request buffer from buffer-pool
		val buffer = bufferManager.pin(block)
		buffer?.run {
			buffers.put(block, buffer)
			pins.add(block)
		}
	}

	/**
	 * pin-new-block to EOF
	 * @param filename pin in spec file
	 * @param pageFormatter formatter
	 */
	fun pinNew(filename: String, pageFormatter: PageFormatter): Block {
		// pin buffer
		val buffer = bufferManager.pinNew(filename, pageFormatter)
		// buffer => block => pin it! and return it!
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

	/**
	 * unpin block-buffer
	 * @param block pin block
	 */
	fun unpin(block: Block) {
		val buffer = buffers[block]
		buffer?.run {
			bufferManager.unpin(buffer)
			pins.remove(block)
			if (!pins.contains(block))
				buffers.remove(block)
		}
	}

	/**
	 * unpin all block
	 */
	fun unpinAll() {
		pins.map { buffer ->
			buffers[buffer]
		}.forEach { buffer ->
			buffer?.run {
				bufferManager.unpin(buffer)
			}
		}.apply {
			buffers.clear()
			pins.clear()
		}
	}
}