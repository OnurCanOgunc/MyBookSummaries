package com.decode.mybooksummaries.data.repository

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.data.mapper.toEntity
import com.decode.mybooksummaries.data.mapper.toMonthlyGoal
import com.decode.mybooksummaries.di.IoDispatcher
import com.decode.mybooksummaries.domain.model.MonthlyGoal
import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MonthlyGoalRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val db: BookDatabase,
    @IoDispatcher private val ioScope: CoroutineScope
) : MonthlyGoalRepository {

    private val monthlyGoalsRef: CollectionReference
        get() = firestore.collection("users").document(auth.currentUser?.uid ?: "")
            .collection("monthlyGoals")

    override suspend fun saveMonthlyGoal(
        goal: Int,
        isConnected: Boolean
    ): Response<Unit> {
        return try {
            val month = getFormattedDate()
            val monthlyGoal = MonthlyGoal(
                userId = auth.currentUser?.uid,
                month = month,
                goalCount = goal,
                isSynced = isConnected
            )

            if (isConnected) {
                monthlyGoalsRef.document(month).set(monthlyGoal).await()
                db.monthlyGoalDao().insertMonthlyGoal(monthlyGoal.toEntity().copy(isSynced = true))
            } else {
                db.monthlyGoalDao().insertMonthlyGoal(monthlyGoal.toEntity().copy(isSynced = false))
            }
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "Error occurred while saving monthly target")
        }
    }

    override fun getMonthlyGoal(isConnected: Boolean): Flow<Response<MonthlyGoal>> = callbackFlow {
        val month = getFormattedDate()

        val cachedGoal = db.monthlyGoalDao().getMonthlyGoal(month)
        if (cachedGoal != null) {
            trySend(Response.Success(cachedGoal.toMonthlyGoal()))
        }

        if (isConnected) {
            val listener = monthlyGoalsRef
                .whereEqualTo("month", month)
                .addSnapshotListener { snapshot, error ->
                    error?.let {
                        trySend(Response.Failure(it.message ?: "Firestore error"))
                        return@addSnapshotListener
                    }

                    val goal = snapshot?.documents?.firstOrNull()?.toObject(MonthlyGoal::class.java)
                    goal?.let {
                        ioScope.launch {
                            db.monthlyGoalDao().insertMonthlyGoal(it.toEntity().copy(isSynced = true))
                        }
                        trySend(Response.Success(it))
                    } ?: trySend(Response.Empty)
                }

            awaitClose { listener.remove() }
        } else if (cachedGoal == null) {
            trySend(Response.Empty)
            awaitClose()
        }
    }

    override fun getFormattedDate(): String =
        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
}