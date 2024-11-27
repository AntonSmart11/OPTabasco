package com.example.optabasco.firebase

import android.content.Context
import android.util.Log
import com.example.optabasco.R
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyFirebaseAuth {
    companion object {
        val scopes = listOf("https://www.googleapis.com/auth/firebase.messaging")
        val rawResourceId = R.raw.firebase_service_account

        suspend fun getAccessToken(context: Context): String? {
            return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.resources.openRawResource(rawResourceId)
                val credentials = GoogleCredentials.fromStream(inputStream).createScoped(scopes)
                credentials.refreshIfExpired()
                credentials.accessToken?.tokenValue
            } catch (e: Exception) {
                Log.e("MyFirebaseAuth", "Error obteniendo el token: ${e.message}", e)
                null
            }
            }
        }
    }
}