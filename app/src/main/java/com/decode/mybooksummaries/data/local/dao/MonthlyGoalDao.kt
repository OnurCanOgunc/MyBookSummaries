package com.decode.mybooksummaries.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.decode.mybooksummaries.data.local.entity.MonthlyGoalEntity
import com.decode.mybooksummaries.domain.model.MonthlyGoal

@Dao
interface MonthlyGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthlyGoal(goal: MonthlyGoalEntity)

    @Query("SELECT * FROM monthly_goals WHERE month = :month LIMIT 1")
    suspend fun getMonthlyGoal(month: String): MonthlyGoalEntity?

    @Query("UPDATE monthly_goals SET isSynced = :isSynced WHERE id = :id")
    suspend fun updateMonthlyGoal(id: String, isSynced: Boolean)

    @Query("SELECT * FROM monthly_goals WHERE isSynced = 0")
    suspend fun getUnsyncedGoals(): List<MonthlyGoalEntity>

    @Query("SELECT * FROM monthly_goals WHERE month = :month AND year = :year AND userId = :userId LIMIT 1")
    suspend fun getGoalByMonth(userId: String, month: Int, year: Int): MonthlyGoal?

    @Query("DELETE FROM monthly_goals")
    suspend fun deleteAllMonthlyGoals()

}