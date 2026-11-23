package com.forge.app.domain

import com.forge.app.data.db.entities.ExerciseEntity
import com.forge.app.data.db.entities.ExerciseInstructionEntity

object ExerciseInstructionProvider {

    fun getInstruction(exercise: ExerciseEntity): ExerciseInstructionEntity {
        val specific = getSpecificInstruction(exercise.id, exercise.name)
        if (specific != null) return specific

        // Fallback generator based on muscle target and equipment
        return generateCategoryInstruction(exercise)
    }

    private fun getSpecificInstruction(id: Long, name: String): ExerciseInstructionEntity? {
        return when (id) {
            1L -> ExerciseInstructionEntity(
                exerciseId = 1,
                startingPosition = "Set adjustable bench to 30 degrees. Retract and pin shoulder blades into the pad. Plant feet firmly flat on the floor. Hold dumbbells with neutral or slight 45° inward angle at upper chest height.",
                executionSteps = "1. Press dumbbells upwards and slightly inward in a smooth arc until elbows are almost fully extended (do not lock out).\n2. Squeeze the upper clavicular chest fibres forcefully at the peak for 1 full second.\n3. Lower the weight under control over 3 seconds until you feel a deep stretch in the upper pectorals.",
                breathingTips = "Inhale deeply into the chest as you lower; exhale forcefully through pursed lips as you drive the dumbbells upward.",
                commonMistakes = "Setting bench higher than 30° (turns into a front delt press), flaring elbows out at 90° (causes shoulder impingement), letting dumbbells clang together at the top.",
                safetyTips = "Keep lower back naturally arched without lifting glutes off the bench. Drop weights outwards to floor safely if failure is reached."
            )
            2L -> ExerciseInstructionEntity(
                exerciseId = 2,
                startingPosition = "Lie flat on horizontal bench with eyes directly below where the dumbbells start. Pinch shoulder blades together and puff chest out. Plant heels firmly for leg drive.",
                executionSteps = "1. Drive dumbbells upward directly over mid-chest until elbows reach 95% extension.\n2. Contract sternal pectorals at the peak without losing shoulder retraction.\n3. Descend over a controlled 3-second negative until dumbbells reach chest level, maintaining a 45°-60° elbow angle to torso.",
                breathingTips = "Deep breath into diaphragm on the negative; exhale past the sticking point on the ascent.",
                commonMistakes = "Flaring elbows to 90 degrees, bouncing at the bottom, lifting head or hips off the bench.",
                safetyTips = "If training without a spotter, do not push to absolute technical failure on heavy sets; keep 1 rep in reserve."
            )
            3L -> ExerciseInstructionEntity(
                exerciseId = 3,
                startingPosition = "Adjust machine seat height so handles/pads align with mid-chest. Sit fully flush against back pad with chest upright and shoulders depressed.",
                executionSteps = "1. Maintain a slight soft bend in elbows; do not bend or straighten arms during the movement.\n2. Sweep arms inward across chest in a hugging motion, bringing hands together in front of sternum.\n3. Squeeze inner chest intensely for 1-2 seconds, then return slowly under cable tension feeling a full stretch across the chest.",
                breathingTips = "Exhale as you sweep hands together; inhale smoothly as handles open wide.",
                commonMistakes = "Bending elbows to turn the fly into a press, letting weight stack slam between reps.",
                safetyTips = "Do not over-stretch shoulders excessively far behind torso on the eccentric phase."
            )
            4L -> ExerciseInstructionEntity(
                exerciseId = 4,
                startingPosition = "Place hands on elevation blocks, handles, or bumper plates 4-6 inches high, slightly wider than shoulder width. Set body in rigid plank with glutes and core braced.",
                executionSteps = "1. Lower chest deep between the elevated blocks until you achieve a deficit stretch below hand level.\n2. Pause for 0.5s at full depth with total control.\n3. Push the floor away through palms, squeezing chest to full arm extension.",
                breathingTips = "Inhale on descent into the deficit; exhale as you press back up to plank.",
                commonMistakes = "Sagging lower back, flaring elbows out wide, rushing through the deficit stretch.",
                safetyTips = "Keep neck neutral; look 6 inches ahead on the floor rather than straight up."
            )
            5L -> ExerciseInstructionEntity(
                exerciseId = 5,
                startingPosition = "Set cable pulley to top height. Kneel on one knee facing the cable (knee on working side is down). Reach up and grip single handle with palm facing neutral.",
                executionSteps = "1. Initiate by depressing the scapula downwards before bending the elbow.\n2. Drive elbow down toward hip bone in a tight arc, keeping torso tall with a slight lateral lean.\n3. Pause and squeeze lower lat peak contraction for 1s, then allow cable to draw arm fully upward into deep lat stretch.",
                breathingTips = "Exhale as you drive elbow to hip; inhale deeply as lat stretches overhead.",
                commonMistakes = "Rotating torso to heave weight, pulling with bicep rather than leading with elbow.",
                safetyTips = "Engage core to prevent lumbar hyperextension during the pull."
            )
            6L -> ExerciseInstructionEntity(
                exerciseId = 6,
                startingPosition = "Attach V-bar or neutral grip attachment to lat pulldown cable. Sit with thighs securely locked under pads. Lean torso back 10-15°.",
                executionSteps = "1. Retract shoulder blades and pull elbows down and back towards ribs.\n2. Bring bar attachment down to touch upper chest/clavicle.\n3. Hold peak contraction for 1 second, then control weight up over 3 seconds to full extension.",
                breathingTips = "Exhale as the bar approaches chest; inhale as arms extend overhead.",
                commonMistakes = "Swinging torso back and forth to create momentum, cutting the top stretch short.",
                safetyTips = "Do not round upper back at the bottom of the pull."
            )
            7L -> ExerciseInstructionEntity(
                exerciseId = 7,
                startingPosition = "Attach straight bar or rope to high pulley. Stand 2-3 feet back with feet shoulder-width, slight knee bend, and hips hinged back 30 degrees.",
                executionSteps = "1. Keep arms nearly straight with only a slight soft elbow bend throughout.\n2. Contract lats to sweep bar down in an arc until it touches upper thighs.\n3. Squeeze lats hard at bottom for 1 second, then control bar back up to eye level.",
                breathingTips = "Exhale as bar sweeps down to thighs; inhale on the upward return.",
                commonMistakes = "Bending elbows to turn it into a tricep pushdown, shrugging shoulders up.",
                safetyTips = "Keep core braced to protect lumbar spine throughout the hinge."
            )
            8L -> ExerciseInstructionEntity(
                exerciseId = 8,
                startingPosition = "Adjust chest pad so upper chest sits above pad. Grip wide pronated handles. Plant feet firmly.",
                executionSteps = "1. Retract scapulae and pull elbows out wide at 70° to torso, driving elbows back.\n2. Squeeze rhomboids, mid-traps, and rear deltoids at peak contraction.\n3. Return forward with control until upper back rounds slightly for a complete stretch.",
                breathingTips = "Exhale as you pull elbows back; inhale as handles move forward.",
                commonMistakes = "Using momentum from hips, pulling with arms instead of retracting shoulder blades.",
                safetyTips = "Keep chest glued to support pad; do not arch off."
            )
            10L, 32L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Stand holding bar with overhand grip just outside thighs. Feet hip-width apart, knees slightly unlocked (soft knees). Shoulders pulled back and lats packed tight.",
                executionSteps = "1. Push hips straight back towards the wall behind you while maintaining soft knee angle.\n2. Trace bar closely down shins until you feel a maximal hamstring and glute stretch (usually mid-shin).\n3. Drive hips forward through heels to lock out, contracting glutes at the top.",
                breathingTips = "Take a big belly breath and brace core (Valsalva) at the top; exhale once past mid-shin on the way up.",
                commonMistakes = "Rounding lower back, turning it into a squat by bending knees too much, letting bar drift away from legs.",
                safetyTips = "Stop descent immediately when hips can no longer travel backwards. Keep spine completely neutral."
            )
            11L -> ExerciseInstructionEntity(
                exerciseId = 11,
                startingPosition = "Hang from pull-up bar with overhand grip slightly wider than shoulder width. Engage core and point toes down.",
                executionSteps = "1. Depress shoulder blades down into active hang.\n2. Drive elbows straight down towards ribs until chin clears the bar.\n3. Lower with control over 3 seconds to full dead hang stretch.",
                breathingTips = "Exhale on the pull; inhale as you lower.",
                commonMistakes = "Kicking legs (kipping), half reps without locking out bottom stretch.",
                safetyTips = "If unable to complete full reps, use resistance band or perform slow eccentric negatives."
            )
            13L, 14L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Grip bar (or towels draped over bar) with full overhand grip. Suspend entire bodyweight with feet off the floor.",
                executionSteps = "1. Relax shoulders, neck, and spine allowing gravity to gently decompress lumbar vertebrae.\n2. Maintain active forearm grip engagement while keeping breathing calm and rhythmic.\n3. Hold for target duration (45-60 seconds) without letting feet touch.",
                breathingTips = "Slow, diaphragmatic belly breathing (4 seconds in, 4 seconds out).",
                commonMistakes = "Holding breath, tensing traps up to ears.",
                safetyTips = "Step onto a box safely when finishing; do not drop from height onto stiff legs."
            )
            15L -> ExerciseInstructionEntity(
                exerciseId = 15,
                startingPosition = "Set cable pulley at hip/wrist level. Stand side-on to cable. Reach across body and grab handle with outside hand. Slight forward torso lean.",
                executionSteps = "1. Raise arm diagonally forward in the scapular plane (30° forward) leading with elbow.\n2. Lift until upper arm is parallel to floor at shoulder height.\n3. Lower slowly over 3 seconds under constant cable resistance.",
                breathingTips = "Exhale as arm raises; inhale as cable pulls arm back across body.",
                commonMistakes = "Shrugging neck to recruit upper traps, swinging torso for momentum.",
                safetyTips = "Do not raise arm above shoulder height to avoid subacromial impingement."
            )
            16L, 17L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Set bench to 25° incline or set machine handles to rear delt position. Keep chest supported and palms facing each other.",
                executionSteps = "1. With elbows slightly bent, fly arms backward and outward in horizontal plane.\n2. Squeeze rear delts intensely at the top for 1 full second.\n3. Control the descent back together without resting at bottom.",
                breathingTips = "Exhale as arms sweep back; inhale on return.",
                commonMistakes = "Bending elbows too much (turns into a row), pulling with mid-traps instead of rear delts.",
                safetyTips = "Keep neck neutral; do not crane head forward."
            )
            18L -> ExerciseInstructionEntity(
                exerciseId = 18,
                startingPosition = "Sit on upright bench with back support. Hold dumbbells at shoulder height, palms forward, elbows under wrists at 75°.",
                executionSteps = "1. Press dumbbells overhead in a smooth upward path until arms are fully extended.\n2. Avoid clanging weights together at top.\n3. Lower under control over 3 seconds until dumbbells reach ear/shoulder level.",
                breathingTips = "Inhale at the bottom; exhale as you push past the forehead level.",
                commonMistakes = "Excessively arching lower back, pressing weights too far forward.",
                safetyTips = "Keep core braced against bench pad throughout the entire press."
            )
            20L, 22L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Stand facing cable pulley with straight bar or independent cables. Pin elbows to side of ribcage. Hinge slightly forward at hips.",
                executionSteps = "1. Extend elbows downwards by contracting triceps until arms are fully locked.\n2. Squeeze lateral and medial heads at peak lockout for 1 full second.\n3. Allow forearms to rise with control until elbows reach 90 degrees.",
                breathingTips = "Exhale on downward push; inhale on the controlled upward return.",
                commonMistakes = "Allowing elbows to flare forward and back, using shoulders to push the weight down.",
                safetyTips = "Keep wrists neutral; avoid bending wrists backward under load."
            )
            21L -> ExerciseInstructionEntity(
                exerciseId = 21,
                startingPosition = "Set cable to mid-height or overhead. Hold rope or straight bar overhead with elbows pointed forward and bent at 90 degrees behind head.",
                executionSteps = "1. Keep upper arms stationary beside head.\n2. Extend elbows forward and upward, fully locking out tricep long head.\n3. Return under control over 3 seconds feeling a deep stretch along the back of the arm.",
                breathingTips = "Exhale on extension; inhale on the eccentric stretch.",
                commonMistakes = "Flaring elbows wide outward, arching lower spine excessively.",
                safetyTips = "Keep glutes and core braced to maintain spine alignment."
            )
            24L -> ExerciseInstructionEntity(
                exerciseId = 24,
                startingPosition = "Set single cable pulley low. Stand facing away from the machine. Grip handle and step forward so working arm is drawn behind torso into shoulder hyperextension.",
                executionSteps = "1. Lock elbow behind torso line to maintain long-head stretch.\n2. Curl forearm up towards shoulder without allowing elbow to drift forward.\n3. Squeeze bicep peak for 1 second, then lower over 3 controlled seconds to full stretch behind hip.",
                breathingTips = "Exhale on the curl; inhale deeply on the stretch.",
                commonMistakes = "Swinging the elbow forward during curl to recruit front deltoid.",
                safetyTips = "Use moderate load to protect the bicep tendon at full stretch."
            )
            25L, 27L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Sit at preacher bench or lie back on 45° incline bench. Allow arms to hang fully with triceps flush against pad/gravity.",
                executionSteps = "1. Curl weight upward through full bicep contraction without lifting elbows off pad.\n2. Squeeze hard at the peak for 1s.\n3. Lower under strict 3-second negative until arm is almost fully extended.",
                breathingTips = "Exhale on the upward curl; inhale as you lower.",
                commonMistakes = "Hyperextending and jerking at the bottom of preacher pad.",
                safetyTips = "Never drop weight quickly to the bottom of the pad; decelerate smoothly."
            )
            26L, 38L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Stand or sit on incline bench holding dumbbells with neutral grip (palms facing inward towards each other).",
                executionSteps = "1. Keeping palm neutral, curl dumbbell across torso towards opposite chest/shoulder.\n2. Squeeze the brachialis and brachioradialis at top contraction for 1s.\n3. Lower with control along the same path to full arm hang.",
                breathingTips = "Exhale on curl; inhale on descent.",
                commonMistakes = "Using hip swing or shoulder elevation to assist.",
                safetyTips = "Maintain a tight wrist position throughout the rep."
            )
            28L, 29L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Position bar across mid-traps or lean into hack squat back pad. Place feet shoulder-width, slightly ahead of bar line. Disengage safety catches.",
                executionSteps = "1. Descend smoothly by pushing hips back and bending knees tracking over second toe.\n2. Hit depth at parallel or below with hips lower than knees.\n3. Drive heels through floor and extend hips and knees simultaneously to stand.",
                breathingTips = "Big belly breath (Valsalva) before descending; exhale past halfway on ascent.",
                commonMistakes = "Knees caving inwards (valgus collapse), lifting heels off plate.",
                safetyTips = "Always set safety pins 2 inches below target parallel depth."
            )
            30L -> ExerciseInstructionEntity(
                exerciseId = 30,
                startingPosition = "Sit with back firmly against pad. Position shin roller just above ankles. Align knee joint directly with the machine's pivot axis.",
                executionSteps = "1. Drive shins upward by contracting quadriceps until legs are fully extended.\n2. Lock out and squeeze rectus femoris hard at the top for 1 full second.\n3. Lower the weight slowly over 3 seconds without letting plates touch.",
                breathingTips = "Exhale on upward kick; inhale on the downward negative.",
                commonMistakes = "Kicking with momentum, sitting forward away from back support.",
                safetyTips = "Do not hyperextend knees aggressively; control the lockout."
            )
            31L -> ExerciseInstructionEntity(
                exerciseId = 31,
                startingPosition = "Sit with thigh pad securely clamped down above knees. Place lower legs on pad just below calf bellies.",
                executionSteps = "1. Dorsiflex ankles (toes pulled up towards shins).\n2. Curl heels back under thighs forcefully by contracting hamstrings.\n3. Squeeze peak contraction for 1s, then return over 3 controlled seconds to full extension.",
                breathingTips = "Exhale as you curl heels under; inhale as legs extend.",
                commonMistakes = "Arching lower back off the pad, rushing through the extension.",
                safetyTips = "Ensure thigh pad is tight to prevent hip lifting during heavy curls."
            )
            34L -> ExerciseInstructionEntity(
                exerciseId = 34,
                startingPosition = "Place balls of feet on edge of calf block with heels hanging free. Position shoulder pads securely. Stand tall with knees straight but unlocked.",
                executionSteps = "1. Lower heels deep below block level feeling a full stretch in Achilles and calves for 1s.\n2. Drive through big toes to elevate heels as high as possible into full plantarflexion.\n3. Hold peak calf contraction for 2 full seconds before lowering.",
                breathingTips = "Inhale on the deep stretch; exhale as you press up onto toes.",
                commonMistakes = "Bouncing quickly at the bottom, bending knees to recruit quads.",
                safetyTips = "Always pause for 1 second at the bottom to eliminate the stretch reflex."
            )
            35L -> ExerciseInstructionEntity(
                exerciseId = 35,
                startingPosition = "Stand between heavy dumbbells or trap bar. Squat down with flat back to grip weights firmly. Stand upright with chest high and shoulders packed.",
                executionSteps = "1. Walk forward with short, deliberate, controlled steps.\n2. Keep torso completely upright with zero side-to-side torso sway.\n3. Maintain crushing grip on handles throughout the distance.",
                breathingTips = "Short, shallow rhythmic breaths while maintaining tight core brace.",
                commonMistakes = "Looking down at feet, slouching shoulders forward, rushing steps.",
                safetyTips = "Keep weights close to legs; avoid letting them swing."
            )
            37L -> ExerciseInstructionEntity(
                exerciseId = 37,
                startingPosition = "Stand holding EZ-bar with pronated (overhand) grip on outer bends. Elbows pinned close to ribs.",
                executionSteps = "1. Curl bar upward towards upper chest keeping palms facing down.\n2. Squeeze the brachioradialis forearm muscle hard at the top.\n3. Lower with 3-second negative feeling intense forearm engagement.",
                breathingTips = "Exhale as bar rises; inhale on descent.",
                commonMistakes = "Bending wrists backward (extending wrists under load).",
                safetyTips = "Keep wrists rigid and locked in line with forearms."
            )
            39L, 40L, 41L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Sit on bench with forearms resting flat on thighs or bench pad, wrists extending 2 inches off the edge.",
                executionSteps = "1. Allow weight to roll down to fingertips (for curls) or wrists to drop into full extension.\n2. Flex wrists upward smoothly through full range of motion.\n3. Squeeze for 1 second at top contraction before lowering with control.",
                breathingTips = "Exhale on wrist flexion/extension; inhale as wrists return.",
                commonMistakes = "Lifting elbows or forearms off the support surface.",
                safetyTips = "Use moderate weight and strict form to protect delicate wrist ligaments."
            )
            42L, 47L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Kneel beneath cable pulley holding rope attachment at temples/forehead. Hinge slightly at hips.",
                executionSteps = "1. Fix hips in place; do not sit back onto heels.\n2. Flex spine by drawing ribcage down towards pelvis using abdominal contraction.\n3. Crunch until elbows touch knees, pause for 1s, then return slowly to starting position.",
                breathingTips = "Exhale all air out as you crunch; inhale as you extend spine back up.",
                commonMistakes = "Sitting back onto heels to use bodyweight instead of abs.",
                safetyTips = "Keep hands glued to temples throughout; do not pull with arms."
            )
            43L, 44L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Hang from pull-up bar or support forearms on captain's chair pads. Legs straight or slightly bent.",
                executionSteps = "1. Initiate by tilting pelvis backwards (posterior pelvic tilt).\n2. Roll knees and pelvis up towards chest until glutes face forward.\n3. Pause and squeeze lower abs, then lower legs with strict 3-second control without swinging.",
                breathingTips = "Exhale forcefully as knees rise; inhale on controlled descent.",
                commonMistakes = "Swinging legs with momentum, arching lower back at the bottom.",
                safetyTips = "Do not allow legs to drop quickly; control the eccentric phase completely."
            )
            45L -> ExerciseInstructionEntity(
                exerciseId = 45,
                startingPosition = "Set cable high. Stand side-on to machine with feet wider than shoulders. Grip handle with both hands over outside shoulder.",
                executionSteps = "1. Rotate torso diagonally downward across body towards opposite knee.\n2. Pivot on back foot while driving motion through oblique contraction.\n3. Return under control along same diagonal path.",
                breathingTips = "Exhale as you chop down; inhale on the return.",
                commonMistakes = "Pulling only with arms instead of rotating through core and hips.",
                safetyTips = "Keep knees slightly bent to protect knee joints during rotation."
            )
            46L -> ExerciseInstructionEntity(
                exerciseId = 46,
                startingPosition = "Stand, kneel, or lie on back. Place hands on hips. Exhale all air completely out of lungs.",
                executionSteps = "1. Without inhaling, pull belly button in towards spine as far as possible.\n2. Hold this vacuum contraction intensely for 15-20 seconds.\n3. Release slowly, take a few normal recovery breaths, and repeat.",
                breathingTips = "Performed completely on empty lungs; breathe normally between sets.",
                commonMistakes = "Taking shallow breaths during the hold, tensing shoulders.",
                safetyTips = "Best performed on an empty stomach first thing in the morning."
            )
            48L, 49L, 50L -> ExerciseInstructionEntity(
                exerciseId = id,
                startingPosition = "Kneel with ab wheel in front of you or assume plank on forearms with body aligned in rigid line.",
                executionSteps = "1. Slowly roll wheel forward (or hold plank), extending body while squeezing glutes and abs.\n2. Reach as far forward as possible while maintaining neutral spine without dipping lower back.\n3. Pull wheel back by contracting abs and lats to return to start.",
                breathingTips = "Inhale as you roll out; exhale forcefully as you pull back.",
                commonMistakes = "Allowing lower back to hyperextend/sag towards floor.",
                safetyTips = "Only roll out as far as you can maintain a rigid, neutral lumbar spine."
            )
            51L -> ExerciseInstructionEntity(
                exerciseId = 51,
                startingPosition = "Assume a deep lunge position with left foot forward and right knee slightly off or on the floor. Place both hands inside left foot.",
                executionSteps = "1. Reach left elbow down toward left instep feeling a deep hip and groin stretch.\n2. Rotate left arm up toward ceiling, following hand with eyes to open thoracic spine.\n3. Place hand back down, push hips back to straighten front leg into hamstring stretch.",
                breathingTips = "Deep, relaxed rhythmic breaths throughout each position.",
                commonMistakes = "Rushing through positions without holding the stretch for 3-5 seconds.",
                safetyTips = "Keep movements smooth and continuous; do not bounce."
            )
            else -> null
        }
    }

    private fun generateCategoryInstruction(exercise: ExerciseEntity): ExerciseInstructionEntity {
        val target = exercise.muscleTarget
        val equipment = exercise.equipment
        return ExerciseInstructionEntity(
            exerciseId = exercise.id,
            startingPosition = "Set up with proper posture using $equipment. Position body with spine neutral, feet planted securely, and grip aligned with $target.",
            executionSteps = "1. Initiate the movement with strict form, contracting the $target through full range of motion.\n2. Hold peak contraction for 1 full second with intense mind-muscle focus.\n3. Return under strict 3-second eccentric control feeling a deep muscular stretch.",
            breathingTips = "Inhale deeply during the eccentric lowering phase; exhale forcefully past the sticking point on the concentric drive.",
            commonMistakes = "Using excessive momentum or body sway, cutting range of motion short, failing to control the negative.",
            safetyTips = "Warm up progressively before working sets. Maintain core engagement and joint alignment throughout."
        )
    }
}
