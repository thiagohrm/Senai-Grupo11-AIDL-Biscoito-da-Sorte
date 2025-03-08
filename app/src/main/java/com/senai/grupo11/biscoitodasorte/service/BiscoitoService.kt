package com.senai.grupo11.biscoitodasorte.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log

import com.senai.grupo11.biscoitodasorte.ICallback
import com.senai.grupo11.biscoitodasorte.IService

class BiscoitoService : Service(){

    private val TAG = "BiscoitoDaSorte.BiscoitoService"
    private val callbacks : RemoteCallbackList<ICallback> = RemoteCallbackList<ICallback>()
    private val listaSorte = listOf(
        "Hoje é seu dia de sorte!",
        "A vida é uma jornada, aproveite cada passo.",
        "A felicidade está ao seu alcance.",
        "Você encontrará a resposta em breve.",
        "Uma nova aventura está prestes a começar."
    )

    override fun onCreate() {
        Log.i(TAG, "onCreate()")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand()")
        return START_STICKY
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        super.onDestroy()
    }

    private val binder = object : IService.Stub(){
        override fun registerCallback(cb: ICallback?) {
            Log.i(TAG, "registerCallback()")
            cb?.let {
                Log.i(TAG, "registering callback...")
                callbacks.register(cb)
            }
        }

        override fun removeCallback(cb: ICallback?) {
            Log.i(TAG, "removeCallback()")
            callbacks.unregister(cb)
        }

        override fun requestText() {
            Log.i(TAG,"requestText()")
            randomFortune()
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    private fun randomFortune(){
        Log.i(TAG, "randomFortune()")
        val fortune = listaSorte.random()
        Log.i(TAG, "sorte: $fortune")
        try {
            val n: Int = callbacks.beginBroadcast()
            Log.i(TAG, "beginBroadcast - $n times")
            for (i in 0 until n) {
                callbacks.getBroadcastItem(i)
                    .receiveText(fortune)
                callbacks.finishBroadcast()
            }
        } catch (exception: RemoteException) {
            println(exception.message)
        }
    }
}