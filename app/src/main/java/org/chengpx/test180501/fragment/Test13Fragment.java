package org.chengpx.test180501.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.chengpx.mylib.AppException;
import org.chengpx.mylib.BaseFragment;
import org.chengpx.mylib.common.DataUtils;
import org.chengpx.mylib.http.HttpUtils;
import org.chengpx.mylib.http.RequestPool;
import org.chengpx.test180501.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 第13题编码实现路况查询模块
 * <p>
 * create at 2018/5/1 17:06 by chengpx
 */
public class Test13Fragment extends BaseFragment implements View.OnClickListener {

    private TextView test13_tv_hcksltop;
    private TextView test13_tv_hckslleft;
    private TextView test13_tv_hckslbottom;
    private TextView test13_tv_hcgsright;
    private TextView test13_tv_xuexiaoroad;
    private TextView test13_tv_xinfuroad;
    private TextView test13_tv_lenovoroad;
    private TextView test13_tv_hospitalroad;
    private TextView test13_tv_parking;
    private TextView test13_tv_date;
    private Button test13_btn_flush;
    private ListView test13_lv_senselist;
    private Test13Fragment mTest13Fragment;
    private ListView test13_lv_roadstatusexmaple;
    private Integer[] mRoadStatusResIdArr = {
            R.drawable.shape_rectangle_status1, R.drawable.shape_rectangle_status2,
            R.drawable.shape_rectangle_status3, R.drawable.shape_rectangle_status4,
            R.drawable.shape_rectangle_status5
    };
    private Integer[] mRoadStatusArr = {
            1, 2, 3, 4, 5
    };
    private String[] mRoadStatusDescArr = {
            "畅通", "缓行", "一般拥堵", "中度拥堵", "严重拥堵"
    };
    private String[] mSenseDescArr = {
            "温度", "相对湿度", "PM2.5"
    };
    private String[] mSenseNameArr = {
            "temperature", "humidity", "pm2.5"
    };
    private View[][] mRoadUiViewArr;
    private String[] mSenseUnitArr = {
            "℃", "%", "ug/m3"
    };
    private Integer[] mRoadIdArr = {
            1, 2, 3, 4, 5, 6, 7
    };
    private Map<String, Double> mSenseDataMap;
    private SenseAdapter mSenseAdapter;
    private Timer mTimer;
    private int mRoadIdReqIndex;

    @Override
    protected void initListener() {
        test13_btn_flush.setOnClickListener(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTest13Fragment = this;
        View view = inflater.inflate(R.layout.fragment_test13, container, false);
        test13_tv_hcksltop = (TextView) view.findViewById(R.id.test13_tv_hcksltop);
        test13_tv_hckslleft = (TextView) view.findViewById(R.id.test13_tv_hckslleft);
        test13_tv_hckslbottom = (TextView) view.findViewById(R.id.test13_tv_hckslbottom);
        test13_tv_hcgsright = (TextView) view.findViewById(R.id.test13_tv_hcgsright);
        test13_tv_xuexiaoroad = (TextView) view.findViewById(R.id.test13_tv_xuexiaoroad);
        test13_tv_xinfuroad = (TextView) view.findViewById(R.id.test13_tv_xinfuroad);
        test13_tv_lenovoroad = (TextView) view.findViewById(R.id.test13_tv_lenovoroad);
        test13_tv_hospitalroad = (TextView) view.findViewById(R.id.test13_tv_hospitalroad);
        test13_tv_parking = (TextView) view.findViewById(R.id.test13_tv_parking);
        test13_tv_date = (TextView) view.findViewById(R.id.test13_tv_date);
        test13_btn_flush = (Button) view.findViewById(R.id.test13_btn_flush);
        test13_lv_senselist = (ListView) view.findViewById(R.id.test13_lv_senselist);
        test13_lv_roadstatusexmaple = (ListView) view.findViewById(R.id.test13_lv_roadstatusexmaple);
        mRoadUiViewArr = new View[][]{
                {test13_tv_xuexiaoroad}, {test13_tv_lenovoroad}, {test13_tv_hospitalroad}, {test13_tv_xinfuroad},
                {test13_tv_hcksltop, test13_tv_hckslleft, test13_tv_hckslbottom}, {test13_tv_hcgsright}, {test13_tv_parking}
        };
        return view;
    }

    @Override
    protected void onDie() {
        mTest13Fragment = null;
        mRoadUiViewArr = null;
    }

    @Override
    protected void main() {
        test13_lv_roadstatusexmaple.setAdapter(new RoadstatusexmapleAdapter());
        mSenseAdapter = new SenseAdapter();
        test13_lv_senselist.setAdapter(mSenseAdapter);
    }

    @Override
    protected void initData() {
        Date date = Calendar.getInstance(Locale.CHINA).getTime();
        test13_tv_date.setText(String.format(Locale.CHINA, "%tY-%tm-%td\n%tA", date, date, date, date));
        mSenseDataMap = new HashMap<>();
        RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetAllSense.do", null,
                new GetAllSenseCallBack(Map.class, mTest13Fragment));
        mTimer = new Timer();
        mTimer.schedule(new GetRoadStatusTimerTask(new AllGetRoadStatusCallBack(Map.class, mTest13Fragment)), 0, 3000);
    }

    @Override
    protected void onDims() {
        mSenseDataMap = null;
        mSenseAdapter = null;
        mTimer.cancel();
        mTimer = null;
        mRoadIdReqIndex = 0;
    }

    //点击按钮刷新传感器
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test13_btn_flush:
                RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetAllSense.do", null,
                        new GetAllSenseCallBack(Map.class, mTest13Fragment));
                break;
        }
    }

    //道路状态的Callback方法
    private static class AllGetRoadStatusCallBack extends HttpUtils.Callback<Map> {

        private final Test13Fragment mTest13Fragment_inner;

        /**
         * @param mapClass       结果数据封装体类型字节码
         * @param test13Fragment
         */
        AllGetRoadStatusCallBack(Class<Map> mapClass, Test13Fragment test13Fragment) {
            super(mapClass);
            mTest13Fragment_inner = test13Fragment;
        }

        @Override
        protected void onSuccess(Map map) {
            try {
                int status = DataUtils.obj2int(map.get("Status"));
                View[] viewArr = mTest13Fragment_inner.getRoadUiViewArr()[mTest13Fragment_inner.getRoadIdReqIndex()];
                for (View view : viewArr) {
                    int binarySearchStatus = Arrays.binarySearch(mTest13Fragment_inner.getRoadStatusArr(), status);
                    view.setBackgroundResource(mTest13Fragment_inner.getRoadStatusResIdArr()[binarySearchStatus]);
                }
            } catch (AppException e) {
                e.printStackTrace();
            }
            mTest13Fragment_inner.setRoadIdReqIndex(mTest13Fragment_inner.getRoadIdReqIndex() + 1);
            if (mTest13Fragment_inner.getRoadIdReqIndex() < mTest13Fragment_inner.getRoadIdArr().length) {
                Map<String, Integer> reqValues = new HashMap<>();
                reqValues.put("RoadId", mTest13Fragment_inner.getRoadIdArr()[mTest13Fragment_inner.getRoadIdReqIndex()]);
                RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetRoadStatus.do",
                        reqValues, this);
            }
        }

    }

    //定时器，定时刷新道路状态
    private class GetRoadStatusTimerTask extends TimerTask {

        private final AllGetRoadStatusCallBack mAllGetRoadStatusCallBack;

        GetRoadStatusTimerTask(AllGetRoadStatusCallBack allGetRoadStatusCallBack) {
            mAllGetRoadStatusCallBack = allGetRoadStatusCallBack;
        }

        @Override
        public void run() {
            mRoadIdReqIndex = 0;
            Map<String, Integer> reqValues = new HashMap<>();
            reqValues.put("RoadId", mRoadIdArr[mRoadIdReqIndex]);
            RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetRoadStatus.do",
                    reqValues, mAllGetRoadStatusCallBack);
        }

    }

    private class SenseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSenseDataMap.size();
        }

        @Override
        public Double getItem(int position) {
            return mSenseDataMap.get(mSenseNameArr[position]);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SenseViewHolder senseViewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mFragmentActivity).inflate(R.layout.lv_test13_lv_senselist,
                        test13_lv_senselist, false);
                senseViewHolder = SenseViewHolder.get(convertView);
            } else {
                senseViewHolder = (SenseViewHolder) convertView.getTag();
            }
            senseViewHolder.getTest13_tv_sensedesc().setText(mSenseDescArr[position]);
            senseViewHolder.getTest13_tv_senseval().setText(getItem(position) + " " + mSenseUnitArr[position]);
            return convertView;
        }

    }

    private static class SenseViewHolder {

        private final TextView test13_tv_sensedesc;
        private final TextView test13_tv_senseval;

        private SenseViewHolder(View view) {
            test13_tv_sensedesc = view.findViewById(R.id.test13_tv_sensedesc);
            test13_tv_senseval = view.findViewById(R.id.test13_tv_senseval);

        }

        public static SenseViewHolder get(View view) {
            Object tag = view.getTag();
            if (tag == null) {
                tag = new SenseViewHolder(view);
                view.setTag(tag);
            }
            return (SenseViewHolder) tag;
        }

        public TextView getTest13_tv_sensedesc() {
            return test13_tv_sensedesc;
        }

        public TextView getTest13_tv_senseval() {
            return test13_tv_senseval;
        }

    }

    private class RoadstatusexmapleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mRoadStatusResIdArr.length;
        }

        @Override
        public Integer getItem(int position) {
            return mRoadStatusResIdArr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RoadstatusexmapleViewHolder roadstatusexmapleViewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mFragmentActivity).inflate(R.layout.lv_test13_lv_roadstatusexmaple,
                        test13_lv_roadstatusexmaple, false);
                roadstatusexmapleViewHolder = RoadstatusexmapleViewHolder.get(convertView);
            } else {
                roadstatusexmapleViewHolder = (RoadstatusexmapleViewHolder) convertView.getTag();
            }
            roadstatusexmapleViewHolder.getTest13_iv_statusimg().setImageResource(getItem(position));
            roadstatusexmapleViewHolder.getTest13_tv_statusdesc().setText(mRoadStatusDescArr[position]);
            return convertView;
        }

    }

    private static class RoadstatusexmapleViewHolder {

        private final TextView test13_tv_statusdesc;
        private final ImageView test13_iv_statusimg;

        private RoadstatusexmapleViewHolder(View view) {
            test13_tv_statusdesc = view.findViewById(R.id.test13_tv_statusdesc);
            test13_iv_statusimg = view.findViewById(R.id.test13_iv_statusimg);
        }

        public static RoadstatusexmapleViewHolder get(View view) {
            Object tag = view.getTag();
            if (tag == null) {
                tag = new RoadstatusexmapleViewHolder(view);
                view.setTag(tag);
            }
            return (RoadstatusexmapleViewHolder) tag;
        }

        public TextView getTest13_tv_statusdesc() {
            return test13_tv_statusdesc;
        }

        public ImageView getTest13_iv_statusimg() {
            return test13_iv_statusimg;
        }

    }

    private static class GetAllSenseCallBack extends HttpUtils.Callback<Map> {

        private final Test13Fragment mTest13Fragment_inner;

        /**
         * @param mapClass       结果数据封装体类型字节码
         * @param test13Fragment
         */
        GetAllSenseCallBack(Class<Map> mapClass, Test13Fragment test13Fragment) {
            super(mapClass);
            mTest13Fragment_inner = test13Fragment;
        }


        @Override
        protected void onSuccess(Map map) {
            Map<String, Double> senseDataMap = mTest13Fragment_inner.getSenseDataMap();
            for (String senseName : mTest13Fragment_inner.getSenseNameArr()) {
                senseDataMap.put(senseName, (Double) map.get(senseName));
            }
            SenseAdapter senseAdapter = mTest13Fragment_inner.getSenseAdapter();
            if (senseAdapter != null) {
                senseAdapter.notifyDataSetChanged();
            }
        }

    }

    public Map<String, Double> getSenseDataMap() {
        return mSenseDataMap;
    }

    public String[] getSenseNameArr() {
        return mSenseNameArr;
    }

    public SenseAdapter getSenseAdapter() {
        return mSenseAdapter;
    }

    public int getRoadIdReqIndex() {
        return mRoadIdReqIndex;
    }

    public void setRoadIdReqIndex(int roadIdReqIndex) {
        mRoadIdReqIndex = roadIdReqIndex;
    }

    public Integer[] getRoadIdArr() {
        return mRoadIdArr;
    }

    public View[][] getRoadUiViewArr() {
        return mRoadUiViewArr;
    }

    public Integer[] getRoadStatusResIdArr() {
        return mRoadStatusResIdArr;
    }

    public Integer[] getRoadStatusArr() {
        return mRoadStatusArr;
    }

}
