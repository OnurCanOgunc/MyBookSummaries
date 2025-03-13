package com.decode.mybooksummaries.domain.model

import java.util.UUID

data class MonthlyGoal(
    val id: String = UUID.randomUUID().toString(),
    val month: String = "",
    val goalCount: Int = 0,
    val isSynced: Boolean = false
)