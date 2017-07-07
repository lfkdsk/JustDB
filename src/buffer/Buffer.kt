package buffer

import core.JustDB
import core.LogManager
import storage.Block
import storage.ExPage

/**
 * Buffer - control ExPage
 * ======
 * Buffer ===>>> Block
 * ======
 * @param justDB DataBase
 * Created by liufengkai on 2017/4/30.
 */
class Buffer(justDB: JustDB) {
	/**
	 * bind ExPage => contents
	 */
	private val contents: ExPage = ExPage(justDB)
	/**
	 * read block
	 */
	private var block: Block? = null
	private var pins = 0
	/**
	 * modify transaction number
	 */
	private var modifiedBy: Int = -1
	/**
	 * write log seq number
	 * modify by write event
	 */
	private var logSequenceNumber = -1

	/**
	 * get log-manager
	 */
	private val logManager = justDB.LogManager()

	/**
	 * read int value
	 */
	fun getInt(offset: Int): Int {
		return contents.getInt(offset)
	}

	/**
	 * read String value
	 */
	fun getString(offset: Int): String {
		return contents.getString(offset)
	}

	/**
	 * write int value => contents
	 * transaction has write to record
	 * @param offset
	 * @param value new Value
	 * @param transactionNum
	 * @param lsn lsn == -1 =>> set int | logSequenceNumber = lsn
	 */
	fun setInt(offset: Int, value: Int, transactionNum: Int, lsn: Int) {
		modifiedBy = transactionNum
		if (lsn >= 0)
			logSequenceNumber = lsn
		contents.setInt(offset, value)
	}

	/**
	 * write string value => contents
	 * @param offset
	 * @param value new Value
	 * @param transactionNum
	 * @param lsn lsn == -1 =>> set int | logSequenceNumber = lsn
	 */
	fun setString(offset: Int, value: String, transactionNum: Int, lsn: Int) {
		modifiedBy = transactionNum
		if (lsn >= 0)
			logSequenceNumber = lsn
		contents.setString(offset, value)
	}

	fun block(): Block? {
		return block
	}

	fun flush() {
		// has modified
		if (modifiedBy >= 0) {
			logManager.flush(logSequenceNumber)
			block?.let { contents.write(it) }
			modifiedBy = -1
		}
	}

	/**
	 * Increases the buffer's pin count.
	 */
	internal fun pin() = pins.inc()

	/**
	 * Decreases the buffer's pin count.
	 */
	internal fun unpin() = pins.inv()

	internal fun isPinned(): Boolean = pins > 0

	/**
	 * File => Block => Buffer
	 * read specific file block to Buffer
	 */
	internal fun assignToBlock(assignBlock: Block) {
		// save msg
		flush()
		block = assignBlock
		contents.read(assignBlock)
		pins = 0
	}

	/**
	 * @param transaction tranNumber
	 * @return true if the transactionID modified the buffer
	 */
	internal fun isModifiedBy(transaction: Int): Boolean = transaction == modifiedBy

	/**
	 * format contents object => save to local file
	 * @param filename spec file-name
	 * @param pageFormatter page-formatter
	 */
	internal fun assignToNew(filename: String, pageFormatter: PageFormatter) {
		// flush => save
		flush()
		// format message
		pageFormatter.format(contents)
		// return new block
		block = contents.append(filename)
		pins = 0
	}
}
