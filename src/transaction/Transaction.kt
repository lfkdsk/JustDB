package transaction

import buffer.PageFormatter
import core.BufferManager
import core.JustDB
import storage.Block
import transaction.buffer.BufferList
import transaction.concurrency.ConcurrencyManager
import transaction.recovery.RecoveryManager
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by liufengkai on 2017/5/1.
 */
class Transaction(val justDB: JustDB) {

	companion object {
		/**
		 * next Transaction Number
		 */
		var nextTransactionNumber: AtomicInteger = AtomicInteger(0)

		val EOF = -1

		/**
		 * Use CAS to dispatch transaction ID
		 */
		fun getNextTransactionNumber(): Int {
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
		this.transactionNumber = getNextTransactionNumber()
		this.recoveryManager = RecoveryManager(justDB, transactionNumber)
		this.concurrencyManager = ConcurrencyManager()
		this.bufferList = BufferList(justDB)
	}

	fun commit() {
		recoveryManager.commit()
		concurrencyManager.release()
		bufferList.unpinAll()
		println("transaction $transactionNumber committed")
	}

	fun rollback() {
		recoveryManager.rollback()
		concurrencyManager.release()
		bufferList.unpinAll()
		println("transaction $transactionNumber rolled back")
	}

	fun recover() {
		justDB.BufferManager().flushAll(transactionNumber)
		recoveryManager.recover()
	}

	fun pin(block: Block) {
		bufferList.pin(block)
	}

	fun unpin(block: Block) {
		bufferList.unpin(block)
	}

	fun getInt(block: Block, offset: Int): Int {
		concurrencyManager.readLock(block)
		val buffer = bufferList.getBuffer(block)
		return buffer.getInt(offset)
	}

	fun getString(block: Block, offset: Int): String {
		concurrencyManager.readLock(block)
		val buffer = bufferList.getBuffer(block)
		return buffer.getString(offset)
	}

	fun setInt(blk: Block, offset: Int, value: Int) {
		concurrencyManager.writeLock(blk)
		val buffer = bufferList.getBuffer(blk)
		val lsn = recoveryManager.setInt(buffer, offset, value)
		buffer.setInt(offset, value, transactionNumber, lsn)
	}

	fun setString(block: Block, offset: Int, value: String) {
		concurrencyManager.writeLock(block)
		val buffer = bufferList.getBuffer(block)
		val lsn = recoveryManager.setString(buffer, offset, value)
		buffer.setString(offset, value, transactionNumber, lsn)
	}

	fun append(filename: String, pageFormatter: PageFormatter): Block {
		val dummyBlock = Block(filename, EOF)
		concurrencyManager.writeLock(dummyBlock)
		val block = bufferList.pinNew(filename, pageFormatter)
		unpin(block)
		return block
	}
}
