package com.zykj.hunqianshiai.tools;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xu on 2017/12/1.
 */

public class AssetCopyer {

    public static void copyFilesFromAssets(Context context) {
        AssetManager assets = context.getAssets();
        InputStream open = null;
        try {
            open = assets.open("chongzhixuanzhong.png");
            File file = new File(SDCardUtils.getSDCardPath() + "/jl_redPacket");
            if (!file.exists()) {
                file.mkdir();
            } else {
                return;
            }
            FileOutputStream fos = new FileOutputStream(file+"/chongzhixuanzhong.png");
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = open.read(buffer)) != -1) {
                fos.write(buffer,0,byteCount);
            }
            fos.flush();// 刷新缓冲区
            open.close();
            fos.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}

