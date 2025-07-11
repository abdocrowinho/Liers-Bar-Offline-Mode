package com.example.domain.UseCase

import com.example.domain.Repo.GamePlayRepo
import com.example.domain.Validation.UseCase.UserNamesValidationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseProvider{

    @Provides
    fun providesValidationUseCase():UserNamesValidationUseCase {
        return UserNamesValidationUseCase()
    }
    @Provides
    fun providesValidationSinglePlayerUseCase():SinglePlayerValidation {
        return SinglePlayerValidation()
    }
    @Provides
    fun providesGenerateImageUseCase(gamePlayRepo: GamePlayRepo):GenerateMultipleGameUseCase {
        return GenerateMultipleGameUseCase(gamePlayRepo)
    }

}