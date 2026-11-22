package com.forge.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.forge.app.data.db.dao.*
import com.forge.app.data.db.entities.*

@Database(
    entities = [
        UserEntity::class,
        ProgrammeEntity::class,
        ProgrammeDayEntity::class,
        ExerciseEntity::class,
        ExerciseInstructionEntity::class,
        ExerciseAlternativeEntity::class,
        ProgrammeDayExerciseEntity::class,
        WorkoutSessionEntity::class,
        SessionExerciseEntity::class,
        SetRecordEntity::class,
        BodyMeasurementEntity::class,
        GoalEntity::class,
        ConsistencyEntity::class,
        AchievementEntity::class,
        PersonalRecordEntity::class,
        RecoveryCheckInEntity::class,
        FoodItemEntity::class,
        MealLogEntity::class,
        MealFoodItemEntity::class,
        DailyNutritionTargetEntity::class,
        HydrationLogEntity::class,
        MealPlanDayEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ForgeDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun programmeDao(): ProgrammeDao
    abstract fun progressDao(): ProgressDao
    abstract fun consistencyDao(): ConsistencyDao
    abstract fun goalDao(): GoalDao
    abstract fun recoveryDao(): RecoveryDao
    abstract fun foodDao(): FoodDao
    abstract fun nutritionLogDao(): NutritionLogDao
    abstract fun hydrationDao(): HydrationDao
    abstract fun mealPlanDao(): MealPlanDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""CREATE TABLE IF NOT EXISTS `personal_record` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `exerciseId` INTEGER NOT NULL,
            `exerciseName` TEXT NOT NULL DEFAULT '',
            `weightKg` REAL NOT NULL DEFAULT 0.0,
            `reps` INTEGER NOT NULL DEFAULT 0,
            `estimatedOneRm` REAL NOT NULL DEFAULT 0.0,
            `totalVolume` REAL NOT NULL DEFAULT 0.0,
            `recordType` TEXT NOT NULL DEFAULT 'WEIGHT',
            `achievedAt` INTEGER NOT NULL DEFAULT 0,
            `sessionId` INTEGER NOT NULL DEFAULT 0,
            FOREIGN KEY(`exerciseId`) REFERENCES `exercise`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)""")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_personal_record_exerciseId` ON `personal_record` (`exerciseId`)")
        database.execSQL("""CREATE TABLE IF NOT EXISTS `recovery_checkin` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `recordedAt` INTEGER NOT NULL DEFAULT 0,
            `dateKey` TEXT NOT NULL DEFAULT '',
            `sleepHours` REAL NOT NULL DEFAULT 7.0,
            `sleepQuality` INTEGER NOT NULL DEFAULT 3,
            `energyLevel` INTEGER NOT NULL DEFAULT 3,
            `muscleSoreness` INTEGER NOT NULL DEFAULT 2,
            `motivation` INTEGER NOT NULL DEFAULT 3,
            `fatigue` INTEGER NOT NULL DEFAULT 2,
            `soreMuscleGroups` TEXT NOT NULL DEFAULT '',
            `notes` TEXT NOT NULL DEFAULT '')""")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""CREATE TABLE IF NOT EXISTS `food_item` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `name` TEXT NOT NULL,
            `category` TEXT NOT NULL,
            `servingSize` REAL NOT NULL DEFAULT 100.0,
            `servingUnit` TEXT NOT NULL DEFAULT 'g',
            `calories` REAL NOT NULL,
            `protein` REAL NOT NULL,
            `carbs` REAL NOT NULL,
            `fat` REAL NOT NULL,
            `fiber` REAL NOT NULL DEFAULT 0.0,
            `isCustom` INTEGER NOT NULL DEFAULT 0,
            `isVerified` INTEGER NOT NULL DEFAULT 1,
            `notes` TEXT NOT NULL DEFAULT '')""")

        database.execSQL("""CREATE TABLE IF NOT EXISTS `meal_log` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `dateKey` TEXT NOT NULL,
            `mealType` TEXT NOT NULL,
            `loggedAt` INTEGER NOT NULL DEFAULT 0)""")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_meal_log_dateKey_mealType` ON `meal_log` (`dateKey`, `mealType`)")

        database.execSQL("""CREATE TABLE IF NOT EXISTS `meal_food_item` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `mealLogId` INTEGER NOT NULL,
            `foodItemId` INTEGER,
            `foodName` TEXT NOT NULL,
            `servingQuantity` REAL NOT NULL DEFAULT 1.0,
            `servingSize` REAL NOT NULL DEFAULT 100.0,
            `servingUnit` TEXT NOT NULL DEFAULT 'g',
            `calories` REAL NOT NULL,
            `protein` REAL NOT NULL,
            `carbs` REAL NOT NULL,
            `fat` REAL NOT NULL,
            `fiber` REAL NOT NULL DEFAULT 0.0,
            FOREIGN KEY(`mealLogId`) REFERENCES `meal_log`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)""")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_meal_food_item_mealLogId` ON `meal_food_item` (`mealLogId`)")

        database.execSQL("""CREATE TABLE IF NOT EXISTS `daily_nutrition_target` (
            `id` INTEGER PRIMARY KEY NOT NULL,
            `targetCalories` REAL NOT NULL DEFAULT 2200.0,
            `targetProtein` REAL NOT NULL DEFAULT 140.0,
            `targetCarbs` REAL NOT NULL DEFAULT 240.0,
            `targetFat` REAL NOT NULL DEFAULT 65.0,
            `targetFiber` REAL NOT NULL DEFAULT 30.0,
            `waterTargetMl` INTEGER NOT NULL DEFAULT 3000,
            `goalType` TEXT NOT NULL DEFAULT 'MAINTENANCE',
            `updatedAt` INTEGER NOT NULL DEFAULT 0)""")

        database.execSQL("""CREATE TABLE IF NOT EXISTS `hydration_log` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `dateKey` TEXT NOT NULL,
            `amountMl` INTEGER NOT NULL,
            `loggedAt` INTEGER NOT NULL DEFAULT 0)""")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_hydration_log_dateKey` ON `hydration_log` (`dateKey`)")

        database.execSQL("""CREATE TABLE IF NOT EXISTS `meal_plan_day` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `dayOfWeek` INTEGER NOT NULL,
            `mealType` TEXT NOT NULL,
            `foodItemId` INTEGER,
            `foodName` TEXT NOT NULL,
            `servingQuantity` REAL NOT NULL DEFAULT 1.0,
            `servingSize` REAL NOT NULL DEFAULT 100.0,
            `servingUnit` TEXT NOT NULL DEFAULT 'g',
            `calories` REAL NOT NULL,
            `protein` REAL NOT NULL,
            `carbs` REAL NOT NULL,
            `fat` REAL NOT NULL)""")
    }
}
