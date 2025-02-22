package com.kworkapp.audiogid.player

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Handler
import android.util.Log
import com.kworkapp.audiogid.R
import com.kworkapp.audiogid.activity.CSource
import com.kworkapp.audiogid.ui.presentation.launcher.pojo.Item
import com.kworkapp.audiogid.ui.presentation.launcher.pojo.Section
import com.kworkapp.audiogid.utils.TextUtils.convertToMilliseconds
import com.kworkapp.audiogid.utils.TextUtils.timingAndDescription
import com.walhalla.ui.DLog.handleException

class PlayerManager private constructor(context: Context) {
    var isPlaying: Boolean = false
        private set

    private val sections: List<Section> = timingAndDescription(context)

    //private Item curentItem;


    fun getSectionsCount(): Int {
        return sections.size
    }

    var timings: List<Item> = emptyList()

    private var isReleased = false

    fun getSection(i: Int): Section {
        return sections[i]
    }

    fun getSectionItems0(sectionNumber: Int): List<Item> {
        return sections[sectionNumber].items
    }

    fun itsLastSection(currentSectionIndex: Int): Boolean {
        return currentSectionIndex >= getSectionsCount()
    }

    var currentSectionIndex: Int = 0


    var currentItemIndex: Int = 0

    private val handler = Handler()

    private var runnable: Runnable? = null

    //public final SimpleExoPlayer mPlayer;
    private var mPlayer: MediaPlayer? = null

    private var callbackActivity: PlayerListener? = null
    private var callbackService: PlayerListener? = null


    private fun initializeMediaPlayer(context: Context) {
        mPlayer = MediaPlayer.create(context, R.raw.vegas)
        mPlayer?.setOnCompletionListener(OnCompletionListener { mp: MediaPlayer ->
            val curentItem = timings!![currentItemIndex]
            val endTime = convertToMilliseconds(curentItem.end!!)
            if (mp.currentPosition >= endTime) {
                mp.pause()
                mp.seekTo(endTime)
                isPlaying = false
                if (callbackActivity != null) {
                    callbackActivity!!.onCompletion(mp)
                }
                if (callbackService != null) {
                    callbackService!!.onCompletion(mp)
                }
            }
        })
    }

    fun releaseFromActivity() {
//        handler.removeCallbacks(updateTimeTask);
//        if (mPlayer != null) {
//            releasePlayer();
//            //@@@    mPlayer = null;
//        }
        if (mPlayer != null) {
            //@@@ releasePlayer();
            //@@@ mPlayer = null;
        }
    }

    private fun releasePlayer() {
        mPlayer!!.release()
        isReleased = true
    }

    //Вызов mPlayer.stop() переводит MediaPlayer в состояние Stopped, из которого его можно вернуть в рабочее состояние только через вызов prepare() или prepareAsync().
    fun releaseService() {
        stopTimer()
        try {
            mPlayer!!.pause()
            isPlaying = false

            //            mPlayer.stop(); //error
//            mPlayer.reset();
//            releasePlayer();
        } catch (e: Exception) {
            Log.d("Nitif Activity", e.toString())
        }
    }

    fun seekToAndStartAndRunTimer0(startTime: Int) {
        try {
            //not work after mPlayer.stop();


            mPlayer!!.seekTo(startTime)
            mPlayer!!.start()
            isPlaying = true
            if (callbackActivity != null) {
                callbackActivity!!.isPlaySuccess()
            }
            if (callbackService != null) {
                callbackService!!.isPlaySuccess()
            }
            startTimer()
        } catch (e: IllegalStateException) {
            handleException(e)
        }
    }


    fun setCallbackActivity(mListener: PlayerListener?) {
        this.callbackActivity = mListener
    }

    fun setCallbackService(mListener: PlayerListener?) {
        this.callbackService = mListener
    }

    val audioSessionId: Int
        get() {
            try {
                return mPlayer!!.audioSessionId
            } catch (e: IllegalStateException) {
                handleException(e)
                return -1
            }
        }

    fun pausePlayer() {
        stopTimer()
        mPlayer!!.pause()
        this.isPlaying = false
        if (callbackActivity != null) {
            callbackActivity!!.isPauseSuccess()
        }
        if (callbackService != null) {
            callbackService!!.isPauseSuccess()
        }
    }

    fun stopTimer() {
        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
            //DLog.d("Stop timer " + this.hashCode());
        }
    }


    private fun startTimer() {
        //DLog.d("Start timer" + this.hashCode());

        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
        }

        runnable = object : Runnable {
            override fun run() {
                if (mPlayer != null) {
                    try {
                        if (mPlayer!!.isPlaying) {
                            val currentPosition = mPlayer!!.currentPosition
                            if (callbackActivity != null) {
                                callbackActivity!!.updateTimer(currentPosition)
                            }
                            if (callbackService != null) {
                                callbackService!!.updateTimer(currentPosition)
                            }
                            handler.postDelayed(this, UPDATE_INTERVAL.toLong())
                        }
                    } catch (e: IllegalStateException) {
                        handleException(e)
                    }
                }

                //DLog.d("[task]" + this.hashCode() + "@" + PlayerManager.this.hashCode());
            }
        }
        handler.post(runnable!!)
    }

    var previousItemIndex: Int = -1

    init {
        this.timings = getSectionItems0(currentSectionIndex)
        this.initializeMediaPlayer(context)
    }

    fun updateImage(currentPosition: Int) {
        if (currentSectionIndex < getSectionsCount()) {
            val currentSection = getSection(currentSectionIndex)
            if (currentItemIndex < currentSection.items!!.size) {
                val currentItem = currentSection.items!![currentItemIndex]
                val startTime = convertToMilliseconds(
                    currentItem.start!!
                )
                val endTime = convertToMilliseconds(
                    currentItem.end!!
                )
                if (currentPosition >= startTime && currentPosition <= endTime) {
                    if (currentItemIndex != previousItemIndex) {
                        if (callbackActivity != null) {
                            callbackActivity!!.setDescriptionRequest(
                                currentItemIndex,
                                currentSectionIndex,
                                CSource.AUTO
                            )
                        }
                        if (callbackService != null) {
                            callbackService!!.setDescriptionRequest(
                                currentItemIndex,
                                currentSectionIndex,
                                CSource.AUTO
                            )
                        }
                        previousItemIndex = currentItemIndex
                    }
                } else if (currentPosition > endTime) {
                    currentItemIndex++
                    if (currentItemIndex >= currentSection.items!!.size) {
                        currentSectionIndex++
                        currentItemIndex = 0
                    }
                }
            }
        }
    }


    interface PlayerListener : OnCompletionListener {
        fun playerStateEnded(playWhenReady: Boolean, playbackState: Int)

        fun troubleReport(err: String)

        fun updateTimer(currentPosition: Int)


        fun setDescriptionRequest(
            currentItemIndex: Int,
            currentSectionIndex: Int,
            cSource: CSource
        )

        fun isPlaySuccess()

        fun isPauseSuccess()
    }

    companion object {
        private var instance: PlayerManager? = null

        @Synchronized
        fun getInstance(context: Context): PlayerManager {
            if (instance == null) {
                instance = PlayerManager(context.applicationContext)
            }
            return instance!!
        }

        private const val UPDATE_INTERVAL = 1000 // Интервал обновления в миллисекундах (1 секунда)
    }
}
