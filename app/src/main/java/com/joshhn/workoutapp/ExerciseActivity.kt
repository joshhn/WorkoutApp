package com.joshhn.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.joshhn.workoutapp.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {

    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restDuration: Long = 3000
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseDuration: Long = 3000
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        //Add back Button to Toolbar
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        exerciseList = Constants.defaultExerciseList()

        setupRestView()
    }

    private fun setupRestView(){
        binding?.flExerciseProgressBar?.visibility = View.GONE
        binding?.ivExerciseImage?.visibility = View.GONE
        binding?.tvExercise?.visibility = View.GONE
        binding?.flRestProgressBar?.visibility = View.VISIBLE
        binding?.tvRest?.visibility = View.VISIBLE
        binding?.tvNextExercise?.visibility = View.VISIBLE


        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }

        if(currentExercisePosition+1 < exerciseList!!.size){
            binding?.tvNextExercise?.text = exerciseList!![currentExercisePosition+1].getName()
        }
        setRestProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.restProgressBar?.progress = restDuration.toInt()

        restTimer = object: CountDownTimer(restDuration,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.restProgressBar?.progress = ((restDuration/1000) - restProgress).toInt()
                binding?.tvRestTimer?.text = ((restDuration/1000) - restProgress).toString()
            }
            override fun onFinish() {
                currentExercisePosition++
                setupExerciseView()
            }
        }.start()

    }

    private fun setupExerciseView(){
        binding?.flRestProgressBar?.visibility = View.GONE
        binding?.tvRest?.visibility = View.GONE
        binding?.tvNextExercise?.visibility = View.GONE
        binding?.flExerciseProgressBar?.visibility = View.VISIBLE
        binding?.ivExerciseImage?.visibility = View.VISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE


        if(exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        binding?.ivExerciseImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExercise?.text = exerciseList!![currentExercisePosition].getName()
        exerciseList!![currentExercisePosition].setIsSelected(true)

        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar(){
        binding?.exerciseProgressBar?.progress = exerciseDuration.toInt()

        exerciseTimer = object: CountDownTimer(exerciseDuration,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.exerciseProgressBar?.progress = ((exerciseDuration/1000) - exerciseProgress).toInt()
                binding?.tvExerciseTimer?.text = ((exerciseDuration/1000) - exerciseProgress).toString()
            }
            override fun onFinish() {
                if(currentExercisePosition < exerciseList!!.size -1){
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    setupRestView()
                }else{
                    Toast.makeText(this@ExerciseActivity, "Done",Toast.LENGTH_LONG).show()
                }
            }
        }.start()

    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }

        if(exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        binding = null
    }
}