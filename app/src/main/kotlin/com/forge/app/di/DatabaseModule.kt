package com.forge.app.di

import android.content.Context
import androidx.room.Room
import com.forge.app.data.db.ForgeDatabase
import com.forge.app.data.db.MIGRATION_1_2
import com.forge.app.data.db.MIGRATION_2_3
import com.forge.app.data.db.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ForgeDatabase =
        Room.databaseBuilder(context, ForgeDatabase::class.java, "forge_db")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideUserDao(db: ForgeDatabase): UserDao = db.userDao()
    @Provides fun provideWorkoutDao(db: ForgeDatabase): WorkoutDao = db.workoutDao()
    @Provides fun provideExerciseDao(db: ForgeDatabase): ExerciseDao = db.exerciseDao()
    @Provides fun provideProgrammeDao(db: ForgeDatabase): ProgrammeDao = db.programmeDao()
    @Provides fun provideProgressDao(db: ForgeDatabase): ProgressDao = db.progressDao()
    @Provides fun provideConsistencyDao(db: ForgeDatabase): ConsistencyDao = db.consistencyDao()
    @Provides fun provideGoalDao(db: ForgeDatabase): GoalDao = db.goalDao()
    @Provides fun provideRecoveryDao(db: ForgeDatabase): RecoveryDao = db.recoveryDao()
    @Provides fun provideFoodDao(db: ForgeDatabase): FoodDao = db.foodDao()
    @Provides fun provideNutritionLogDao(db: ForgeDatabase): NutritionLogDao = db.nutritionLogDao()
    @Provides fun provideHydrationDao(db: ForgeDatabase): HydrationDao = db.hydrationDao()
    @Provides fun provideMealPlanDao(db: ForgeDatabase): MealPlanDao = db.mealPlanDao()
}
