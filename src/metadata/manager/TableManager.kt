package metadata.manager

import metadata.data.Schema
import metadata.data.Table
import transaction.Transaction

/**
 * Created by liufengkai on 2017/7/9.
 */
class TableManager {

	companion object {
		// max-table-name-length
		const val MAX_LENGTH: Int = 200
	}

	private val tableCatInfo: Table
	private val fieldCatInfo: Table

	constructor(isNew: Boolean, transaction: Transaction) {

		tableCatInfo = Table(
				schema = Schema.build {
					addStringField("table-name", MAX_LENGTH)
							.addIntegerField("record-length")
				},
				tableName = "table-cat")

		fieldCatInfo = Table(
				schema = Schema.build {
					addStringField("table-name", MAX_LENGTH)
							.addIntegerField("field-name")
							.addIntegerField("type")
							.addIntegerField("length")
							.addIntegerField("offset")
				},
				tableName = "field-cat"
		)

		isNew.run {
			// create table-cat table
			createTable(tableCatInfo.tableName,
					tableCatInfo.schema,
					transaction)
			// create fiedl-cat table
			createTable(fieldCatInfo.tableName,
					fieldCatInfo.schema,
					transaction)
		}
	}

	/**
	 * create - table
	 */
	private fun createTable(tableName: String, schema: Schema, transaction: Transaction) {

	}
}