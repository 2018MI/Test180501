package org.chengpx.test180501.fragment.test46;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.chengpx.mylib.AppException;
import org.chengpx.mylib.ViewPagerFragment;
import org.chengpx.mylib.common.DataUtils;
import org.chengpx.mylib.http.HttpUtils;
import org.chengpx.mylib.http.RequestPool;
import org.chengpx.test180501.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 我的路况
 * <p>
 * create at 2018/5/2 11:15 by chengpx
 */
public class MyRoad46Fragment extends ViewPagerFragment implements View.OnClickListener {

    private static String sTag = "org.chengpx.test180501.fragment.test46.MyRoad46Fragment";

    private ListView myroad46_lv_trafficinfolist;
    private Integer[] mTrafficLightIdArr = {
            1, 2, 3, 4, 5
    };
    private Integer[] mRoadIdArr = {
            1, 2, 3, 4, 5
    };
    private String[] mTrafficlightStatusArr = {
            "Red", "Yellow", "Green"
    };
    private Integer[] mTrafficlightStatusImgResArr = {
            R.drawable.shape_oval_red, R.drawable.shape_oval_yellow, R.drawable.shape_oval_green
    };
    private String[] mTrafficlightStatusDescArr = {
            "红灯", "黄灯", "绿灯"
    };
    private String[] mTrafficlightTimeSearchKeyArr = {
            "RedTime", "YellowTime", "GreenTime"
    };
    private Timer mTimer;
    private int mTrafficLightIdGetTrafficLightNowStatusReqIndex;
    private int mTrafficLightIdGetTrafficLightConfigActionReqIndex;
    private int mRoadIdReqIndex;
    private Map<Integer, Map<String, Object>> mTrafficLightInfoMap;
    private MyRoad46Fragment mMyRoad46Fragment;
    private MyAdapter mMyAdapter;
    private AlertDialog mAlertDialog;
    private EditText myroad_et_trafficklightconfigred;
    private EditText myroad_et_trafficklightconfigyellow;
    private EditText myroad_et_trafficklightconfiggreen;

    @Override
    protected void initListener() {
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMyRoad46Fragment = this;
        View view = inflater.inflate(R.layout.fragment_myroad46, container, false);
        myroad46_lv_trafficinfolist = (ListView) view.findViewById(R.id.test46_lv_trafficlightinfolist);
        return view;
    }

    @Override
    protected void onDie() {
    }

    @Override
    protected void main() {
        mMyAdapter = new MyAdapter();
        myroad46_lv_trafficinfolist.setAdapter(mMyAdapter);
    }

    @Override
    protected void initData() {
        mTrafficLightInfoMap = new HashMap<>();
        for (Integer aMTrafficLightIdArr : mTrafficLightIdArr) {
            mTrafficLightInfoMap.put(aMTrafficLightIdArr, new HashMap<String, Object>());
        }
        Map<String, Integer> reqValues = new HashMap<>();
        reqValues.put("TrafficLightId", mTrafficLightIdArr[mTrafficLightIdGetTrafficLightConfigActionReqIndex]);
        RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetTrafficLightConfigAction.do",
                reqValues, new AllGetTrafficLightConfigActionCallBack(Map.class, mMyRoad46Fragment));
        mTimer = new Timer();
        mTimer.schedule(new MyTimerTask(new AllGetTrafficLightNowStatusCallBack(Map.class, mMyRoad46Fragment), new AllGetRoadStatusCallBack(Map.class, mMyRoad46Fragment)),
                0, 1000);
    }

    @Override
    protected void onDims() {
        mTimer.cancel();
        mTimer = null;
        mMyAdapter = null;
        mTrafficLightInfoMap = null;
        mTrafficLightIdGetTrafficLightNowStatusReqIndex = 0;
        mTrafficLightIdGetTrafficLightConfigActionReqIndex = 0;
        mRoadIdReqIndex = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myroad_btn_horizontalcontrol:
                showTrafficlightConfigDialog(v.getTag());
                break;
            case R.id.myroad_btn_verticalcontrol:
                showTrafficlightConfigDialog(v.getTag());
                break;
            case R.id.myroad_imagebutton_trafficlightconfigquit:
                mAlertDialog.dismiss();
                mAlertDialog = null;
                break;
            case R.id.myroad_btn_trafficklightconfigconfirm:
                configTrafficLight((int) v.getTag());
                break;
            case R.id.myroad_btn_trafficklightconfigcancel:
                mAlertDialog.dismiss();
                mAlertDialog = null;
                break;
        }
    }

    private void configTrafficLight(int trafficlightId) {
        String strRedTime = myroad_et_trafficklightconfigred.getText().toString();
        String strYellowTime = myroad_et_trafficklightconfigyellow.getText().toString();
        String strGreenTime = myroad_et_trafficklightconfiggreen.getText().toString();
        if (!verifyStr(strRedTime)) {
            Toast.makeText(mFragmentActivity, "RedTime 非法", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!verifyStr(strYellowTime)) {
            Toast.makeText(mFragmentActivity, "YellowTime 非法", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!verifyStr(strGreenTime)) {
            Toast.makeText(mFragmentActivity, "GreenTime 非法", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Integer> reqValues = new HashMap<>();
        reqValues.put("TrafficLightId", trafficlightId);
        reqValues.put("RedTime", Integer.parseInt(strRedTime));
        reqValues.put("YellowTime", Integer.parseInt(strYellowTime));
        reqValues.put("GreenTime", Integer.parseInt(strGreenTime));
        RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/SetTrafficLightConfig.do",
                reqValues, new SetTrafficLightConfigCallBack(Map.class, mMyRoad46Fragment, trafficlightId));
        mAlertDialog.dismiss();
        mAlertDialog = null;
    }

    private boolean verifyStr(String str) {
        return !"".equals(str) && str.matches("^\\d{0,2}$") && 0 < Integer.parseInt(str) && Integer.parseInt(str) <= 30;
    }

    /**
     * 弹出红绿灯配置窗口
     *
     * @param tag
     */
    private void showTrafficlightConfigDialog(Object tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mFragmentActivity);
        View view = LayoutInflater.from(mFragmentActivity).inflate(R.layout.dialog_trafficlightconfig, null);
        builder.setView(view);
        mAlertDialog = builder.create();
        mAlertDialog.show();
        ImageButton myroad_imagebutton_trafficlightconfigquit = view.findViewById(R.id.myroad_imagebutton_trafficlightconfigquit);
        myroad_et_trafficklightconfigred = view.findViewById(R.id.myroad_et_trafficklightconfigred);
        myroad_et_trafficklightconfigyellow = view.findViewById(R.id.myroad_et_trafficklightconfigyellow);
        myroad_et_trafficklightconfiggreen = view.findViewById(R.id.myroad_et_trafficklightconfiggreen);
        Button myroad_btn_trafficklightconfigconfirm = view.findViewById(R.id.myroad_btn_trafficklightconfigconfirm);
        Button myroad_btn_trafficklightconfigcancel = view.findViewById(R.id.myroad_btn_trafficklightconfigcancel);
        myroad_imagebutton_trafficlightconfigquit.setOnClickListener(this);
        myroad_btn_trafficklightconfigconfirm.setOnClickListener(this);
        myroad_btn_trafficklightconfigcancel.setOnClickListener(this);
        myroad_btn_trafficklightconfigconfirm.setTag(tag);
    }

    private static class SetTrafficLightConfigCallBack extends HttpUtils.Callback<Map> {

        private final MyRoad46Fragment mMyRoad46Fragment_inner;
        private final int mTrafficlightId;

        /**
         * @param mapClass         结果数据封装体类型字节码
         * @param myRoad46Fragment
         * @param trafficlightId
         */
        SetTrafficLightConfigCallBack(Class<Map> mapClass, MyRoad46Fragment myRoad46Fragment, int trafficlightId) {
            super(mapClass);
            mMyRoad46Fragment_inner = myRoad46Fragment;
            mTrafficlightId = trafficlightId;
        }

        @Override
        protected void onSuccess(Map map) {
            String result = (String) map.get("result");
            if ("ok".equals(result)) {
                Toast.makeText(mMyRoad46Fragment_inner.getActivity(), mTrafficlightId + " 号路口红绿灯配置信息成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mMyRoad46Fragment_inner.getActivity(), "配置失败请重新提交", Toast.LENGTH_SHORT).show();
            }
            mMyRoad46Fragment_inner.setTrafficLightIdGetTrafficLightConfigActionReqIndex(0);
            Map<String, Integer> reqValues = new HashMap<>();
            reqValues.put("TrafficLightId", mMyRoad46Fragment_inner.getTrafficLightIdArr()[mMyRoad46Fragment_inner.getTrafficLightIdGetTrafficLightConfigActionReqIndex()]);
            RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetTrafficLightConfigAction.do",
                    reqValues, new AllGetTrafficLightConfigActionCallBack(Map.class, mMyRoad46Fragment_inner));
        }

    }

    private static class ViewHolder {

        private TextView myroad_tv_roadid;
        private TextView myroad_tv_trafficlightinfo;
        private TextView myroad_tv_horizontalstatus;
        private TextView myroad_tv_horizontalimgstatus;
        private TextView myroad_tv_verticalstatus;
        private TextView myroad_tv_verticalimgstatus;
        private Button myroad_btn_horizontalcontrol;
        private Button myroad_btn_verticalcontrol;

        ViewHolder(View view, MyRoad46Fragment myRoad46Fragment) {
            myroad_tv_roadid = (TextView) view.findViewById(R.id.myroad_tv_roadid);
            myroad_tv_trafficlightinfo = (TextView) view.findViewById(R.id.myroad_tv_trafficlightinfo);
            myroad_tv_horizontalstatus = (TextView) view.findViewById(R.id.myroad_tv_horizontalstatus);
            myroad_tv_horizontalimgstatus = (TextView) view.findViewById(R.id.myroad_tv_horizontalimgstatus);
            myroad_tv_verticalstatus = (TextView) view.findViewById(R.id.myroad_tv_verticalstatus);
            myroad_tv_verticalimgstatus = (TextView) view.findViewById(R.id.myroad_tv_verticalimgstatus);
            myroad_btn_horizontalcontrol = (Button) view.findViewById(R.id.myroad_btn_horizontalcontrol);
            myroad_btn_verticalcontrol = (Button) view.findViewById(R.id.myroad_btn_verticalcontrol);
            myroad_btn_horizontalcontrol.setOnClickListener(myRoad46Fragment);
            myroad_btn_verticalcontrol.setOnClickListener(myRoad46Fragment);
        }

        public static ViewHolder get(View view, MyRoad46Fragment myRoad46Fragment) {
            Object tag = view.getTag();
            if (tag == null) {
                tag = new ViewHolder(view, myRoad46Fragment);
                view.setTag(tag);
            }
            return (ViewHolder) tag;
        }

        public TextView getMyroad_tv_roadid() {
            return myroad_tv_roadid;
        }

        public TextView getMyroad_tv_trafficlightinfo() {
            return myroad_tv_trafficlightinfo;
        }

        public TextView getMyroad_tv_horizontalstatus() {
            return myroad_tv_horizontalstatus;
        }

        public TextView getMyroad_tv_horizontalimgstatus() {
            return myroad_tv_horizontalimgstatus;
        }

        public TextView getMyroad_tv_verticalstatus() {
            return myroad_tv_verticalstatus;
        }

        public TextView getMyroad_tv_verticalimgstatus() {
            return myroad_tv_verticalimgstatus;
        }

        public Button getMyroad_btn_horizontalcontrol() {
            return myroad_btn_horizontalcontrol;
        }

        public Button getMyroad_btn_verticalcontrol() {
            return myroad_btn_verticalcontrol;
        }

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTrafficLightInfoMap == null ? 0 : mTrafficLightInfoMap.size();
        }

        @Override
        public Map<String, Object> getItem(int position) {
            return mTrafficLightInfoMap.get(mTrafficLightIdArr[position]);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mFragmentActivity).inflate(R.layout.lv_test46_lv_trafficinfolist,
                        myroad46_lv_trafficinfolist, false);
                viewHolder = ViewHolder.get(convertView, mMyRoad46Fragment);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Map<String, Object> item = getItem(position);
            Log.d(sTag, item.toString());
            viewHolder.getMyroad_tv_roadid().setText(item.get("RoadId") + "");
            viewHolder.getMyroad_tv_trafficlightinfo().setText("红灯 " + item.get("RedTime")
                    + " 黄灯 " + item.get("YellowTime")
                    + " 绿灯 " + item.get("GreenTime"));
            String status = (String) item.get("Status");
            if (status != null) {
                // int binarySearchStatus = Arrays.binarySearch(mTrafficlightStatusArr, status);
                int binarySearchStatus = -1;
                for (binarySearchStatus = 0; binarySearchStatus < mTrafficlightStatusArr.length; binarySearchStatus++) {
                    if (status.equals(mTrafficlightStatusArr[binarySearchStatus])) {
                        break;
                    }
                }
                Log.d(sTag, "binarySearchStatus = " + binarySearchStatus);
                if (binarySearchStatus > -1) {
                    viewHolder.getMyroad_tv_horizontalstatus()
                            .setText(mTrafficlightStatusDescArr[binarySearchStatus] + item.get(mTrafficlightTimeSearchKeyArr[binarySearchStatus]) + " 秒");
                    viewHolder.getMyroad_tv_horizontalimgstatus().setBackgroundResource(mTrafficlightStatusImgResArr[binarySearchStatus]);
                    viewHolder.getMyroad_tv_verticalstatus()
                            .setText(mTrafficlightStatusDescArr[binarySearchStatus] + item.get(mTrafficlightTimeSearchKeyArr[binarySearchStatus]) + " 秒");
                    viewHolder.getMyroad_tv_verticalimgstatus().setBackgroundResource(mTrafficlightStatusImgResArr[binarySearchStatus]);
                }
            }
            viewHolder.getMyroad_btn_horizontalcontrol().setTag(mTrafficLightIdArr[position]);
            viewHolder.getMyroad_btn_verticalcontrol().setTag(mTrafficLightIdArr[position]);
            return convertView;
        }

    }

    private static class AllGetRoadStatusCallBack extends HttpUtils.Callback<Map> {

        private final MyRoad46Fragment mMyRoad46Fragment_inner;

        /**
         * @param mapClass         结果数据封装体类型字节码
         * @param myRoad46Fragment
         */
        AllGetRoadStatusCallBack(Class<Map> mapClass, MyRoad46Fragment myRoad46Fragment) {
            super(mapClass);
            mMyRoad46Fragment_inner = myRoad46Fragment;
        }

        @Override
        protected void onSuccess(Map map) {
            try {
                int status = DataUtils.obj2int(map.get("Status"));
                if (status > 3) {
                    notifyMsg(mMyRoad46Fragment_inner.getRoadIdArr()[mMyRoad46Fragment_inner.mRoadIdReqIndex] + " 号路口处于拥挤状态, 请选择合适的路线");
                }
                mMyRoad46Fragment_inner
                        .getTrafficLightInfoMap()
                        .get(mMyRoad46Fragment_inner.getTrafficLightIdArr()[mMyRoad46Fragment_inner.getRoadIdReqIndex()])
                        .put("RoadStatus", status);
                mMyRoad46Fragment_inner
                        .getTrafficLightInfoMap()
                        .get(mMyRoad46Fragment_inner.getTrafficLightIdArr()[mMyRoad46Fragment_inner.getRoadIdReqIndex()])
                        .put("RoadId", mMyRoad46Fragment_inner.getRoadIdArr()[mMyRoad46Fragment_inner.getRoadIdReqIndex()]);
                MyAdapter myAdapter = mMyRoad46Fragment_inner.getMyAdapter();
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged();
                }
            } catch (AppException e) {
                e.printStackTrace();
            }
            mMyRoad46Fragment_inner
                    .setRoadIdReqIndex((mMyRoad46Fragment_inner.getRoadIdReqIndex() + 1));
            if (mMyRoad46Fragment_inner.getRoadIdReqIndex() < mMyRoad46Fragment_inner.getRoadIdArr().length) {
                Map<String, Integer> reqValues = new HashMap<>();
                reqValues.put("RoadId", mMyRoad46Fragment_inner.getRoadIdArr()[mMyRoad46Fragment_inner.getRoadIdReqIndex()]);
                RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetRoadStatus.do",
                        reqValues, this);
            } else {
                MyAdapter myAdapter = mMyRoad46Fragment_inner.getMyAdapter();
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged();
                }
            }
        }

        private void notifyMsg(String msg) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mMyRoad46Fragment_inner.getActivity());
            Notification notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(msg)
                    .build();
            NotificationManager notificationManager = (NotificationManager) mMyRoad46Fragment_inner.getActivity()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(1, notification);
        }

    }

    private static class AllGetTrafficLightConfigActionCallBack extends HttpUtils.Callback<Map> {

        private final MyRoad46Fragment mMyRoad46Fragment_inner;

        /**
         * @param mapClass         结果数据封装体类型字节码
         * @param myRoad46Fragment
         */
        AllGetTrafficLightConfigActionCallBack(Class<Map> mapClass, MyRoad46Fragment myRoad46Fragment) {
            super(mapClass);
            mMyRoad46Fragment_inner = myRoad46Fragment;
        }

        @Override
        protected void onSuccess(Map map) {
            Map<String, Object> objMap = mMyRoad46Fragment_inner
                    .getTrafficLightInfoMap()
                    .get(mMyRoad46Fragment_inner.getTrafficLightIdArr()[mMyRoad46Fragment_inner.getTrafficLightIdGetTrafficLightConfigActionReqIndex()]);
            for (Object key : map.keySet()) {
                objMap.put((String) key, map.get(key));
            }
            mMyRoad46Fragment_inner.setTrafficLightIdGetTrafficLightConfigActionReqIndex(mMyRoad46Fragment_inner.getTrafficLightIdGetTrafficLightConfigActionReqIndex() + 1);
            if (mMyRoad46Fragment_inner.getTrafficLightIdGetTrafficLightConfigActionReqIndex() < mMyRoad46Fragment_inner.getTrafficLightIdArr().length) {
                Map<String, Integer> reqValues = new HashMap<>();
                reqValues.put("TrafficLightId", mMyRoad46Fragment_inner.getTrafficLightIdArr()[mMyRoad46Fragment_inner.getTrafficLightIdGetTrafficLightConfigActionReqIndex()]);
                RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetTrafficLightConfigAction.do",
                        reqValues, this);
            } else {
                MyAdapter myAdapter = mMyRoad46Fragment_inner.getMyAdapter();
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    private static class AllGetTrafficLightNowStatusCallBack extends HttpUtils.Callback<Map> {

        private final MyRoad46Fragment mMyRoad46Fragment_inner;

        /**
         * @param mapClass         结果数据封装体类型字节码
         * @param myRoad46Fragment
         */
        AllGetTrafficLightNowStatusCallBack(Class<Map> mapClass, MyRoad46Fragment myRoad46Fragment) {
            super(mapClass);
            mMyRoad46Fragment_inner = myRoad46Fragment;
        }

        @Override
        protected void onSuccess(Map map) {
            Map<String, Object> objMap = mMyRoad46Fragment_inner
                    .getTrafficLightInfoMap()
                    .get(mMyRoad46Fragment_inner.getTrafficLightIdArr()[mMyRoad46Fragment_inner.getTrafficLightIdGetTrafficLightNowStatusReqIndex()]);
            for (Object key : map.keySet()) {
                objMap.put((String) key, map.get(key));
            }
            mMyRoad46Fragment_inner.setTrafficLightIdGetTrafficLightNowStatusReqIndex(mMyRoad46Fragment_inner.getTrafficLightIdGetTrafficLightNowStatusReqIndex() + 1);
            if (mMyRoad46Fragment_inner.getTrafficLightIdGetTrafficLightNowStatusReqIndex() < mMyRoad46Fragment_inner.getTrafficLightIdArr().length) {
                Map<String, Integer> reqValues = new HashMap<>();
                reqValues.put("TrafficLightId", mMyRoad46Fragment_inner.getTrafficLightIdArr()[mMyRoad46Fragment_inner.getTrafficLightIdGetTrafficLightNowStatusReqIndex()]);
                RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetTrafficLightNowStatus.do",
                        reqValues, this);
            } else {
                MyAdapter myAdapter = mMyRoad46Fragment_inner.getMyAdapter();
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    private class MyTimerTask extends TimerTask {

        private final AllGetTrafficLightNowStatusCallBack mAllGetTrafficLightNowStatusCallBack;
        private final AllGetRoadStatusCallBack mAllGetRoadStatusCallBack;

        MyTimerTask(AllGetTrafficLightNowStatusCallBack allGetTrafficLightNowStatusCallBack, AllGetRoadStatusCallBack allGetRoadStatusCallBack) {
            mAllGetTrafficLightNowStatusCallBack = allGetTrafficLightNowStatusCallBack;
            mAllGetRoadStatusCallBack = allGetRoadStatusCallBack;
        }

        @Override
        public void run() {
            mTrafficLightIdGetTrafficLightNowStatusReqIndex = 0;
            Map<String, Integer> reqValues = new HashMap<>();
            reqValues.put("TrafficLightId", mTrafficLightIdArr[mTrafficLightIdGetTrafficLightNowStatusReqIndex]);
            RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetTrafficLightNowStatus.do",
                    reqValues, mAllGetTrafficLightNowStatusCallBack);
            mRoadIdReqIndex = 0;
            reqValues.put("RoadId", mRoadIdArr[mRoadIdReqIndex]);
            RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetRoadStatus.do",
                    reqValues, mAllGetRoadStatusCallBack);
        }

    }

    public Map<Integer, Map<String, Object>> getTrafficLightInfoMap() {
        return mTrafficLightInfoMap;
    }

    public Integer[] getTrafficLightIdArr() {
        return mTrafficLightIdArr;
    }

    public int getTrafficLightIdGetTrafficLightNowStatusReqIndex() {
        return mTrafficLightIdGetTrafficLightNowStatusReqIndex;
    }

    public void setTrafficLightIdGetTrafficLightNowStatusReqIndex(int trafficLightIdGetTrafficLightNowStatusReqIndex) {
        mTrafficLightIdGetTrafficLightNowStatusReqIndex = trafficLightIdGetTrafficLightNowStatusReqIndex;
    }

    public int getTrafficLightIdGetTrafficLightConfigActionReqIndex() {
        return mTrafficLightIdGetTrafficLightConfigActionReqIndex;
    }

    public void setTrafficLightIdGetTrafficLightConfigActionReqIndex(int trafficLightIdGetTrafficLightConfigActionReqIndex) {
        mTrafficLightIdGetTrafficLightConfigActionReqIndex = trafficLightIdGetTrafficLightConfigActionReqIndex;
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

    public MyAdapter getMyAdapter() {
        return mMyAdapter;
    }

}
