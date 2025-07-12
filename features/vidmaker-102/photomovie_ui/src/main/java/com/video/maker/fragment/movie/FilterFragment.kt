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
import com.video.maker.adapter.FilterAdapter
import com.video.maker.adapter.FilterAdapter.FilterAdapterListener
import com.video.maker.advertManager.RewardManager.Companion.instance
import com.video.maker.fragment.listeners.FilterFragmentListener
import com.video.maker.model.FilterItem
import com.video.maker.model.FilterType
import com.video.maker.util.DLog
import com.video.maker.util.KSUtil
import com.walhalla.utils.AManagerI.RewardManagerCallback

//import com.applovin.mediation.MaxAd;
//import com.applovin.mediation.MaxError;
//import com.applovin.mediation.MaxReward;
//import com.applovin.mediation.MaxRewardedAdListener;
//import com.applovin.mediation.ads.MaxRewardedAd;
class FilterFragment : FragmentPresenter(), FilterAdapterListener, RewardManagerCallback {
    var filterAdapter: FilterAdapter? = null
    var listener: FilterFragmentListener? = null
    var recyclerFilter: RecyclerView? = null

    private var position = 0
    private var filterItem: FilterItem? = null

    private var m: KSUtil? = null


    override fun onFilterSelected(filterItem: FilterItem?, position: Int) {
        if (m!!.isFilterItemLocked(position)) {
            this.position = position
            this.filterItem = filterItem
            val videoActivity = activity as VideoPreviewActivity?
            if (videoActivity != null) {
                videoActivity.onPauseVideo()
            }
            showPopupDialog(activity) { v: View? ->
                rm.createRewardedAd(
                    requireActivity(), position, this@FilterFragment
                )
            }
        } else {
            if (this@FilterFragment.listener != null) {
                this@FilterFragment.listener!!.onFilter(filterItem)
            } else {
                Toast.makeText(
                    this@FilterFragment.activity,
                    this@FilterFragment.getString(R.string.try_again),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rm = instance
        m = KSUtil.getInstance(context)
    }

    fun setFilterFragmentListener(filterFragmentListener: FilterFragmentListener?) {
        this.listener = filterFragmentListener
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        val inflate = layoutInflater.inflate(R.layout.fragment_movie_filter, viewGroup, false)
        rm.loadRewardAd(requireActivity())
        this.recyclerFilter = inflate.findViewById<RecyclerView>(R.id.recyclerFilter)
        setRecyclerFilter()
        return inflate
    }

    private fun setRecyclerFilter() {
        this.recyclerFilter!!.setHasFixedSize(true)
        this.recyclerFilter!!.setLayoutManager(
            LinearLayoutManager(
                getActivity(),
                RecyclerView.HORIZONTAL,
                false
            )
        )
        val arrayList = ArrayList<FilterItem?>()
        arrayList.add(FilterItem(R.drawable.filter, "Normal", FilterType.NONE))
        arrayList.add(FilterItem(R.drawable.filter_gray, "Black White", FilterType.GRAY))
        arrayList.add(FilterItem(R.drawable.filter_snow, "Snow", FilterType.SNOW))
        arrayList.add(FilterItem(R.drawable.filter_l1, "Walden", FilterType.LUT1))
        arrayList.add(FilterItem(R.drawable.filter_l2, "Beauty", FilterType.LUT2))
        arrayList.add(FilterItem(R.drawable.filter_l3, "Reddest", FilterType.LUT3))
        arrayList.add(FilterItem(R.drawable.filter_l4, "Vintage", FilterType.LUT4))
        arrayList.add(FilterItem(R.drawable.filter_l5, "Lasso", FilterType.LUT5))
        arrayList.add(FilterItem(R.drawable.cameo, "Cameo", FilterType.CAMEO))

        this.filterAdapter = FilterAdapter(arrayList, activity, m!!.filterposs, this)
        this.recyclerFilter!!.setAdapter(filterAdapter)
    }


    override fun successResult7(position: Int) {
        DLog.d("@@@@@@")
        m!!.unlockFilterItem(position)

        if (this@FilterFragment.listener != null) {
            this@FilterFragment.listener!!.onFilter(filterItem)
        } else {
            Toast.makeText(
                this@FilterFragment.activity,
                this@FilterFragment.getString(R.string.try_again),
                Toast.LENGTH_SHORT
            ).show()
        }
        filterAdapter!!.notifyDataSetChanged()
    }

    override fun errorShowAds(position: Int) {
        if (listener != null) {
            listener!!.errorShowAds()
        }
    }

    companion object {
        private const val SKIP_IF_AD_ERROR_ENABLED = true
    }
}
