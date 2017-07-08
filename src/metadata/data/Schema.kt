package metadata.data

import core.Types

/**
 * Schema
 * Created by liufengkai on 2017/7/7.
 */
class Schema {

	/**
	 * Inner Field
	 * @param type Type of value
	 * @param length Length of value
	 * @see core.JustDBService.Types : support type in system
	 */
	data class Field(val type: Types, val length: Int)

	companion object {
		fun build(init: Builder.() -> Unit) = Builder(init).build()
	}

	/**
	 * field - map
	 */
	private val fieldMap = HashMap<String, Field>()

	class Builder(init: Builder.() -> Unit) {

		val schema: Schema

		init {
			init()
			schema = Schema()
		}

		/**
		 * add field to schema
		 * @param fieldName FieldName
		 * @param type Types
		 * @param length Value Length
		 * @see core.JustDBService.Types support Type Value
		 */
		fun addField(fieldName: String, type: Types, length: Int) =
				apply {
					schema.fieldMap.put(fieldName, Field(type, length))
				}


		fun addStringField(fieldName: String, length: Int) =
				apply {
					addField(fieldName, Types.VARCHAR, length)
				}

		fun addIntegerField(fieldName: String) =
				apply {
					addField(fieldName, Types.INTEGER, 0)
				}

		fun build(): Schema = schema
	}

	fun addAll(schema: Schema) =
			fieldMap.putAll(schema.fieldMap)


	fun addFrom(fieldName: String, schema: Schema) =
			fieldMap.put(fieldName, schema[fieldName])


	fun fields() = fieldMap.keys


	fun hasField(fieldName: String) = fieldMap.containsKey(fieldName)


	fun infoType(fieldName: String) = fieldMap[fieldName]?.type ?: throw
	RuntimeException("cannot found $fieldName.type")


	fun infoLength(fieldName: String) = fieldMap[fieldName]?.length ?: throw
	RuntimeException("cannot found $fieldName.length")


	operator fun get(fieldName: String) = fieldMap[fieldName] ?:
			throw RuntimeException("cannot found field : $fieldName")

}