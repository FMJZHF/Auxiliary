package com.zhf.auxiliaryjar.indicator.magicindicator.buildins.commonnavigator.abs;


import com.zhf.auxiliaryjar.indicator.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;

/**
 * 抽象的viewpager指示器，适用于CommonNavigator
 */
public interface IPagerIndicator {
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

    void onPositionDataProvide(List<PositionData> dataList);
}
