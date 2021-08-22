package com.dicoding.consumerapp.receiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.consumerapp.R
import com.dicoding.consumerapp.activity.FavoriteActivity
import com.dicoding.consumerapp.activity.UserListActivity
import com.dicoding.consumerapp.fragment.MyPreferanceFragment
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient.log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_REPEATING = "Popular!"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        // Siapkan 2 id untuk 2 macam alarm, onetime dan repeating
        private const val ID_REPEATING = 101

        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"

    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val title = TYPE_REPEATING
        val notifId = ID_REPEATING

        //Jika Anda ingin menampilkan dengan toast anda bisa menghilangkan komentar pada baris dibawah ini.
//        showToast(context, title, message)

        if (message != null) {
            showAlarmNotification(context, title, message, notifId)
        }
    }

    // Gunakan metode ini untuk menampilkan toast

    private fun showToast(context: Context, title: String, message: String?) {
        Toast.makeText(context, "$title : $message", Toast.LENGTH_LONG).show()
    }

    // Gunakan metode ini untuk menampilkan notifikasi
    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {

        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"

        val intent = Intent(context, UserListActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.mark_github)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManagerCompat.notify(notifId, notification)

    }

    // Metode ini digunakan untuk menjalankan alarm repeating
    fun setRepeatingAlarm(context: Context, type: String, message: String) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(TYPE_REPEATING, type)
        val putExtra = intent.putExtra(EXTRA_TYPE, type)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 22)
        calendar.set(Calendar.MINUTE, 54)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        log.d("MyContext", context.toString())

        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show()
    }

    fun cancelRepeatingAlarm(context: Context, type: String) {
        log.d("MyContext", context.toString())
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, "Repeating alarm canceled", Toast.LENGTH_SHORT).show()
    }

    // Gunakan metode ini untuk mengecek apakah alarm tersebut sudah terdaftar di alarm manager
//    fun isAlarmSet(context: Context, type: String): Boolean {
//        val intent = Intent(context, AlarmReceiver::class.java)
//        val requestCode =
//            if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) ID_ONETIME else ID_REPEATING
//
//        return PendingIntent.getBroadcast(
//            context,
//            requestCode,
//            intent,
//            PendingIntent.FLAG_NO_CREATE
//        ) != null
//    }

    // Metode ini digunakan untuk validasi date dan time
    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}