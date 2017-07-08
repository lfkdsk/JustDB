package metadata.data

import storage.Block
import transaction.Transaction

/**
 * Created by liufengkai on 2017/5/1.
 */
class FileRecord {
	val table: Table
	val transaction: Transaction
	val fileName: String
	var pageRecord: PageRecord? = null
	var currentBlockNumber: Int = 0

	constructor(table: Table, transaction: Transaction) {
		this.table = table
		this.transaction = transaction
		this.fileName = table.fileName()

		(transaction.size(fileName) == 0)
				.run {
					appendBlock()
				}
	}

	private fun moveTo(blockNumber: Int) {
		pageRecord?.run {
			this.close()
		}
		currentBlockNumber = blockNumber
		val block: Block = Block(fileName, currentBlockNumber)
		pageRecord = PageRecord()
	}

	private fun appendBlock() {

	}
}