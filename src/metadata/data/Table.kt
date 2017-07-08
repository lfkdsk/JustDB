package metadata.data

import core.lengthOfType

class CanNotFoundOffSetOfType(type: String)
	: Exception("cannot found offset of $type")

/**
 * Table - abstract model of Table
 * Consist of Table & LogRecord
 * Created by liufengkai on 2017/5/1.
 */
class Table {
	val schema: Schema
	val offsets: Map<String, Int>
	val recordLength: Int
	val tableName: String

	/**
	 * create table calculates each fields of
	 * physical offset of each field
	 * @param schema schema of table record
	 * @param tableName tableName
	 */
	constructor(schema: Schema, tableName: String) {
		this.schema = schema
		this.tableName = tableName
		this.offsets = HashMap()

		// update-length of position
		var pos = 0
		schema.fields().map { type ->
			offsets.put(type, pos)
			pos += schema.lengthOfType(type)
		}
		this.recordLength = pos
	}

	/**
	 * create table message - copy constructor
	 */
	constructor(tableName: String, offsets: Map<String, Int>,
	            schema: Schema, recordLength: Int) {
		this.tableName = tableName
		this.offsets = offsets
		this.schema = schema
		this.recordLength = recordLength
	}

	fun fileName() = "$tableName.tb"

	fun offset(field: String) = offsets[field]
			?: throw CanNotFoundOffSetOfType(field)
}