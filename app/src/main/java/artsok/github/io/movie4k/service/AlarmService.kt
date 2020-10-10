package artsok.github.io.movie4k.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import artsok.github.io.movie4k.receiver.AlarmReceiver
import java.util.concurrent.atomic.AtomicInteger

class AlarmService(private val context: Context) {

    private val alarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    fun setExactAlarm(time: Long) {
        setAlarm(time, getPendingIntent(getIntent().apply {
            putExtra("EXACT_ALARM", time)
        }))
    }

    private fun setAlarm(time: Long, pendingIntent: PendingIntent) {
        alarmManager.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }
        }
    }

    private fun getIntent(): Intent = Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            RandomIntUtil.getRandomInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}

object RandomIntUtil {
    private val seed = AtomicInteger()
    fun getRandomInt() = seed.getAndIncrement() + System.currentTimeMillis().toInt()
}