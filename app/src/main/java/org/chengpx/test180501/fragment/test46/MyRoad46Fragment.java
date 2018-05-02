package org.chengpx.test180501.fragment.test46;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.chengpx.mylib.BaseFragment;
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
public class MyRoad46Fragment extends BaseFragment {

    private static String sTag = "org.chengpx.test180501.fragment.test46.MyRoad46Fragment";

    private ListView test46_lv_trafficinfoList;
    private Integer[] mTrafficLightIdArr = {
            1, 2, 3, 4, 5
    };
    private Integer[] mRoadIdArr = {
            1, 2, 3, 4, 5
    };
    private Timer mTimer;
    private int mTrafficLightIdGetTrafficLightNowStatusReqIndex;
    private int mTrafficLightIdGetTrafficLightConfigActionReqIndex;
    private int mRoadIdReqIndex;
    private Map<Integer, Map<String, Object>> mTrafficLightInfoMap;
    private MyRoad46Fragment mMyRoad46Fragment;

    @Override
    protected void initListener() {

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMyRoad46Fragment = this;
        View view = inflater.inflate(R.layout.fragment_myroad46, container, false);
        test46_lv_trafficinfoList = (ListView) view.findViewById(R.id.test46_lv_trafficlightinfoList);
        return view;
    }

    @Override
    protected void onDie() {

    }

    @Override
    protected void main() {

    }

    @Override
    protected void initData() {
        mTrafficLightInfoMap = new HashMap<>();
        for (Integer aMTrafficLightIdArr : mTrafficLightIdArr) {
            mTrafficLightInfoMap.put(aMTrafficLightIdArr, new HashMap<String, Object>());
        }
        mTrafficLightIdGetTrafficLightConfigActionReqIndex = 0;
        Map<String, Integer> reqValues = new HashMap<>();
        reqValues.put("TrafficLightId", mTrafficLightIdArr[mTrafficLightIdGetTrafficLightConfigActionReqIndex]);
        RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetTrafficLightConfigAction.do",
                reqValues, new GetTrafficLightConfigActionCallBack(Map.class, mMyRoad46Fragment));
        mTimer = new Timer();
        mTimer.schedule(new MyTimerTask(new GetTrafficLightNowStatusCallBack(Map.class, mMyRoad46Fragment), new GetRoadStatusCallBack(Map.class, mMyRoad46Fragment)),
                0, 1000);
    }

    @Override
    protected void onDims() {
        mTrafficLightInfoMap = null;
        mTimer.cancel();
        mTimer = null;
        mTrafficLightIdGetTrafficLightNowStatusReqIndex = 0;
        mTrafficLightIdGetTrafficLightConfigActionReqIndex = 0;
        mRoadIdReqIndex = 0;
    }

    private static class GetRoadStatusCallBack extends HttpUtils.Callback<Map> {

        /**
         * @param mapClass 结果数据封装体类型字节码
         * @param myRoad46Fragment
         */
        GetRoadStatusCallBack(Class<Map> mapClass, MyRoad46Fragment myRoad46Fragment) {
            super(mapClass);
        }

        @Override
        protected void onSuccess(Map map) {

        }

    }

    private static class GetTrafficLightConfigActionCallBack extends HttpUtils.Callback<Map> {

        private final MyRoad46Fragment mMyRoad46Fragment_inner;

        /**
         * @param mapClass            结果数据封装体类型字节码
         * @param myRoad46Fragment
         */
        GetTrafficLightConfigActionCallBack(Class<Map> mapClass, MyRoad46Fragment myRoad46Fragment) {
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
                Log.d(sTag, objMap.toString());
            }
        }

    }

    private static class GetTrafficLightNowStatusCallBack extends HttpUtils.Callback<Map> {

        private final MyRoad46Fragment mMyRoad46Fragment_inner;

        /**
         * @param mapClass            结果数据封装体类型字节码
         * @param myRoad46Fragment
         */
        GetTrafficLightNowStatusCallBack(Class<Map> mapClass, MyRoad46Fragment myRoad46Fragment) {
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
                Log.d(sTag, objMap.toString());
            }
        }

    }

    private class MyTimerTask extends TimerTask {

        private final GetTrafficLightNowStatusCallBack mGetTrafficLightNowStatusCallBack;
        private final GetRoadStatusCallBack mGetRoadStatusCallBack;

        MyTimerTask(GetTrafficLightNowStatusCallBack getTrafficLightNowStatusCallBack, GetRoadStatusCallBack getRoadStatusCallBack) {
            mGetTrafficLightNowStatusCallBack = getTrafficLightNowStatusCallBack;
            mGetRoadStatusCallBack = getRoadStatusCallBack;
        }

        @Override
        public void run() {
            mTrafficLightIdGetTrafficLightNowStatusReqIndex = 0;
            Map<String, Integer> reqValues = new HashMap<>();
            reqValues.put("TrafficLightId", mTrafficLightIdArr[mTrafficLightIdGetTrafficLightNowStatusReqIndex]);
            RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetTrafficLightNowStatus.do",
                    reqValues, mGetTrafficLightNowStatusCallBack);
            reqValues.clear();
            mRoadIdReqIndex = 0;
            reqValues.put("RoadId", mRoadIdArr[mRoadIdReqIndex]);
            RequestPool.getInstance().add("http://192.168.2.19:9090/transportservice/type/jason/action/GetRoadStatus.do",
                    reqValues, mGetRoadStatusCallBack);
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

}
