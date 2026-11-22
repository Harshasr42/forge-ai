package com.forge.app.ui.navigation

sealed class ForgeDestination(val route: String) {
    object Today : ForgeDestination("today")
    object Programme : ForgeDestination("programme")
    object Workout : ForgeDestination("workout?dayId={dayId}") {
        fun createRoute(dayId: Long = 0L) = "workout?dayId=$dayId"
    }
    object ExerciseLibrary : ForgeDestination("exercises")
    object ExerciseDetail : ForgeDestination("exercise/{exerciseId}") {
        fun createRoute(exerciseId: Long) = "exercise/$exerciseId"
    }
    object History : ForgeDestination("history")
    object Consistency : ForgeDestination("consistency")
    object Onboarding : ForgeDestination("onboarding")
    object Progress : ForgeDestination("progress")
    object PersonalRecords : ForgeDestination("personal_records")
    object ExerciseAnalytics : ForgeDestination("exercise_analytics/{exerciseId}") {
        fun createRoute(exerciseId: Long) = "exercise_analytics/$exerciseId"
    }
    object BodyMeasurements : ForgeDestination("body_measurements")
    object Goals : ForgeDestination("goals")
    object AddGoal : ForgeDestination("add_goal")
    object Recovery : ForgeDestination("recovery")
    object Coach : ForgeDestination("coach")
    object WeeklyReport : ForgeDestination("weekly_report")

    // Phase 4: Nutrition & Lifestyle OS
    object Nutrition : ForgeDestination("nutrition")
    object FoodSearch : ForgeDestination("food_search/{mealType}/{dateKey}") {
        fun createRoute(mealType: String, dateKey: String) = "food_search/$mealType/$dateKey"
    }
    object Hydration : ForgeDestination("hydration")
    object MacroTargets : ForgeDestination("macro_targets")
    object MealPlanner : ForgeDestination("meal_planner")
}
