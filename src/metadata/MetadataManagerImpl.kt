package metadata

import metadata.data.Index
import metadata.data.Schema
import metadata.data.Table
import transaction.Transaction

/**
 * Created by liufengkai on 2017/5/1.
 */
class MetadataManagerImpl : MetadataManager {
	override
	fun createTable(tableName: String, schema: Schema, transaction: Transaction) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override
	fun createView(viewName: String, viewDef: String, transaction: Transaction) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override
	fun createIndex(indexName: String, tableName: String, fieldName: String, transaction: Transaction) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override
	fun getTableInfo(tableName: String, transaction: Transaction): Table {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override
	fun getViewDef(viewName: String, transaction: Transaction): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override
	fun getIndexInfo(tableName: String, transaction: Transaction): Map<String, Index> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override
	fun getStatusInfo(tableName: String, table: Table, transaction: Transaction) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}