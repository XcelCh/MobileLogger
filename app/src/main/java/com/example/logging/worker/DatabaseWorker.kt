package com.example.logging.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory
import com.amazonaws.regions.Regions
import com.example.logging.lambda.MyInterface
import com.example.logging.lambda.RequestClass
import com.example.logging.lambda.ResponseClass
import java.io.File
import java.io.FileInputStream

class DatabaseWorker(private val ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        putItem(inputData.getString("Latitude"), inputData.getString("Longitude")
            ,inputData.getString("Altitude"), inputData.getString("Date"), inputData.getString("Time"))

        return Result.success()
    }

    private fun putItem(lat: String?, lon: String?, alt: String?, date: String?, time: String?){
        // Create an instance of CognitoCachingCredentialsProvider
        val cognitoProvider = CognitoCachingCredentialsProvider(
            this.applicationContext,
            "",
            Regions.US_EAST_1
        )

        // Create LambdaInvokerFactory, to be used to instantiate the Lambda proxy.
        val factory = LambdaInvokerFactory.builder()
            .context(this.applicationContext)
            .region(Regions.US_EAST_1)
            .credentialsProvider(cognitoProvider)

        val file = File(ctx.filesDir, "appID")
        val id = FileInputStream(file).bufferedReader().use { it.readText() }

        // Create the Lambda proxy object with a default Json data binder.
        // You can provide your own data binder by implementing
        // LambdaDataBinder.
        val request = RequestClass("PhoneId", id, "", lat, lon,
            alt, date, time)

        val response: ResponseClass? = factory.build().build(MyInterface::class.java)?.writeDynamoDB(request)

        Log.d("InvokeLambda", response.toString())
    }
}