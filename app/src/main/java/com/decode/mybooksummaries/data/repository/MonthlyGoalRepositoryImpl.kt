package com.decode.mybooksummaries.data.repository

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.data.local.dao.MonthlyGoalDao
import com.decode.mybooksummaries.data.mapper.toEntity
import com.decode.mybooksummaries.data.mapper.toMonthlyGoal
import com.decode.mybooksummaries.domain.model.MonthlyGoal
import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

class MonthlyGoalRepositoryImpl @Inject constructor(
    @Named("monthlyGoalRef")private val monthlyGoalsRef: CollectionReference,
    private val monthlyGoalDao: MonthlyGoalDao
) : MonthlyGoalRepository {

    override suspend fun saveMonthlyGoal(
        goal: Int,
        isConnected: Boolean
    ): Response<Unit> {
        return try {
            val month = getFormattedDate()
            val monthlyGoal = MonthlyGoal(
                month = month,
                goalCount = goal,
                isSynced = isConnected
            )

            if (isConnected) {
                monthlyGoalsRef.document(month).set(monthlyGoal).await()
                monthlyGoalDao.insertMonthlyGoal(monthlyGoal.toEntity().copy(isSynced = true))
            } else {
                monthlyGoalDao.insertMonthlyGoal(monthlyGoal.toEntity().copy(isSynced = false))
            }
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "Aylık hedef kaydedilirken hata oluştu")
        }
    }

    override fun getMonthlyGoal(
        isConnected: Boolean
    ): Flow<Response<MonthlyGoal>> = callbackFlow {
        val month = getFormattedDate()
        val cachedGoal = monthlyGoalDao.getMonthlyGoal(month)
        if (cachedGoal != null) {
            trySend(Response.Success(cachedGoal.toMonthlyGoal())).isSuccess
        }

        if (isConnected) {
            val listener = monthlyGoalsRef.document(month)
                .addSnapshotListener { snapshot, error ->
                    error?.let {
                        trySend(Response.Failure(it.message ?: "Firestore error")).isSuccess
                        return@addSnapshotListener
                    }

                    val goal = snapshot?.toObject(MonthlyGoal::class.java)
                    goal?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            monthlyGoalDao.insertMonthlyGoal(it.toEntity().copy(isSynced = true))
                        }
                        trySend(Response.Success(it))
                    } ?: trySend(Response.Empty)
                }

            awaitClose { listener.remove() }
        } else {
            if (cachedGoal == null) {
                trySend(Response.Empty).isSuccess
            }
        }
        awaitClose { }
    }

    override fun getFormattedDate(): String =
        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
}