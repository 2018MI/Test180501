package org.chengpx.test180501.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.chengpx.mylib.BaseFragment;
import org.chengpx.test180501.R;
import org.chengpx.test180501.fragment.Test4.Test4Fragment;
import org.chengpx.test180501.fragment.test46.Test46Fragment;

/**
 * 侧滑菜单
 * <p>
 * create at 2018/4/28 20:20 by chengpx
 */
public class MainSlidingMenuFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mainsldingmenu_lv_itemlist;
    private FragmentActivity mFragmentActivity;

    private String[] mItemStrArr = {
            "第4题编码实现车辆违章浏览功能1", "第12题编码实现车辆违章查看功能", "第13题编码实现路况查询模块",
            "第30题编码实现车辆违章视频浏览播放功能", "第46题编码实现我的交通功能"
    };
    private BaseFragment[] mBaseFragmentArr = {
            new Test4Fragment(), new Test12Fragment(), new Test13Fragment(), new Test30Fragment(), new Test46Fragment()
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainslidingmenu, container, false);
        mFragmentActivity = getActivity();
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        mainsldingmenu_lv_itemlist.setOnItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        main();
    }

    private void main() {
        mainsldingmenu_lv_itemlist.setAdapter(new ArrayAdapter<String>(
                mFragmentActivity, android.R.layout.simple_list_item_1, mItemStrArr
        ));
    }

    private void initData() {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initView(View view) {
        mainsldingmenu_lv_itemlist = (ListView) view.findViewById(R.id.mainsldingmenu_lv_itemlist);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        assert getFragmentManager() != null;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fl_content, mBaseFragmentArr[position], "");
        fragmentTransaction.commit();
    }

}
