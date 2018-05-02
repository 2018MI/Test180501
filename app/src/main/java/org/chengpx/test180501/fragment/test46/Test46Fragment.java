package org.chengpx.test180501.fragment.test46;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.chengpx.mylib.BaseFragment;
import org.chengpx.test180501.R;
import org.chengpx.test180501.view.NoScrollViewPager;

/**
 * 第46题编码实现我的交通功能
 * <p>
 * create at 2018/5/1 17:06 by chengpx
 */
public class Test46Fragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    private String mTag = "org.chengpx.test180501.fragment.test46.Test46Fragment";

    private NoScrollViewPager test46_noscrollviewpager_content;
    private RadioGroup test46_radiogroup_indicators;
    private BaseFragment[] mBaseFragmentArr = {
            new MyRoad46Fragment(), new MyTraffic46Fragment()
    };
    private String[] mMoudleNameArr = {
            "我的路况", "我的交通"
    };

    @Override
    protected void initListener() {
        test46_radiogroup_indicators.setOnCheckedChangeListener(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test46, container, false);
        test46_noscrollviewpager_content = (NoScrollViewPager) view.findViewById(R.id.test46_noscrollviewpager_content);
        test46_radiogroup_indicators = view.findViewById(R.id.test46_radiogroup_indicators);
        return view;
    }

    @Override
    protected void onDie() {
    }

    @Override
    protected void main() {
        test46_noscrollviewpager_content.setAdapter(new MyPagerAdapter(getFragmentManager()));
    }

    @Override
    protected void initData() {
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(0,
                RadioGroup.LayoutParams.WRAP_CONTENT, 1);// 第三个参数为权重
        for (String moduleName : mMoudleNameArr) {
            RadioButton radioButton = new RadioButton(mFragmentActivity);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setText(moduleName);
            radioButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));// 通过给定一个透明的 Drawable 去除默认原点样式
            radioButton.setGravity(Gravity.CENTER);
            test46_radiogroup_indicators.addView(radioButton);
        }
    }

    @Override
    protected void onDims() {
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        test46_noscrollviewpager_content.setCurrentItem(group.indexOfChild(group.findViewById(checkedId)), false);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mBaseFragmentArr[position];
        }

        @Override
        public int getCount() {
            return mBaseFragmentArr.length;
        }

    }

}
