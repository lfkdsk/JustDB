package logger

import core.JustDB

/**
 * Created by liufengkai on 2017/5/1.
 */
fun main(args: Array<String>) {
	val justDB = JustDB()
	val manager = LogManagerImpl(justDB)
	val list = arrayListOf("lfkdsk", "ffffff", "ss")
	manager.append(list)
	manager.flush(10)
	manager.forEach { println(it.toString() + " list ") }
}
