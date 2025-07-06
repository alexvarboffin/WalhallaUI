//package com.kworkapp.audiogid.fragment
//
//import android.content.Context
//import android.graphics.Color
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.DialogFragment
//import androidx.fragment.app.Fragment
//import com.kworkapp.audiogid.databinding.FragmentColorDialogBinding
//import top.defaults.colorpicker.ColorPickerPopup
//import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
//import java.util.Locale
//
//class ColorFragment : DialogFragment(), View.OnClickListener {
//    private var callback: Callback? = null
//
//    private var binding: FragmentColorDialogBinding? = null
//
//    interface Callback {
//        fun changeColor(color: Int)
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentColorDialogBinding.inflate(inflater, container, false)
//        return binding!!.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding!!.buttonClose.setOnClickListener { v: View? -> dismiss() }
//        binding!!.pickedColor.setOnClickListener(this)
//        binding!!.colorHex.setOnClickListener(this)
//        binding!!.resetColor.setOnClickListener { v -> binding!!.colorPicker.reset() }
//
//        binding!!.colorPicker.subscribe { color: Int, fromUser: Boolean, shouldPropagate: Boolean ->
//            binding!!.pickedColor.setBackgroundColor(color)
//            binding!!.colorHex.setText(colorHex(color))
//            if (callback != null) {
//                callback!!.changeColor(color)
//            }
//        }
//        var color = INITIAL_COLOR
//        if (savedInstanceState != null) {
//            color = savedInstanceState.getInt(Customize.SAVED_STATE_KEY_COLOR, INITIAL_COLOR)
//        }
//        binding!!.colorPicker.setInitialColor(color)
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putInt(Customize.SAVED_STATE_KEY_COLOR, binding!!.colorPicker.color)
//    }
//
//    private fun colorHex(color: Int): String {
//        val a = Color.alpha(color)
//        val r = Color.red(color)
//        val g = Color.green(color)
//        val b = Color.blue(color)
//        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", a, r, g, b)
//    }
//
//    override fun onClick(v: View) {
//        ColorPickerPopup.Builder(context)
//            .initialColor(binding!!.colorPicker.color)
//            .enableAlpha(true)
//            .okTitle("Choose")
//            .cancelTitle("Cancel")
//            .showIndicator(true)
//            .showValue(true)
//            .onlyUpdateOnTouchEventUp(true)
//            .build()
//            .show(object : ColorPickerObserver() {
//                override fun onColorPicked(color: Int) {
//                    binding!!.colorPicker.setInitialColor(color)
//                }
//            })
//    }
//
//    override fun onAttachFragment(childFragment: Fragment) {
//        super.onAttachFragment(childFragment)
//        //DLog.d("@@@@@@@@@@@@@@@@@@@@"+childFragment.getClass().getSimpleName());
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        //DLog.d("@@@@@@@@@@@@@@@@@@@@"+context.getClass().getSimpleName());
//    }
//
//    fun bbbb(callback: Callback?) {
//        this.callback = callback
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }
//
//    companion object {
//        private const val INITIAL_COLOR = -0x8000
//    }
//}
