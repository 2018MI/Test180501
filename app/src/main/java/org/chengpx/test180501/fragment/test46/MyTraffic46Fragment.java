package org.chengpx.test180501.fragment.test46;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;

import org.chengpx.mylib.AppException;
import org.chengpx.mylib.BaseFragment;
import org.chengpx.mylib.common.DataUtils;
import org.chengpx.mylib.http.HttpUtils;
import org.chengpx.test180501.R;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 我的交通
 * <p>
 * create at 2018/5/2 11:16 by chengpx
 */
public class MyTraffic46Fragment extends BaseFragment {

    private View inflate;
    private TextView tvPm25;
    private TextView tvPm25Des;
    private android.widget.VideoView vvPm25;
    private TextView tvLightNow;
    private TextView tvLightMax;
    private TextView tvLightMin;
    private TextView tvLightDes;
    private String url = "http://192.168.2.19:9090/transportservice/action/GetAllSense.do";
    private android.widget.SeekBar seekbar;
    private TextView tvLightSeekbarNum;
    private Timer timer;

    @Override
    protected void initListener() {

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_myloadstatus, container, false);
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

        timer = new Timer();
        timer.schedule(new MyTimerTask(new Callback(Map.class)), 0, 3000);
    }

    public class Callback extends HttpUtils.Callback<Map> {

        /**
         * @param mapClass 结果数据封装体类型字节码
         */
        public Callback(Class<Map> mapClass) {
            super(mapClass);
        }

        @Override
        protected void onSuccess(Map map) {
            try {
                Object serverpm25 = map.get("pm2.5");
                int pm25 = DataUtils.obj2int(serverpm25);
                tvPm25.setText("PM2.5当前值:" + pm25);
                Object serverLight = map.get("LightIntensity");
                int light = DataUtils.obj2int(serverLight);
                tvLightNow.setText("光照强度当前值：" + light);
                if (pm25 > 200) {
                    tvPm25Des.setText("不适合出行");
                    NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(getContext())
                            .setContentText("pm2.5大于两百，不适合出行")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .build();
                    manager.notify(1, notification);

                    MediaController mediaController = new MediaController(getContext());
                    vvPm25.setMediaController(mediaController);
                    vvPm25.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "//" + R.raw.test));
                    vvPm25.start();
                } else {
                    tvPm25Des.setText("适合出行");
                }
                if (light > 3190) {
                    tvLightDes.setText("不适合出行");
                    seekbar.setProgress(light);
                    tvLightSeekbarNum.setText(light + "");
                    NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(getContext())
                            .setContentText("光照较强，出门请戴墨镜")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .build();
                    manager.notify(1, notification);
                } else if (light < 1100) {
                    tvLightDes.setText("不适合出行");
                    seekbar.setProgress(light);
                    tvLightSeekbarNum.setText(light + "");
                    NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(getContext())
                            .setContentText("光照较弱，出门请开灯")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .build();
                    manager.notify(1, notification);
                } else {
                    tvLightDes.setText("适合出行");
                    seekbar.setProgress(light);
                    tvLightSeekbarNum.setText(light + "");
                }

            } catch (AppException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyTimerTask extends TimerTask {
        private Callback mCallback;

        MyTimerTask(Callback callback) {
            mCallback = callback;
        }

        @Override
        public void run() {
            HttpUtils.getInstance().sendPost(url, "", mCallback);
        }
    }

    @Override
    protected void onDims() {
        timer.cancel();
        timer = null;
    }

    private void initView() {
        tvPm25 = inflate.findViewById(R.id.tv_pm25);
        tvPm25Des = inflate.findViewById(R.id.tv_pm25_des);
        vvPm25 = inflate.findViewById(R.id.vv_pm25);
        tvLightNow = inflate.findViewById(R.id.tv_light_now);
        tvLightMax = inflate.findViewById(R.id.tv_light_max);
        tvLightMin = inflate.findViewById(R.id.tv_light_min);
        tvLightDes = inflate.findViewById(R.id.tv_light_des);
        seekbar = inflate.findViewById(R.id.seekbar);
        tvLightSeekbarNum = inflate.findViewById(R.id.tv_light_seekbar_num);
        seekbar.setMax(5000);
    }
}
