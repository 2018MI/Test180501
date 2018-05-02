package org.chengpx.test180501.fragment.Test4;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.chengpx.test180501.R;

public class PicFragment extends Fragment {
    private View view;
    private GridView gridviewPic;
    private int[] lists = {1, 2, 3, 4, 5};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.fragment_pic, null);
        initView();
        initData();
        return view;
    }

    private void initData() {
        MyAdapter adapter = new MyAdapter();
        gridviewPic.setAdapter(adapter);
    }


    public class MyAdapter extends BaseAdapter implements View.OnClickListener {

        @Override
        public int getCount() {
            return lists.length;
        }

        @Override
        public Object getItem(int position) {
            return lists.length;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(getContext(), R.layout.pic_item, null);
            } else {
                view = convertView;
            }
            return view;
        }

        @Override
        public void onClick(View v) {

        }
    }

    private void initView() {
        gridviewPic = view.findViewById(R.id.gridview_pic);
    }
}
