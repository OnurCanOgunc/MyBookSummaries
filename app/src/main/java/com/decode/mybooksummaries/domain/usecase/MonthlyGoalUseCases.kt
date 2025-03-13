package com.decode.mybooksummaries.domain.usecase

import com.decode.mybooksummaries.domain.usecase.goal.GetMonthlyGoalUseCase
import com.decode.mybooksummaries.domain.usecase.goal.SaveMonthlyGoalUseCase

data class MonthlyGoalUseCases(
    val saveMonthlyGoal: SaveMonthlyGoalUseCase,
    val getMonthlyGoal: GetMonthlyGoalUseCase
)
