package storage

import core.JustDB
import groovy.util.GroovyTestCase

/**
 * Created by liufengkai on 2017/7/4.
 */

class ExPageTest : GroovyTestCase() {

	val justDB = JustDB()
	val exPage = ExPage(justDB)
	val testInt = 100

	override fun setUp() {
		super.setUp()
	}

	fun testAppend() {
		val p2 = ExPage(justDB)
		p2.setString(20, "hello")
		val blk = p2.append("junk")
		val p3 = ExPage(justDB)
		p3.read(blk)
		val s = p3.getString(20)
		println("re-get ===> " + s)
		assertEquals("hello", s)
	}

	fun testGetInt() {
		val block = Block("testInt", 0)
		exPage.read(block)
		val n = exPage.getInt(0)
		println("n ===> " + n)
	}

	fun testSetInt() {
		val block = Block("testInt", 0)
		exPage.read(block)
		println("write ===> " + exPage.setInt(0, testInt))
		exPage.write(block)
	}

	fun testGetString() {
		val block = Block("testStr", 0)
		exPage.read(block)
		val n = exPage.getString(0)
		println("n ===> " + n)
	}

	fun testSetString() {
		val block = Block("testStr", 0)
		exPage.read(block)
		println("write ===> " + exPage.setString(0, "lfkdsk"))
		exPage.write(block)
	}
}