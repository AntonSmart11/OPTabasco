package com.example.optabasco.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.optabasco.MainActivity
import com.example.optabasco.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Maneja el mensaje recibido
        Log.d("FCM", "Mensaje recibido: ${remoteMessage}")

        // Generar notificación si es necesario
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title ?: "Título predeterminado"
            val body = remoteMessage.notification!!.body ?: "Mensaje predeterminado"

            showNotificacion(title, body)

            Log.d("FCM", "Notificación: $title - $body")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token: $token")
    }

    // Función estática para obtener el token actual
    companion object {
        fun getToken(callback: (String?) -> Unit) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM", "Token obtenido: $token")
                    callback(token) // Devuelve el token a través del callback
                } else {
                    Log.e("FCM", "Error al obtener el token", task.exception)
                    callback(null) // Devuelve null si hubo un error
                }
            }
        }
    }

    private fun showNotificacion(title: String, message: String) {
        val channelId = "default_channel"
        val notificationId = 1

        // Intent para abrir la actividad principal al hacer clic
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Crear el canal de notificaciones
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Canal predeterminado",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Construir la notificación
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Mostrar la notificación
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}