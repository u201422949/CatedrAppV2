package pe.com.creamos.catedrappv2.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.TextView
import pe.com.creamos.catedrappv2.R
import java.lang.reflect.Field

class OutlineTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    private var colorField: Field? = null
    private var textColor = 0
    private var outlineColor = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        android.R.attr.textViewStyle
    )

    override fun setTextColor(color: Int) {
        // We want to track this ourselves
        // The super call will invalidate()
        textColor = color
        super.setTextColor(color)
    }

    fun setOutlineColor(color: Int) {
        outlineColor = color
        invalidate()
    }

    fun setOutlineWidth(width: Float) {
        setOutlineStrokeWidth(width)
        invalidate()
    }

    private fun setOutlineStrokeWidth(width: Float) {
        paint.strokeWidth = 2 * width + 1
    }

    override fun onDraw(canvas: Canvas?) {
        // If we couldn't get the Field, then we
        // need to skip this, and just draw as usual
        if (colorField != null) {
            // Outline
            setColorField(outlineColor)
            paint.style = Paint.Style.STROKE
            super.onDraw(canvas)

            // Reset for text
            setColorField(textColor)
            paint.style = Paint.Style.FILL
        }
        super.onDraw(canvas)
    }

    private fun setColorField(color: Int) {
        // We did the null check in onDraw()
        try {
            colorField?.setInt(this, color)
        } catch (e: IllegalAccessException) {
            // Optionally catch Exception and remove print after testing
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    // Optional saved state stuff
    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.textColor = textColor
        ss.outlineColor = outlineColor
        ss.outlineWidth = paint.strokeWidth
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss: SavedState = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        textColor = ss.textColor
        outlineColor = ss.outlineColor
        paint.strokeWidth = ss.outlineWidth
    }

    private class SavedState : BaseSavedState {
        var textColor = 0
        var outlineColor = 0
        var outlineWidth = 0f

        internal constructor(superState: Parcelable?) : super(superState)
        private constructor(parcel: Parcel) : super(parcel) {
            textColor = parcel.readInt()
            outlineColor = parcel.readInt()
            outlineWidth = parcel.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(textColor)
            out.writeInt(outlineColor)
            out.writeFloat(outlineWidth)
        }

        companion object {
            val CREATOR: Parcelable.Creator<SavedState?> =
                object : Parcelable.Creator<SavedState?> {
                    override fun createFromParcel(parcel: Parcel): SavedState? {
                        return SavedState(parcel)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    init {
        try {
            colorField = TextView::class.java.getDeclaredField("mCurTextColor")
            colorField?.isAccessible = true

            // If the reflection fails (which really shouldn't happen), we
            // won't need the rest of this stuff, so we keep it in the try-catch
            textColor = textColors.defaultColor

            // These can be changed to hard-coded default
            // values if you don't need to use XML attributes
            val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.OutlineTextView)
            outlineColor = a.getColor(R.styleable.OutlineTextView_outlineColor, Color.TRANSPARENT)
            setOutlineStrokeWidth(
                a.getDimensionPixelSize(
                    R.styleable.OutlineTextView_outlineWidth,
                    0
                ).toFloat()
            )
            a.recycle()
        } catch (e: NoSuchFieldException) {
            // Optionally catch Exception and remove print after testing
            e.printStackTrace()
            colorField = null
        }
    }
}