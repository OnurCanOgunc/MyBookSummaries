package com.decode.mybooksummaries.domain.repository

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.model.MonthlyGoal
import kotlinx.coroutines.flow.Flow

interface MonthlyGoalRepository {
    suspend fun saveMonthlyGoal(goal: Int, isConnected: Boolean): Response<Unit>
    fun getMonthlyGoal(isConnected: Boolean): Flow<Response<MonthlyGoal>>
    fun getFormattedDate(): String
}