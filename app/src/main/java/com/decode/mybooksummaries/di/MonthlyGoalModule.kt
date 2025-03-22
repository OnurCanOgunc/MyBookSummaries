package com.decode.mybooksummaries.di

import com.decode.mybooksummaries.data.repository.MonthlyGoalRepositoryImpl
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import com.decode.mybooksummaries.domain.usecase.goal.GetMonthlyGoalUseCase
import com.decode.mybooksummaries.domain.usecase.MonthlyGoalUseCases
import com.decode.mybooksummaries.domain.usecase.goal.SaveMonthlyGoalUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MonthlyGoalModule {
    @Provides
    @Singleton
    fun provideMonthlyGoalRepository(
        db: BookDatabase,
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ): MonthlyGoalRepository {
        return MonthlyGoalRepositoryImpl(firestore,firebaseAuth,db,)
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