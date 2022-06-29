package com.joshhn.workoutapp

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()

        val jumpingJacks = ExerciseModel(1,"Jumping Jacks", R.drawable.ic_jumping_jacks)
        exerciseList.add(jumpingJacks)

        val wallSit = ExerciseModel(2,"Wall Sit", R.drawable.ic_wall_sit)
        exerciseList.add(wallSit)

        val pushUp = ExerciseModel(3,"Push Up", R.drawable.ic_push_up)
        exerciseList.add(pushUp)

        val abdominalCrunch = ExerciseModel(4,"Abdominal Crunch", R.drawable.ic_abdominal_crunch)
        exerciseList.add(abdominalCrunch)

        val stepUpOntoChair = ExerciseModel(5,"Step Up Onto Chair", R.drawable.ic_step_up_onto_chair)
        exerciseList.add(stepUpOntoChair)

        val squat = ExerciseModel(6,"Squat", R.drawable.ic_squat)
        exerciseList.add(squat)

        val highKnees = ExerciseModel(7,"High Knees", R.drawable.ic_high_knees_running_in_place)
        exerciseList.add(highKnees)

        val lunge = ExerciseModel(8,"Lunge", R.drawable.ic_lunge)
        exerciseList.add(lunge)

        val plank = ExerciseModel(9,"Plank", R.drawable.ic_plank)
        exerciseList.add(plank)

        val sidePlank = ExerciseModel(10,"Side Plank", R.drawable.ic_side_plank)
        exerciseList.add(sidePlank)

        val pushUpAndRotation = ExerciseModel(11,"Push Up and Rotation", R.drawable.ic_push_up_and_rotation)
        exerciseList.add(pushUpAndRotation)

        val triceptsDip = ExerciseModel(12,"Triceps Dip", R.drawable.ic_triceps_dip_on_chair)
        exerciseList.add(triceptsDip)

        return exerciseList
    }
}