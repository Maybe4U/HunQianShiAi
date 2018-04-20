package com.zykj.hunqianshiai.intro;

import android.Manifest;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.login_register.RegisterActivity;
import com.zykj.hunqianshiai.login_register.goago.ChooseSexActivity;
import com.zykj.hunqianshiai.login_register.login.LoginActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 选择登录
 */
public class ChooseLoginActivity extends BasesActivity {

    @Bind(R.id.video_view)
    CustomVideoView video_view;
    private MediaPlayer player;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_choose_login;
    }

    @Override
    protected void initView() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        String uri = "android.resource://" + getPackageName() + "/" + R.raw.b2;
        video_view.setVideoURI(Uri.parse(uri));
        video_view.start();
        //监听视频播放完的代码
        video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                // TODO Auto-generated method stub
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
    }

    @OnClick({R.id.tv_login, R.id.tv_register, R.id.tv_goago})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_login://登录
                openActivity(LoginActivity.class);
                //finish();
                break;
            case R.id.tv_register://注册
                openActivity(RegisterActivity.class);
                //finish();
                break;
            case R.id.tv_goago://逛一逛
                openActivity(ChooseSexActivity.class);
//                openActivity(HomeMainActivity.class);
//                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!video_view.isPlaying()) {
            video_view.start();
        }
    }

}
