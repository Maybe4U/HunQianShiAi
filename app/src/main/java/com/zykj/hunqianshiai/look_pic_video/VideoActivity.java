package com.zykj.hunqianshiai.look_pic_video;

import android.os.Bundle;
import android.view.View;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * 视频播放
 */
public class VideoActivity extends BasesActivity {

    @Bind(R.id.videoplayer)
    JZVideoPlayerStandard mJZVideoPlayerStandard;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        String video = bundle.getString("video");
        mJZVideoPlayerStandard.setUp(UrlContent.PIC_URL + video, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        glide(UrlContent.PIC_URL + video, mJZVideoPlayerStandard.thumbImageView, mOptions);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View view) {
        super.onClick(view);
    }
}
