package storage

/**
 * Block in File
 * @param fileName : Block's file Name
 * @param blockName: Block Name
 * Created by liufengkai on 2017/4/24.
 */
class Block(val fileName: String, val blockNumber: Int) {

	override fun toString(): String {
		return "Block(fileName='$fileName', blockName='$blockNumber')"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false
		other as Block
		if (fileName != other.fileName) return false
		if (blockNumber != other.blockNumber) return false
		return true
	}

	override fun hashCode(): Int {
		return toString().hashCode()
	}
}