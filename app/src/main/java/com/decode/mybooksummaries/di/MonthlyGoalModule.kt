package com.decode.mybooksummaries.di

import com.decode.mybooksummaries.data.repository.MonthlyGoalRepositoryImpl
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import com.decode.mybooksummaries.domain.usecase.goal.GetMonthlyGoalUseCase
import com.decode.mybooksummaries.domain.usecase.MonthlyGoalUseCases
import com.decode.mybooksummaries.domain.usecase.goal.SaveMonthlyGoalUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MonthlyGoalModule {
    @Provides
    @Singleton
    fun provideMonthlyGoalRepository(
        db: BookDatabase,
        @Named("monthlyGoalRef")monthlyGoalRef: CollectionReference
    ): MonthlyGoalRepository {
        return MonthlyGoalRepositoryImpl(monthlyGoalRef,db)
    }

    @Provides
    @Singleton
    @Named("monthlyGoalRef")
    fun provideMonthlyGoalsCollectionRef(auth: FirebaseAuth): CollectionReference {
        return Firebase.firestore.collection("users")
            .document(auth.currentUser?.uid ?: "no user")
            .collection("monthlyGoals")
    }

    @Provides
    @Singleton
    fun provideMonthlyGoalUseCases(repository: MonthlyGoalRepository): MonthlyGoalUseCases {
        return MonthlyGoalUseCases(
            saveMonthlyGoal = SaveMonthlyGoalUseCase(repository),
            getMonthlyGoal = GetMonthlyGoalUseCase(repository)
        )
    }
}