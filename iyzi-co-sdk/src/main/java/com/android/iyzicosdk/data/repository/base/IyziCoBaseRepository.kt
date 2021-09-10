package com.android.iyzicosdk.data.repository.base

import com.android.iyzicosdk.callback.IyziCoServiceCallback
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal open class IyziCoBaseRepository {

    inline fun <reified T> execute(
        call: Call<IyziCoBaseResponse<T>>,
        callbackIyziCo: IyziCoServiceCallback<T>
    ) {
        call.enqueue(object : Callback<IyziCoBaseResponse<T>> {

            override fun onResponse(
                call: Call<IyziCoBaseResponse<T>>,
                response: Response<IyziCoBaseResponse<T>>
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        callbackIyziCo.onSuccess(response.body()?.data)
                    } else {
                        try {
                            callbackIyziCo.onError(
                                response.code(),
                                JSONObject(
                                    response.errorBody()?.string()
                                ).getString("consumerErrorMessage")
                            )
                        } catch (ex: Exception) {
                            callbackIyziCo.onError(response.code(), "Bir sorun oluştu")
                        }

                    }
                }
            }

            override fun onFailure(call: Call<IyziCoBaseResponse<T>>, t: Throwable) {
                if (t != null) callbackIyziCo.onError(500, t.localizedMessage)
            }

        })
    }

    inline fun <reified T> executeWithoutData(call: Call<T>,callbackIyziCo: IyziCoServiceCallback<T>) {
        call.enqueue(object:Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response != null) {
                    if (response.isSuccessful) {
                        callbackIyziCo.onSuccess(response.body())
                    } else {
                        try {
                            callbackIyziCo.onError(
                                response.code(),
                                JSONObject(
                                    response.errorBody()?.string()
                                ).getString("consumerErrorMessage")
                            )
                        } catch (ex: Exception) {
                            callbackIyziCo.onError(response.code(), "Bir sorun oluştu")
                        }

                    }
                }            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (t != null) callbackIyziCo.onError(500, t.localizedMessage)
            }


        })
    }
}