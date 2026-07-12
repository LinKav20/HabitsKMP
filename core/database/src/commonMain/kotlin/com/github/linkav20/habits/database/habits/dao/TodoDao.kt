package com.github.linkav20.habits.database.habits.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.github.linkav20.habits.database.habits.model.habit.HabitEntity
import com.github.linkav20.habits.database.habits.model.ItemCompletion
import com.github.linkav20.habits.database.habits.model.Tables
import com.github.linkav20.habits.database.habits.model.todo.TodoEntity
import com.github.linkav20.habits.database.habits.model.todo.TodoRepeatRule
import kotlinx.datetime.LocalDate

@Dao
interface TodoDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Query(
        """
        SELECT * FROM ${Tables.TODOS}
    """,
    ) // todo
    suspend fun getActiveTodosOnDate(): List<TodoEntity>

    @Query("DELETE FROM ${Tables.TODOS}")
    suspend fun clearAll()
}
