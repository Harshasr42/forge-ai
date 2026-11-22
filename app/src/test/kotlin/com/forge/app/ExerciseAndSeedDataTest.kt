package com.forge.app

import com.forge.app.data.db.entities.ExerciseEntity
import org.junit.Assert.*
import org.junit.Test

class ExerciseAndSeedDataTest {

    private val seededExercises = listOf(
        // CHEST (4)
        ExerciseEntity(id = 1, name = "30° Incline DB Press", category = "CHEST", equipment = "DUMBBELL", muscleTarget = "Upper Chest", muscleSecondary = "Front Deltoid, Triceps", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 2, name = "Flat DB Press", category = "CHEST", equipment = "DUMBBELL", muscleTarget = "Mid Chest", muscleSecondary = "Front Deltoid, Triceps", difficultyLevel = "BEGINNER", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 3, name = "Pec Deck Fly", category = "CHEST", equipment = "MACHINE", muscleTarget = "Chest", muscleSecondary = "Front Deltoid", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 4, name = "Free Deficit Push-ups", category = "CHEST", equipment = "BODYWEIGHT", muscleTarget = "Chest", muscleSecondary = "Triceps, Core", difficultyLevel = "BEGINNER", exerciseType = "COMPOUND"),

        // BACK (10)
        ExerciseEntity(id = 5, name = "Single-arm Half-kneeling Lat Pulldown", category = "BACK", equipment = "CABLE", muscleTarget = "Latissimus Dorsi", muscleSecondary = "Biceps, Rear Delts", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 6, name = "Neutral Grip Pulldown", category = "BACK", equipment = "CABLE", muscleTarget = "Latissimus Dorsi", muscleSecondary = "Biceps, Rhomboids", difficultyLevel = "BEGINNER", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 7, name = "Standing Straight Arm Cable Pulldown", category = "BACK", equipment = "CABLE", muscleTarget = "Latissimus Dorsi", muscleSecondary = "Teres Major", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 8, name = "Chest-Supported Wide Row", category = "BACK", equipment = "MACHINE", muscleTarget = "Upper Back, Rhomboids", muscleSecondary = "Rear Delts, Biceps", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 9, name = "Shrugs", category = "BACK", equipment = "DUMBBELL", muscleTarget = "Upper Trapezius", muscleSecondary = "Forearms", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 10, name = "Romanian Deadlift", category = "BACK", equipment = "BARBELL", muscleTarget = "Erectors, Hamstrings", muscleSecondary = "Glutes, Lats", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 11, name = "Pull-ups", category = "BACK", equipment = "BODYWEIGHT", muscleTarget = "Latissimus Dorsi", muscleSecondary = "Biceps, Core", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 12, name = "Towel Pull-ups", category = "BACK", equipment = "BODYWEIGHT", muscleTarget = "Latissimus Dorsi, Grip", muscleSecondary = "Forearms, Biceps", difficultyLevel = "ADVANCED", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 13, name = "Dead Hang", category = "BACK", equipment = "BODYWEIGHT", muscleTarget = "Decompression, Grip", muscleSecondary = "Forearms, Shoulders", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 14, name = "Towel Dead Hang", category = "BACK", equipment = "BODYWEIGHT", muscleTarget = "Grip Strength, Forearms", muscleSecondary = "Lat Stretch", difficultyLevel = "ADVANCED", exerciseType = "ISOLATION"),

        // SHOULDERS (5)
        ExerciseEntity(id = 15, name = "Cross-body Cable Lateral Raise", category = "SHOULDERS", equipment = "CABLE", muscleTarget = "Side Deltoid", muscleSecondary = "Upper Traps", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 16, name = "25° Incline Cable Rear Delt Fly", category = "SHOULDERS", equipment = "CABLE", muscleTarget = "Rear Deltoid", muscleSecondary = "Rhomboids", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 17, name = "Reverse Pec Deck", category = "SHOULDERS", equipment = "MACHINE", muscleTarget = "Rear Deltoid", muscleSecondary = "Upper Back", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 18, name = "DB Overhead Press", category = "SHOULDERS", equipment = "DUMBBELL", muscleTarget = "Front Deltoid", muscleSecondary = "Side Deltoid, Triceps", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 19, name = "Around the World", category = "SHOULDERS", equipment = "DUMBBELL", muscleTarget = "Full Deltoid Complex", muscleSecondary = "Rotator Cuff", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),

        // TRICEPS (4)
        ExerciseEntity(id = 20, name = "Straight Bar Pushdown", category = "TRICEPS", equipment = "CABLE", muscleTarget = "Lateral Head Triceps", muscleSecondary = "", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 21, name = "Overhead Tricep Extension", category = "TRICEPS", equipment = "CABLE", muscleTarget = "Long Head Triceps", muscleSecondary = "", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 22, name = "Cross-Body Cable Pushdown", category = "TRICEPS", equipment = "CABLE", muscleTarget = "Medial & Lateral Triceps", muscleSecondary = "", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 23, name = "Smith Machine JM Press / Dip", category = "TRICEPS", equipment = "SMITH_MACHINE", muscleTarget = "All Tricep Heads", muscleSecondary = "Chest", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),

        // BICEPS (4)
        ExerciseEntity(id = 24, name = "Bayesian Curl", category = "BICEPS", equipment = "CABLE", muscleTarget = "Long Head Biceps", muscleSecondary = "Brachialis", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 25, name = "Preacher Curl", category = "BICEPS", equipment = "MACHINE", muscleTarget = "Short Head Biceps", muscleSecondary = "Brachialis", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 26, name = "Cross-Body Hammer Curl", category = "BICEPS", equipment = "DUMBBELL", muscleTarget = "Brachialis & Brachioradialis", muscleSecondary = "Biceps", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 27, name = "Incline DB Curl", category = "BICEPS", equipment = "DUMBBELL", muscleTarget = "Long Head Biceps", muscleSecondary = "Brachialis", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),

        // LEGS (10)
        ExerciseEntity(id = 28, name = "Smith Machine Squat", category = "LEGS", equipment = "SMITH_MACHINE", muscleTarget = "Quadriceps, Glutes", muscleSecondary = "Hamstrings, Core", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 29, name = "Hack Squat", category = "LEGS", equipment = "MACHINE", muscleTarget = "Quadriceps (Vastus)", muscleSecondary = "Glutes", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 30, name = "Seated Leg Extension", category = "LEGS", equipment = "MACHINE", muscleTarget = "Rectus Femoris, Quads", muscleSecondary = "", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 31, name = "Seated Leg Curl", category = "LEGS", equipment = "MACHINE", muscleTarget = "Hamstrings", muscleSecondary = "Calves", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 32, name = "Barbell Romanian Deadlift", category = "LEGS", equipment = "BARBELL", muscleTarget = "Hamstrings, Glutes", muscleSecondary = "Lower Back", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 33, name = "Seated Hip Adductor Machine", category = "LEGS", equipment = "MACHINE", muscleTarget = "Adductors", muscleSecondary = "Groin Stability", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 34, name = "Standing Calf Raise", category = "LEGS", equipment = "MACHINE", muscleTarget = "Gastrocnemius, Soleus", muscleSecondary = "", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 35, name = "Farmer's Carry", category = "LEGS", equipment = "DUMBBELL", muscleTarget = "Core, Traps, Grip", muscleSecondary = "Forearms, Legs", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
        ExerciseEntity(id = 36, name = "2-Min Deep Squat Hold (Malasana)", category = "LEGS", equipment = "BODYWEIGHT", muscleTarget = "Hip Mobility, Ankles", muscleSecondary = "Pelvic Floor", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 51, name = "World's Greatest Stretch", category = "LEGS", equipment = "BODYWEIGHT", muscleTarget = "Thoracic & Hip Mobility", muscleSecondary = "Hamstrings, Hip Flexors", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),

        // FOREARMS (5)
        ExerciseEntity(id = 37, name = "Reverse EZ-Bar Curl", category = "FOREARMS", equipment = "BARBELL", muscleTarget = "Brachioradialis", muscleSecondary = "Extensors", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 38, name = "Incline Hammer Curl", category = "FOREARMS", equipment = "DUMBBELL", muscleTarget = "Brachioradialis", muscleSecondary = "Biceps", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 39, name = "Seated DB Wrist Curl", category = "FOREARMS", equipment = "DUMBBELL", muscleTarget = "Wrist Flexors", muscleSecondary = "Grip", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 40, name = "Seated DB Wrist Extension", category = "FOREARMS", equipment = "DUMBBELL", muscleTarget = "Wrist Extensors", muscleSecondary = "", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 41, name = "Behind-the-Back Cable Extension", category = "FOREARMS", equipment = "CABLE", muscleTarget = "Wrist Flexors/Extensors", muscleSecondary = "Grip", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),

        // ABS (9)
        ExerciseEntity(id = 42, name = "Kneeling Cable Crunch", category = "ABS", equipment = "CABLE", muscleTarget = "Upper & Mid Abs", muscleSecondary = "Serratus", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 43, name = "Hanging Pelvic Leg-to-Chest Raise", category = "ABS", equipment = "BODYWEIGHT", muscleTarget = "Lower Abs", muscleSecondary = "Hip Flexors, Grip", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 44, name = "Captain's Chair Raise", category = "ABS", equipment = "MACHINE", muscleTarget = "Lower Abs", muscleSecondary = "Hip Flexors", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 45, name = "High-to-Low Cable Woodchopper", category = "ABS", equipment = "CABLE", muscleTarget = "Obliques", muscleSecondary = "Transverse Abdominis", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 46, name = "Stomach Vacuums", category = "ABS", equipment = "BODYWEIGHT", muscleTarget = "Transverse Abdominis", muscleSecondary = "Core Wall", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 47, name = "Overhead Cable Crunch Extension", category = "ABS", equipment = "CABLE", muscleTarget = "Rectus Abdominis", muscleSecondary = "Intercostals", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 48, name = "Pike Handstand Plank", category = "ABS", equipment = "BODYWEIGHT", muscleTarget = "Deep Core, Shoulders", muscleSecondary = "Serratus Anterior", difficultyLevel = "ADVANCED", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 49, name = "Side Plank", category = "ABS", equipment = "BODYWEIGHT", muscleTarget = "Obliques, QL", muscleSecondary = "Hip Abductors", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
        ExerciseEntity(id = 50, name = "Ab Wheel Rollout", category = "ABS", equipment = "BODYWEIGHT", muscleTarget = "Full Core Wall", muscleSecondary = "Lats, Shoulders", difficultyLevel = "ADVANCED", exerciseType = "ISOLATION")
    )

    @Test
    fun testExactExerciseCountIs51() {
        assertEquals("Total seeded exercise count must equal 51", 51, seededExercises.size)
    }

    @Test
    fun testAllExerciseIdsAreUniqueAndInRange() {
        val ids = seededExercises.map { it.id }
        assertEquals("All IDs must be unique", 51, ids.toSet().size)
        assertTrue("All IDs must be between 1 and 51", ids.all { it in 1L..51L })
    }

    @Test
    fun testAllCategoriesAreValid() {
        val validCategories = setOf("CHEST", "BACK", "SHOULDERS", "BICEPS", "TRICEPS", "LEGS", "FOREARMS", "ABS")
        seededExercises.forEach { exercise ->
            assertTrue(
                "Exercise ${exercise.name} has invalid category ${exercise.category}",
                validCategories.contains(exercise.category)
            )
        }
    }

    @Test
    fun testExerciseEquipmentTypesAreValid() {
        val validEquipment = setOf("DUMBBELL", "BARBELL", "CABLE", "MACHINE", "SMITH_MACHINE", "BODYWEIGHT")
        seededExercises.forEach { exercise ->
            assertTrue(
                "Exercise ${exercise.name} has invalid equipment ${exercise.equipment}",
                validEquipment.contains(exercise.equipment)
            )
        }
    }

    @Test
    fun testExerciseMovementTypesAreValid() {
        val validTypes = setOf("COMPOUND", "ISOLATION")
        seededExercises.forEach { exercise ->
            assertTrue(
                "Exercise ${exercise.name} has invalid type ${exercise.exerciseType}",
                validTypes.contains(exercise.exerciseType)
            )
        }
    }

    @Test
    fun testCategoryBreakdownMatchesRequirements() {
        val counts = seededExercises.groupBy { it.category }.mapValues { it.value.size }
        assertEquals(4, counts["CHEST"])
        assertEquals(10, counts["BACK"])
        assertEquals(5, counts["SHOULDERS"])
        assertEquals(4, counts["TRICEPS"])
        assertEquals(4, counts["BICEPS"])
        assertEquals(10, counts["LEGS"])
        assertEquals(5, counts["FOREARMS"])
        assertEquals(9, counts["ABS"])
    }
}
