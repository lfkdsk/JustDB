package storage

import core.SystemService
import java.nio.ByteBuffer

/**
 * Created by liufengkai on 2017/4/30.
 */
interface FileManager : SystemService {
	override fun read(block: Block, byteBuffer: ByteBuffer)

	override fun write(block: Block, byteBuffer: ByteBuffer)

	override fun append(filename: String, byteBuffer: ByteBuffer): Block
}