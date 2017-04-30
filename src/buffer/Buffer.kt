package buffer

import core.JustDB
import logger.LogManager
import storage.Block
import storage.ExPage

/**
 * Created by liufengkai on 2017/4/30.
 */
class Buffer {
	private val contents: ExPage = ExPage()
	private var block: Block? = null
	private var pins = 0
	private var modifiedBy = -1
	/**
	 * write log seq number
	 * modify by write event
	 */
	private var logSequenceNumber = -1

	companion object {
		val logManager: LogManager = JustDB[JustDB.LOGGER_MANAGER] as LogManager
	}

	fun getInt(offset: Int): Int {
		return contents.getInt(offset)
	}

	fun getString(offset: Int): String {
		return contents.getString(offset)
	}

	fun setInt(offset: Int, value: Int, txnum: Int, lsn: Int) {
		modifiedBy = txnum
		if (lsn >= 0)
			logSequenceNumber = lsn
		contents.setInt(offset, value)
	}

	fun setString(offset: Int, value: String, txnum: Int, lsn: Int) {
		modifiedBy = txnum
		if (lsn >= 0)
			logSequenceNumber = lsn
		contents.setString(offset, value)
	}

	fun block(): Block? {
		return block
	}

	fun flush() {
		if (modifiedBy >= 0) {
			logManager.flush(logSequenceNumber)
			block?.let { contents.write(it) }
			modifiedBy = -1
		}
	}

	/**
	 * Increases the buffer's pin count.
	 */
	internal fun pin() {
		pins++
	}

	/**
	 * Decreases the buffer's pin count.
	 */
	internal fun unpin() {
		pins--
	}

	internal fun isPinned(): Boolean {
		return pins > 0
	}

	/**
	 * File => Block => Buffer
	 * read specific file block to Buffer
	 */
	internal fun assignToBlock(b: Block) {
		// save msg
		flush()
		block = b
		contents.read(b)
		pins = 0
	}

	/**
	 * @param transaction tranNumber
	 * @return true if the transaction modified the buffer
	 */
	internal fun isModifiedBy(transaction: Int): Boolean {
		return transaction == modifiedBy
	}

	/**
	 * 按照指定的页格式格式化contents对象，并将该Page对象附加到指定的filename文件下
	 */
	internal fun assignToNew(filename: String, pageFormatter: PageFormatter) {
		flush()
		pageFormatter.format(contents)
		block = contents.append(filename)
		pins = 0
	}
}