package org.chengpx.test180501.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chengpx.mylib.AppException;
import org.chengpx.mylib.BaseFragment;
import org.chengpx.mylib.common.DataUtils;
import org.chengpx.mylib.http.HttpUtils;
import org.chengpx.mylib.http.RequestPool;
import org.chengpx.test180501.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * create at 2018/5/4 14:47 by chengpx
 */
public class Test36Fragment extends BaseFragment implements View.OnClickListener {

    private String[] mCommonWeatherDescArr = {
            "晴朗", "多云", "阴",
            "大雾", "雾霾",
            "小雨", "中雨", "大雨", "大暴雨", "雷电", "雷阵雨",
            "晴转多云", "小雨转中雨", "大暴雨转特大暴雨"

    };
    private String[] mHibernateWeatherDescArr = {
            "晴朗", "多云", "阴",
            "大雾", "雾霾",
            "小雨", "中雨", "大雨", "大暴雨", "雷电", "雷阵雨",
            "晴转多云", "小雨转中雨", "大暴雨转特大暴雨",
            "小雪", "中雪", "大雪", "暴雪",
            "冻雨", "雨夹雪", "雷雨冰雹", "小雪转中雪", "中雪转大雪", "大雪转暴雪"
    };
    private Integer[] mCommonWeatherResArr = {
            R.drawable.qing, R.drawable.duoyun, R.drawable.ying,
            R.drawable.dawu, R.drawable.wumai,
            R.drawable.xiaoyu, R.drawable.zhongyu, R.drawable.dayu, R.drawable.dabaoyu, R.drawable.leidian, R.drawable.leizhengyu,
            R.drawable.qingzhuanduoyun, R.drawable.xiaoyuzhuangzhongyu, R.drawable.dabaoyuzhuantedabaoyu
    };
    private Integer[] mHibernateWeatherResArr = {
            R.drawable.qing, R.drawable.duoyun, R.drawable.ying,
            R.drawable.dawu, R.drawable.wumai,
            R.drawable.xiaoyu, R.drawable.zhongyu, R.drawable.dayu, R.drawable.dabaoyu, R.drawable.leidian, R.drawable.leizhengyu,
            R.drawable.qingzhuanduoyun, R.drawable.xiaoyuzhuangzhongyu, R.drawable.dabaoyuzhuantedabaoyu,
            R.drawable.xiaoxue, R.drawable.zhongxue, R.drawable.daxue, R.drawable.baoxue,
            R.drawable.dongyu, R.drawable.yujiaxue, R.drawable.leiyubingbao, R.drawable.xiaoyuzhuangzhongyu, R.drawable.zhongxuezhuandaxue, R.drawable.daxuezhuanbaoxue
    };
    private Integer[] mDayArr = {
            0, 1, 2, 3, 4
    };
    private String[] mDateDescArr = {
            "今天", "明天", "后天"
    };
    private int[][] mWeatherRgbArr = {// 深蓝, 淡蓝, 浅灰
            new int[]{0XFF1181E0, 0XFF3698E7, 0XFF65B6EF}, new int[]{0XFF5AB6FA, 0XFF76C1F9, 0XFFA2D2F6},//分别为开始颜色，中间夜色，结束颜色
            new int[]{0XFF8AABCB, 0XFF9FBBD5, 0XFFBBD2E2}
    };
    private String[][] mWeatherRgbStrArr = {
            new String[]{"晴朗", "晴转多云"}, new String[]{"多云", "阴"},
            new String[]{"大雾", "雾霾", "小雨", "中雨", "大雨", "大暴雨", "雷电", "雷阵雨", "小雨转中雨", "大暴雨转特大暴雨",
                    "小雪", "中雪", "大雪", "暴雪", "冻雨", "雨夹雪", "雷雨冰雹", "小雪转中雪", "中雪转大雪", "大雪转暴雪"}
    };
    private ImageButton test36_imgbtn_flush;
    private ImageView test36_iv_nowweather;
    private TextView test36_tv_nowdate;
    private TextView test36_tv_placeandtemperature;
    private GridView test36_gridview_weatherdata;
    private MyAdapter mMyAdapter;
    private Test36Fragment mTest36Fragment;
    private Map<Integer, Integer> mTemperatureMap;
    private Random mRandom;
    private static String sTag = "org.chengpx.test180501.fragment.Test36Fragment";

    @Override
    protected void initListener() {
        test36_imgbtn_flush.setOnClickListener(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTest36Fragment = this;
        mRandom = new Random();
        View view = inflater.inflate(R.layout.fragment_test36, container, false);
        test36_imgbtn_flush = (ImageButton) view.findViewById(R.id.test36_imgbtn_flush);
        test36_iv_nowweather = (ImageView) view.findViewById(R.id.test36_iv_nowweather);
        test36_tv_nowdate = (TextView) view.findViewById(R.id.test36_tv_nowdate);
        test36_tv_placeandtemperature = (TextView) view.findViewById(R.id.test36_tv_placeandtemperature);
        test36_gridview_weatherdata = (GridView) view.findViewById(R.id.test36_gridview_weatherdata);
        return view;
    }

    @Override
    protected void onDie() {
        mTest36Fragment = null;
        mRandom = null;
    }

    @Override
    protected void main() {
        mMyAdapter = new MyAdapter();
        test36_gridview_weatherdata.setAdapter(mMyAdapter);
        Date date = Calendar.getInstance(Locale.CHINESE).getTime();
        test36_tv_nowdate.setText(String.format("%tY年%tm月%td日 %tA", date, date, date, date));
    }

    @Override
    protected void initData() {
        mTemperatureMap = new HashMap<>();
        Map<String, String> reqValues = new HashMap<>();
        reqValues.put("SenseName", "temperature");
        RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetSenseByName.do",
                reqValues, new GetSenseByNameCallBack(Map.class, mTest36Fragment));
    }

    @Override
    protected void onDims() {
        mMyAdapter = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test36_imgbtn_flush:
                Map<String, String> reqValues = new HashMap<>();
                reqValues.put("SenseName", "temperature");
                RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetSenseByName.do",
                        reqValues, new GetSenseByNameCallBack(Map.class, mTest36Fragment));
                break;
        }
    }

    private static class ViewHolder {

        private final TextView test36_gridview_weatherdata_tv_date;
        private final ImageView test36_gridview_weatherdata_iv_weather;
        private final TextView test36_gridview_weatherdata_tv_weather;
        private final TextView test36_gridview_weatherdata_tv_temperature;
        private final LinearLayout test36_gridview_weatherdata_ll_content;

        private ViewHolder(View convertView) {
            test36_gridview_weatherdata_tv_date = (TextView) convertView.findViewById(R.id.test36_gridview_weatherdata_tv_date);
            test36_gridview_weatherdata_iv_weather = (ImageView) convertView.findViewById(R.id.test36_gridview_weatherdata_iv_weather);
            test36_gridview_weatherdata_tv_weather = (TextView) convertView.findViewById(R.id.test36_gridview_weatherdata_tv_weather);
            test36_gridview_weatherdata_tv_temperature = (TextView) convertView.findViewById(R.id.test36_gridview_weatherdata_tv_temperature);
            test36_gridview_weatherdata_ll_content = convertView.findViewById(R.id.test36_gridview_weatherdata_ll_content);
        }

        public static ViewHolder get(View convertView) {
            Object tag = convertView.getTag();
            if (tag == null) {
                tag = new ViewHolder(convertView);
                convertView.setTag(tag);
            }
            return (ViewHolder) tag;
        }

        public TextView getTest36_gridview_weatherdata_tv_date() {
            return test36_gridview_weatherdata_tv_date;
        }

        public ImageView getTest36_gridview_weatherdata_iv_weather() {
            return test36_gridview_weatherdata_iv_weather;
        }

        public TextView getTest36_gridview_weatherdata_tv_weather() {
            return test36_gridview_weatherdata_tv_weather;
        }

        public TextView getTest36_gridview_weatherdata_tv_temperature() {
            return test36_gridview_weatherdata_tv_temperature;
        }

        public LinearLayout getTest36_gridview_weatherdata_ll_content() {
            return test36_gridview_weatherdata_ll_content;
        }

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mTemperatureMap.size() == 0) {
                return 0;
            }
            return mDayArr.length;
        }

        @Override
        public Integer getItem(int position) {
            Integer temperature = mTemperatureMap.get(0);
            if (position == 0) {
                return temperature;
            }
            // 0 - 40
            int maxLess = temperature;
            int maxAdd = 40 - temperature;
            if (mRandom.nextBoolean()) {
                return temperature + Math.min(5, maxAdd);
            } else {
                return temperature - Math.min(5, maxLess);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(sTag, "postion = " + position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mFragmentActivity).inflate(R.layout.item_test36_gridview_weatherdata,
                        test36_gridview_weatherdata, false);
                convertView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, parent.getHeight()));
                viewHolder = ViewHolder.get(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Integer item = getItem(position);
            viewHolder.getTest36_gridview_weatherdata_tv_temperature().setText(Math.max((item - 5), 0) + "/" + Math.min((item + 5), 40));
            Calendar calendar = Calendar.getInstance(Locale.CHINESE);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + position);
            if (position < 3) {
                viewHolder.getTest36_gridview_weatherdata_tv_date()
                        .setText(calendar.get(Calendar.DAY_OF_MONTH) + "日(" + mDateDescArr[position] + ")");
            } else {
                viewHolder.getTest36_gridview_weatherdata_tv_date()
                        .setText(calendar.get(Calendar.DAY_OF_MONTH) + "日(" + String.format("%tA", calendar.getTime()) + ")");
            }
            int index = -1;
            String weatherStr;
            if (item < 10) {
                index = mRandom.nextInt(mHibernateWeatherDescArr.length);
                viewHolder.getTest36_gridview_weatherdata_iv_weather().setImageResource(mHibernateWeatherResArr[index]);
                weatherStr = mHibernateWeatherDescArr[index];
                viewHolder.getTest36_gridview_weatherdata_tv_weather().setText(weatherStr);
            } else {
                index = mRandom.nextInt(mCommonWeatherDescArr.length);
                viewHolder.getTest36_gridview_weatherdata_iv_weather().setImageResource(mCommonWeatherResArr[index]);
                weatherStr = mCommonWeatherDescArr[index];
                viewHolder.getTest36_gridview_weatherdata_tv_weather().setText(mCommonWeatherDescArr[index]);
            }
            int binarySearchWeatherStr = -1;
            out:
            for (int i = 0; i < mWeatherRgbStrArr.length; i++) {
                for (int j = 0; j < mWeatherRgbStrArr[i].length; j++) {
                    if (weatherStr.equals(mWeatherRgbStrArr[i][j])) {
                        binarySearchWeatherStr = i;
                        break out;
                    }
                }
            }
            if (binarySearchWeatherStr > 0) {
                GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mWeatherRgbArr[binarySearchWeatherStr]);// 创建drawable
                gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);
                gradientDrawable.setCornerRadius(10);
                gradientDrawable.setStroke(5, 0XFFFFFFFF);
                viewHolder.getTest36_gridview_weatherdata_ll_content().setBackgroundDrawable(gradientDrawable);
            }
            return convertView;
        }

    }

    private static class GetSenseByNameCallBack extends HttpUtils.Callback<Map> {

        private final Test36Fragment mTest36Fragment_inner;

        /**
         * @param mapClass       结果数据封装体类型字节码
         * @param test36Fragment
         */
        GetSenseByNameCallBack(Class<Map> mapClass, Test36Fragment test36Fragment) {
            super(mapClass);
            mTest36Fragment_inner = test36Fragment;
        }

        @Override
        protected void onSuccess(Map map) {
            try {
                mTest36Fragment_inner.getTemperatureMap().put(0, DataUtils.obj2int(map.get("temperature")));
                MyAdapter myAdapter = mTest36Fragment_inner.getMyAdapter();
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged();
                }
                mTest36Fragment_inner.getTest36_tv_placeandtemperature().setText("南京" + map.get("temperature") + "度");
            } catch (AppException e) {
                e.printStackTrace();
            }
        }

    }

    public MyAdapter getMyAdapter() {
        return mMyAdapter;
    }

    public Map<Integer, Integer> getTemperatureMap() {
        return mTemperatureMap;
    }

    public ImageView getTest36_iv_nowweather() {
        return test36_iv_nowweather;
    }

    public GridView getTest36_gridview_weatherdata() {
        return test36_gridview_weatherdata;
    }

    public TextView getTest36_tv_placeandtemperature() {
        return test36_tv_placeandtemperature;
    }

}
