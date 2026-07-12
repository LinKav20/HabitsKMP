package com.github.linkav20.habits.database.habits.model.todo

import androidx.room.Entity
import com.github.linkav20.habits.database.habits.model.CompletableItem
import com.github.linkav20.habits.database.habits.model.Tables
import kotlinx.datetime.LocalDate

@Entity(
	tableName = Tables.TODOS,
	primaryKeys = ["uid"],
)
data class TodoEntity(
	override val uid: String,
	val title: String,
	val createdDate: LocalDate,
	val repeatRule: TodoRepeatRule,
    val deadline: LocalDate? = null,
    val isActive: Boolean = true,
) : CompletableItem
