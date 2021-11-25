package com.example.services

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.*
import com.example.services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonEmotionAnalysis.setOnClickListener {
            val userText = binding.editTextUserComment.text?.toString() ?: ""
            setOneTimeEmotionAnalysisRequest(userText)
        }
    }

    private fun setOneTimeEmotionAnalysisRequest(userText: String) {
        val workManager = WorkManager.getInstance(this)

        val data = Data.Builder()
            .putString(Constant.KEY_USER_COMMENT_TEXT, userText)
            .build()

        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val emotionalAnalysis = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(data)
            .setConstraints(constraint)
            .build()

        workManager.enqueue(emotionalAnalysis)


        workManager.getWorkInfoByIdLiveData(emotionalAnalysis.id)
            .observe(this, {
                binding.textViewWorkState.text = it.state.name
                if (it.state == WorkInfo.State.SUCCEEDED) {
                    val userEmotionalResult =
                        it.outputData.getString(Constant.KEY_USER_EMOTION_RESULT)
                    Toast.makeText(this, userEmotionalResult, Toast.LENGTH_SHORT).show()
                }
            })
    }

}