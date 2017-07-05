package transaction

import buffer.PageFormatter
import core.BufferManager
import core.JustDB
import storage.Block
import transaction.concurrency.ConcurrencyManager
import transaction.recovery.RecoveryManager
import utils.logger.Logger
import java.util.concurrent.atomic.AtomicInteger

/**
 * Transaction Object
 * @param justDB database
 * Created by liufengkai on 2017/5/1.
 */
class Transaction(val justDB: JustDB) {

	companion object {
		/**
		 * next Transaction Number
		 */
		var nextTransactionNumber: AtomicInteger = AtomicInteger(0)

		/**
		 * End of File
		 */
		val EOF = -1

		/**
		 * Use CAS to dispatch transaction ID
		 * @return generate next transaction number
		 */
		fun generateTransactionNumber(): Int {
			while (true) {
				val value = nextTransactionNumber.get()
				// ++
				val newValue = value.inc()
				if (nextTransactionNumber.compareAndSet(value, newValue)) {
					return newValue
				}
			}
		}
	}

	val transactionNumber: Int
	private val recoveryManager: RecoveryManager
	private val concurrencyManager: ConcurrencyManager
	private val bufferList: BufferList

	init {
		// init transaction number - just use default generate method
		this.transactionNumber = generateTransactionNumber()
		// bind recovery
		this.recoveryManager = RecoveryManager(justDB, transactionNumber)
		// bind concurrency
		this.concurrencyManager = ConcurrencyManager()
		// bind buffer-list
		this.bufferList = BufferList(justDB)
	}

	/**
	 * tx-commit
	 */
	fun commit() {
		// commit-log | release log buffer
		recoveryManager.commit()
		// unlock all blocks
		concurrencyManager.release()
		// unpin all buffers
		bufferList.unpinAll()
		Logger.d("transaction $transactionNumber committed")
	}

	/**
	 * rx-rollback
	 */
	fun rollback() {
		recoveryManager.rollback()
		concurrencyManager.release()
		bufferList.unpinAll()
		Logger.d("transaction $transactionNumber rolled back")
	}

	/**
	 * tx-recover
	 */
	fun recover() {
		val bufferManager = justDB.BufferManager()
		bufferManager.flushAll(transactionNumber)
		recoveryManager.recover()
	}

	/**
	 * tx-pin-block to buffer
	 * @param block tx-block
	 */
	fun pin(block: Block) {
		bufferList.pin(block)
	}

	/**
	 * tx-unpin-block to buffer
	 * @param block tx-block
	 */
	fun unpin(block: Block) {
		bufferList.unpin(block)
	}

	/**
	 * get int
	 * @param block tx-block
	 * @param offset offset to pointer
	 */
	fun getInt(block: Block, offset: Int): Int {
		// add read-lock
		concurrencyManager.readLock(block)
		// get buffer
		val buffer = bufferList.getBuffer(block)
		// read int
		return buffer.getInt(offset)
	}

	/**
	 * get string
	 * @param block tx-block
	 * @param offset offset to pointer
	 */
	fun getString(block: Block, offset: Int): String {
		// add read-lock
		concurrencyManager.readLock(block)
		// get buffer
		val buffer = bufferList.getBuffer(block)
		// read string
		return buffer.getString(offset)
	}

	/**
	 * set int
	 * @param block block-msg
	 * @param offset offset to pointer
	 * @param newValue set-newValue
	 */
	fun setInt(block: Block, offset: Int, newValue: Int) {
		// add write-lock
		concurrencyManager.writeLock(block)
		// get buffer
		val buffer = bufferList.getBuffer(block)
		// set to recovery-log-file
		val lsn = recoveryManager.setInt(buffer, offset, newValue)
		// write to database-file
		buffer.setInt(offset, newValue, transactionNumber, lsn)
	}

	/**
	 * set string
	 * @param block block-msg
	 * @param offset offset to pointer
	 * @param newValue set-newValue
	 */
	fun setString(block: Block, offset: Int, newValue: String) {
		// add write-lock
		concurrencyManager.writeLock(block)
		// get buffer
		val buffer = bufferList.getBuffer(block)
		// set to recovery-log-file
		val lsn = recoveryManager.setString(buffer, offset, newValue)
		// write to database-file
		buffer.setString(offset, newValue, transactionNumber, lsn)
	}

	/**
	 * appends a new block to the end of the spec file
	 * and returns a reference to it.
	 * @param filename file-name
	 * @param pageFormatter formatter
	 * @return block reference of new block
	 */
	fun append(filename: String, pageFormatter: PageFormatter): Block {
		// create block-to-get lock
		val dummyBlock = Block(filename, EOF)
		// add write-lock
		concurrencyManager.writeLock(dummyBlock)
		// pin new block
		val block = bufferList.pinNew(filename, pageFormatter)
		// un-pin-block
		unpin(block)

		return block
	}
}
