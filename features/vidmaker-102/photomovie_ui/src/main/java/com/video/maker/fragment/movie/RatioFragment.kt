package com.video.maker.fragment.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.video.maker.R
import com.video.maker.advertManager.RewardManager.Companion.instance
import com.video.maker.databinding.FragmentMovieRatioBinding
import com.video.maker.fragment.listeners.RatioFragmentListener
import com.walhalla.utils.AManagerI.RewardManagerCallback
import androidx.core.view.isGone

class RatioFragment : FragmentPresenter(), View.OnClickListener, RewardManagerCallback {
    private var listener: RatioFragmentListener? = null
    private var binding: FragmentMovieRatioBinding? = null
    private var position = 0


    //Set<Integer> unlockedItems = new HashSet<>();
    fun setRatioFragmentListener(ratioFragmentListener: RatioFragmentListener?) {
        this.listener = ratioFragmentListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rm = instance
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        binding = FragmentMovieRatioBinding.inflate(layoutInflater, viewGroup, false)
        binding!!.btnSquare.setOnClickListener(this)
        binding!!.btn34.setOnClickListener(this)
        binding!!.btn43.setOnClickListener(this)
        binding!!.btn169.setOnClickListener(this)
        binding!!.btn916.setOnClickListener(this)


        //wads
        //unlockedItems.add();
        binding!!.premium.setVisibility(View.GONE)
        binding!!.premium1.setVisibility(View.GONE)
        binding!!.premium2.setVisibility(View.GONE)


        rm.loadRewardAd(requireActivity())
        setBorder(0)
        return binding!!.getRoot()
    }

    override fun onClick(view: View) {
        if (listener != null) {
            val id = view.getId()
            if (id == R.id.btn_square) {
                listener!!.onRatio(11)
                setBorder(View.VISIBLE)
            } else if (id == R.id.btn169) {
                if (binding!!.premium.isGone) {
                    listener!!.onRatio(169)
                    setBorder(2)
                } else {
                    Log.e("onClick: ", "1111")
                    showPopupDialog(getActivity(), 169)
                    setBorder(View.VISIBLE)
                }
            } else if (id == R.id.btn34) {
                if (binding!!.premium1.isGone) {
                    listener!!.onRatio(34)
                    setBorder(3)
                } else {
                    Log.e("onClick: ", "2222")
                    showPopupDialog(getActivity(), 34)
                    setBorder(View.VISIBLE)
                }
            } else if (id == R.id.btn43) {
                if (binding!!.premium2.getVisibility() == View.GONE) {
                    listener!!.onRatio(43)
                    setBorder(4)
                } else {
                    Log.e("onClick: ", "3333")
                    showPopupDialog(getActivity(), 43)
                    setBorder(View.VISIBLE)
                }
            } else if (id == R.id.btn916) {
                listener!!.onRatio(916)
                setBorder(1)
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPopupDialog(activity: FragmentActivity?, i: Int) {
        this.position = i
        showPopupDialog(activity, object : View.OnClickListener {
            override fun onClick(v: View?) {
                rm.createRewardedAd(requireActivity(), position, this@RatioFragment)
            }
        })
    }

    private fun setBorder(i: Int) {
        val frameLayouts = arrayOf<FrameLayout?>(
            binding!!.border1,
            binding!!.border2,
            binding!!.border3,
            binding!!.border4,
            binding!!.border5
        )
        for (currentIndex in frameLayouts.indices) {
            frameLayouts[currentIndex]!!.setVisibility(if (currentIndex == i) View.VISIBLE else View.GONE)
        }
    }

    override fun successResult7(position: Int) {
        if (position == 169) {
            binding!!.premium.setVisibility(View.GONE)
            listener!!.onRatio(169)
            setBorder(2)
        } else if (position == 34) {
            binding!!.premium1.setVisibility(View.GONE)
            listener!!.onRatio(34)
            setBorder(3)
        } else if (position == 43) {
            binding!!.premium2.setVisibility(View.GONE)
            listener!!.onRatio(43)
            setBorder(4)
        }
    }

    override fun errorShowAds(position: Int) {
        if (listener != null) {
            listener!!.errorShowAds()
        }
    }
}

