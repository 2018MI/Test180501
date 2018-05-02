package org.chengpx.test180501.fragment.Test4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.chengpx.mylib.BaseFragment;
import org.chengpx.test180501.R;

import java.util.ArrayList;

/**
 * 第4题编码实现车辆违章浏览功能1
 * <p>
 * create at 2018/5/1 17:04 by chengpx
 */
public class Test4Fragment extends BaseFragment implements View.OnClickListener {

    private android.widget.TextView tvVideo;
    private android.widget.TextView tvPic;
    private android.support.v4.view.ViewPager vp;
    private View inflate;
    private ArrayList<Fragment> fragments;
    private FragmentManager fm;

    @Override
    protected void initListener() {
        tvVideo.setOnClickListener(this);
        tvPic.setOnClickListener(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_test4, container, false);
        initView();//初始化控件
        return inflate;
    }

    @Override
    protected void onDie() {

    }

    @Override
    protected void main() {

    }

    @Override
    protected void initData() {
        fragments = new ArrayList<>();
        fm = getChildFragmentManager();//todo fragment在嵌套fragment必须使用getChildFragment管理fragment
        initFragment();
        MyAdapter adapter = new MyAdapter(fm);
        vp.setAdapter(adapter);
        tvVideo.setEnabled(false);
    }

    private void initFragment() {
        VideoFragment videoFragment = new VideoFragment();
        PicFragment picFragment = new PicFragment();
        fragments.add(videoFragment);
        fragments.add(picFragment);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_video:
                vp.setCurrentItem(0);
                tvVideo.setEnabled(false);
                tvPic.setEnabled(true);
                break;
            case R.id.tv_pic:
                vp.setCurrentItem(1);
                tvPic.setEnabled(false);
                tvVideo.setEnabled(true);
                break;
        }
    }

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    protected void onDims() {

    }

    private void initView() {
        tvVideo = inflate.findViewById(R.id.tv_video);
        tvPic = inflate.findViewById(R.id.tv_pic);
        vp = inflate.findViewById(R.id.vp);
    }
}
