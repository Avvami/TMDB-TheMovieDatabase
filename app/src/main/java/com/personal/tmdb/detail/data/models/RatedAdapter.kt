package com.personal.tmdb.detail.data.models

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

class RatedAdapter {

    @FromJson
    fun fromJson(reader: JsonReader): Rated {
        return when (reader.peek()) {
            JsonReader.Token.BOOLEAN -> {
                if (!reader.nextBoolean()) Rated.NotRated else
                    throw JsonDataException("Unexpected true value for 'rated'")
            }
            JsonReader.Token.BEGIN_OBJECT -> {
                var ratingValue: Int? = null
                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "value" -> ratingValue = reader.nextInt()
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()
                ratingValue?.let { Rated.Value(it) }
                    ?: throw JsonDataException("Missing 'value' in rated object")
            }
            else -> throw JsonDataException("Unexpected token: ${reader.peek()} for 'rated'")
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, rated: Rated) {
        when (rated) {
            is Rated.NotRated -> writer.value(false)
            is Rated.Value -> {
                writer.beginObject()
                writer.name("value").value(rated.value)
                writer.endObject()
            }
        }
    }
}