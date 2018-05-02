package org.chengpx.test180501.fragment.Test4;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.chengpx.test180501.R;
import org.chengpx.test180501.VideoActivity;


public class VideoFragment extends Fragment {
    private View view;
    private GridView gridview;
    private int[] lists = {1, 2, 3, 4, 5};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.fragment_video, null);
        initView();
        initData();
        return view;
    }

    private void initData() {
        MyAdapter adapter = new MyAdapter();
        gridview.setAdapter(adapter);
    }

    public class MyAdapter extends BaseAdapter {

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
                view = View.inflate(getContext(), R.layout.video_item, null);
            } else {
                view = convertView;
            }
            ImageView ivvideo = view.findViewById(R.id.iv_video_item);
            ivvideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), VideoActivity.class));
                }
            });
            return view;
        }
    }

    private void initView() {
        gridview = view.findViewById(R.id.gridview);
    }
}
