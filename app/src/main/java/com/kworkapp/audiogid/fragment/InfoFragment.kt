package com.kworkapp.audiogid.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kworkapp.audiogid.CardUtils
import com.kworkapp.audiogid.R
import com.kworkapp.audiogid.SimpleQuest
import com.kworkapp.audiogid.databinding.FragmentInfoBinding
import com.kworkapp.audiogid.player.PlayerManager
import com.kworkapp.audiogid.utils.TextUtils
import java.util.Locale

class InfoFragment : Fragment(), SimpleQuest {
    private var binding: FragmentInfoBinding? = null
    private var currentItemIndex = 0
    private var currentSectionIndex = 0
    private var presenter: PlayerManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = PlayerManager.getInstance(requireContext())
        if (arguments != null) {
            currentItemIndex = requireArguments().getInt(ARG_INDEX)
            currentSectionIndex = requireArguments().getInt(ARG_SECTION_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        setDescription(currentItemIndex, currentSectionIndex)
    }

    private fun setDescription(index: Int, sectionNumber: Int) {
        //String description = "***";
        val timings = presenter!!.getSectionItems0(sectionNumber)
        val item = timings[index]

        //        if (desc.containsKey(item.name)) {
//            Item itemDesc = desc.get(item.name);
//            if (itemDesc != null) {
//                description = itemDesc.getDescription();
//            } else {
//                description = "" + item;
//            }
//        }

        //String sectionNameFromDescription = item.getTimingName();
        val sectionNameFromDescription = item.descriptionName

        val section = presenter!!.getSection(sectionNumber)
        val regionName = section.timingRegionName


        val sectionNameFromDescriptionLower =
            sectionNameFromDescription!!.lowercase(Locale.getDefault())
        val regionNameLower = regionName.lowercase(Locale.getDefault())

        //DLog.d("@@" + sectionNameFromDescription);
        var avatarRes: Int = -1
        var imageUrl: String? = null

        val sectionNameFromDescriptionLowerToLowerCase = sectionNameFromDescriptionLower.lowercase(
            Locale.getDefault()
        )
        if (sectionNameFromDescriptionLowerToLowerCase.contains("предисловие")
            || sectionNameFromDescriptionLowerToLowerCase.contains("подводка")
            || sectionNameFromDescriptionLowerToLowerCase.contains(" район")
            || sectionNameFromDescriptionLower.contains(regionNameLower)
        ) {
            //binding.image.setVisibility(View.GONE);

            avatarRes = CardUtils.sectionImage(sectionNumber, index)
            imageUrl = ""
        } else {
            val tmp = String.format("images/%1s", regionName)
            val jpgFiles = TextUtils.getJpgFilesFromAssets2(
                requireContext(), tmp, sectionNameFromDescription
            )
            imageUrl = if (!jpgFiles.isEmpty()) {
                jpgFiles[0]
            } else {
                ""
            }
            avatarRes = -1

            //binding.image0.setOnTouchListener(new ImageMatrixTouchHandler(binding.image0.getContext()));
        }
        setCardData(
            sectionNameFromDescription, item.description!!,
            item.link!!, imageUrl, avatarRes
        )
    }

    override fun setCardData(
        title: String,
        description: String,
        link: String,
        imageUrl: String,
        avatarRes: Int
    ) {
        binding!!.subtitle.text = title

        //binding.subtitle.setText(currentSectionIndex + "@" + currentItemIndex + "|" + title);
        binding!!.description.text = description
        binding!!.link.text = link

        if (avatarRes != null) {
            binding!!.avatar.setImageResource(avatarRes)
            binding!!.avatar.visibility = View.VISIBLE
        } else {
            binding!!.avatar.visibility = View.GONE
        }

        if (imageUrl != null) {
            //binding.image0.setImage(ImageSource.uri(jpgFiles.get(0)));
            Glide.with(this)
                .load(Uri.parse(imageUrl)) //                        .apply(new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading_placeholder) //                                .error(R.drawable.im_error))
                .into(binding!!.image0)
            //                BaseBannerAdapter webBannerAdapter = new BaseBannerAdapter(this, jpgFiles);
//                webBannerAdapter.setOnBannerItemClickListener(position -> {
//
//                });
//                binding.image.setAdapter(webBannerAdapter);
            binding!!.image0.visibility = View.VISIBLE
            binding!!.image0.setOnClickListener { v: View? ->
                if (callback != null) {
                    callback!!.fullScreenImage(imageUrl)
                }
            }
        } else {
            binding!!.image0.visibility = View.GONE
        }
    }

    protected var callback: QCallback? = null


    //protected View mRootView;
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is QCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement IOnFragmentInteractionListener")
        }
    }

    companion object {
        private const val ARG_INDEX = "index"
        private const val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(index: Int, sectionNumber: Int): InfoFragment {
            val fragment = InfoFragment()
            val args = Bundle()
            args.putInt(ARG_INDEX, index)
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}
