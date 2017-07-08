package metadata

import core.SystemService
import metadata.data.Index
import metadata.data.Schema
import metadata.data.Table
import transaction.Transaction

/**
 * Metadata-manager
 * Created by liufengkai on 2017/5/1.
 */
interface MetadataManager : SystemService {

	// create method

	fun createTable(tableName: String,
	                schema: Schema,
	                transaction: Transaction)

	fun createView(viewName: String,
	               viewDef: String,
	               transaction: Transaction)

	fun createIndex(indexName: String,
	                tableName: String,
	                fieldName: String,
	                transaction: Transaction)

	// get info method

	fun getTableInfo(tableName: String,
	                 transaction: Transaction): Table

	fun getViewDef(viewName: String,
	               transaction: Transaction): String

	fun getIndexInfo(tableName: String,
	                 transaction: Transaction): Map<String, Index>

	fun getStatusInfo(tableName: String,
	                  table: Table,
	                  transaction: Transaction)
}