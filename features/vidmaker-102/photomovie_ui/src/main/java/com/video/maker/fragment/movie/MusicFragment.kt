package com.video.maker.fragment.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.video.maker.R
import com.video.maker.activity.VideoPreviewActivity
import com.video.maker.adapter.MusicAdapter
import com.video.maker.adapter.MusicAdapter.MusicAdapterListener
import com.video.maker.advertManager.RewardManager.Companion.instance
import com.video.maker.databinding.FragmentMovieMusicBinding
import com.video.maker.fragment.listeners.MusicFragmentListener
import com.video.maker.model.MusicType
import com.video.maker.util.KSUtil
import com.walhalla.utils.AManagerI.RewardManagerCallback

class MusicFragment : FragmentPresenter(), MusicAdapterListener, RewardManagerCallback {
    private var position = 0
    var adapter: MusicAdapter? = null

    var listener: MusicFragmentListener? = null
    private var binding: FragmentMovieMusicBinding? = null

    private var m: KSUtil? = null


    override fun onMusicSelected(position: Int) {
        this.position = position
        if (m!!.isMusicpossItemLocked(position)) {
            val videoActivity = getActivity() as VideoPreviewActivity?
            if (videoActivity != null) {
                videoActivity.onPauseVideo()
            }
            showPopupDialog(activity) { rm.createRewardedAd(requireActivity(), position, this@MusicFragment) }
        } else {
            if (this@MusicFragment.listener != null) {
                this@MusicFragment.listener!!.onTypeMusic(position)
                binding!!.imgUnChecked.setImageResource(0)
                return
            }
            Toast.makeText(
                this@MusicFragment.activity,
                this@MusicFragment.getString(R.string.try_again),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun setMusicFragmentListener(musicFragmentListener: MusicFragmentListener?) {
        this.listener = musicFragmentListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rm = instance

        m = KSUtil.getInstance(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        binding = FragmentMovieMusicBinding.inflate(inflater, viewGroup, false)
        rm.loadRewardAd(requireActivity())

        setRecyclerMusic()

        binding!!.btnMyMusic.setOnClickListener(View.OnClickListener { view: View? ->
            if (this@MusicFragment.listener != null) {
                this@MusicFragment.listener!!.onMyMusic()
                this@MusicFragment.adapter!!.setRowSelected(-1)
                this@MusicFragment.adapter!!.notifyDataSetChanged()
                binding!!.imgUnChecked.setImageResource(R.drawable.ic_music_check_shape)
                return@OnClickListener
            }
            Toast.makeText(
                this@MusicFragment.activity,
                this@MusicFragment.getString(R.string.try_again),
                Toast.LENGTH_SHORT
            ).show()
        })
        return binding!!.getRoot()
    }

    private fun setRecyclerMusic() {
        binding!!.recyclerMusic.setHasFixedSize(true)
        binding!!.recyclerMusic.setLayoutManager(
            LinearLayoutManager(
                activity,
                RecyclerView.HORIZONTAL,
                false
            )
        )
        val arrayList = ArrayList<MusicType?>()
        arrayList.add(MusicType(R.drawable.love, getString(R.string.love)))
        arrayList.add(MusicType(R.drawable.friendship, getString(R.string.friend)))
        arrayList.add(MusicType(R.drawable.christmas, getString(R.string.christmas)))
        arrayList.add(MusicType(R.drawable.positive, getString(R.string.positive)))
        arrayList.add(MusicType(R.drawable.movie, getString(R.string.movie)))
        arrayList.add(MusicType(R.drawable.beach, getString(R.string.beach)))
        arrayList.add(MusicType(R.drawable.summer, getString(R.string.summer)))
        arrayList.add(MusicType(R.drawable.travel, getString(R.string.travel)))
        arrayList.add(MusicType(R.drawable.happy, getString(R.string.happy)))
        val musicAdapter = MusicAdapter(arrayList, activity, m!!.musicposs, this)
        this.adapter = musicAdapter
        binding!!.recyclerMusic.setAdapter(musicAdapter)
    }


    override fun successResult7(position: Int) {
        m!!.unlockMusicpossItem(position)

        if (this@MusicFragment.listener != null) {
            this@MusicFragment.listener!!.onTypeMusic(position)
            binding!!.imgUnChecked.setImageResource(0)
        }
        Toast.makeText(
            this@MusicFragment.activity,
            this@MusicFragment.getString(R.string.try_again),
            Toast.LENGTH_SHORT
        ).show()

        adapter!!.notifyDataSetChanged()
    }

    override fun errorShowAds(position: Int) {
        if (listener != null) {
            listener!!.errorShowAds()
        }
    }
}
