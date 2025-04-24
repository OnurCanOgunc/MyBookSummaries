package com.decode.mybooksummaries.di

import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import com.decode.mybooksummaries.domain.usecase.goal.GetMonthlyGoalUseCase
import com.decode.mybooksummaries.domain.usecase.MonthlyGoalUseCases
import com.decode.mybooksummaries.domain.usecase.goal.SaveMonthlyGoalUseCase
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
    fun provideMonthlyGoalUseCases(repository: MonthlyGoalRepository): MonthlyGoalUseCases {
        return MonthlyGoalUseCases(
            saveMonthlyGoal = SaveMonthlyGoalUseCase(repository),
            getMonthlyGoal = GetMonthlyGoalUseCase(repository)
        )
    }
}