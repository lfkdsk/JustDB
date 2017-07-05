package transaction

import core.JustDB
import groovy.util.GroovyTestCase
import storage.Block
import kotlin.concurrent.thread

/**
 * Created by liufengkai on 2017/7/4.
 */
class TransactionTest : GroovyTestCase() {

	fun testGetNextTransactionNumber() {
		repeat(100) {
			thread {
				println("get-next-id ${Transaction.generateTransactionNumber()}")
			}
		}
	}

	fun testTransaction() {
		val justDB = JustDB("transaction-test-database")
		val transaction = Transaction(justDB)

		val block = Block("transaction-test", 0)
		transaction.pin(block)
		transaction.setString(block, 0, "lfkdsk lfkdsk")
		transaction.commit()

		val transaction1 = Transaction(justDB)
		transaction1.pin(block)
		transaction1.setString(block, 0, "lfkdsk ssss")
		transaction1.rollback()
	}
}