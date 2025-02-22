package com.kworkapp.audiogid.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.chibde.BaseVisualizer
import com.chibde.visualizer.BarVisualizer
import com.chibde.visualizer.CircleBarVisualizer
import com.chibde.visualizer.CircleVisualizer
import com.chibde.visualizer.LineBarVisualizer
import com.chibde.visualizer.LineVisualizer
import com.chibde.visualizer.SquareBarVisualizer
import com.kworkapp.audiogid.R

class VisualizerContainer : FrameLayout {

    private val layouts = intArrayOf( //            R.layout.activity_line_bar_visualizer,
        //            R.layout.activity_bar_visualizer,
        //            R.layout.activity_circle_bar_visualizer,
        //            R.layout.activity_circle_visualizer,
        //            R.layout.activity_line_visualizer,
        R.layout.activity_square_bar_visualizer
    )

    private var currentIndex = 0

    private var mVisualizer: BaseVisualizer? = null

    //private Visualizer mVisualizer;
    private val audioSessionId = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }


    interface VisualizerCallback {
        fun initialize(m: BaseVisualizer)
    }

    var clb: VisualizerCallback? = null

    fun setCallback(callback: VisualizerCallback) {
        this.clb = callback
        mVisualizer?.let { callback.initialize(it) }
    }

    private fun init(context: Context) {
        currentIndex = currentIndexFromPreferences
        mVisualizer = init0(context, layouts[currentIndex])
        mVisualizer?.let {
            switchVisualizer(it)
            if (clb != null) {
                clb!!.initialize(it)
            }
        }
    }

    fun showNextVisualizer() {
        val m = nextLayout
        mVisualizer = init0(getContext(), m)
        mVisualizer?.let {
            switchVisualizer(it)
            if (clb != null) {
                clb!!.initialize(it)
            }
        }
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    fun showBarVisualizer() {
        val barVisualizer: BarVisualizer = BarVisualizer(getContext())
        switchVisualizer(barVisualizer)
    }

    fun showCircleBarVisualizer() {
        val circleBarVisualizer: CircleBarVisualizer = CircleBarVisualizer(getContext())
        switchVisualizer(circleBarVisualizer)
    }

    private fun switchVisualizer(newVisualizer: BaseVisualizer) {
        removeAllViews()

        newVisualizer.setLayoutParams(
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        addView(newVisualizer)

        //        if (mVisualizer != null) {
//            mVisualizer.release();
//        }
//
//        mVisualizer = new Visualizer(audioSessionId);
//        if (newVisualizer instanceof BarVisualizer) {
//            ((BarVisualizer) newVisualizer).setAudioSessionId(audioSessionId);
//        } else if (newVisualizer instanceof CircleBarVisualizer) {
//            ((CircleBarVisualizer) newVisualizer).setAudioSessionId(audioSessionId);
//        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mVisualizer != null) {
            mVisualizer?.release()
        }
    }


    private fun init0(context: Context, type: Int): BaseVisualizer {
        if (type == R.layout.activity_bar_visualizer) {
            val visualizer: BarVisualizer = BarVisualizer(getContext())
            visualizer.setColor(ContextCompat.getColor(context, R.color.def_visualiserColor))
            // define custom number of bars you want in the visualizer between (10 - 256).
            visualizer.setDensity(70F)
            return visualizer
        } else if (type == R.layout.activity_circle_bar_visualizer) {
            val circleBarVisualizer: CircleBarVisualizer = CircleBarVisualizer(getContext())
            circleBarVisualizer.setColor(
                ContextCompat.getColor(
                    context,
                    R.color.def_visualiserColor
                )
            )
            return circleBarVisualizer
        } else if (type == R.layout.activity_circle_visualizer) {
            val circleVisualizer: CircleVisualizer = CircleVisualizer(getContext())
            circleVisualizer.setColor(ContextCompat.getColor(context, R.color.def_visualiserColor))
            // Customize the size of the circle. by defalut multipliers is 1.
            circleVisualizer.setRadiusMultiplier(2f)
            // set the line with for the visualizer between 1-10 default 1.
            circleVisualizer.setStrokeWidth(1)
            return circleVisualizer
        } else if (type == R.layout.activity_line_bar_visualizer) {
            val lineBarVisualizer: LineBarVisualizer = LineBarVisualizer(getContext())
            lineBarVisualizer.setColor(ContextCompat.getColor(context, R.color.def_visualiserColor))
            // define custom number of bars you want in the visualizer between (10 - 256).
            lineBarVisualizer.setDensity(90f)
            return lineBarVisualizer
        } else if (type == R.layout.activity_line_visualizer) {
            val lineVisualizer: LineVisualizer = LineVisualizer(getContext())
            lineVisualizer.setColor(ContextCompat.getColor(context, R.color.def_visualiserColor))
            // set the line with for the visualizer between 1-10 default 1.
            lineVisualizer.setStrokeWidth(1)
            return lineVisualizer
        } else if (type == R.layout.activity_square_bar_visualizer) {
            val squareBarVisualizer: SquareBarVisualizer = SquareBarVisualizer(getContext())
            squareBarVisualizer.setColor(
                ContextCompat.getColor(
                    context,
                    R.color.def_visualiserColor
                )
            )
            // define custom number of bars you want in the visualizer between (10 - 256).
            squareBarVisualizer.setDensity(65F)
            // set Gap
            squareBarVisualizer.setGap(2)
            return squareBarVisualizer
        }
        val view1: CircleVisualizer = CircleVisualizer(getContext())
        view1.setColor(ContextCompat.getColor(context, R.color.def_visualiserColor))
        view1.setRadiusMultiplier(2f)
        // set the line with for the visualizer between 1-10 default 1.
        view1.setStrokeWidth(1)
        return view1
    }

    private val nextLayout: Int
        get() {
            currentIndex = (currentIndex + 1) % layouts.size
            saveCurrentIndexToPreferences(currentIndex)
            return layouts[currentIndex]
        }

    private fun saveCurrentIndexToPreferences(index: Int) {
        val prefs: SharedPreferences =
            getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putInt(KEY_CURRENT_INDEX, index)
        editor.apply()
    }

    private val currentIndexFromPreferences: Int
        get() {
            val prefs: SharedPreferences = getContext().getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            return prefs.getInt(KEY_CURRENT_INDEX, 0)
        }

    companion object {
        private const val PREFS_NAME = "VisualizerPrefs"
        private const val KEY_CURRENT_INDEX = "currentIndex00"
    }
}
