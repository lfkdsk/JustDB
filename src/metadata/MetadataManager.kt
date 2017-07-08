package metadata

import core.SystemService
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

	// get method

	fun getTableInfo(tableName: String,
	                 transaction: Transaction): Table

	fun getViewDef(viewName: String,
	               transaction: Transaction): String

}