package com.forge.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.forge.app.ui.screens.coach.CoachScreen
import com.forge.app.ui.screens.coach.WeeklyReportScreen
import com.forge.app.ui.screens.consistency.ConsistencyScreen
import com.forge.app.ui.screens.exercise.ExerciseDetailScreen
import com.forge.app.ui.screens.exercise.ExerciseLibraryScreen
import com.forge.app.ui.screens.goals.AddGoalScreen
import com.forge.app.ui.screens.goals.GoalsScreen
import com.forge.app.ui.screens.history.HistoryScreen
import com.forge.app.ui.screens.nutrition.*
import com.forge.app.ui.screens.programme.ProgrammeScreen
import com.forge.app.ui.screens.progress.BodyMeasurementsScreen
import com.forge.app.ui.screens.progress.ExerciseAnalyticsScreen
import com.forge.app.ui.screens.progress.PersonalRecordsScreen
import com.forge.app.ui.screens.progress.ProgressDashboardScreen
import com.forge.app.ui.screens.recovery.RecoveryScreen
import com.forge.app.ui.screens.today.TodayScreen
import com.forge.app.ui.screens.workout.ActiveWorkoutScreen
import com.forge.app.ui.theme.*

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(ForgeDestination.Today.route, "Today", Icons.Filled.Today, Icons.Outlined.Today),
    BottomNavItem(ForgeDestination.Programme.route, "Train", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
    BottomNavItem(ForgeDestination.Progress.route, "Progress", Icons.Filled.TrendingUp, Icons.Outlined.TrendingUp),
    BottomNavItem(ForgeDestination.Coach.route, "Coach", Icons.Filled.Psychology, Icons.Outlined.Psychology),
    BottomNavItem(ForgeDestination.Consistency.route, "Streaks", Icons.Filled.LocalFireDepartment, Icons.Outlined.LocalFireDepartment)
)

@Composable
fun ForgeApp(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = currentRoute in listOf(
        ForgeDestination.Today.route,
        ForgeDestination.Programme.route,
        ForgeDestination.Progress.route,
        ForgeDestination.Coach.route,
        ForgeDestination.Consistency.route
    )

    Scaffold(
        containerColor = Background,
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(
                    containerColor = Surface,
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SurfaceBorder)
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label,
                                    tint = if (isSelected) Accent else TextMuted
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    color = if (isSelected) Accent else TextMuted,
                                    style = ForgeTypography.labelSmall,
                                    fontSize = 10.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = SurfaceElevated
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = ForgeDestination.Today.route,
            modifier = Modifier.padding(padding)
        ) {
            // Core Screens
            composable(ForgeDestination.Today.route) {
                TodayScreen(
                    onStartWorkout = { dayId ->
                        navController.navigate(ForgeDestination.Workout.createRoute(dayId))
                    },
                    onNavigateToProgramme = {
                        navController.navigate(ForgeDestination.Programme.route)
                    },
                    onNavigateToExercises = {
                        navController.navigate(ForgeDestination.ExerciseLibrary.route)
                    },
                    onNavigateToNutrition = {
                        navController.navigate(ForgeDestination.Nutrition.route)
                    },
                    onNavigateToHistory = {
                        navController.navigate(ForgeDestination.History.route)
                    }
                )
            }

            composable(ForgeDestination.Programme.route) {
                ProgrammeScreen(
                    onSelectDayWorkout = { dayId ->
                        navController.navigate(ForgeDestination.Workout.createRoute(dayId))
                    }
                )
            }

            composable(ForgeDestination.Progress.route) {
                ProgressDashboardScreen(
                    onNavigateToPRs = {
                        navController.navigate(ForgeDestination.PersonalRecords.route)
                    },
                    onNavigateToExercise = { exerciseId ->
                        navController.navigate(ForgeDestination.ExerciseAnalytics.createRoute(exerciseId))
                    },
                    onNavigateToMeasurements = {
                        navController.navigate(ForgeDestination.BodyMeasurements.route)
                    },
                    onNavigateToWorkout = {
                        navController.navigate(ForgeDestination.Workout.createRoute())
                    }
                )
            }

            composable(ForgeDestination.Coach.route) {
                CoachScreen(
                    onNavigate = { route ->
                        when (route) {
                            "today" -> navController.navigate(ForgeDestination.Today.route)
                            "personal_records" -> navController.navigate(ForgeDestination.PersonalRecords.route)
                            "recovery" -> navController.navigate(ForgeDestination.Recovery.route)
                            "goals" -> navController.navigate(ForgeDestination.Goals.route)
                            "weekly_report" -> navController.navigate(ForgeDestination.WeeklyReport.route)
                            else -> navController.navigate(route)
                        }
                    }
                )
            }

            composable(ForgeDestination.Consistency.route) {
                ConsistencyScreen()
            }

            // Sub-Screens & Detail Flows
            composable(
                route = ForgeDestination.Workout.route,
                arguments = listOf(
                    navArgument("dayId") {
                        type = NavType.StringType
                        defaultValue = "1"
                    }
                )
            ) {
                ActiveWorkoutScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onWorkoutFinished = {
                        navController.navigate(ForgeDestination.Today.route) {
                            popUpTo(ForgeDestination.Today.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(ForgeDestination.ExerciseLibrary.route) {
                ExerciseLibraryScreen(
                    onSelectExercise = { exerciseId ->
                        navController.navigate(ForgeDestination.ExerciseDetail.createRoute(exerciseId))
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = ForgeDestination.ExerciseDetail.route,
                arguments = listOf(
                    navArgument("exerciseId") { type = NavType.LongType }
                )
            ) {
                ExerciseDetailScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ForgeDestination.History.route) {
                HistoryScreen(
                    onStartWorkout = { navController.navigate(ForgeDestination.Today.route) },
                    onExploreProgramme = { navController.navigate(ForgeDestination.Programme.route) }
                )
            }

            composable(ForgeDestination.PersonalRecords.route) {
                PersonalRecordsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = ForgeDestination.ExerciseAnalytics.route,
                arguments = listOf(
                    navArgument("exerciseId") { type = NavType.LongType }
                )
            ) {
                ExerciseAnalyticsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ForgeDestination.BodyMeasurements.route) {
                BodyMeasurementsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ForgeDestination.Goals.route) {
                GoalsScreen(
                    onAddGoal = { navController.navigate(ForgeDestination.AddGoal.route) },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ForgeDestination.AddGoal.route) {
                AddGoalScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ForgeDestination.Recovery.route) {
                RecoveryScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ForgeDestination.WeeklyReport.route) {
                WeeklyReportScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Phase 4: Nutrition & Lifestyle OS Routes
            composable(ForgeDestination.Nutrition.route) {
                NutritionDashboardScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onAddFood = { mealType, dateKey ->
                        navController.navigate(ForgeDestination.FoodSearch.createRoute(mealType, dateKey))
                    },
                    onNavigateToHydration = {
                        navController.navigate(ForgeDestination.Hydration.route)
                    },
                    onNavigateToTargets = {
                        navController.navigate(ForgeDestination.MacroTargets.route)
                    },
                    onNavigateToPlanner = {
                        navController.navigate(ForgeDestination.MealPlanner.route)
                    }
                )
            }

            composable(
                route = ForgeDestination.FoodSearch.route,
                arguments = listOf(
                    navArgument("mealType") { type = NavType.StringType },
                    navArgument("dateKey") { type = NavType.StringType }
                )
            ) {
                FoodSearchScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ForgeDestination.Hydration.route) {
                HydrationTrackerScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ForgeDestination.MacroTargets.route) {
                MacroTargetsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ForgeDestination.MealPlanner.route) {
                MealPlannerScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
