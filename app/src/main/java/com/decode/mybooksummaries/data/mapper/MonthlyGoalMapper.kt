package com.decode.mybooksummaries.data.mapper

import com.decode.mybooksummaries.data.local.entity.MonthlyGoalEntity
import com.decode.mybooksummaries.domain.model.MonthlyGoal

fun MonthlyGoalEntity.toMonthlyGoal() = MonthlyGoal(
    id = id, month = month, goalCount = goalCount, isSynced = isSynced
)


fun MonthlyGoal.toEntity() = MonthlyGoalEntity(
    id = id,  month = month, goalCount = goalCount, isSynced = isSynced
)