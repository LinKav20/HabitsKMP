package com.github.linkav20.habits.database.habits.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.linkav20.habits.database.habits.model.ItemCompletion
import com.github.linkav20.habits.database.habits.model.Tables

@Dao
interface CompletionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setItemCompletion(itemCompletion: ItemCompletion)

    @Query("DELETE FROM ${Tables.ITEM_COMPLETION} WHERE itemId=:id AND date=:date")
    suspend fun deleteCompletionByIdIdAndDate(id: String, date: String)

    @Query(
        """
        SELECT * FROM ${Tables.ITEM_COMPLETION}
        WHERE itemId=:itemId 
        AND date BETWEEN :startDate AND :endDate
        ORDER BY date ASC
    """
    )
    suspend fun getCompletionsForHabit(
        itemId: String,
        startDate: String,
        endDate: String,
    ): List<ItemCompletion>

}
