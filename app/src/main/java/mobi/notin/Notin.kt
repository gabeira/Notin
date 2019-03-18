package mobi.notin

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat


class Notin {

    fun scheduleNotification(
        context: Context,
        delay: Long,
        notificationId: Int
    ) {//delay is after how much time(in millis) from current time you want to schedule the notification
        val builder = NotificationCompat.Builder(context)
            .setContentTitle(context.getString(R.string.title))
            .setContentText(context.getString(R.string.content))
            .setAutoCancel(true)
            .setChannelId("12333")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon((context.resources.getDrawable(android.R.drawable.arrow_up_float) as BitmapDrawable).bitmap)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        val intent = Intent(context, SettingsActivity::class.java)
        val activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setContentIntent(activity)

        val notification = builder.build()


        val notificationIntent = Intent(context, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        val pendingIntent =
            PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel("12333", "Canal Notificacao", importance)
            channel.setDescription("Descricao do canal notif")
            notificationManager.createNotificationChannel(channel)
        }
//        val notification = intent.getParcelableExtra<Notification>(MyNotificationPublisher.NOTIFICATION)
//        val notificationId = intent.getIntExtra(MyNotificationPublisher.NOTIFICATION_ID, 0)
//        notificationManager.notify(123, notification)
    }
}