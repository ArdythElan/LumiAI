package com.example.lumiai.data.network

import com.example.lumiai.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://openrouter.ai/api/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val apiKey = BuildConfig.OPENROUTER_API_KEY
            android.util.Log.d("API_KEY_CHECK", "API_KEY = $apiKey")

            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("HTTP-Referer", "https://www.example.com")
                .addHeader("X-Title", "Lumi AI")
                .build()

            // debug: log alle headers die je meestuurt
            for ((name, value) in request.headers) {
                android.util.Log.d("HEADER_CHECK", "$name: $value")
            }

            chain.proceed(request)
        }
        .build()

    val api: AiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AiApiService::class.java)
    }
}
