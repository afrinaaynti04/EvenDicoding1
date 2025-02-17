package com.afrina.eventdicoding.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrina.eventdicoding.data.response.DetailData
import com.afrina.eventdicoding.data.response.EventsResponse
import com.afrina.eventdicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModelFinish : ViewModel() {

    private val _listEvents = MutableLiveData<List<DetailData>>()
    val listEvent : LiveData<List<DetailData>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        const val TAG = "MainViewModel"
    }
    init{
        findEventVertical()

    }

    fun searchFinishedEvents(active : Int,limit : Int, query : String?) {
        _isLoading.value = true

        ApiConfig.getApiService().getEvents(active = active,limit = limit,query = query)
            .enqueue(object : Callback<EventsResponse> {
                override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>) {
                    _isLoading.value = false

                    if(response.isSuccessful) {
                        _listEvents.value = response.body()?.data
                    }
                }

                override fun onFailure(call: Call<EventsResponse>, response: Throwable) {
                    _isLoading.value = false
                }
            })
    }
    private fun findEventVertical() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(active = 0, limit = 40)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response : Response<EventsResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _listEvents.value = response.body()?.data
                }
                    else{
                        Log.e(TAG, "onFailure : ${response.message()}")
                    }
                }
            override fun onFailure(error : Call<EventsResponse>, msg : Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${msg.message}")
            }
        })
    }
}