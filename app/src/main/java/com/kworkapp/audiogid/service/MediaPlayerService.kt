package com.kworkapp.audiogid.service

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayer
import com.kworkapp.audiogid.Constants
import com.kworkapp.audiogid.R
import com.kworkapp.audiogid.activity.CSource
import com.kworkapp.audiogid.activity.MainActivity
import com.kworkapp.audiogid.player.PlayerHandler
import com.kworkapp.audiogid.player.PlayerManager
import com.kworkapp.audiogid.service.ServiceHelper.isMyServiceRunning
import com.kworkapp.audiogid.ui.presentation.launcher.pojo.Item
import com.kworkapp.audiogid.utils.TextUtils.convertToMilliseconds
import com.walhalla.ui.BuildConfig
import com.walhalla.ui.DLog.d
import com.walhalla.ui.DLog.handleException
import com.walhalla.ui.DLog.i

class MediaPlayerService : Service(), PlayerManager.PlayerListener, PlayerHandler {
    private lateinit var pm: PlayerManager


    val position: Int
        //public static boolean IS_SERVICE_RUNNING = false;
        get() = Companion.position

    private var builder: NotificationCompat.Builder? = null
    private var mNotificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        pm = PlayerManager.getInstance(this)
        pm.setCallbackService(this)
        d("@@@@" + this.hashCode())
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            return START_STICKY
        }

        val startedFromNotification =
            intent.getBooleanExtra(Constants.EXTRA_STARTED_FROM_NOTIFICATION, false)
        if (startedFromNotification) {
            //stopSelf();
        }
        if ("STOP_SERVICE" == intent.action) {
            stopSelf()
            return START_NOT_STICKY
        } else if (Constants.ACTION.STARTFOREGROUND_ACTION == intent.action) {
            Log.i(TAG, "Received Start Foreground Intent ")
            showNotification(applicationContext)

            //Toast.makeText(this, "Service Started!", Toast.LENGTH_SHORT).show();
        } else if (Constants.ACTION.PREV_ACTION == intent.action) {
            Log.i(TAG, "Clicked Previous")
            Companion.position--
            playSongList(list)
            //            Toast.makeText(this, "Clicked Previous!", Toast.LENGTH_SHORT)
//                    .show();
        } else if (Constants.ACTION.PLAY_ACTION == intent.action) {
            val __tmp = intent.getParcelableArrayListExtra<Item>(Constants.EXTRA.ITEM_KEY)
            if (__tmp != null) {
                list.clear()
                list.addAll(__tmp)
                d("<----$__tmp")
            }
            if (intent.extras != null) {
                Companion.position = intent.extras!!
                    .getInt(Constants.EXTRA.PLAY_EXTRA, 0)
                playSongList(list)
                //Toast.makeText(this, "Clicked Play!", Toast.LENGTH_SHORT).show();
            }
        } else if (Constants.ACTION.NEXT_ACTION == intent.action) {
            this.nextSong()
        } else if (Constants.ACTION.STOPFOREGROUND_ACTION == intent.action) {
            i("Received Stop Foreground Intent")
            //manager.releaseService();
            stopForeground(true)
            stopSelf()
        }
        return START_STICKY
    }

    private fun playSongList(data: List<Item>?) {
        if (data == null || Companion.position == -1) {
            return
        }

        i("##############: " + (data == null) + " " + Companion.position + " " + data.toString())

        if (Companion.position > data.size - 1) {
            Companion.position = 0 //reset data to firest position
        }
        val stream = data[Companion.position]

        /**+++++++++++++++++++ IndexOfBoundsException */
        val startTime = convertToMilliseconds(stream.start!!)
        pm!!.seekToAndStartAndRunTimer0(startTime)

        //            for (Song song : data) {
//                Log.d(TAG, "onStartCommand: " + song);
//            }
        if (builder == null) {
            return
        }

        val name = stream.timingName
        val duration = stream.start

        Log.d(TAG, "playSongList: $name")

        //@@@@ builder.setContentText(name);

//        RemoteViews var2 = new RemoteViews(getPackageName(), R.layout.notification_small);
//        RemoteViews var3 = new RemoteViews(getPackageName(), R.layout.notification_large);
//        var2.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
//        var2.setTextViewText(R.id.title, duration);
//        var2.setTextViewText(R.id.text, name);
//        var3.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
//        var3.setTextViewText(R.id.title, duration);
//        var3.setTextViewText(R.id.text, name);
        builder!!.setStyle(NotificationCompat.DecoratedCustomViewStyle())
        //        builder.setCustomContentView(var2);
//        builder.setCustomBigContentView(var3);
        if (mNotificationManager == null) {
            mNotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        if (mNotificationManager != null && builder != null) {
            builder!!.setSmallIcon(NOTIFICATION_SMALL_ICON) //R.mipmap.ic_launcher
            val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            builder!!.setLargeIcon(icon)
            mNotificationManager!!.notify(
                Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                builder!!.build()
            )
        }


        //IllegalArgumentException: width and height must be > 0


        //0000
    }

    override fun nextSong() {
        Companion.position++
        playSongList(list)
        _d("Clicked Next!")
    }

    private fun _d(s: String) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
            Log.i(TAG, s + list.size + " :: " + Companion.position)
        }
    }

    private fun showNotification(context: Context) {
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()
        else {
            ""
        }
        val notificationIntent = Intent(
            this,
            MainActivity::class.java
        )
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION)
        notificationIntent.setFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }


        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, flags
        )

        val previousIntent = Intent(
            this,
            MediaPlayerService::class.java
        )
        previousIntent.setAction(Constants.ACTION.PREV_ACTION)
        val ppreviousIntent = PendingIntent.getService(
            this, 0,
            previousIntent, flags
        )


        //Center btn
//        Intent intent = new Intent(this, ForegroundService.class);
//        intent.setAction(Constants.ACTION.PLAY_ACTION);
//        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);
//        PendingIntent pplayIntent =
//                PendingIntent.getService(this, 0, intent, 0);


        //intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);
        val nextIntent = Intent(
            this,
            MediaPlayerService::class.java
        )
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION)
        val pnextIntent =
            PendingIntent.getService(this, 0, nextIntent, flags)

        val icon1 = BitmapFactory.decodeResource(
            resources,
            R.drawable.abc_none
        )

        //        Bitmap icon2 = BitmapFactory.decodeResource(getResources(), R.drawable.exo_controls_play);
        val title = context.getString(R.string.app_name)
        builder = NotificationCompat.Builder(
            this,
            channel
        ) //                .setColor(getColor(R.color.colorPrimary))
            //                .setColorized(true)
            .setContentTitle(title)


        //        RemoteViews var2 = new RemoteViews(getPackageName(), R.layout.notification_small);
//        RemoteViews var3 = new RemoteViews(getPackageName(), R.layout.notification_large);
//        var2.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
//        var2.setTextViewText(R.id.title, title);
//        var2.setTextViewText(R.id.text, title);


//        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
//        builder.setCustomContentView(var2);
//        builder.setCustomBigContentView(var3);
        builder!!.setPriority(Notification.PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setTicker(title)
            .setContentText(title) //.setContent(var3)
            .setSmallIcon(NOTIFICATION_SMALL_ICON) //.setLargeIcon(Bitmap.createScaledBitmap(icon2, 0, 0, false))//Bitmap.createScaledBitmap(icon, 128, 128, false)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
        //@@@         .addAction(android.R.drawable.ic_media_previous, context.getString(R.string.action_previous),ppreviousIntent)
        //@@@         .addAction(android.R.drawable.ic_media_play, context.getString(R.string.action_open), openIntent())
        //@@@         .addAction(android.R.drawable.ic_media_next, context.getString(R.string.action_next), pnextIntent)
        //@@@.addAction(android.R.drawable.ic_media_pause, getString(R.string.action_stop),getStopServicePendingIntent())
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, builder!!.build())
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }

    private fun openIntent(): PendingIntent {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }
        val openActivity = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), flags
        )
        return openActivity
    }

    private val stopServicePendingIntent: PendingIntent
        get() {
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_CANCEL_CURRENT
            }
            val stopIntent = Intent(
                this,
                MediaPlayerService::class.java
            )
            stopIntent.setAction("STOP_SERVICE")
            return PendingIntent.getService(this, 0, stopIntent, flags)
        }

    //    @Override
    //    public void onPause() {
    //        super.onPause();
    //        player.reset();
    //    }
    override fun onDestroy() {
        i("In onDestroy")
        pm!!.releaseService()
        //Toast.makeText(this, "Service Detroyed!", Toast.LENGTH_SHORT).show();
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @TargetApi(26)
    @Synchronized
    private fun createChannel(): String {
        mNotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val name = "snap map fake location "
        val importance = NotificationManager.IMPORTANCE_LOW

        val mChannel = NotificationChannel("snap map channel", name, importance)

        mChannel.enableLights(true)
        mChannel.lightColor = Color.BLUE
        if (mNotificationManager != null) {
            mNotificationManager!!.createNotificationChannel(mChannel)
        } else {
            stopSelf()
        }
        return "snap map channel"
    }

    override fun playerStateEnded(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == ExoPlayer.STATE_ENDED) { //code 4
            nextSong()
            Log.d(TAG, "########## $playbackState")
        }
    }

    override fun troubleReport(err: String) {
        d(err)
    }

    override fun updateTimer(currentPosition: Int) {
        d("@@@$currentPosition")
    }

    override fun setDescriptionRequest(
        currentItemIndex: Int,
        currentSectionIndex: Int,
        cSource: CSource
    ) {
        d("@@@$currentItemIndex,$currentItemIndex")
    }

    override fun isPlaySuccess() {
    }

    override fun isPauseSuccess() {
    }

    override fun onCompletion(mp: MediaPlayer) {
        //0
    } //    public class LocalBinder extends Binder {
    //        ForegroundService getService() {
    //            // Return this instance of LocalService so clients can call public methods
    //            return ForegroundService.this;
    //        }
    //    }
    //    @Override
    //    public IBinder onBind(Intent intent) {
    //        return mBinder;
    //    }

    companion object {
        private val NOTIFICATION_SMALL_ICON = R.mipmap.ic_launcher

        //All songs
        private val list = ArrayList<Item>() //Song or stream

        //private final IBinder mBinder = new LocalBinder();
        private val TAG: String = MediaPlayerService::class.java.simpleName
        fun play(context: Context, data: ArrayList<Item>?, position: Int) {
            try {
                //DLog.d("---->" + data.toString());
                val var0 = Intent(
                    context,
                    MediaPlayerService::class.java
                )
                var0.setAction(Constants.ACTION.PLAY_ACTION)
                var0.putExtra(Constants.EXTRA.PLAY_EXTRA, position)
                var0.putParcelableArrayListExtra(Constants.EXTRA.ITEM_KEY, data)
                //startService(var0);
                ContextCompat.startForegroundService(context, var0)
            } catch (e: Exception) {
                handleException(e)
            }
        }

        fun next(context: Context) {
            try {
                val var0 = Intent(
                    context,
                    MediaPlayerService::class.java
                )
                var0.setAction(Constants.ACTION.NEXT_ACTION)
                //startService(var0);
                ContextCompat.startForegroundService(context, var0)
            } catch (e: Exception) {
                handleException(e)
            }
        }

        private var position = 0

        fun playListOfStream(context: Context, songList: ArrayList<Item>, position: Int) {
            if (!isMyServiceRunning(context, MediaPlayerService::class.java)) {
                val intent = Intent(
                    context,
                    MediaPlayerService::class.java
                )
                intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION)
                intent.putExtra(Constants.EXTRA.PLAY_EXTRA, songList[position].start)
                //ForegroundService.IS_SERVICE_RUNNING = true;
                //button.setText("Stop Service");
                context.startService(intent)
            }
            play(context, songList, position)
        }
    }
}