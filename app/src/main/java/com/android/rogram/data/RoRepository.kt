package com.android.rogram.data

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException

class RoRepository {

    suspend fun fetchRoData() : List<RoData> {
        val data = CompletableDeferred<List<RoData>>()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL)
            .build()

        withContext(Dispatchers.IO) {
            client.run {
                newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        NetworkState.Error<Nothing>(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val dataResponse = response.body?.string()
                        if (dataResponse != null) {
                            parseToMoshi(dataResponse)?.let {
                                NetworkState.Success(data.complete(it))
                            }
                        }
                    }
                })
            }
        }
        Log.d("REPO", data.toString())
        return data.await()
    }


    fun parseToMoshi(jsonString: String): List<RoData>? {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val type = Types.newParameterizedType(
            List::class.java,
            RoData::class.java
        )
        val adapter: JsonAdapter<List<RoData>> =
            moshi.adapter(type)
        return adapter.fromJson(jsonString)
    }

    companion object {
        const val URL = "https://jsonplaceholder.typicode.com/album/1/photos"
    }
}