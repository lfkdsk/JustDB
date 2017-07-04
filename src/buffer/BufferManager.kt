package buffer

import core.SystemService
import storage.Block

/**
 * Buffer-Manager
 * Created by liufengkai on 2017/4/30.
 */
interface BufferManager : SystemService {

	fun pin(block: Block): Buffer?

	fun pinNew(fileName: String, pageFormatter: PageFormatter): Buffer?

	fun unpin(buffer: Buffer)

	fun flushAll(transaction: Int)
	
	fun available(): Int
}