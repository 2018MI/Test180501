package org.chengpx.mylib;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * create at 2018/5/3 15:01 by chengpx
 */
public abstract class ViewPagerFragment extends Fragment {

    protected FragmentActivity mFragmentActivity;
    protected boolean mIsVisibleToUser;
    protected boolean mIsFirstUsserVisibleHint = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentActivity = getActivity();
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mIsFirstUsserVisibleHint = false;
        View view = initView(inflater, container, savedInstanceState);
        initListener();
        if (getUserVisibleHint()) {
            initData();
            main();
        }
        return view;
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initListener();

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        onDie();
    }

    protected abstract void onDie();

    /**
     * 领先于 onCreateView
     *
     * @param isVisibleToUser
     */
    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mIsVisibleToUser == isVisibleToUser) {
            return;
        }
        if (isVisibleToUser) {
            if (!mIsFirstUsserVisibleHint) {
                initData();
                main();
            }
        } else {
            onDims();
        }
        mIsVisibleToUser = isVisibleToUser;
    }

    protected abstract void onDims();

    protected abstract void main();

    protected abstract void initData();

}
