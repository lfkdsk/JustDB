package logger

/**
 * Created by liufengkai on 2017/5/1.
 */
fun main(args: Array<String>) {
	val manager = LogManagerImpl()
	val list = arrayListOf("lfkdsk", "ffffff", "ss")
	manager.append(list.toArray())
	manager.flush(10)
	manager.forEach { println(it.toString() + " list ") }
}
