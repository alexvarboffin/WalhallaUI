package com.video.maker.fragment.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hw.photomovie.PhotoMovieFactory
import com.video.maker.R
import com.video.maker.activity.VideoPreviewActivity
import com.video.maker.adapter.TransitionsAdapter
import com.video.maker.adapter.TransitionsAdapter.TransferAdapterListener
import com.video.maker.advertManager.RewardManager.Companion.instance
import com.video.maker.databinding.FragmentMovieTransitionBinding
import com.video.maker.fragment.listeners.TransferFragmentListener
import com.video.maker.model.TransferItem
import com.video.maker.util.KSUtil
import com.walhalla.utils.AManagerI.RewardManagerCallback

class TransitionFragment : FragmentPresenter(), TransferAdapterListener, RewardManagerCallback {
    var adapter: TransitionsAdapter? = null
    var listener: TransferFragmentListener? = null

    private var transferItem: TransferItem? = null
    private var position = 0
    private var binding: FragmentMovieTransitionBinding? = null

    private var m: KSUtil? = null


    override fun onTransferSelected(transferItem: TransferItem?, position: Int) {
        if (m!!.isTransitionpossItemLocked(position)) {
            this.position = position
            this.transferItem = transferItem

            val videoActivity = activity as VideoPreviewActivity?
            if (videoActivity != null) {
                videoActivity.onPauseVideo()
            }
            showPopupDialog(activity
            ) { rm.createRewardedAd(requireActivity(), position, this@TransitionFragment) }
        } else {
            if (this@TransitionFragment.listener != null) {
                this@TransitionFragment.listener!!.onTransfer(transferItem)
            } else {
                Toast.makeText(
                    this@TransitionFragment.activity, this@TransitionFragment.getString(
                        R.string.try_again
                    ), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun successResult7(position: Int) {
        m!!.unlockTransitionpossItem(position)

        if (this@TransitionFragment.listener != null) {
            this@TransitionFragment.listener!!.onTransfer(transferItem)
        } else {
            Toast.makeText(
                this@TransitionFragment.activity, this@TransitionFragment.getString(
                    R.string.try_again
                ), Toast.LENGTH_SHORT
            ).show()
        }
        adapter!!.notifyDataSetChanged()
    }

    public override fun errorShowAds(position: Int) {
        if (listener != null) {
            listener!!.errorShowAds()
        }
    }


    fun setTransferFragmentListener(transferFragmentListener: TransferFragmentListener?) {
        this.listener = transferFragmentListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        m = KSUtil.getInstance(getContext())
        rm = instance
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        binding = FragmentMovieTransitionBinding.inflate(inflater, viewGroup, false)
        this.rm.loadRewardAd(requireActivity())
        setRecyclerTransfer()
        return binding!!.getRoot()
    }

    private fun setRecyclerTransfer() {
        binding!!.recyclerTransfer.setHasFixedSize(true)
        binding!!.recyclerTransfer.setLayoutManager(
            LinearLayoutManager(
                activity,
                RecyclerView.HORIZONTAL,
                false
            )
        )
        val arrayList = ArrayList<TransferItem?>()
        arrayList.add(
            TransferItem(
                R.drawable.gradient_transfer,
                getString(R.string.str_gradient),
                PhotoMovieFactory.PhotoMovieType.GRADIENT
            )
        )
        arrayList.add(
            TransferItem(
                R.drawable.window_transfer,
                getString(R.string.str_window),
                PhotoMovieFactory.PhotoMovieType.WINDOW
            )
        )
        arrayList.add(
            TransferItem(
                R.drawable.random_transfer,
                getString(R.string.str_random),
                PhotoMovieFactory.PhotoMovieType.RANDOM
            )
        )
        arrayList.add(
            TransferItem(
                R.drawable.tranlastion_transfer,
                getString(R.string.str_tranlation),
                PhotoMovieFactory.PhotoMovieType.SCALE_TRANS
            )
        )
        arrayList.add(
            TransferItem(
                R.drawable.leftright_transfer,
                getString(R.string.str_leftright),
                PhotoMovieFactory.PhotoMovieType.HORIZONTAL_TRANS
            )
        )
        arrayList.add(
            TransferItem(
                R.drawable.up_down_transfer,
                getString(R.string.str_updown),
                PhotoMovieFactory.PhotoMovieType.VERTICAL_TRANS
            )
        )
        arrayList.add(
            TransferItem(
                R.drawable.thaw_transfer,
                getString(R.string.str_thaw),
                PhotoMovieFactory.PhotoMovieType.THAW
            )
        )
        arrayList.add(
            TransferItem(
                R.drawable.scale_transfer,
                getString(R.string.str_scale),
                PhotoMovieFactory.PhotoMovieType.SCALE
            )
        )


        val mm = m!!.transitionposs0

        //(position + 1) >= 3 && !KSUtil.Transitionposs.contains(position)
        val transferAdapter =
            TransitionsAdapter(arrayList, activity, m!!.transitionposs0, this)
        this.adapter = transferAdapter
        binding!!.recyclerTransfer.setAdapter(transferAdapter)
    }
}
