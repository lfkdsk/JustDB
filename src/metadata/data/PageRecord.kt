package metadata.data

import storage.Block
import storage.ExPage
import transaction.Transaction

/**
 * Created by liufengkai on 2017/7/9.
 */

fun Table.fieldOffset(fieldName: String) = ExPage.INT_SIZE + this.offset(fieldName)

class PageRecord(var block: Block?,
                 val table: Table,
                 val transaction: Transaction) {

	val slotSize: Int = table.recordLength + ExPage.INT_SIZE
	var currentSlot: Int = -1

	init {
		block?.run {
			transaction.pin(this)
		}
	}


	fun close() = block?.run {
		transaction.unpin(this)
		block = null
	}

	fun getInt(fieldName: String) {

	}

	fun moveTo(id: Int) {
		currentSlot = id
	}

	fun fieldPosition(fieldName: String) = currentSlot() + table.offset(fieldName)

	fun currentSlot() = currentSlot * slotSize

	fun currentID() = currentSlot

	fun isValidSlot() = currentSlot() + slotSize <= ExPage.BLOCK_SIZE

	fun searchForFlag(flag: Int): Boolean {
		currentSlot.inc()

		while (isValidSlot()) {
			val position = currentSlot

			block?.run {
				if (transaction.getInt(this, position) == flag)
					return true
			}

			currentSlot.inc()
		}

		return false
	}
}