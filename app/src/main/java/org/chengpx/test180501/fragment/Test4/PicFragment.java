package org.chengpx.test180501.fragment.Test4;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.GetChars;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.chengpx.mylib.BaseFragment;
import org.chengpx.test180501.R;

public class PicFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private GridView gridviewPic;
    private int[] lists = {1, 2, 3, 4, 5};
    private View inflate;
    private Bitmap bitmap;
    private float scaleWidth;
    private float scaleHeight;

    @Override
    protected void initListener() {

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_pic, container, false);
        initView();
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
        DisplayMetrics dm = new DisplayMetrics();//创建矩阵
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        bitmap= BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.cerer);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int w = dm.widthPixels; //得到屏幕的宽度
        int h = dm.heightPixels; //得到屏幕的高度
        scaleWidth = ((float) w) / width;
        scaleHeight = ((float) h) / height;
        MyAdapter adapter = new MyAdapter();
        gridviewPic.setAdapter(adapter);
        gridviewPic.setOnItemClickListener(this);
    }


    @Override
    protected void onDims() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Dialog dialog = new Dialog(getContext(), R.style.edit_AlertDialog_style);
        ImageView newImageView = getImageView();
        dialog.setContentView(newImageView);
        getActivity().setVisible(false);
        newImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().setVisible(true);
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                getActivity().setVisible(true);
            }
        });
        dialog.show();
    }
    //动态的ImageView
    private ImageView getImageView() {
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        Matrix matrix = new Matrix();
        float nScale = (scaleWidth > scaleHeight) ? scaleHeight : scaleWidth;
        matrix.postScale(nScale, nScale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        iv.setImageBitmap(newBitmap);
        return iv;
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
                view = View.inflate(getContext(), R.layout.pic_item, null);
            } else {
                view = convertView;
            }
            return view;
        }

    }

    private void initView() {
        gridviewPic = inflate.findViewById(R.id.gridview_pic);
    }
}
