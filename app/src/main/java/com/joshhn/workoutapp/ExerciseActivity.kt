package com.joshhn.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.joshhn.workoutapp.databinding.ActivityExerciseBinding
import com.joshhn.workoutapp.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restDuration: Long = 15000
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseDuration: Long = 45000
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    //Add Text to Speech feature
    private var tts: TextToSpeech? = null

    //Add a Media Player to play sound
    private var player: MediaPlayer? = null

    //Add Exercise Adapter for RecyclerView
    private var exerciseAdapter: ExerciseStatusAdapter? = null

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
            customDialogForBackButton()
        }

        exerciseList = Constants.defaultExerciseList()

        //TextToSpeech
        tts = TextToSpeech(this, this)

        speakOut("Go")
        setupRestView()
        setupExerciseRecyclerView()
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
        super.onBackPressed()
    }

    private fun setupExerciseRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
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

        binding?.tvNextExercise?.text = exerciseList!![currentExercisePosition+1].getName()
        speakOut("Get ready for ${exerciseList!![currentExercisePosition+1].getName()}")
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
                speakOut("Go")
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
        exerciseAdapter!!.notifyDataSetChanged()

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

                exerciseList!![currentExercisePosition].setIsSelected(false )
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()

                if(currentExercisePosition < exerciseList!!.size -1){

                    //Play sound to stop
//                    try{
//                        val soundURI = Uri.parse("android.resource://com.joshhn.workoutapp/" + R.raw.stop_sound)
//                        player = MediaPlayer.create(applicationContext,soundURI)
//                        player?.isLooping = false
//                        player?.start()
//                    }catch (e: Exception){
//                        e.printStackTrace()
//                    }

                    setupRestView()
                }else{
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    finish()
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

        if (tts!= null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player!= null){
            player!!.stop()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            //set US English as language for tts
            val result = tts?.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The language specified is not supported!" )
            }else{
                Log.e("TTS", "Initialization Failed!")
            }
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}