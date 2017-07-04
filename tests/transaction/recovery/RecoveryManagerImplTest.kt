package transaction.recovery

import core.JustDB
import groovy.util.GroovyTestCase
import transaction.Transaction

/**
 * Created by liufengkai on 2017/7/4.
 */
class RecoveryManagerImplTest : GroovyTestCase() {

	lateinit var recoveryManager: RecoveryManager

	override fun setUp() {
		super.setUp()
	}

	fun testStartLogRecord() {
		val justDB = JustDB()
		val testTrac = Transaction(justDB)
		recoveryManager = RecoveryManager(JustDB(), testTrac.transactionNumber)
		recoveryManager.commit()
	}

	fun testCommit() {

	}

	fun testRollback() {

	}

	fun testRecover() {

	}

	fun testSetInt() {

	}

	fun testSetString() {

	}

	fun testIterator() {

	}

	fun testGetJustDB() {

	}

	fun testGetTransaction() {

	}
}
