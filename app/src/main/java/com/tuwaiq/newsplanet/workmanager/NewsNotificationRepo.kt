package com.tuwaiq.newsplanet.workmanager

import java.util.concurrent.TimeUnit

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.tuwaiq.newsplanet.ui.NewsActivity

class NewsNotificationRepo () {
    private val list = listOf("There are new articles !" , "Check the latest Sports news",
    "Check the latest Business news" , "Check the latest Technology news" ,
        "Check the latest Technology news" , "Check the latest Science news"
    ).random()
    fun myNotification(mainActivity: NewsActivity){
        val myWorkRequest= PeriodicWorkRequest
            .Builder(NewsWorker::class.java,15,TimeUnit.MINUTES)
            .setInputData(workDataOf(
                "title" to "NewsPlanet",
                "message" to list)
            )
            .build()
        WorkManager.getInstance(mainActivity).enqueueUniquePeriodicWork(
            "periodicStockWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            myWorkRequest
        )
    }
}