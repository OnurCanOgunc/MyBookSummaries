package com.decode.mybooksummaries.domain.usecase.goal

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import javax.inject.Inject

class SaveMonthlyGoalUseCase @Inject constructor(
    private val repository: MonthlyGoalRepository
) {
    suspend operator fun invoke(goal: Int, isConnected: Boolean): Response<Unit> {
        return repository.saveMonthlyGoal(goal, isConnected)
    }
}