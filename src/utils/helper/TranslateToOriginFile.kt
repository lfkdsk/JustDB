package utils.helper

import java.io.File
import java.io.RandomAccessFile

/**
 * Created by liufengkai on 2017/7/4.
 */

class TranslateToOriginFile(val homeDir: String) {
	fun translate(fileName: String) {
		val access: RandomAccessFile = RandomAccessFile(File(homeDir, fileName), "r")

		var lastPosition = access.readInt()
		while (lastPosition > 0) {
			access.seek(lastPosition.toLong())
			val length = access.readInt()
			val byteBuffer = ByteArray(length)
			val log = access.read(byteBuffer).toString()
			println("lastPosition : $lastPosition == log: $log")
			lastPosition -= length
		}

	}
}


fun main(args: Array<String>) {
	val translation = TranslateToOriginFile("./tests/databasetemp/just-db")
	translation.translate("just-db-log.log")
}