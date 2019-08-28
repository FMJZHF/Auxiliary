package com.zhf.auxiliaryjar.picker

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.zhf.auxiliaryjar.R

import java.util.ArrayList
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
* Android 选择器 单项滚动选择
* Created by zhf on 2019/8/28 14:06
*/
class WheelPickerView : View {

    private val mExecutor = Executors.newSingleThreadScheduledExecutor()
    private var mScheduledFuture: ScheduledFuture<*>? = null
    private var mTotalScrollY: Int = 0
    private var mLoopListener: WheelPickerScrollListener? = null
    private var mGestureDetector: GestureDetector? = null
    var selectedItem: Int = 0
        private set
    private var mOnGestureListener: GestureDetector.SimpleOnGestureListener? = null
    private var mContext: Context? = null
    private var mTopBottomTextPaint: Paint? = null  //paint that draw top and bottom text
    private var mCenterTextPaint: Paint? = null  // paint that draw center text
    private var mCenterLinePaint: Paint? = null  // paint that draw line besides center text
    private var mDataList: ArrayList<*>? = null
    private var mTextSize: Int = 0
    private var mMaxTextWidth: Int = 0
    private var mMaxTextHeight: Int = 0
    private var mTopBottomTextColor: Int = 0
    private var mCenterTextColor: Int = 0
    private var mCenterLineColor: Int = 0
    private var lineSpacingMultiplier: Float = 0.toFloat()
    private var mCanLoop: Boolean = false
    private var mTopLineY: Int = 0
    private var mBottomLineY: Int = 0
    private var mCurrentIndex: Int = 0
    private var mInitPosition: Int = 0
    private var mPaddingLeftRight: Int = 0
    private var mPaddingTopBottom: Int = 0
    private var mItemHeight: Float = 0.toFloat()
    private var mDrawItemsCount: Int = 0
    private var mCircularDiameter: Int = 0
    private var mWidgetHeight: Int = 0
    private var mCircularRadius: Int = 0
    private var mWidgetWidth: Int = 0


    var mHandler = Handler(Handler.Callback { msg ->
        if (msg.what == MSG_INVALIDATE)
            invalidate()
        if (msg.what == MSG_SCROLL_LOOP)
            startSmoothScrollTo()
        else if (msg.what == MSG_SELECTED_ITEM)
            itemSelected()
        false
    })

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initView(context, attrs)
    }


    private fun initView(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.WheelPickerView)
        if (array != null) {
            mTopBottomTextColor = array.getColor(R.styleable.WheelPickerView_topBottomTextColor, -0x505051)
            mCenterTextColor = array.getColor(R.styleable.WheelPickerView_centerTextColor, -0xcececf)
            mCenterLineColor = array.getColor(R.styleable.WheelPickerView_lineColor, -0x3a3a3b)
            mCanLoop = array.getBoolean(R.styleable.WheelPickerView_canLoop, true)
            mInitPosition = array.getInt(R.styleable.WheelPickerView_initPosition, -1)
            mTextSize = array.getDimensionPixelSize(R.styleable.WheelPickerView_textSize, sp2px(context, 16f))
            mDrawItemsCount = array.getInt(R.styleable.WheelPickerView_drawItemCount, 7)
            array.recycle()
        }

        lineSpacingMultiplier = 2.0f

        this.mContext = context

        mOnGestureListener = LoopViewGestureListener()

        mTopBottomTextPaint = Paint()
        mCenterTextPaint = Paint()
        mCenterLinePaint = Paint()

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

        mGestureDetector = GestureDetector(context, mOnGestureListener)
        mGestureDetector!!.setIsLongpressEnabled(false)
    }


    private fun initData() {

        if (mDataList == null) {
            throw IllegalArgumentException("data list must not be null!")
        }
        mTopBottomTextPaint!!.color = mTopBottomTextColor
        mTopBottomTextPaint!!.isAntiAlias = true
        mTopBottomTextPaint!!.typeface = Typeface.MONOSPACE
        mTopBottomTextPaint!!.textSize = mTextSize.toFloat()

        mCenterTextPaint!!.color = mCenterTextColor
        mCenterTextPaint!!.isAntiAlias = true
        mCenterTextPaint!!.textScaleX = 1.05f
        mCenterTextPaint!!.typeface = Typeface.MONOSPACE
        mCenterTextPaint!!.textSize = mTextSize.toFloat()

        mCenterLinePaint!!.color = mCenterLineColor
        mCenterLinePaint!!.isAntiAlias = true
        mCenterLinePaint!!.typeface = Typeface.MONOSPACE
        mCenterLinePaint!!.textSize = mTextSize.toFloat()

        measureTextWidthHeight()

        //计算半圆周 -- mMaxTextHeight * lineSpacingMultiplier 表示每个item的高度  mDrawItemsCount = 7
        //实际显示5个,留两个是在圆周的上下面
        //lineSpacingMultiplier是指text上下的距离的值和maxTextHeight一样的意思 所以 = 2
        //mDrawItemsCount - 1 代表圆周的上下两面各被剪切了一半 相当于高度少了一个 mMaxTextHeight
        val mHalfCircumference =
            (mMaxTextHeight.toFloat() * lineSpacingMultiplier * (mDrawItemsCount - 1).toFloat()).toInt()
        //the diameter of circular 2πr = cir, 2r = height
        mCircularDiameter = (mHalfCircumference * 2 / Math.PI).toInt()
        //the radius of circular
        mCircularRadius = (mHalfCircumference / Math.PI).toInt()
        //  通过控件的高度来计算圆弧的周长
        if (mInitPosition == -1) {
            if (mCanLoop) {
                mInitPosition = (mDataList!!.size + 1) / 2
            } else {
                mInitPosition = 0
            }
        }
        mCurrentIndex = mInitPosition
        invalidate()
    }

    private fun measureTextWidthHeight() {
        val rect = Rect()
        for (i in mDataList!!.indices) {
            val s1 = mDataList!![i] as String
            mCenterTextPaint!!.getTextBounds(s1, 0, s1.length, rect)
            val textWidth = rect.width()
            if (textWidth > mMaxTextWidth) {
                mMaxTextWidth = textWidth
            }
            val textHeight = rect.height()
            if (textHeight > mMaxTextHeight) {
                mMaxTextHeight = textHeight
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidgetWidth = measuredWidth
        mWidgetHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        mItemHeight = lineSpacingMultiplier * mMaxTextHeight
        //auto calculate the text's left/right value when draw
        mPaddingLeftRight = (mWidgetWidth - mMaxTextWidth) / 2
        mPaddingTopBottom = (mWidgetHeight - mCircularDiameter) / 2
        //topLineY = diameter/2 - itemHeight(mItemHeight)/2 + mPaddingTopBottom
        mTopLineY = ((mCircularDiameter - mItemHeight) / 2.0f).toInt() + mPaddingTopBottom
        mBottomLineY = ((mCircularDiameter + mItemHeight) / 2.0f).toInt() + mPaddingTopBottom
    }


    override fun onDraw(canvas: Canvas) {
        if (mDataList == null) {
            super.onDraw(canvas)
            return
        }
        super.onDraw(canvas)
        //the length of single item is mItemHeight
        val mChangingItem = (mTotalScrollY / mItemHeight).toInt()
        mCurrentIndex = mInitPosition + mChangingItem % mDataList!!.size
        if (!mCanLoop) { // can loop
            if (mCurrentIndex < 0) {
                mCurrentIndex = 0
            }
            if (mCurrentIndex > mDataList!!.size - 1) {
                mCurrentIndex = mDataList!!.size - 1
            }
        } else { //can not loop
            if (mCurrentIndex < 0) {
                mCurrentIndex = mDataList!!.size + mCurrentIndex
            }
            if (mCurrentIndex > mDataList!!.size - 1) {
                mCurrentIndex = mCurrentIndex - mDataList!!.size
            }
        }

        var count = 0
        val itemCount = arrayOfNulls<String>(mDrawItemsCount)
        //reconfirm each item's value from dataList according to currentIndex,
        while (count < mDrawItemsCount) {
            var templateItem = mCurrentIndex - (mDrawItemsCount / 2 - count)
            if (mCanLoop) {
                if (templateItem < 0) {
                    templateItem = templateItem + mDataList!!.size
                }
                if (templateItem > mDataList!!.size - 1) {
                    templateItem = templateItem - mDataList!!.size
                }
                itemCount[count] = mDataList!![templateItem] as String
            } else if (templateItem < 0) {
                itemCount[count] = ""
            } else if (templateItem > mDataList!!.size - 1) {
                itemCount[count] = ""
            } else {
                itemCount[count] = mDataList!![templateItem] as String
            }
            count++
        }

        //draw top and bottom line
        canvas.drawLine(0.0f, mTopLineY.toFloat(), mWidgetWidth.toFloat(), mTopLineY.toFloat(), mCenterLinePaint!!)
        canvas.drawLine(
            0.0f,
            mBottomLineY.toFloat(),
            mWidgetWidth.toFloat(),
            mBottomLineY.toFloat(),
            mCenterLinePaint!!
        )

        count = 0
        val changingLeftY = (mTotalScrollY % mItemHeight).toInt()
        while (count < mDrawItemsCount) {
            canvas.save()
            // L= å * r -> å = rad
            val itemHeight = mMaxTextHeight * lineSpacingMultiplier
            //get radian  L = (itemHeight * count - changingLeftY),r = mCircularRadius
            val radian = ((itemHeight * count - changingLeftY) / mCircularRadius).toDouble()
            // a = rad * 180 / π
            //get angle
            val angle = (radian * 180 / Math.PI).toFloat()

            //when angle >= 180 || angle <= 0 don't draw
            if (angle >= 180f || angle <= 0f) {
                canvas.restore()
            } else {
                // translateY = r - r*cos(å) -
                //(Math.sin(radian) * mMaxTextHeight) / 2 this is text offset
                val translateY =
                    (mCircularRadius.toDouble() - Math.cos(radian) * mCircularRadius - Math.sin(radian) * mMaxTextHeight / 2).toInt() + mPaddingTopBottom
                canvas.translate(0.0f, translateY.toFloat())
                //scale offset = Math.sin(radian) -> 0 - 1
                canvas.scale(1.0f, Math.sin(radian).toFloat())
                if (translateY <= mTopLineY) {
                    //draw text y between 0 -> mTopLineY,include incomplete text
                    canvas.save()
                    canvas.clipRect(0, 0, mWidgetWidth, mTopLineY - translateY)
                    canvas.drawText(
                        itemCount[count],
                        mPaddingLeftRight.toFloat(),
                        mMaxTextHeight.toFloat(),
                        mTopBottomTextPaint!!
                    )
                    canvas.restore()
                    canvas.save()
                    canvas.clipRect(0, mTopLineY - translateY, mWidgetWidth, itemHeight.toInt())
                    canvas.drawText(
                        itemCount[count],
                        mPaddingLeftRight.toFloat(),
                        mMaxTextHeight.toFloat(),
                        mCenterTextPaint!!
                    )
                    canvas.restore()
                } else if (mMaxTextHeight + translateY >= mBottomLineY) {
                    //draw text y between  mTopLineY -> mBottomLineY ,include incomplete text
                    canvas.save()
                    canvas.clipRect(0, 0, mWidgetWidth, mBottomLineY - translateY)
                    canvas.drawText(
                        itemCount[count],
                        mPaddingLeftRight.toFloat(),
                        mMaxTextHeight.toFloat(),
                        mCenterTextPaint!!
                    )
                    canvas.restore()
                    canvas.save()
                    canvas.clipRect(0, mBottomLineY - translateY, mWidgetWidth, itemHeight.toInt())
                    canvas.drawText(
                        itemCount[count],
                        mPaddingLeftRight.toFloat(),
                        mMaxTextHeight.toFloat(),
                        mTopBottomTextPaint!!
                    )
                    canvas.restore()
                } else if (translateY >= mTopLineY && mMaxTextHeight + translateY <= mBottomLineY) {
                    //draw center complete text
                    canvas.clipRect(0, 0, mWidgetWidth, itemHeight.toInt())
                    canvas.drawText(
                        itemCount[count],
                        mPaddingLeftRight.toFloat(),
                        mMaxTextHeight.toFloat(),
                        mCenterTextPaint!!
                    )
                    //center one indicate selected item
                    selectedItem = mDataList!!.indexOf(itemCount[count])
                }
                canvas.restore()
            }
            count++
        }
    }

    override fun onTouchEvent(motionevent: MotionEvent): Boolean {

        when (motionevent.action) {
            MotionEvent.ACTION_UP -> if (!mGestureDetector!!.onTouchEvent(motionevent)) {
                startSmoothScrollTo()
            }
            else -> if (!mGestureDetector!!.onTouchEvent(motionevent)) {
                startSmoothScrollTo()
            }
        }
        return true
    }

    fun setCanLoop(canLoop: Boolean) {
        mCanLoop = canLoop
        invalidate()
    }

    /**
     * set text size
     *
     * @param size size indicate sp,not px
     */
    fun setTextSize(size: Float) {
        if (size > 0) {
            mTextSize = sp2px(mContext, size)
        }
    }

    fun setInitPosition(initPosition: Int) {
        this.mInitPosition = initPosition
        invalidate()
    }

    fun setLoopListener(LoopListener: WheelPickerScrollListener) {
        mLoopListener = LoopListener
    }

    /**
     * All public method must be called before this method
     *
     * @param list data list
     */
    fun setDataList(list: ArrayList<String>) {
        this.mDataList = list
        initData()
    }


    private fun itemSelected() {
        if (mLoopListener != null) {
            postDelayed(SelectedRunnable(), 200L)
        }
    }

    private fun cancelSchedule() {

        if (mScheduledFuture != null && !mScheduledFuture!!.isCancelled) {
            mScheduledFuture!!.cancel(true)
            mScheduledFuture = null
        }
    }

    private fun startSmoothScrollTo() {
        val offset = (mTotalScrollY % mItemHeight).toInt()
        cancelSchedule()
        mScheduledFuture = mExecutor.scheduleWithFixedDelay(HalfHeightRunnable(offset), 0, 10, TimeUnit.MILLISECONDS)
    }

    private fun startSmoothScrollTo(velocityY: Float) {
        cancelSchedule()
        val velocityFling = 20
        mScheduledFuture =
            mExecutor.scheduleWithFixedDelay(FlingRunnable(velocityY), 0, velocityFling.toLong(), TimeUnit.MILLISECONDS)
    }

    internal inner class LoopViewGestureListener : android.view.GestureDetector.SimpleOnGestureListener() {

        override fun onDown(motionevent: MotionEvent): Boolean {
            cancelSchedule()
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            startSmoothScrollTo(velocityY)
            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            mTotalScrollY = (mTotalScrollY.toFloat() + distanceY).toInt()
            if (!mCanLoop) {
                val initPositionCircleLength = (mInitPosition * mItemHeight).toInt()
                val initPositionStartY = -1 * initPositionCircleLength
                if (mTotalScrollY < initPositionStartY) {
                    mTotalScrollY = initPositionStartY
                }

                val circleLength = ((mDataList!!.size - 1 - mInitPosition).toFloat() * mItemHeight).toInt()
                if (mTotalScrollY >= circleLength) {
                    mTotalScrollY = circleLength
                }
            }

            invalidate()
            return true
        }
    }

    fun sp2px(context: Context?, spValue: Float): Int {
        val fontScale = context!!.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    internal inner class SelectedRunnable : Runnable {

        override fun run() {
            val listener = this@WheelPickerView.mLoopListener
            val selectedItem = selectedItem
            mDataList!![selectedItem]
            listener!!.onItemSelect(selectedItem)
        }
    }

    /**
     * Use in ACTION_UP
     */
    internal inner class HalfHeightRunnable(var offset: Int) : Runnable {

        var realTotalOffset: Int = 0
        var realOffset: Int = 0

        init {
            realTotalOffset = Integer.MAX_VALUE
            realOffset = 0
        }

        override fun run() {
            //first in
            if (realTotalOffset == Integer.MAX_VALUE) {

                if (offset.toFloat() > mItemHeight / 2.0f) {
                    //move to next item
                    realTotalOffset = (mItemHeight - offset.toFloat()).toInt()
                } else {
                    //move to pre item
                    realTotalOffset = -offset
                }
            }

            realOffset = (realTotalOffset.toFloat() * 0.1f).toInt()

            if (realOffset == 0) {

                if (realTotalOffset < 0) {
                    realOffset = -1
                } else {
                    realOffset = 1
                }
            }
            if (Math.abs(realTotalOffset) <= 0) {
                cancelSchedule()
                mHandler.sendEmptyMessage(MSG_SELECTED_ITEM)
                return
            } else {
                mTotalScrollY = mTotalScrollY + realOffset
                mHandler.sendEmptyMessage(MSG_INVALIDATE)
                realTotalOffset = realTotalOffset - realOffset
                return
            }
        }
    }

    /**
     * Use in [LoopViewGestureListener.onFling]
     */
    internal inner class FlingRunnable(val velocityY: Float) : Runnable {

        var velocity: Float = 0.toFloat()

        init {
            velocity = Integer.MAX_VALUE.toFloat()
        }

        override fun run() {
            if (velocity == Integer.MAX_VALUE.toFloat()) {
                if (Math.abs(velocityY) > 2000f) {
                    if (velocityY > 0.0f) {
                        velocity = 2000f
                    } else {
                        velocity = -2000f
                    }
                } else {
                    velocity = velocityY
                }
            }
            if (Math.abs(velocity) >= 0.0f && Math.abs(velocity) <= 20f) {
                cancelSchedule()
                mHandler.sendEmptyMessage(MSG_SCROLL_LOOP)
                return
            }
            val i = (velocity * 10f / 1000f).toInt()
            mTotalScrollY = mTotalScrollY - i
            if (!mCanLoop) {
                val itemHeight = lineSpacingMultiplier * mMaxTextHeight
                if (mTotalScrollY <= ((-mInitPosition).toFloat() * itemHeight).toInt()) {
                    velocity = 40f
                    mTotalScrollY = ((-mInitPosition).toFloat() * itemHeight).toInt()
                } else if (mTotalScrollY >= ((mDataList!!.size - 1 - mInitPosition).toFloat() * itemHeight).toInt()) {
                    mTotalScrollY = ((mDataList!!.size - 1 - mInitPosition).toFloat() * itemHeight).toInt()
                    velocity = -40f
                }
            }
            if (velocity < 0.0f) {
                velocity = velocity + 20f
            } else {
                velocity = velocity - 20f
            }
            mHandler.sendEmptyMessage(MSG_INVALIDATE)
        }
    }

    companion object {

        private val TAG = WheelPickerView::class.java.simpleName

        val MSG_INVALIDATE = 1000
        val MSG_SCROLL_LOOP = 2000
        val MSG_SELECTED_ITEM = 3000
    }
}

