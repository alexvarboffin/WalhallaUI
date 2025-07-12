package com.video.maker.activity

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.widget.ImageViewCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.hw.photomovie.PhotoMovie
import com.hw.photomovie.PhotoMovieFactory
import com.hw.photomovie.PhotoMovieFactory.PhotoMovieType
import com.hw.photomovie.PhotoMoviePlayer
import com.hw.photomovie.model.PhotoData
import com.hw.photomovie.model.PhotoSource
import com.hw.photomovie.model.SimplePhotoData
import com.hw.photomovie.record.GLMovieRecorder
import com.hw.photomovie.record.GLMovieRecorder.OnRecordListener
import com.hw.photomovie.render.GLSurfaceMovieRenderer
import com.hw.photomovie.render.GLTextureMovieRender
import com.hw.photomovie.render.GLTextureView
import com.hw.photomovie.timer.IMovieTimer.MovieListener
import com.video.maker.BuildConfig
import com.video.maker.R
import com.video.maker.adapter.ViewPagerAdapter
import com.video.maker.databinding.ActivityVideoPreviewBinding
import com.video.maker.dialog.ExitDialogFragment
import com.video.maker.fragment.listeners.FilterFragmentListener
import com.video.maker.fragment.listeners.MusicFragmentListener
import com.video.maker.fragment.listeners.RatioFragmentListener
import com.video.maker.fragment.listeners.TransferFragmentListener
import com.video.maker.fragment.movie.DurationFragment
import com.video.maker.fragment.movie.DurationFragment.DurationFragmentListener
import com.video.maker.fragment.movie.FilterFragment
import com.video.maker.fragment.movie.MusicFragment
import com.video.maker.fragment.movie.RatioFragment
import com.video.maker.fragment.movie.TransitionFragment
import com.video.maker.model.FilterItem
import com.video.maker.model.TransferItem
import com.video.maker.util.DLog
import com.video.maker.util.DeviceUtils
import com.video.maker.util.ShareUtils
import com.video.maker.view.PlayPauseView.PlayPauseListener
import com.video.maker.view.radioview.RatioDatumMode
import com.video.photoeditor.utils.FileUtils
import com.walhalla.sharedlib.SharedObj
import com.walhalla.utils.AdvertManager

class VideoPreviewActivity : BaseActivity(), View.OnClickListener, MovieListener,
    FilterFragmentListener, TransferFragmentListener, MusicFragmentListener,
    DurationFragmentListener, RatioFragmentListener, PhotoMoviePlayer.OnPreparedListener {
    //int duration = 2000;
    var duration: Int = DurationFragment.DURATION_7_SECONDS

    private val handler = Handler()
    private val isSeekBarTracking = false

    private var mMovieRenderer: GLSurfaceMovieRenderer? = null
    private var mMovieType = PhotoMovieType.GRADIENT
    private var mMusicUri: Uri? = null
    private var mPhotoMovie: PhotoMovie<*>? = null

    var mPhotoMoviePlayer: PhotoMoviePlayer? = null

    var musicPath: Uri? = null
    var audioPath: Uri? = null

    var progressDialog: ProgressDialog? = null


    var savingLayout: ViewGroup? = null
    private var sbControl: SeekBar? = null
    private var tabLayoutMovie: TabLayout? = null
    private var tvControlCurrentTime: TextView? = null
    private var tvControlTotalTime: TextView? = null


    private var binding: ActivityVideoPreviewBinding? = null
    private val alphaRunnable =
        Runnable {
            binding!!.controlContainer.animate().alpha(0.0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationCancel(animator: Animator) {
                    }

                    override fun onAnimationRepeat(animator: Animator) {
                    }

                    override fun onAnimationStart(animator: Animator) {
                    }

                    override fun onAnimationEnd(animator: Animator) {
                        binding!!.controlContainer.visibility = View.GONE
                    }
                }).start()
        }

    private var sharedObj: SharedObj? = null


    override fun onMovieEnd() {
    }


    override fun onMovieResumed() {
    }


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityVideoPreviewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Наблюдаем за изменениями объекта
//        sharedViewModel.getSelectedItem().observe(this, new Observer<YourObject>() {
//            @Override
//            public void onChanged(@Nullable YourObject yourObject) {
//                // Используем объект
//                if (yourObject != null) {
//                    // Делайте что нужно с объектом
//                }
//            }
//        });
        createProgress()
        DLog.d("onCreate")


        initView()
        binding!!.btnPlayPause.setPlayPauseListener(object : PlayPauseListener {
            override fun play() {
                this@VideoPreviewActivity.onResumeVideo()
            }

            override fun pause() {
                this@VideoPreviewActivity.onPauseVideo()
            }
        })

        FileUtils.dropAudioFiles(this)


        getMusicData(0)
        initMoviePlayer(binding!!.mGLTextureView)
        handleIntent()


        val am = AdvertManager.getInstance0()
        val rl_native_ad = findViewById<RelativeLayout>(R.id.rl_native_ad)

        //@@@       //MAX + Fb bidding Ads
//@@@       am.initAD(VideoPreviewActivity.this);
        am.LoadInterstitalAd(this@VideoPreviewActivity)
        am.createNativeAdMAX(this@VideoPreviewActivity, rl_native_ad)
    }

    private fun createProgress() {
        this.progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setCanceledOnTouchOutside(false)
    }

    fun initMoviePlayer(mGLTextureView: GLTextureView?) {
        this.mMovieRenderer = GLTextureMovieRender(mGLTextureView)
        this.mPhotoMoviePlayer = PhotoMoviePlayer(applicationContext)
        mPhotoMoviePlayer!!.setMovieRenderer(this.mMovieRenderer)
        mPhotoMoviePlayer!!.setMovieListener(this)
        mPhotoMoviePlayer!!.setLoop(true)
        mPhotoMoviePlayer!!.setOnPreparedListener(this)
    }

    //    /data/user/0/com.christianquotestoinspire.bibleverses.motivation/cache/Quotes_1719501960486.png
    /**storage/emulated/0/Android/data/com.video.makerpro/files/Pictures/image_1719422390566.jpg */
    private fun handleIntent() {
        var stringArrayList: ArrayList<String?>? = null
        val intent = intent
        val extra = intent.extras
        if (extra != null && extra.containsKey(ShareUtils.KEY_INTENT_PHOTO)) {
            stringArrayList = getIntent().extras!!.getStringArrayList(ShareUtils.KEY_INTENT_PHOTO)
        }

        if (intent.hasExtra(ShareUtils.ARG_KEY_SHARE_OBJ)) {
            sharedObj = intent.getSerializableExtra(ShareUtils.ARG_KEY_SHARE_OBJ) as SharedObj?
        }

        //        if (stringArrayList == null || stringArrayList.isEmpty()) {
//            if (BuildConfig.DEBUG) {
//                stringArrayList = new ArrayList<>();
//                String tmp = "/data/user/0/com.christianquotestoinspire.bibleverses.motivation/cache/Quotes_1719923496979.png";
//                DLog.d("@@@exist" + new File(tmp).exists());
//
//                stringArrayList.add(tmp);
//                stringArrayList.add(tmp);
//                stringArrayList.add(tmp);
//                stringArrayList.add(tmp);
//
//                stringArrayList.add(tmp);
//            }
//        }
        DLog.d("@@@$stringArrayList")


        if (stringArrayList != null) {
            onPhotoPick(stringArrayList)
        }
    }


    fun startShareActivity(intent: Intent, reqCode: Int) {
        if (sharedObj != null) {
            intent.putExtra(ShareUtils.ARG_KEY_SHARE_OBJ, sharedObj)
        }
        AdvertManager.adCounter++
        AdvertManager.showMaxInterstitial(this@VideoPreviewActivity, intent, reqCode)
    }

    private fun initView() {
        binding!!.include.btnBack.setOnClickListener { v: View? ->
            onBackPressed()
        }
        val imageView2 = findViewById<CardView>(R.id.btnFinish)
        imageView2.setOnClickListener { v: View? ->
            saveVideo(
                binding!!.mGLTextureView, mPhotoMovie!!
            )
        }
        binding!!.mGLTextureView.setOnClickListener(this)
        setupViewPager(binding!!.viewpagerMovie)
        val tabLayout = findViewById<TabLayout>(R.id.tablayoutMovie)
        this.tabLayoutMovie = tabLayout
        tabLayout.setupWithViewPager(binding!!.viewpagerMovie)
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.viewpagerMovie.currentItem = tab.position

                //                startActivityes(null, 0);
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        setupTabIconMovie()
        tabLayoutMovie!!.setTabTextColors(
            resources.getColor(R.color.tab_white), resources.getColor(
                R.color.white
            )
        )

        binding!!.btnPlayPause.isPlaying = true

        this.sbControl = findViewById(R.id.sb_control)
        this.tvControlCurrentTime = findViewById(R.id.tv_control_current_time)
        this.tvControlTotalTime = findViewById(R.id.tv_control_total_time)
        sbControl?.setOnTouchListener(View.OnTouchListener { view: View?, motionEvent: MotionEvent? -> true })
        this.savingLayout = findViewById(R.id.saving_layout)

        hideControl()
    }

    private fun showExitDialog() {
//        ExitDialog photoDiscardDialog = new ExitDialog(this);
//        photoDiscardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        photoDiscardDialog.show();
        val exitDialog = ExitDialogFragment.newInstance()
        exitDialog.show(supportFragmentManager, "ExitDialogFragment")
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        super.onBackPressed()
        //showExitDialog();
    }

    private fun setupTabIconMovie() {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_selected),  // selected state
            intArrayOf(-android.R.attr.state_selected) // unselected state
        )
        @ColorInt val selectedColor =
            Color.parseColor("#7555FB") // Replace with your desired color for selected state
        @ColorInt val unselectedColor =
            Color.parseColor("#FFFFFF") // Replace with your desired color for unselected state
        val colors = intArrayOf(
            selectedColor,
            unselectedColor
        )
        val tabColors = ColorStateList(states, colors)
        val textView = LayoutInflater.from(this).inflate(R.layout.main_tab, null) as ImageView
        //        textView.setText(getString(R.string.str_filter));
//        textView.setTextColor(color);
        textView.setImageResource(R.drawable.ic_movie_filter)
        tabLayoutMovie!!.getTabAt(0)!!.setCustomView(textView)
        val textView2 = LayoutInflater.from(this).inflate(R.layout.main_tab, null) as ImageView
        //        textView2.setText(getString(R.string.str_transfer));
//        textView2.setTextColor(color);
        textView2.setImageResource(R.drawable.ic_movie_transfer)
        tabLayoutMovie!!.getTabAt(1)!!.setCustomView(textView2)
        val textView3 = LayoutInflater.from(this).inflate(R.layout.main_tab, null) as ImageView
        //        textView3.setText(getString(R.string.str_music));
//        textView3.setTextColor(color);
        textView3.setImageResource(R.drawable.ic_movie_music)
        tabLayoutMovie!!.getTabAt(2)!!.setCustomView(textView3)
        val textView4 = LayoutInflater.from(this).inflate(R.layout.main_tab, null) as ImageView
        //        textView4.setText(getString(R.string.str_duration));
//        textView4.setTextColor(color);
        textView4.setImageResource(R.drawable.ic_duration_movie)
        tabLayoutMovie!!.getTabAt(3)!!.setCustomView(textView4)
        val textView5 = LayoutInflater.from(this).inflate(R.layout.main_tab, null) as ImageView
        //        textView5.setText(getString(R.string.str_ratio));
//        textView5.setTextColor(color);
        textView5.setImageResource(R.drawable.ic_aspect_ratio)
        tabLayoutMovie!!.getTabAt(4)!!.setCustomView(textView5)
        if ((resources.configuration.screenLayout and 15) == 1) {
            tabLayoutMovie!!.tabMode = TabLayout.MODE_SCROLLABLE
        }

        ImageViewCompat.setImageTintList(textView, tabColors)
        ImageViewCompat.setImageTintList(textView2, tabColors)
        ImageViewCompat.setImageTintList(textView3, tabColors)
        ImageViewCompat.setImageTintList(textView4, tabColors)
        ImageViewCompat.setImageTintList(textView5, tabColors)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val viewPagerAdapter = ViewPagerAdapter(
            supportFragmentManager
        )

        val filterFragment = FilterFragment()
        val transferFragment = TransitionFragment()
        val durationFragment = DurationFragment()
        val ratioFragment = RatioFragment()


        viewPagerAdapter.addFrag(filterFragment, getString(R.string.str_filter))


        viewPagerAdapter.addFrag(transferFragment, getString(R.string.str_transfer))

        val musicFragment = MusicFragment()
        viewPagerAdapter.addFrag(musicFragment, getString(R.string.str_music))


        viewPagerAdapter.addFrag(durationFragment, getString(R.string.str_duration))
        durationFragment.setDurationFragmentListener(this)

        viewPagerAdapter.addFrag(ratioFragment, getString(R.string.str_ratio))

        transferFragment.setTransferFragmentListener(this)
        filterFragment.setFilterFragmentListener(this)
        musicFragment.setMusicFragmentListener(this)
        ratioFragment.setRatioFragmentListener(this)

        viewPager.adapter = viewPagerAdapter
    }


    fun onPhotoPick(arrayList: ArrayList<String?>) {
        val arrayList2 = ArrayList<PhotoData>(arrayList.size)
        val it: Iterator<String?> = arrayList.iterator()
        while (it.hasNext()) {
            arrayList2.add(SimplePhotoData(this, it.next(), 2))
        }
        val photoSource = PhotoSource(arrayList2)
        if (this.mPhotoMoviePlayer == null) {
            startPlay(photoSource)
        } else {
            createVideo(photoSource, PhotoMovieType.GRADIENT)
        }
    }


    fun startVideo() {
        mPhotoMoviePlayer!!.start()
        val duration2 = mPhotoMovie!!.duration / 1000
        tvControlTotalTime!!.text =
            DeviceUtils.getTimeString(duration2)
        sbControl!!.max = duration2
    }


    fun onPauseVideo() {
        mPhotoMoviePlayer!!.pause()
    }

    fun onResumeVideo() {
        mPhotoMoviePlayer!!.start()
    }

    private fun startPlay(photoSource: PhotoSource) {
        this.mPhotoMovie =
            PhotoMovieFactory.generatePhotoMovie(photoSource, this.mMovieType, this.duration)
        mPhotoMoviePlayer!!.setDataSource(this.mPhotoMovie)
        mPhotoMoviePlayer!!.prepare()
    }

    fun saveVideo(parent: GLTextureView, photoMovie: PhotoMovie<*>) {
        if (mPhotoMoviePlayer!!.isPlaying) {
            mPhotoMoviePlayer!!.pause()
        }

        savingLayout!!.visibility = View.VISIBLE


        binding!!.savingLayout.tvSaving.text =
            getString(R.string.str_saving_video) + "..."
        val currentTimeMillis = System.currentTimeMillis()
        val gLMovieRecorder = GLMovieRecorder(this)
        val initVideoFile = FileUtils.initVideoFile(this)
        DLog.i(
            TAG,
            "saveVideo initVideoFile $initVideoFile"
        )


        gLMovieRecorder.configOutput(
            parent.width,
            parent.height,
            if (parent.width * parent.height > 1500000) 8000000 else 4000000,
            30,
            1,
            initVideoFile.absolutePath
        )
        val generatePhotoMovie = PhotoMovieFactory.generatePhotoMovie(
            photoMovie.photoSource, mMovieType,
            this.duration
        )

        val gLSurfaceMovieRenderer = GLSurfaceMovieRenderer(this.mMovieRenderer)
        gLSurfaceMovieRenderer.photoMovie = generatePhotoMovie
        if (this.mMusicUri != null) {
            this.audioPath = this.musicPath
            Log.d("onTypeMusic mp ", musicPath.toString())
            Log.d("onTypeMusic op ", audioPath.toString())
            Log.d("onTypeMusic mu ", "" + mMusicUri)
        }
        if (audioPath != null && !TextUtils.isEmpty(audioPath!!.path)) {
            if (Build.VERSION.SDK_INT < 18) {
                Toast.makeText(applicationContext, "Mix audio needs api18!", Toast.LENGTH_LONG)
                    .show()
            } else {
                gLMovieRecorder.setMusic(this.audioPath)
            }
        }
        gLMovieRecorder.setDataSource(gLSurfaceMovieRenderer)
        DLog.i(TAG, "saveVideo setDataSource")
        val gLMovieRecorder2 = gLMovieRecorder


        val callback: OnRecordListener = object : OnRecordListener {
            override fun onRecordFinish(z: Boolean) {
                val currentTimeMillis = System.currentTimeMillis()
                DLog.i(TAG, "onRecordFinish:" + (0L))
                if (z) {
                    this@VideoPreviewActivity.sendBroadcast(
                        Intent(
                            "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
                            Uri.fromFile(initVideoFile)
                        )
                    )


                    AdvertManager.adCounter = AdvertManager.adDisplayCounter - 1


                    val intent =
                        ShareActivity.newIntent(this@VideoPreviewActivity, initVideoFile, false)
                    this@VideoPreviewActivity.startShareActivity(intent, 0)
                } else {
                    Toast.makeText(
                        this@VideoPreviewActivity.applicationContext,
                        "Record error!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                if (gLMovieRecorder2.audioRecordException != null) {
                    Log.e(
                        "XXXXXXXX",
                        gLMovieRecorder2.audioRecordException.message!!
                    )
                    val applicationContext = this@VideoPreviewActivity.applicationContext
                    Toast.makeText(
                        applicationContext,
                        "record audio failed:" + gLMovieRecorder2.audioRecordException.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
                savingLayout!!.visibility =
                    View.GONE
            }

            override fun onRecordProgress(i: Int, i2: Int) {
                this@VideoPreviewActivity.runOnUiThread {
                    var per = ((((i.toFloat()) / (i2.toFloat())) * 100.0f).toInt())
                    if (per > 100) {
                        per = 100
                    }
                    binding!!.savingLayout.tvSaving.text =
                        this@VideoPreviewActivity.getString(R.string.str_saving_video) + " " + per + "%"
                }
            }
        }
        gLMovieRecorder.startRecord(callback)
    }

    fun setMusic(uri: Uri?) {
        this.mMusicUri = uri
        createVideo(mPhotoMovie!!.photoSource, this.mMovieType)
    }

    fun createVideo(photoSource: PhotoSource?, photoMovieType: PhotoMovieType) {
        if (this.progressDialog != null) {
            progressDialog!!.show()
        }
        mPhotoMoviePlayer!!.stop()
        val generatePhotoMovie = PhotoMovieFactory.generatePhotoMovie(
            photoSource, photoMovieType,
            this.duration
        )
        this.mPhotoMovie = generatePhotoMovie
        mPhotoMoviePlayer!!.setDataSource(generatePhotoMovie)
        mPhotoMoviePlayer!!.setOnPreparedListener(object : PhotoMoviePlayer.OnPreparedListener {
            override fun onPreparing(photoMoviePlayer: PhotoMoviePlayer, f: Float) {
                //DLog.d("onPreparing " + f);
            }

            override fun onPrepared(photoMoviePlayer: PhotoMoviePlayer, prepared: Int, total: Int) {
                DLog.d("--> onPrepared $prepared@@$total")
                this@VideoPreviewActivity.runOnUiThread {
                    this@VideoPreviewActivity.startVideo()
                    if (this@VideoPreviewActivity.progressDialog != null) {
                        progressDialog!!.dismiss()
                    }
                }
            }

            override fun onError(photoMoviePlayer: PhotoMoviePlayer) {
                if (this@VideoPreviewActivity.progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                DLog.d("onPrepare onError")
            }
        })
        //        MovieActivity.this.mPhotoMoviePlayer.prepare();
        if (this.mMusicUri != null) {
            val progressDialog2 = this.progressDialog
            progressDialog2?.show()
            mPhotoMoviePlayer!!.setMusic(
                this, this.mMusicUri
            ) { mPhotoMoviePlayer!!.prepare() }
        }
    }

    override fun onMoviePaused() {
        DLog.d("__paused__")
        //VideoPreviewActivity.this.mPhotoMoviePlayer.prepare();
    }


    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.mGLTextureView) {
            handler.removeCallbacks(this.alphaRunnable)
            binding!!.controlContainer.alpha = 1.0f
            binding!!.controlContainer.visibility = View.VISIBLE
            hideControl()
        }
    }

    private fun hideControl() {
        handler.removeCallbacks(this.alphaRunnable)
        handler.postDelayed(this.alphaRunnable, 3500)
    }


    override fun onFilter(filterItem: FilterItem) {
        DLog.d("@@@setfilter $filterItem")
        mMovieRenderer!!.movieFilter = filterItem.initFilter()
    }

    override fun onMyMusic() {
        val intent = Intent()
        intent.setType("audio/*")
        intent.setAction("android.intent.action.GET_CONTENT")
        startActivityForResult(intent, REQUEST_MUSIC)
    }

    override fun onTypeMusic(i: Int) {
        getMusicData(i)
        DLog.d("onTypeMusic: $i")
        setMusic(this.mMusicUri)
        DLog.d("~~ onTypeMusic o mu $mMusicUri")
        DLog.d("~~ onTypeMusic o mp $musicPath")
        DLog.e("onTypeMusic o op ", "@@@$audioPath")
    }

    private fun getMusicData(i: Int) {
//        Log.e("Raw songs", "song id & name: " +getResources().getIdentifier("love", "raw", getPackageName()) + " " + "love");
//        Log.e("Raw songs", "song id & name: " +getResources().getIdentifier("friend", "raw", getPackageName()) + " " + "friend");
//        Log.e("Raw songs", "song id & name: " +getResources().getIdentifier("christmas", "raw", getPackageName()) + " " + "christmas");
//        Log.e("Raw songs", "song id & name: " +getResources().getIdentifier("positive", "raw", getPackageName()) + " " + "positive");
//        Log.e("Raw songs", "song id & name: " +getResources().getIdentifier("movie", "raw", getPackageName()) + " " + "movie");
//        Log.e("Raw songs", "song id & name: " +getResources().getIdentifier("beach", "raw", getPackageName()) + " " + "beach");
//        Log.e("Raw songs", "song id & name: " +getResources().getIdentifier("summer", "raw", getPackageName()) + " " + "summer");
//        Log.e("Raw songs", "song id & name: " +getResources().getIdentifier("travel", "raw", getPackageName()) + " " + "travel");
//        Log.e("Raw songs", "song id & name: " +getResources().getIdentifier("happy", "raw", getPackageName()) + " " + "happy");

        val packageName0 = packageName
        val packageNameRes = "android.resource://$packageName0/"

        when (i) {
            0 -> {
                //String uriString="android.resource://"+packageNameRes + getResources().getIdentifier("love", "raw", getPackageName());
                val uriString = packageNameRes + R.raw.love
                this.mMusicUri = Uri.parse(uriString)
                this.musicPath = FileUtils.getAudioFileUri(this, "0.m4r")
                return
            }

            1 -> {
                this.mMusicUri = Uri.parse(
                    packageNameRes + resources.getIdentifier(
                        "friend", "raw",
                        packageName
                    )
                )
                this.musicPath = FileUtils.getAudioFileUri(this, "1.m4r")
                return
            }

            4 -> {
                this.mMusicUri = Uri.parse(
                    packageNameRes + resources.getIdentifier(
                        "movie", "raw",
                        packageName
                    )
                )
                this.musicPath = FileUtils.getAudioFileUri(this, "4.m4r")
                return
            }

            7 -> {
                this.mMusicUri = Uri.parse(
                    packageNameRes + resources.getIdentifier(
                        "travel", "raw",
                        packageName
                    )
                )
                this.musicPath = FileUtils.getAudioFileUri(this, "7.m4r")
                return
            }

            6 -> {
                this.mMusicUri = Uri.parse(
                    packageNameRes + resources.getIdentifier(
                        "summer", "raw",
                        packageName
                    )
                )
                this.musicPath = FileUtils.getAudioFileUri(this, "6.m4r")
                return
            }

            2 -> {
                this.mMusicUri = Uri.parse(
                    packageNameRes + resources.getIdentifier(
                        "christmas", "raw",
                        packageName
                    )
                )
                this.musicPath = FileUtils.getAudioFileUri(this, "2.m4r")
                return
            }

            8 -> {
                this.mMusicUri = Uri.parse(
                    packageNameRes + resources.getIdentifier(
                        "happy", "raw",
                        packageName
                    )
                )
                this.musicPath = FileUtils.getAudioFileUri(this, "8.m4r")
                return
            }

            3 -> {
                this.mMusicUri = Uri.parse(
                    packageNameRes + resources.getIdentifier(
                        "positive", "raw",
                        packageName
                    )
                )
                this.musicPath = FileUtils.getAudioFileUri(this, "3.m4r")
                return
            }

            5 -> {
                this.mMusicUri = Uri.parse(
                    packageNameRes + resources.getIdentifier(
                        "beach", "raw",
                        packageName
                    )
                )
                this.musicPath = FileUtils.getAudioFileUri(this, "5.m4r")
                return
            }

            else -> {}
        }
    }

    override fun onTransfer(transferItem: TransferItem) {
        this.mMovieType = transferItem.type
        createVideo(mPhotoMovie!!.photoSource, this.mMovieType)
    }

    override fun onDurationSelect(i: Int) {
        this.duration = i
        createVideo(mPhotoMovie!!.photoSource, this.mMovieType)
    }

    override fun onMovieUpdate(i: Int) {
        if (!this.isSeekBarTracking) {
            val round = Math.round((i.toFloat()) / 1000.0f)
            sbControl!!.progress = round
            tvControlCurrentTime!!.text =
                DeviceUtils.getTimeString(round)
        }
    }

    override fun onMovieStarted() {
        DLog.d(TAG, "onMovieStarted")
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == -1 && requestCode == REQUEST_MUSIC) {
            setMusic(intent?.data)
            this.musicPath = mMusicUri //UriUtil.getPath(this, this.mMusicUri);
            DLog.d("##$musicPath")
        }

        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "@@@$requestCode", Toast.LENGTH_SHORT).show()
        }
    }


    public override fun onPostResume() {
        super.onPostResume()
    }


    public override fun onPause() {
        super.onPause()
    }


    public override fun onStop() {
        super.onStop()
        val photoMoviePlayer = this.mPhotoMoviePlayer
        photoMoviePlayer?.pause()
    }

    public override fun onStart() {
        super.onStart()
//        if (this.mPhotoMoviePlayer != null) {
//            this.mPhotoMoviePlayer.start();
//        }
        val fragmentManager = supportFragmentManager
        fragmentManager.setFragmentResultListener(
            ExitDialogFragment.REQUEST_KEY, this
        ) { requestKey: String?, result: Bundle ->
            if (result.containsKey(ExitDialogFragment.KEY_INPUT_OUTPUT_DATA_FONTVALUE)) {
                val fontName = result.getInt(ExitDialogFragment.KEY_INPUT_OUTPUT_DATA_FONTVALUE)
                if (fontName > -1) {
                    destroyMovie()
                    finish()
                }
            }
        }
    }


    fun destroyMovie() {
        val photoMoviePlayer = this.mPhotoMoviePlayer
        if (photoMoviePlayer != null) {
            photoMoviePlayer.pause()
            mPhotoMoviePlayer!!.destroy()
            //            Intent intent = new Intent(VideoPreviewActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        }
    }

    override fun onRatio(i: Int) {
        if (i == 11) {
            binding!!.videoContainer.setRatio(RatioDatumMode.valueOf(1), 1.0f, 1.0f)
            createVideo(mPhotoMovie!!.photoSource, this.mMovieType)
        } else if (i == 34) {
            binding!!.videoContainer.setRatio(RatioDatumMode.valueOf(1), 3.0f, 4.0f)
            createVideo(mPhotoMovie!!.photoSource, this.mMovieType)
        } else if (i == 43) {
            binding!!.videoContainer.setRatio(RatioDatumMode.valueOf(1), 4.0f, 3.0f)
            createVideo(mPhotoMovie!!.photoSource, this.mMovieType)
        } else if (i == 169) {
            binding!!.videoContainer.setRatio(RatioDatumMode.valueOf(1), 16.0f, 9.0f)
            createVideo(mPhotoMovie!!.photoSource, this.mMovieType)
        } else if (i == 916) {
            binding!!.videoContainer.setRatio(RatioDatumMode.valueOf(1), 9.0f, 16.0f)
            createVideo(mPhotoMovie!!.photoSource, this.mMovieType)
        }
    }


    public override fun onDestroy() {
        super.onDestroy()
        Runtime.getRuntime().gc()
    }


    //======================================================================
    override fun onPreparing(photoMoviePlayer: PhotoMoviePlayer, f: Float) {
        DLog.d("@@")
    }

    override fun onPrepared(photoMoviePlayer: PhotoMoviePlayer, prepared: Int, total: Int) {
        DLog.d("@@$prepared/$total")
        this@VideoPreviewActivity.runOnUiThread {
            mPhotoMoviePlayer!!.start()
        }
    }

    override fun onError(photoMoviePlayer: PhotoMoviePlayer) {
        DLog.i("onPrepare error")
    }

    override fun onResume() {
        super.onResume()
        if (mPhotoMoviePlayer != null && !mPhotoMoviePlayer!!.isPlaying) {
            //mPhotoMoviePlayer.start();
            startVideo()
        }
    }

    override fun errorShowAds() {
        Toast.makeText(this, R.string.ad_not_loaded_try_another_time, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_MUSIC = 234

        const val TAG: String = "@@@"


        //    private String path="/storage/emulated/0/Music";
        //    static String path = Environment.getExternalStorageDirectory() + "/Download/StoryMaker";
        var path0: String = Environment.getExternalStorageDirectory().toString() + "/Music"

        //    private SharedViewModel sharedViewModel;
        fun newIntent0(context: Context?, sendPhotos0: ArrayList<String?>?): Intent {
            val intent = Intent(
                context,
                VideoPreviewActivity::class.java
            )
            val bundle = Bundle()
            bundle.putStringArrayList(ShareUtils.KEY_INTENT_PHOTO, sendPhotos0)
            intent.putExtras(bundle)
            return intent
        }
    }
}
