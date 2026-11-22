package com.forge.app.data.seed

import com.forge.app.data.db.dao.ConsistencyDao
import com.forge.app.data.db.dao.ExerciseDao
import com.forge.app.data.db.dao.ProgrammeDao
import com.forge.app.data.db.dao.UserDao
import com.forge.app.data.db.entities.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForgeSeeder @Inject constructor(
    private val userDao: UserDao,
    private val exerciseDao: ExerciseDao,
    private val programmeDao: ProgrammeDao,
    private val consistencyDao: ConsistencyDao
) {
    suspend fun seedIfNeeded() {
        if (exerciseDao.getExerciseCount() > 0) return

        seedUser()
        seedExercises()
        seedProgramme()
        seedAchievements()
        seedConsistency()
    }

    private suspend fun seedUser() {
        userDao.upsertUser(
            UserEntity(
                id = 1L,
                name = "Athlete",
                age = 22,
                heightCm = 175f,
                weightKg = 70f,
                experienceLevel = "INTERMEDIATE",
                primaryGoal = "BUILD_STRENGTH",
                preferredWorkoutTime = "MORNING",
                useKg = true,
                onboardingComplete = true
            )
        )
    }

    private suspend fun seedExercises() {
        val exercises = listOf(
            // CHEST
            ExerciseEntity(id = 1, name = "30° Incline DB Press", category = "CHEST", equipment = "DUMBBELL", muscleTarget = "Upper Chest", muscleSecondary = "Front Deltoid, Triceps", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
            ExerciseEntity(id = 2, name = "Flat DB Press", category = "CHEST", equipment = "DUMBBELL", muscleTarget = "Mid Chest", muscleSecondary = "Front Deltoid, Triceps", difficultyLevel = "BEGINNER", exerciseType = "COMPOUND"),
            ExerciseEntity(id = 3, name = "Pec Deck Fly", category = "CHEST", equipment = "MACHINE", muscleTarget = "Chest", muscleSecondary = "Front Deltoid", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 4, name = "Free Deficit Push-ups", category = "CHEST", equipment = "BODYWEIGHT", muscleTarget = "Chest", muscleSecondary = "Triceps, Core", difficultyLevel = "BEGINNER", exerciseType = "COMPOUND"),

            // BACK
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

            // SHOULDERS
            ExerciseEntity(id = 15, name = "Cross-body Cable Lateral Raise", category = "SHOULDERS", equipment = "CABLE", muscleTarget = "Side Deltoid", muscleSecondary = "Upper Traps", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 16, name = "25° Incline Cable Rear Delt Fly", category = "SHOULDERS", equipment = "CABLE", muscleTarget = "Rear Deltoid", muscleSecondary = "Rhomboids", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 17, name = "Reverse Pec Deck", category = "SHOULDERS", equipment = "MACHINE", muscleTarget = "Rear Deltoid", muscleSecondary = "Upper Back", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 18, name = "DB Overhead Press", category = "SHOULDERS", equipment = "DUMBBELL", muscleTarget = "Front Deltoid", muscleSecondary = "Side Deltoid, Triceps", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
            ExerciseEntity(id = 19, name = "Around the World", category = "SHOULDERS", equipment = "DUMBBELL", muscleTarget = "Full Deltoid Complex", muscleSecondary = "Rotator Cuff", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),

            // TRICEPS
            ExerciseEntity(id = 20, name = "Straight Bar Pushdown", category = "TRICEPS", equipment = "CABLE", muscleTarget = "Lateral Head Triceps", muscleSecondary = "", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 21, name = "Overhead Tricep Extension", category = "TRICEPS", equipment = "CABLE", muscleTarget = "Long Head Triceps", muscleSecondary = "", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 22, name = "Cross-Body Cable Pushdown", category = "TRICEPS", equipment = "CABLE", muscleTarget = "Medial & Lateral Triceps", muscleSecondary = "", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 23, name = "Smith Machine JM Press / Dip", category = "TRICEPS", equipment = "SMITH_MACHINE", muscleTarget = "All Tricep Heads", muscleSecondary = "Chest", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),

            // BICEPS
            ExerciseEntity(id = 24, name = "Bayesian Curl", category = "BICEPS", equipment = "CABLE", muscleTarget = "Long Head Biceps", muscleSecondary = "Brachialis", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 25, name = "Preacher Curl", category = "BICEPS", equipment = "MACHINE", muscleTarget = "Short Head Biceps", muscleSecondary = "Brachialis", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 26, name = "Cross-Body Hammer Curl", category = "BICEPS", equipment = "DUMBBELL", muscleTarget = "Brachialis & Brachioradialis", muscleSecondary = "Biceps", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 27, name = "Incline DB Curl", category = "BICEPS", equipment = "DUMBBELL", muscleTarget = "Long Head Biceps", muscleSecondary = "Brachialis", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),

            // LEGS
            ExerciseEntity(id = 28, name = "Smith Machine Squat", category = "LEGS", equipment = "SMITH_MACHINE", muscleTarget = "Quadriceps, Glutes", muscleSecondary = "Hamstrings, Core", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
            ExerciseEntity(id = 29, name = "Hack Squat", category = "LEGS", equipment = "MACHINE", muscleTarget = "Quadriceps (Vastus)", muscleSecondary = "Glutes", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
            ExerciseEntity(id = 30, name = "Seated Leg Extension", category = "LEGS", equipment = "MACHINE", muscleTarget = "Rectus Femoris, Quads", muscleSecondary = "", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 31, name = "Seated Leg Curl", category = "LEGS", equipment = "MACHINE", muscleTarget = "Hamstrings", muscleSecondary = "Calves", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 32, name = "Barbell Romanian Deadlift", category = "LEGS", equipment = "BARBELL", muscleTarget = "Hamstrings, Glutes", muscleSecondary = "Lower Back", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
            ExerciseEntity(id = 33, name = "Seated Hip Adductor Machine", category = "LEGS", equipment = "MACHINE", muscleTarget = "Adductors", muscleSecondary = "Groin Stability", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 34, name = "Standing Calf Raise", category = "LEGS", equipment = "MACHINE", muscleTarget = "Gastrocnemius, Soleus", muscleSecondary = "", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 35, name = "Farmer's Carry", category = "LEGS", equipment = "DUMBBELL", muscleTarget = "Core, Traps, Grip", muscleSecondary = "Forearms, Legs", difficultyLevel = "INTERMEDIATE", exerciseType = "COMPOUND"),
            ExerciseEntity(id = 36, name = "2-Min Deep Squat Hold (Malasana)", category = "LEGS", equipment = "BODYWEIGHT", muscleTarget = "Hip Mobility, Ankles", muscleSecondary = "Pelvic Floor", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),

            // FOREARMS
            ExerciseEntity(id = 37, name = "Reverse EZ-Bar Curl", category = "FOREARMS", equipment = "BARBELL", muscleTarget = "Brachioradialis", muscleSecondary = "Extensors", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 38, name = "Incline Hammer Curl", category = "FOREARMS", equipment = "DUMBBELL", muscleTarget = "Brachioradialis", muscleSecondary = "Biceps", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 39, name = "Seated DB Wrist Curl", category = "FOREARMS", equipment = "DUMBBELL", muscleTarget = "Wrist Flexors", muscleSecondary = "Grip", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 40, name = "Seated DB Wrist Extension", category = "FOREARMS", equipment = "DUMBBELL", muscleTarget = "Wrist Extensors", muscleSecondary = "", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 41, name = "Behind-the-Back Cable Extension", category = "FOREARMS", equipment = "CABLE", muscleTarget = "Wrist Flexors/Extensors", muscleSecondary = "Grip", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),

            // ABS & CORE
            ExerciseEntity(id = 42, name = "Kneeling Cable Crunch", category = "ABS", equipment = "CABLE", muscleTarget = "Upper & Mid Abs", muscleSecondary = "Serratus", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 43, name = "Hanging Pelvic Leg-to-Chest Raise", category = "ABS", equipment = "BODYWEIGHT", muscleTarget = "Lower Abs", muscleSecondary = "Hip Flexors, Grip", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 44, name = "Captain's Chair Raise", category = "ABS", equipment = "MACHINE", muscleTarget = "Lower Abs", muscleSecondary = "Hip Flexors", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 45, name = "High-to-Low Cable Woodchopper", category = "ABS", equipment = "CABLE", muscleTarget = "Obliques", muscleSecondary = "Transverse Abdominis", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 46, name = "Stomach Vacuums", category = "ABS", equipment = "BODYWEIGHT", muscleTarget = "Transverse Abdominis", muscleSecondary = "Core Wall", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 47, name = "Overhead Cable Crunch Extension", category = "ABS", equipment = "CABLE", muscleTarget = "Rectus Abdominis", muscleSecondary = "Intercostals", difficultyLevel = "INTERMEDIATE", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 48, name = "Pike Handstand Plank", category = "ABS", equipment = "BODYWEIGHT", muscleTarget = "Deep Core, Shoulders", muscleSecondary = "Serratus Anterior", difficultyLevel = "ADVANCED", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 49, name = "Side Plank", category = "ABS", equipment = "BODYWEIGHT", muscleTarget = "Obliques, QL", muscleSecondary = "Hip Abductors", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 50, name = "Ab Wheel Rollout", category = "ABS", equipment = "BODYWEIGHT", muscleTarget = "Full Core Wall", muscleSecondary = "Lats, Shoulders", difficultyLevel = "ADVANCED", exerciseType = "ISOLATION"),
            ExerciseEntity(id = 51, name = "World's Greatest Stretch", category = "LEGS", equipment = "BODYWEIGHT", muscleTarget = "Thoracic & Hip Mobility", muscleSecondary = "Hamstrings, Hip Flexors", difficultyLevel = "BEGINNER", exerciseType = "ISOLATION")
        )
        exerciseDao.insertExercises(exercises)

        // Seed Alternatives
        val alternatives = listOf(
            ExerciseAlternativeEntity(primaryExerciseId = 5, alternativeExerciseId = 6), // Lat Pulldown -> Neutral Grip
            ExerciseAlternativeEntity(primaryExerciseId = 16, alternativeExerciseId = 17), // Incline Rear Delt -> Reverse Pec Deck
            ExerciseAlternativeEntity(primaryExerciseId = 24, alternativeExerciseId = 27), // Bayesian Curl -> Incline DB Curl
            ExerciseAlternativeEntity(primaryExerciseId = 28, alternativeExerciseId = 29), // Smith Squat -> Hack Squat
            ExerciseAlternativeEntity(primaryExerciseId = 37, alternativeExerciseId = 38), // Reverse EZ Curl -> Incline Hammer
            ExerciseAlternativeEntity(primaryExerciseId = 43, alternativeExerciseId = 44), // Hanging Raise -> Captain's Chair
            ExerciseAlternativeEntity(primaryExerciseId = 40, alternativeExerciseId = 41)  // Wrist Extension -> Behind-the-Back Cable
        )
        exerciseDao.insertAlternatives(alternatives)

        // Seed Instructions
        val instructions = listOf(
            ExerciseInstructionEntity(
                exerciseId = 1,
                startingPosition = "Set adjustable bench to 30 degrees. Retract shoulder blades and hold dumbbells above shoulders.",
                executionSteps = "1. Press dumbells upwards in an arc until arms are extended but not locked.\n2. Squeeze upper chest peak contraction for 1s.\n3. Lower with deep stretch over 3 controlled seconds.",
                breathingTips = "Inhale deeply as you lower; exhale forcefully through pursed lips on the press.",
                commonMistakes = "Bench set too steep (turns into shoulder press), flaring elbows out at 90 degrees.",
                safetyTips = "Keep feet planted firmly on ground; don't lift hips off the bench."
            ),
            ExerciseInstructionEntity(
                exerciseId = 24,
                startingPosition = "Stand facing away from cable stack, cable handle set low. Step forward until arm is drawn back behind torso.",
                executionSteps = "1. Keep shoulder and elbow locked behind torso.\n2. Curl forearm up towards shoulder squeezing bicep.\n3. Return under control feeling extreme long-head stretch.",
                breathingTips = "Exhale on contraction, inhale on the slow stretch.",
                commonMistakes = "Swinging the elbow forward during curl.",
                safetyTips = "Use moderate weight to protect biceps tendon at full extension."
            ),
            ExerciseInstructionEntity(
                exerciseId = 28,
                startingPosition = "Position bar across mid-traps. Place feet shoulder-width slightly in front of bar line.",
                executionSteps = "1. Unrack bar by twisting wrists.\n2. Descend smoothly by pushing hips back and bending knees.\n3. Hit parallel, drive heels through floor to stand.",
                breathingTips = "Deep belly breath at top (Valsalva), exhale past the sticking point.",
                commonMistakes = "Knees caving inwards; rounding lower spine.",
                safetyTips = "Set safety catches 2 inches below lowest target depth."
            )
        )
        exerciseDao.insertInstructions(instructions)
    }

    private suspend fun seedProgramme() {
        val programmeId = programmeDao.insertProgramme(
            ProgrammeEntity(
                id = 1L,
                name = "FORGE Performance Split",
                description = "6-Day High-Performance A/B Split with Active Recovery",
                isActive = true
            )
        )

        // Mon: Chest + Biceps (+ Forearms) [Dead Hang] — Workout A
        val monId = programmeDao.insertProgrammeDay(
            ProgrammeDayEntity(
                programmeId = programmeId,
                dayOfWeek = 1,
                workoutName = "Chest & Biceps",
                muscleGroups = "Chest, Biceps, Forearms",
                workoutVariant = "A",
                estimatedMinutes = 65,
                order = 1,
                notes = "Dead Hang finisher included"
            )
        )
        programmeDao.insertProgrammeDayExercises(
            listOf(
                ProgrammeDayExerciseEntity(programmeDayId = monId, exerciseId = 1, orderInWorkout = 1, targetSets = 4, targetRepsMin = 8, targetRepsMax = 12, restSeconds = 90),  // 30° Incline DB Press
                ProgrammeDayExerciseEntity(programmeDayId = monId, exerciseId = 2, orderInWorkout = 2, targetSets = 3, targetRepsMin = 10, targetRepsMax = 12, restSeconds = 90), // Flat DB Press
                ProgrammeDayExerciseEntity(programmeDayId = monId, exerciseId = 3, orderInWorkout = 3, targetSets = 3, targetRepsMin = 12, targetRepsMax = 15, restSeconds = 60), // Pec Deck Fly
                ProgrammeDayExerciseEntity(programmeDayId = monId, exerciseId = 24, orderInWorkout = 4, targetSets = 3, targetRepsMin = 10, targetRepsMax = 12, restSeconds = 60),// Bayesian Curl
                ProgrammeDayExerciseEntity(programmeDayId = monId, exerciseId = 26, orderInWorkout = 5, targetSets = 3, targetRepsMin = 12, targetRepsMax = 15, restSeconds = 60),// Cross-body Hammer Curl
                ProgrammeDayExerciseEntity(programmeDayId = monId, exerciseId = 37, orderInWorkout = 6, targetSets = 3, targetRepsMin = 12, targetRepsMax = 15, restSeconds = 45),// Reverse EZ-Bar Curl
                ProgrammeDayExerciseEntity(programmeDayId = monId, exerciseId = 39, orderInWorkout = 7, targetSets = 3, targetRepsMin = 15, targetRepsMax = 20, restSeconds = 45),// Seated DB Wrist Curl
                ProgrammeDayExerciseEntity(programmeDayId = monId, exerciseId = 13, orderInWorkout = 8, targetSets = 2, targetRepsMin = 1, targetRepsMax = 1, restSeconds = 60, notes = "45-60s Hang") // Dead Hang
            )
        )

        // Tue: Back + Triceps (+ Abs) [Dead Hang] — Workout A
        val tueId = programmeDao.insertProgrammeDay(
            ProgrammeDayEntity(
                programmeId = programmeId,
                dayOfWeek = 2,
                workoutName = "Back & Triceps",
                muscleGroups = "Back, Triceps, Abs",
                workoutVariant = "A",
                estimatedMinutes = 65,
                order = 2,
                notes = "Dead Hang finisher included"
            )
        )
        programmeDao.insertProgrammeDayExercises(
            listOf(
                ProgrammeDayExerciseEntity(programmeDayId = tueId, exerciseId = 5, orderInWorkout = 1, targetSets = 4, targetRepsMin = 8, targetRepsMax = 12, restSeconds = 90),  // Single-arm Lat Pulldown
                ProgrammeDayExerciseEntity(programmeDayId = tueId, exerciseId = 7, orderInWorkout = 2, targetSets = 3, targetRepsMin = 12, targetRepsMax = 15, restSeconds = 60), // Straight Arm Pulldown
                ProgrammeDayExerciseEntity(programmeDayId = tueId, exerciseId = 8, orderInWorkout = 3, targetSets = 4, targetRepsMin = 8, targetRepsMax = 12, restSeconds = 90),  // Chest-Supported Wide Row
                ProgrammeDayExerciseEntity(programmeDayId = tueId, exerciseId = 9, orderInWorkout = 4, targetSets = 3, targetRepsMin = 12, targetRepsMax = 15, restSeconds = 60), // Shrugs
                ProgrammeDayExerciseEntity(programmeDayId = tueId, exerciseId = 20, orderInWorkout = 5, targetSets = 4, targetRepsMin = 10, targetRepsMax = 15, restSeconds = 60),// Straight Bar Pushdown
                ProgrammeDayExerciseEntity(programmeDayId = tueId, exerciseId = 21, orderInWorkout = 6, targetSets = 3, targetRepsMin = 10, targetRepsMax = 15, restSeconds = 60),// Overhead Tricep Ext
                ProgrammeDayExerciseEntity(programmeDayId = tueId, exerciseId = 42, orderInWorkout = 7, targetSets = 3, targetRepsMin = 12, targetRepsMax = 20, restSeconds = 60),// Kneeling Cable Crunch
                ProgrammeDayExerciseEntity(programmeDayId = tueId, exerciseId = 43, orderInWorkout = 8, targetSets = 3, targetRepsMin = 10, targetRepsMax = 15, restSeconds = 60),// Hanging Pelvic Raise
                ProgrammeDayExerciseEntity(programmeDayId = tueId, exerciseId = 13, orderInWorkout = 9, targetSets = 2, targetRepsMin = 1, targetRepsMax = 1, restSeconds = 60, notes = "45-60s Hang") // Dead Hang
            )
        )

        // Wed: Legs & Shoulders (Quads + Side-delt focus) — Workout A
        val wedId = programmeDao.insertProgrammeDay(
            ProgrammeDayEntity(
                programmeId = programmeId,
                dayOfWeek = 3,
                workoutName = "Legs & Shoulders (Quads & Side-Delt)",
                muscleGroups = "Quads, Glutes, Side Delts",
                workoutVariant = "A",
                estimatedMinutes = 70,
                order = 3,
                notes = "Quad & Side-delt focus"
            )
        )
        programmeDao.insertProgrammeDayExercises(
            listOf(
                ProgrammeDayExerciseEntity(programmeDayId = wedId, exerciseId = 28, orderInWorkout = 1, targetSets = 4, targetRepsMin = 8, targetRepsMax = 10, restSeconds = 120),// Smith Machine Squat
                ProgrammeDayExerciseEntity(programmeDayId = wedId, exerciseId = 30, orderInWorkout = 2, targetSets = 3, targetRepsMin = 12, targetRepsMax = 15, restSeconds = 60), // Seated Leg Extension
                ProgrammeDayExerciseEntity(programmeDayId = wedId, exerciseId = 31, orderInWorkout = 3, targetSets = 3, targetRepsMin = 10, targetRepsMax = 15, restSeconds = 60), // Seated Leg Curl
                ProgrammeDayExerciseEntity(programmeDayId = wedId, exerciseId = 33, orderInWorkout = 4, targetSets = 3, targetRepsMin = 15, targetRepsMax = 20, restSeconds = 60), // Hip Adductor
                ProgrammeDayExerciseEntity(programmeDayId = wedId, exerciseId = 34, orderInWorkout = 5, targetSets = 4, targetRepsMin = 15, targetRepsMax = 20, restSeconds = 60), // Standing Calf Raise
                ProgrammeDayExerciseEntity(programmeDayId = wedId, exerciseId = 15, orderInWorkout = 6, targetSets = 4, targetRepsMin = 12, targetRepsMax = 20, restSeconds = 60), // Cross-body Cable Lateral Raise
                ProgrammeDayExerciseEntity(programmeDayId = wedId, exerciseId = 18, orderInWorkout = 7, targetSets = 3, targetRepsMin = 8, targetRepsMax = 12, restSeconds = 90)   // DB Overhead Press
            )
        )

        // Thu: Chest & Biceps (+ Forearms) — Workout B
        val thuId = programmeDao.insertProgrammeDay(
            ProgrammeDayEntity(
                programmeId = programmeId,
                dayOfWeek = 4,
                workoutName = "Chest & Biceps",
                muscleGroups = "Chest, Biceps, Forearms",
                workoutVariant = "B",
                estimatedMinutes = 65,
                order = 4,
                notes = "Workout B"
            )
        )
        programmeDao.insertProgrammeDayExercises(
            listOf(
                ProgrammeDayExerciseEntity(programmeDayId = thuId, exerciseId = 4, orderInWorkout = 1, targetSets = 3, targetRepsMin = 10, targetRepsMax = 15, restSeconds = 60),  // Free Deficit Push-ups
                ProgrammeDayExerciseEntity(programmeDayId = thuId, exerciseId = 1, orderInWorkout = 2, targetSets = 4, targetRepsMin = 8, targetRepsMax = 12, restSeconds = 90),   // 30° Incline DB Press
                ProgrammeDayExerciseEntity(programmeDayId = thuId, exerciseId = 3, orderInWorkout = 3, targetSets = 4, targetRepsMin = 12, targetRepsMax = 15, restSeconds = 60),  // Pec Deck Fly
                ProgrammeDayExerciseEntity(programmeDayId = thuId, exerciseId = 27, orderInWorkout = 4, targetSets = 3, targetRepsMin = 10, targetRepsMax = 12, restSeconds = 60), // Incline DB Curl
                ProgrammeDayExerciseEntity(programmeDayId = thuId, exerciseId = 25, orderInWorkout = 5, targetSets = 3, targetRepsMin = 10, targetRepsMax = 12, restSeconds = 60), // Preacher Curl
                ProgrammeDayExerciseEntity(programmeDayId = thuId, exerciseId = 38, orderInWorkout = 6, targetSets = 3, targetRepsMin = 12, targetRepsMax = 15, restSeconds = 45), // Incline Hammer Curl
                ProgrammeDayExerciseEntity(programmeDayId = thuId, exerciseId = 40, orderInWorkout = 7, targetSets = 3, targetRepsMin = 15, targetRepsMax = 20, restSeconds = 45)  // Seated DB Wrist Extension
            )
        )

        // Fri: Back & Triceps (+ Abs) — Workout B
        val friId = programmeDao.insertProgrammeDay(
            ProgrammeDayEntity(
                programmeId = programmeId,
                dayOfWeek = 5,
                workoutName = "Back & Triceps",
                muscleGroups = "Back, Triceps, Abs",
                workoutVariant = "B",
                estimatedMinutes = 65,
                order = 5,
                notes = "Workout B"
            )
        )
        programmeDao.insertProgrammeDayExercises(
            listOf(
                ProgrammeDayExerciseEntity(programmeDayId = friId, exerciseId = 11, orderInWorkout = 1, targetSets = 3, targetRepsMin = 6, targetRepsMax = 10, restSeconds = 120),// Pull-ups
                ProgrammeDayExerciseEntity(programmeDayId = friId, exerciseId = 6, orderInWorkout = 2, targetSets = 4, targetRepsMin = 8, targetRepsMax = 12, restSeconds = 90),   // Neutral Grip Pulldown
                ProgrammeDayExerciseEntity(programmeDayId = friId, exerciseId = 8, orderInWorkout = 3, targetSets = 4, targetRepsMin = 8, targetRepsMax = 12, restSeconds = 90),   // Chest-Supported Wide Row
                ProgrammeDayExerciseEntity(programmeDayId = friId, exerciseId = 10, orderInWorkout = 4, targetSets = 3, targetRepsMin = 8, targetRepsMax = 10, restSeconds = 120),// Romanian Deadlift
                ProgrammeDayExerciseEntity(programmeDayId = friId, exerciseId = 23, orderInWorkout = 5, targetSets = 4, targetRepsMin = 8, targetRepsMax = 12, restSeconds = 90),  // Smith Machine Dip / JM Press
                ProgrammeDayExerciseEntity(programmeDayId = friId, exerciseId = 22, orderInWorkout = 6, targetSets = 3, targetRepsMin = 10, targetRepsMax = 15, restSeconds = 60), // Cross-Body Cable Pushdown
                ProgrammeDayExerciseEntity(programmeDayId = friId, exerciseId = 45, orderInWorkout = 7, targetSets = 3, targetRepsMin = 12, targetRepsMax = 15, restSeconds = 60), // Woodchopper
                ProgrammeDayExerciseEntity(programmeDayId = friId, exerciseId = 47, orderInWorkout = 8, targetSets = 3, targetRepsMin = 12, targetRepsMax = 15, restSeconds = 60)  // Overhead Cable Crunch
            )
        )

        // Sat: Legs & Shoulders (Hamstrings & Rear-delt focus) — Workout B
        val satId = programmeDao.insertProgrammeDay(
            ProgrammeDayEntity(
                programmeId = programmeId,
                dayOfWeek = 6,
                workoutName = "Legs & Shoulders (Hamstring & Rear-Delt)",
                muscleGroups = "Hamstrings, Rear Delts, Calves",
                workoutVariant = "B",
                estimatedMinutes = 70,
                order = 6,
                notes = "Hamstring & Rear-delt focus"
            )
        )
        programmeDao.insertProgrammeDayExercises(
            listOf(
                ProgrammeDayExerciseEntity(programmeDayId = satId, exerciseId = 32, orderInWorkout = 1, targetSets = 4, targetRepsMin = 8, targetRepsMax = 10, restSeconds = 120),// Barbell RDL
                ProgrammeDayExerciseEntity(programmeDayId = satId, exerciseId = 31, orderInWorkout = 2, targetSets = 4, targetRepsMin = 10, targetRepsMax = 15, restSeconds = 60), // Seated Leg Curl
                ProgrammeDayExerciseEntity(programmeDayId = satId, exerciseId = 29, orderInWorkout = 3, targetSets = 3, targetRepsMin = 10, targetRepsMax = 12, restSeconds = 90), // Hack Squat
                ProgrammeDayExerciseEntity(programmeDayId = satId, exerciseId = 34, orderInWorkout = 4, targetSets = 4, targetRepsMin = 15, targetRepsMax = 20, restSeconds = 60), // Calf Raise
                ProgrammeDayExerciseEntity(programmeDayId = satId, exerciseId = 35, orderInWorkout = 5, targetSets = 3, targetRepsMin = 1, targetRepsMax = 1, restSeconds = 90, notes = "30m Carry"), // Farmer's Carry
                ProgrammeDayExerciseEntity(programmeDayId = satId, exerciseId = 16, orderInWorkout = 6, targetSets = 4, targetRepsMin = 12, targetRepsMax = 20, restSeconds = 60), // 25° Incline Rear Delt Fly
                ProgrammeDayExerciseEntity(programmeDayId = satId, exerciseId = 19, orderInWorkout = 7, targetSets = 3, targetRepsMin = 10, targetRepsMax = 12, restSeconds = 90)  // Around the World
            )
        )

        // Sun: Active Recovery + Mobility
        val sunId = programmeDao.insertProgrammeDay(
            ProgrammeDayEntity(
                programmeId = programmeId,
                dayOfWeek = 7,
                workoutName = "Active Recovery & Mobility",
                muscleGroups = "Full Body Mobility & Core",
                workoutVariant = "Recovery",
                estimatedMinutes = 30,
                isRestDay = true,
                order = 7,
                notes = "8-10k Steps, Stomach Vacuum, 2-Min Malasana, Dead Hang, World's Greatest Stretch"
            )
        )
        programmeDao.insertProgrammeDayExercises(
            listOf(
                ProgrammeDayExerciseEntity(programmeDayId = sunId, exerciseId = 46, orderInWorkout = 1, targetSets = 3, targetRepsMin = 1, targetRepsMax = 1, restSeconds = 45, notes = "15-20s Vacuum Hold"),
                ProgrammeDayExerciseEntity(programmeDayId = sunId, exerciseId = 36, orderInWorkout = 2, targetSets = 2, targetRepsMin = 1, targetRepsMax = 1, restSeconds = 60, notes = "2-Min Deep Squat Hold"),
                ProgrammeDayExerciseEntity(programmeDayId = sunId, exerciseId = 13, orderInWorkout = 3, targetSets = 2, targetRepsMin = 1, targetRepsMax = 1, restSeconds = 60, notes = "45-60s Hang"),
                ProgrammeDayExerciseEntity(programmeDayId = sunId, exerciseId = 51, orderInWorkout = 4, targetSets = 3, targetRepsMin = 5, targetRepsMax = 5, restSeconds = 60, notes = "5 reps per side")
            )
        )
    }

    private suspend fun seedAchievements() {
        val achievements = listOf(
            AchievementEntity(id = "FIRST_WORKOUT", title = "First Rep", description = "Completed your first workout in FORGE", iconName = "bolt", xpReward = 100, isUnlocked = true, unlockedAt = System.currentTimeMillis(), category = "GENERAL"),
            AchievementEntity(id = "STREAK_3", title = "Ignition", description = "Reached a 3-day workout streak", iconName = "local_fire_department", xpReward = 50, category = "STREAK"),
            AchievementEntity(id = "STREAK_7", title = "Unstoppable", description = "Maintained a 7-day consistency streak", iconName = "local_fire_department", xpReward = 150, category = "STREAK"),
            AchievementEntity(id = "STREAK_30", title = "Forged in Iron", description = "30 consecutive days of discipline", iconName = "workspace_premium", xpReward = 500, category = "STREAK"),
            AchievementEntity(id = "FIRST_PR", title = "Breaking Limits", description = "Set your first Personal Record", iconName = "trending_up", xpReward = 75, category = "STRENGTH"),
            AchievementEntity(id = "VOLUME_10K", title = "10 Ton Club", description = "Moved over 10,000 kg total volume", iconName = "fitness_center", xpReward = 200, category = "VOLUME"),
            AchievementEntity(id = "DEAD_HANG_MASTER", title = "Iron Grip", description = "Completed 10 scheduled Dead Hangs", iconName = "pan_tool", xpReward = 100, category = "GENERAL"),
            AchievementEntity(id = "MOBILITY_HERO", title = "Supple Leopard", description = "Logged 5 Sunday recovery & mobility sessions", iconName = "self_improvement", xpReward = 100, category = "GENERAL")
        )
        consistencyDao.insertAchievements(achievements)
    }

    private suspend fun seedConsistency() {
        consistencyDao.upsertConsistency(
            ConsistencyEntity(
                id = 1L,
                currentStreak = 1,
                longestStreak = 1,
                totalWorkoutDays = 1,
                totalXp = 100,
                level = 1,
                levelTitle = "Apprentice",
                lastWorkoutDate = System.currentTimeMillis(),
                lastStreakDate = System.currentTimeMillis()
            )
        )
    }
}
