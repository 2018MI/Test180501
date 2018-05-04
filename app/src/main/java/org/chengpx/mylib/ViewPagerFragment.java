package org.chengpx.mylib;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * create at 2018/5/3 15:01 by chengpx
 */
public abstract class ViewPagerFragment extends Fragment {

    protected FragmentActivity mFragmentActivity;
    /**
     * 存储 isVisibleToUser
     */
    private boolean mIsVisibleToUser;
    /**
     * 是否已经调用 initData(), main()
     */
    private boolean mIsInit;
    private boolean mIsCompleteOnCreateView;
    private String mTag = getClass().getName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentActivity = getActivity();
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(mTag, "onCreateView, this: " + this);
        View view = initView(inflater, container, savedInstanceState);
        initListener();
        if (!mIsInit && getUserVisibleHint()) {
            initData();
            main();
            mIsInit = true;
            Log.d(mTag, "onCreateView initData main: " + this);
        }
        mIsCompleteOnCreateView = true;
        return view;
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initListener();

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        onDie();
        Log.d(mTag, "onDestroyView: " + this);
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
        Log.d(mTag, "setUserVisibleHint setUserVisibleHint: " + isVisibleToUser + ", this: " + this);
        if (mIsVisibleToUser == isVisibleToUser) {
            return;
        }
        mIsVisibleToUser = isVisibleToUser;
        if (getUserVisibleHint()) {
            if (mIsCompleteOnCreateView) {
                initData();
                main();
                mIsInit = true;
                Log.d(mTag, "setUserVisibleHint initData main: " + this);
            }
        } else {
            if (mIsInit) {
                onDims();
                mIsVisibleToUser = false;
                mIsInit = false;
                Log.d(mTag, "setUserVisibleHint onDims: " + this);
            }
        }
    }

    protected abstract void onDims();

    protected abstract void main();

    protected abstract void initData();

    @Override
    public String toString() {
        return "ViewPagerFragment{" +
                "mIsVisibleToUser=" + mIsVisibleToUser +
                ", mIsInit=" + mIsInit +
                ", mIsCompleteOnCreateView=" + mIsCompleteOnCreateView +
                '}';
    }

}
