package org.chengpx.test180501.fragment.test46;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.chengpx.mylib.BaseFragment;

/**
 * 我的路况
 * <p>
 * create at 2018/5/2 11:15 by chengpx
 */
public class MyRoad46Fragment extends BaseFragment {

    @Override
    protected void initListener() {

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(mFragmentActivity);
        textView.setText("我的路况");
        return textView;
    }

    @Override
    protected void onDie() {

    }

    @Override
    protected void main() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDims() {

    }

}
