package com.zhf.auxiliary.city.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ScrollWithGridView extends GridView {
    public ScrollWithGridView(Context context) {
        super(context);
    }

    public ScrollWithGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWithGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
