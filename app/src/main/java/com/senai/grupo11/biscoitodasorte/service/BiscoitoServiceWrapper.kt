package com.senai.grupo11.biscoitodasorte.service

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.senai.grupo11.biscoitodasorte.ICallback
import com.senai.grupo11.biscoitodasorte.IService

class BiscoitoServiceWrapper {

    private val TAG = "BiscoitoDaSorte.BiscoitoServiceWrapper"


    private var remoteService : IService? = null
    private var _state = MutableLiveData(false)
    var state : LiveData<Boolean> = _state
    private var _textoDaSorte = MutableLiveData("")
    var textoDaSorte : LiveData<String> = _textoDaSorte

    private val callback = object : ICallback.Stub(){
        override fun receiveText(string: String?) {
            string?.let {
                _textoDaSorte.postValue(it)
            }
        }

        override fun state(state: Boolean) {
            Log.i(TAG,"state($state)")
            if (_state.value != state){
                _state.postValue(state)
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "onServiceConnected()")
            remoteService = IService.Stub.asInterface(service)
            try {
                remoteService?.registerCallback(callback)
                _state.value = true
            } catch (exception: RemoteException) {
                println(exception.message)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "onServiceDisconnected()")
            try {
                remoteService?.removeCallback(callback)
                _state.value = false
            } catch (exception: RemoteException) {
                println(exception.message)
            }
            remoteService = null
        }
    }

    fun bind(application: Application) {
        Log.i(TAG, "bind()")
        application.bindService(
            Intent(application, BiscoitoService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun unbind(application: Application) {
        Log.i(TAG, "unbind()")
        try {
            remoteService?.removeCallback(callback)
            _state.value = false
        } catch (exception: RemoteException) {
            println(exception.message)
        }
        application.unbindService(serviceConnection)
    }

    fun requestText(){
        Log.i(TAG,"requestText()")
        if (state.value == true) {
            Log.i(TAG,"requesting text...")
            remoteService?.requestText()
        }
    }

}