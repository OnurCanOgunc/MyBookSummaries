package com.decode.mybooksummaries.domain.usecase.goal

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.model.MonthlyGoal
import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMonthlyGoalUseCase @Inject constructor(
    private val repository: MonthlyGoalRepository
) {
    operator fun invoke(isConnected: Boolean): Flow<Response<MonthlyGoal>> {
        return repository.getMonthlyGoal(isConnected)
    }
}