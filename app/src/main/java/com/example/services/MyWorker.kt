package com.example.services

import  android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception

class MyWorker(
    context: Context,
    parameters: WorkerParameters
): Worker(context,parameters){
    override fun doWork(): Result {
        val text = inputData.getString(Constant.KEY_USER_COMMENT_TEXT)

        return try {
            for (i in 0..200) {
                Log.i(Constant.TAG, "Emotion Analysing $i")
            }
            val outputData = Data.Builder()
                .putString(Constant.KEY_USER_EMOTION_RESULT, getEmotion(text))
                .build()
            Result.success(outputData)
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun getEmotion(text: String?) = listOf("Sad","Angry","Surprise","Tries","Bored").random()

}