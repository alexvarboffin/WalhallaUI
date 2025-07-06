package com.kworkapp.audiogid.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuInflater
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.chibde.BaseVisualizer

import com.kworkapp.audiogid.Constants
import com.kworkapp.audiogid.R
import com.kworkapp.audiogid.SimpleQuest
import com.kworkapp.audiogid.databinding.ActivityMainBinding
import com.kworkapp.audiogid.fragment.FullscreenImageDialogFragment
import com.kworkapp.audiogid.fragment.InfoFragment
import com.kworkapp.audiogid.fragment.QCallback
import com.kworkapp.audiogid.player.PlayerManager
import com.kworkapp.audiogid.service.MediaPlayerService
import com.kworkapp.audiogid.ui.VisualizerContainer
import com.kworkapp.audiogid.ui.presentation.launcher.pojo.Item
import com.kworkapp.audiogid.ui.presentation.launcher.pojo.Section
import com.walhalla.ui.DLog
import com.walhalla.ui.plugins.DialogAbout.aboutDialog
import com.walhalla.ui.plugins.Launcher
import com.walhalla.ui.plugins.Module_U
import java.util.Locale

class MainActivity : AppCompatActivity(), SimpleQuest, QCallback,
    PlayerManager.PlayerListener {
    private var binding: ActivityMainBinding? = null


    private var m: BaseVisualizer? = null
    private var dialogFragment: FullscreenImageDialogFragment? = null
    private lateinit var manager: PlayerManager

    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        handler = Handler()

        manager = PlayerManager.getInstance(applicationContext)
        manager.setCallbackActivity(this)

        //binding.description.setText(statistics());
        binding!!.btnPlayPause.setOnClickListener { v: View? ->
            if (manager.isPlaying) {
                pauseAudio()
            } else {
                playAudio(CSource.FROM_START_BTN)
            }
        }
        binding!!.btnNext.setOnClickListener { v: View? -> nextAudio() }
        binding!!.btnPrevious.setOnClickListener { v: View? -> previousAudio() }

        binding!!.regionMenu.setOnClickListener { view: View ->
            this.showRegionSelectorMenu(
                view
            )
        }


        //binding.optionMenu.setOnClickListener(this::showOptionMenu);


//        Glide.with(this)
//                .asGif()
//                .load(R.drawable.orig)
//                .into(binding.gifImageView);
        binding!!.visual.setCallback(object : VisualizerContainer.VisualizerCallback {
            override fun initialize(m: BaseVisualizer) {
                this@MainActivity.m = m
                initialize0(this@MainActivity, this@MainActivity.m)
            }

        })
        binding!!.visual.setOnClickListener { v: View? ->
            binding!!.visual.showNextVisualizer()
        }
    }


    private fun initialize0(context: Activity, visualizer: BaseVisualizer?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                WRITE_EXTERNAL_STORAGE_PERMS,
                AUDIO_PERMISSION_REQUEST_CODE
            )
        } else {
            setPlayerVisualizer()
        }
    }

    fun setPlayerVisualizer() {
        val sessionId: Int = manager?.audioSessionId ?: -1
        if (sessionId > 0 && m != null) {
            try {
                m?.setPlayer(sessionId)
            } catch (e: Exception) {
                DLog.handleException(e)
            }
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        val hours = (milliseconds / (1000 * 60 * 60)) % 24

        return if (hours > 0) {
            String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
            )
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }


    private fun showOptionMenu(view: View) {
        val popup = PopupMenu(view.context, view)
        val inflater: MenuInflater = popup.menuInflater
        val menu = popup.menu
        inflater.inflate(R.menu.popup_options, menu)
        val menuHelper: Any
        val argTypes: Array<Class<*>?>
        try {
            @SuppressLint("DiscouragedPrivateApi") val fMenuHelper =
                PopupMenu::class.java.getDeclaredField("mPopup")
            fMenuHelper.isAccessible = true
            menuHelper = fMenuHelper[popup]
            argTypes = arrayOf(Boolean::class.javaPrimitiveType)
            menuHelper?.javaClass?.getDeclaredMethod("setForceShowIcon", *argTypes)
                ?.invoke(menuHelper, true)
        } catch (ignored: Exception) {
        }
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            val itemId = menuItem.itemId
            val groupId = menuItem.groupId
            if (itemId == R.id.action_about) {
                aboutDialog(this)
                return@setOnMenuItemClickListener true
            } else if (itemId == R.id.action_privacy_policy) {
                Launcher.openBrowser(this, getString(R.string.url_privacy_policy))
                return@setOnMenuItemClickListener true
            } else if (itemId == R.id.action_share_app) {
                Module_U.shareThisApp(this)
                return@setOnMenuItemClickListener true
            } else if (itemId == R.id.action_feedback) {
                Module_U.feedback(this)
                return@setOnMenuItemClickListener true
            } else {
                return@setOnMenuItemClickListener true
            }
        }
        popup.show()
    }

    private fun showRegionSelectorMenu(view: View) {
        val popup = PopupMenu(view.context, view)
        val inflater: MenuInflater = popup.menuInflater
        val menu = popup.menu

        //        MenuPopupHelper menuHelper = new MenuPopupHelper(
//                mView.getActivity(), (MenuBuilder) menu, v);
//        menuHelper.setForceShowIcon(true);
//        menuHelper.show();

        //reaper.wrapper(menu, new File(packageInfo.applicationInfo.sourceDir));
        inflater.inflate(R.menu.abc_popup_app, menu)
        for (i in 0 until manager.getSectionsCount()) {
            val item: Section = manager.getSection(i)
            val items = item.items
            //            menu
//                    .add(Menu.NONE, i, 100 + i, String.valueOf(item.region))
//                    .setIcon(R.drawable.region_city)
//                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            val groupId = i
            val regionsSubMenu: SubMenu = menu.addSubMenu(
                444,  //group_id
                groupId,  //item_id
                100 + i, item.timingRegionName.toString()
            ).setIcon(R.drawable.region_city)
            val t0 = items!!.size
            for (i1 in 0 until t0) {
                val mm = items[i1]

                val newValue = removeTrailingColon(mm.timingName)
                regionsSubMenu.add(
                    groupId,  //group_id
                    i1, 100 + i, newValue
                ).setIcon(R.drawable.ic_baseline_text_snippet_24)
            }
        }


        val menuHelper: Any
        val argTypes: Array<Class<*>?>
        try {
            @SuppressLint("DiscouragedPrivateApi") val fMenuHelper =
                PopupMenu::class.java.getDeclaredField("mPopup")
            fMenuHelper.isAccessible = true
            menuHelper = fMenuHelper[popup]
            argTypes = arrayOf(Boolean::class.javaPrimitiveType)
            menuHelper?.javaClass?.getDeclaredMethod("setForceShowIcon", *argTypes)
                ?.invoke(menuHelper, true)
        } catch (ignored: Exception) {
        }
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            val itemId = menuItem.itemId
            val groupId = menuItem.groupId

            //Toast.makeText(this, groupId + "/" + itemId, Toast.LENGTH_SHORT).show();
           manager?.let {
               if (groupId == 444 && itemId < it.getSectionsCount()) {
//                sectionNumber = itemId;
//                timings = getSectionItems(sectionNumber);
//                currentIndex = 0;
//                playAudio(currentIndex, sectionNumber);
               } else {
                   if (groupId < it.getSectionsCount()) {
                       it.currentSectionIndex = groupId
                       it.timings = it.getSectionItems0(it.currentSectionIndex)
                       it.currentItemIndex = itemId
                       playAudio(CSource.MENU)
                   }
               }
           }
            false
        }
        popup.show()
    }

    private fun playAudio(cSource: CSource) {
        manager?.let {
            playAudio(it.currentItemIndex, it.currentSectionIndex, cSource)
        }
    }


    //    private List<Item> loadTimingsFromJson() {
    //        try {
    //            InputStream is = getResources().openRawResource(R.raw.timing);
    //            InputStreamReader reader = new InputStreamReader(is);
    //            Data data = new Gson().fromJson(reader, Data.class);
    //            List<Section> mm = data.sections;
    //            return .get(0).items;
    //        } catch (Exception e) {
    //            Toast.makeText(this, "Failed to load timings", Toast.LENGTH_SHORT).show();
    //            return null;
    //        }
    //    }
    private fun statistics(): String {
        val sb = StringBuilder()

        //        sb.append("Объектов в регионе: ").append(timings.size()).append("; ")
//                .append(" Описание кол-во")
//                .append(desc.size())
//        ;

//        for (Item item : timings) {
//            item
//        }
        return sb.toString()
    }

    private fun playAudio(itemIndex: Int, sectionNumber: Int, source: CSource) {

        manager?.let {
            if (itemIndex < 0 || itemIndex >= it.timings.size) {
                return
            }
            val curentItem: Item = it.timings.get(itemIndex)

            //int startTime = convertToMilliseconds(curentItem.start);
            //manager.seekToAndStartAndRunTimer(startTime);
            val list0 = ArrayList<Item>()
            list0.add(curentItem)

            updateButtons(true)
            MediaPlayerService.playListOfStream(this, list0, 0)


            //@@@
            setDescription(itemIndex, sectionNumber, source)
            //        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//
//                return false;
//            }
//        });
        }

    }


    private fun setDescriptionSkip(index: Int, sectionNumber: Int) {
        //skip
    }


    fun setDescription(index: Int, sectionNumber: Int, source: CSource) {
        DLog.d("@@$source")
        manager?.previousItemIndex = index
        if (source == CSource.MENU) {
            val fragment: Fragment = InfoFragment.newInstance(index, sectionNumber)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.commit()
        } else if (source == CSource.NEXT) {
            val fragment: Fragment = InfoFragment.newInstance(index, sectionNumber)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.commit()
        } else if (source == CSource.PREV) {
            val fragment: Fragment = InfoFragment.newInstance(index, sectionNumber)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.commit()
        } else if (source == CSource.AUTO) {
            val fragment: Fragment = InfoFragment.newInstance(index, sectionNumber)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.commit()
        } else if (source == CSource.FROM_START_BTN) {
            setDescriptionSkip(index, sectionNumber)
        }
    }


    override fun setCardData(
        title: String,
        description: String,
        link: String,
        imageUrl: String,
        avatarRes: Int
    ) {
//        binding.subtitle.setText(title);
//        //binding.subtitle.setText(sectionNumber + "@" + index + "|" + sectionNameFromDescription);
//        binding.description.setText(description);
//        binding.link.setText(link);
//
//        if (avatarRes != null) {
//            binding.avatar.setImageResource(avatarRes);
//            binding.avatar.setVisibility(View.VISIBLE);
//        } else {
//            binding.avatar.setVisibility(View.GONE);
//        }
//
//        if (imageUrl != null) {
//            //binding.image0.setImage(ImageSource.uri(jpgFiles.get(0)));
//            Glide.with(this)
//                    .load(Uri.parse(imageUrl))
////                        .apply(new RequestOptions()
//                    .centerCrop()
//                    .placeholder(R.drawable.loading_placeholder)
////                                .error(R.drawable.im_error))
//                    .into(binding.image0);
////                BaseBannerAdapter webBannerAdapter = new BaseBannerAdapter(this, jpgFiles);
////                webBannerAdapter.setOnBannerItemClickListener(position -> {
////
////                });
////                binding.image.setAdapter(webBannerAdapter);
//            binding.image0.setVisibility(View.VISIBLE);
//            binding.image0.setOnClickListener(v -> {
//                dialogFragment = FullscreenImageDialogFragment.newInstance(imageUrl);
//                dialogFragment.show(getSupportFragmentManager(), "fullscreen_image");
//            });
//        } else {
//            binding.image0.setVisibility(View.GONE);
//        }
    }

    override fun onResume() {
        super.onResume()
        //DLog.d("@@@" + this.hashCode() + " @@@ " + manager.hashCode());
        manager?.let {
            setDescription(
                it.currentItemIndex,
                it.currentSectionIndex,
                CSource.MENU
            )
            updateButtons(it.isPlaying)
        }

    }


    private fun pauseAudio() {
        updateButtons(false)
        manager?.pausePlayer()
    }

    private fun updateButtons(isPlaying: Boolean) {
        val res0: Int =
            if ((isPlaying)) R.drawable.exo_ic_pause_circle_filled else R.drawable.exo_ic_play_circle_filled
        binding!!.btnPlayPause.setImageResource(res0)
    }

    private fun nextAudio() {
        next()
        //nextChapterAudio();
    }

    //    private void nextChapterAudio() {
    //        if (currentSectionIndex < totalSections - 1) {
    //            currentSectionIndex++;
    //            timings = getSectionItems0(currentSectionIndex);
    //            currentItemIndex = 0;
    //            Section item = sections.get(currentSectionIndex);
    ////                String regionName = item.getTimingRegionName();
    ////                Toast.makeText(this, "" + regionName, Toast.LENGTH_SHORT).show();
    //            playAudio(currentItemIndex, currentSectionIndex);
    //        } else {
    //            Toast.makeText(this, "Это последний сегмент", Toast.LENGTH_SHORT).show();
    //        }
    //    }
    private fun next() {
        manager?.let {
            if (it.currentItemIndex < it.timings.size - 1) {
                it.currentItemIndex++
                playAudio(CSource.NEXT)
            } else {
                //Toast.makeText(this, "Это последний сегмент", Toast.LENGTH_SHORT).show();
                //DLog.d("[->]" + it.getCurrentSectionIndex() + " " + totalSections);

                if (!it.itsLastSection(it.currentSectionIndex)) {
                    it.currentSectionIndex++
                    it.timings = it.getSectionItems0(it.currentSectionIndex)
                    it.currentItemIndex = 0
                    //                Section item = presenter.getSection(it.getCurrentSectionIndex());
//                String regionName = item.getTimingRegionName();
//                Toast.makeText(this, "" + regionName, Toast.LENGTH_SHORT).show();
                    playAudio(CSource.NEXT)
                } else {
                    Toast.makeText(this, "Это последний сегмент", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun playerStateEnded(playWhenReady: Boolean, playbackState: Int) {
    }

    override fun troubleReport(err: String) {
    }

    override fun onBackPressed() {
        manager?.stopTimer()
        val intent = Intent(this@MainActivity, MediaPlayerService::class.java)
        intent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION)
        startService(intent)
        super.onBackPressed()
        finish()
    }

    override fun updateTimer(currentPosition: Int) {
        binding!!.currentTimeTextView.text = formatTime(currentPosition)
        manager?.updateImage(currentPosition)
    }

    override fun setDescriptionRequest(
        currentItemIndex: Int,
        currentSectionIndex: Int,
        cSource: CSource
    ) {
        // Обновляем изображение в главном потоке
        handler!!.post {
            //DLog.d("@@@@{IMG}");
            setDescription(currentItemIndex, currentSectionIndex, cSource)
        }
    }

    override fun isPlaySuccess() {
        updateButtons(true)
    }

    override fun isPauseSuccess() {
        updateButtons(false)
    }



    override fun onCompletion(mp: MediaPlayer) {
        updateButtons(false)
    }


    private fun previousAudio() {
        manager?.let {
            if (it.currentItemIndex > 0) {
                it.currentItemIndex--
                playAudio(CSource.PREV)
            } else {
                Toast.makeText(this, "Это первый сегмент", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //manager.releaseFromActivity();
        if (dialogFragment != null) {
            dialogFragment?.dismiss()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setPlayerVisualizer()
            } else {
                //this.finish();
            }
        }
    }


    override fun fullScreenImage(imageUrl: String?) {
        dialogFragment = FullscreenImageDialogFragment.newInstance(imageUrl)
        dialogFragment?.show(supportFragmentManager, "fullscreen_image")
    }

    companion object {
        private const val AUDIO_PERMISSION_REQUEST_CODE = 102

        private val WRITE_EXTERNAL_STORAGE_PERMS = arrayOf(Manifest.permission.RECORD_AUDIO)

        private fun removeTrailingColon(str: String?): String? {
            if (str != null && str.endsWith(":")) {
                return str.substring(0, str.length - 1)
            }
            return str
        }
    }
}