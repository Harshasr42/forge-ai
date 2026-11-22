package com.forge.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.forge.app.MainActivity
import com.forge.app.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WorkoutTimerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var restCountDownTimer: CountDownTimer? = null

    companion object {
        const val CHANNEL_ID = "forge_workout_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START_WORKOUT = "ACTION_START_WORKOUT"
        const val ACTION_STOP_WORKOUT = "ACTION_STOP_WORKOUT"
        const val ACTION_START_REST = "ACTION_START_REST"
        const val ACTION_ADD_REST_TIME = "ACTION_ADD_REST_TIME"
        const val ACTION_SKIP_REST = "ACTION_SKIP_REST"

        const val EXTRA_REST_SECONDS = "EXTRA_REST_SECONDS"
        const val EXTRA_ADD_SECONDS = "EXTRA_ADD_SECONDS"
        const val EXTRA_WORKOUT_NAME = "EXTRA_WORKOUT_NAME"
        const val EXTRA_INITIAL_DURATION_SECONDS = "EXTRA_INITIAL_DURATION_SECONDS"

        private val _workoutDurationSeconds = MutableStateFlow(0L)
        val workoutDurationSeconds: StateFlow<Long> = _workoutDurationSeconds

        private val _restSecondsRemaining = MutableStateFlow(0)
        val restSecondsRemaining: StateFlow<Int> = _restSecondsRemaining

        private val _isRestActive = MutableStateFlow(false)
        val isRestActive: StateFlow<Boolean> = _isRestActive

        private val _isWorkoutActive = MutableStateFlow(false)
        val isWorkoutActive: StateFlow<Boolean> = _isWorkoutActive
    }

    private var durationJob: Job? = null
    private var workoutName: String = "Workout Session"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_WORKOUT -> {
                workoutName = intent.getStringExtra(EXTRA_WORKOUT_NAME) ?: "Workout Session"
                val initialDuration = intent.getLongExtra(EXTRA_INITIAL_DURATION_SECONDS, 0L)
                startWorkoutSession(initialDuration)
            }
            ACTION_STOP_WORKOUT -> {
                stopWorkoutSession()
            }
            ACTION_START_REST -> {
                val seconds = intent.getIntExtra(EXTRA_REST_SECONDS, 90)
                startRestTimer(seconds)
            }
            ACTION_ADD_REST_TIME -> {
                val addSec = intent.getIntExtra(EXTRA_ADD_SECONDS, 15)
                addRestSeconds(addSec)
            }
            ACTION_SKIP_REST -> {
                skipRestTimer()
            }
        }
        return START_STICKY
    }

    private fun startWorkoutSession(initialSeconds: Long = 0L) {
        _isWorkoutActive.value = true
        if (initialSeconds > 0L) {
            _workoutDurationSeconds.value = initialSeconds
        }
        try {
            val notification = buildNotification("Active — ${formatTime(_workoutDurationSeconds.value)}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ServiceCompat.startForeground(
                    this,
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (_: Exception) {
            // Guard against MissingForegroundServiceTypeException / ForegroundServiceStartNotAllowedException on Android 12-15
        }

        durationJob?.cancel()
        durationJob = serviceScope.launch {
            while (isActive) {
                delay(1000)
                _workoutDurationSeconds.value += 1
                updateNotification()
            }
        }
    }

    private fun stopWorkoutSession() {
        durationJob?.cancel()
        restCountDownTimer?.cancel()
        _isWorkoutActive.value = false
        _isRestActive.value = false
        _workoutDurationSeconds.value = 0L
        _restSecondsRemaining.value = 0
        try {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } catch (_: Exception) {}
        stopSelf()
    }

    private fun startRestTimer(seconds: Int) {
        restCountDownTimer?.cancel()
        _isRestActive.value = true
        _restSecondsRemaining.value = seconds

        restCountDownTimer = object : CountDownTimer((seconds * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _restSecondsRemaining.value = (millisUntilFinished / 1000).toInt()
                updateNotification()
            }

            override fun onFinish() {
                _isRestActive.value = false
                _restSecondsRemaining.value = 0
                vibrateAlert()
                updateNotification()
            }
        }.start()
    }

    private fun addRestSeconds(seconds: Int) {
        val current = _restSecondsRemaining.value
        val newDuration = current + seconds
        startRestTimer(newDuration)
    }

    private fun skipRestTimer() {
        restCountDownTimer?.cancel()
        _isRestActive.value = false
        _restSecondsRemaining.value = 0
        updateNotification()
    }

    private fun vibrateAlert() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createWaveform(longArrayOf(0, 200, 100, 200, 100, 300), -1)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 200, 100, 200, 100, 300), -1)
            }
        } catch (_: Exception) {}
    }

    private fun formatTime(seconds: Long): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return "%02d:%02d".format(mins, secs)
    }

    private fun updateNotification() {
        try {
            val subtext = if (_isRestActive.value) {
                "Resting: %02d:%02d | Total: %s".format(
                    _restSecondsRemaining.value / 60,
                    _restSecondsRemaining.value % 60,
                    formatTime(_workoutDurationSeconds.value)
                )
            } else {
                "Duration: %s".format(formatTime(_workoutDurationSeconds.value))
            }

            val notification = buildNotification(subtext)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_ID, notification)
        } catch (_: Exception) {}
    }

    private fun buildNotification(contentText: String): Notification {
        val openIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("FORGE — $workoutName")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_forge_splash)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        try {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Workout Active Session",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows live workout time and rest timer countdown"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        } catch (_: Exception) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        restCountDownTimer?.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
