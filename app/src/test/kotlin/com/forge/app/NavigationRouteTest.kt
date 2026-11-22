package com.forge.app

import com.forge.app.ui.navigation.ForgeDestination
import org.junit.Assert.assertEquals
import org.junit.Test

class NavigationRouteTest {

    @Test
    fun testTodayRoute() {
        assertEquals("today", ForgeDestination.Today.route)
    }

    @Test
    fun testProgrammeRoute() {
        assertEquals("programme", ForgeDestination.Programme.route)
    }

    @Test
    fun testWorkoutRouteCreation() {
        assertEquals("workout?dayId=1", ForgeDestination.Workout.createRoute(1L))
        assertEquals("workout?dayId=4", ForgeDestination.Workout.createRoute(4L))
        assertEquals("workout?dayId=7", ForgeDestination.Workout.createRoute(7L))
    }

    @Test
    fun testExerciseLibraryRoute() {
        assertEquals("exercises", ForgeDestination.ExerciseLibrary.route)
    }

    @Test
    fun testExerciseDetailRouteCreation() {
        assertEquals("exercise/1", ForgeDestination.ExerciseDetail.createRoute(1L))
        assertEquals("exercise/28", ForgeDestination.ExerciseDetail.createRoute(28L))
        assertEquals("exercise/51", ForgeDestination.ExerciseDetail.createRoute(51L))
    }

    @Test
    fun testHistoryRoute() {
        assertEquals("history", ForgeDestination.History.route)
    }

    @Test
    fun testConsistencyRoute() {
        assertEquals("consistency", ForgeDestination.Consistency.route)
    }

    @Test
    fun testPhase2And3Routes() {
        assertEquals("progress", ForgeDestination.Progress.route)
        assertEquals("personal_records", ForgeDestination.PersonalRecords.route)
        assertEquals("exercise_analytics/5", ForgeDestination.ExerciseAnalytics.createRoute(5L))
        assertEquals("body_measurements", ForgeDestination.BodyMeasurements.route)
        assertEquals("goals", ForgeDestination.Goals.route)
        assertEquals("add_goal", ForgeDestination.AddGoal.route)
        assertEquals("recovery", ForgeDestination.Recovery.route)
        assertEquals("coach", ForgeDestination.Coach.route)
        assertEquals("weekly_report", ForgeDestination.WeeklyReport.route)
    }

    @Test
    fun testPhase4NutritionRoutes() {
        assertEquals("nutrition", ForgeDestination.Nutrition.route)
        assertEquals("food_search/BREAKFAST/2026-11-21", ForgeDestination.FoodSearch.createRoute("BREAKFAST", "2026-11-21"))
        assertEquals("hydration", ForgeDestination.Hydration.route)
        assertEquals("macro_targets", ForgeDestination.MacroTargets.route)
        assertEquals("meal_planner", ForgeDestination.MealPlanner.route)
    }
}
