package transaction

import groovy.util.GroovyTestCase
import kotlin.concurrent.thread

/**
 * Created by liufengkai on 2017/7/4.
 */
class TransactionTest : GroovyTestCase() {

	fun testGetNextTranscationNumber() {
		repeat(100) {
			thread {
				println("get-next-id ${Transaction.getNextTransactionNumber()}")
			}
		}
	}
}