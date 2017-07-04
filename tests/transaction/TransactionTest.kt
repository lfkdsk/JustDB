package transaction

import core.JustDB
import groovy.util.GroovyTestCase
import storage.Block

/**
 * Created by liufengkai on 2017/7/4.
 */
class TransactionTest : GroovyTestCase() {

	fun testGetNextTranscationNumber() {
//		repeat(100) {
//			thread {
//				println("get-next-id ${Transaction.getNextTransactionNumber()}")
//			}
//		}
	}

	fun testTransaction() {
		val justDB = JustDB()
		val transaction = Transaction(justDB)
		val block = Block("transaction-test", 0)
		transaction.pin(block)
		transaction.setString(block, 0, "lfkdsk lfkdsk")
		transaction.commit()
		transaction.pin(block)

		transaction.setString(block, 0, "lfkdsk ssss")
		transaction.commit()
		transaction.rollback()
	}
}