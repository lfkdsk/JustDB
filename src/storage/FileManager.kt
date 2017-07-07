package storage

import core.SystemService
import java.nio.ByteBuffer

/**
 * FileManager - control block <=> buffer
 * Created by liufengkai on 2017/4/30.
 */
interface FileManager : SystemService {

	fun blockNumber(filename: String): Int

	fun read(block: Block, byteBuffer: ByteBuffer)

	fun write(block: Block, byteBuffer: ByteBuffer)

	fun append(filename: String, byteBuffer: ByteBuffer): Block
}

