package com.github.linkav20.habits.database.converters

import androidx.room.TypeConverter
import com.github.linkav20.habits.database.habits.model.todo.TodoRepeatRule
import kotlinx.serialization.json.Json

class TodoRepeatRuleConverter {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        useAlternativeNames = false
    }

    @TypeConverter
    fun fromRepeatRule(rule: TodoRepeatRule): String = json.encodeToString(rule)

    @TypeConverter
    fun toRepeatRule(jsonString: String): TodoRepeatRule = json.decodeFromString(jsonString)
}
