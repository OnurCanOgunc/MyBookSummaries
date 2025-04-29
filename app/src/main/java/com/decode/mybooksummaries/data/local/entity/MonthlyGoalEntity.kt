package com.decode.mybooksummaries.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "monthly_goals")
data class MonthlyGoalEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val month: String,
    val goalCount: Int,
    val isSynced: Boolean = false,
    val year: Int,
    val createdAt: Long
)