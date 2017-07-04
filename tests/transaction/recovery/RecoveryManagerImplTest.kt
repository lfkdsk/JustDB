package transaction.recovery

import core.JustDB
import groovy.util.GroovyTestCase
import transaction.Transaction

/**
 * RecoveryManagerImplTest
 * Created by liufengkai on 2017/7/4.
 */
class RecoveryManagerImplTest : GroovyTestCase() {

	lateinit var recoveryManager: RecoveryManager

	val TAG = "recovery-manager-impl-test"
	val justDB = JustDB(TAG)
	val testTrac = Transaction(justDB)

	override fun setUp() {
		super.setUp()
	}

	fun testStartLogRecord() {
		recoveryManager = RecoveryManager(JustDB(), testTrac.transactionNumber)
	}

	fun testCommit() {
		recoveryManager = RecoveryManager(JustDB(), testTrac.transactionNumber)
		recoveryManager.commit()
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
