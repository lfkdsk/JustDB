package storage

import core.FileManager
import core.JustDB
import java.nio.ByteBuffer
import java.nio.charset.Charset

/**
 * Simple Save Collection
 * default size is 400 * 10 bytes => 4M
 * =======       ======
 * =Block= <===> =Page=
 * =======       ======
 * Str save to a page. Page save to a block.
 * Created by liufengkai on 2017/4/30.
 */
class ExPage(justDB: JustDB) {
	companion object DEFAULT {
		const val BLOCK_SIZE = 400
		const val INT_SIZE = 4
		/**
		 * @param n size of string
		 * @return the maximum number of bytes required to store a string of size n
		 */
		fun strSize(n: Int): Int {
			return INT_SIZE + n * Charset
					.defaultCharset()
					.newEncoder()
					.maxBytesPerChar()
					.toInt()
		}
	}

	private val contents = ByteBuffer.allocateDirect(BLOCK_SIZE)

	private val fileManager: FileManager = justDB.FileManager()

	/**
	 * Populates the page with the contents of the specified disk block.
	 * @param block a reference to a disk block
	 */
	@Synchronized
	fun read(block: Block) {
		fileManager.read(block, contents)
	}

	/**
	 * Writes the contents of the page to the specified disk block.
	 * @param block a reference to a disk block
	 */
	@Synchronized
	fun write(block: Block) {
		fileManager.write(block, contents)
	}

	/**
	 * Appends the contents of the page to the specified file.
	 * @param filename the name of the file
	 * *
	 * @return the reference to the newly-created disk block
	 */
	@Synchronized
	fun append(filename: String)
			= fileManager.append(filename, contents)

	/**
	 * Returns the integer value at a specified offset of the page.
	 * If an integer was not stored at that location,
	 * the behavior of the method is unpredictable.
	 * @param offset the byte offset within the page
	 * *
	 * @return the integer value at that offset
	 */
	@Synchronized
	fun getInt(offset: Int): Int {
		contents.position(offset)
		return contents.int
	}

	/**
	 * Writes an integer to the specified offset on the page.
	 * @param offset the byte offset within the page
	 * *
	 * @param value the integer to be written to the page
	 */
	@Synchronized
	fun setInt(offset: Int, value: Int): Int {
		contents.position(offset)
		contents.putInt(value)

		return value
	}

	/**
	 * Returns the string value at the specified offset of the page.
	 * If a string was not stored at that location,
	 * the behavior of the method is unpredictable.
	 * @param offset the byte offset within the page
	 * *
	 * @return the string value at that offset
	 */
	@Synchronized
	fun getString(offset: Int): String {
		contents.position(offset)
		val len = contents.int
		val byteVal = ByteArray(len)
		contents[byteVal] // FIXME I don't understand why you didn't use the return value -- ice1000

		return String(byteVal)
	}

	/**
	 * Writes a string to the specified offset on the page.
	 * @param offset the byte offset within the page
	 * *
	 * @param value the string to be written to the page
	 */
	@Synchronized
	fun setString(offset: Int, value: String): String {
		contents.position(offset)
		value.toByteArray().run {
			contents.putInt(size)
			contents.put(this)
		}

		return value
	}
}
