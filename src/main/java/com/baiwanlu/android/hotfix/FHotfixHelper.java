package com.baiwanlu.android.hotfix;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.alipay.euler.andfix.patch.PatchManager;
import com.baiwanlu.android.hotfix.utils.DownLoaderTask;
import com.baiwanlu.android.hotfix.utils.FHotfixLog;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by benren.fj on 6/18/16.
 */
public class FHotfixHelper {
    private PatchManager mPatchManager;

    /**
     * 热修复
     */
    public void hotfix(Context context, final String hotfixFileUrl, int targetVersionCode) {

        FHotfixLog.d("hotfixFileUrl=", hotfixFileUrl, ",targetVersionCode=", targetVersionCode);

        //initialize 热修复
        mPatchManager = new PatchManager(context);
        try {
            //获取app版本号
            String appVersion= context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            mPatchManager.init(appVersion);
        } catch (PackageManager.NameNotFoundException e) {
            FHotfixLog.e(e);
        }
        mPatchManager.loadPatch();


        try {
            int appVersionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            if (targetVersionCode != appVersionCode) {
                FHotfixLog.e("hotfix 版本不一致", "appVersionCode=", String.valueOf(appVersionCode));
                return;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(hotfixFileUrl)) {
            FHotfixLog.e("hotfix 链接为空");
            return;
        }

        try {
            final String fileName = new File(new URL(hotfixFileUrl).getFile()).getName();
            if (TextUtils.isEmpty(fileName)) {
                FHotfixLog.e("fileName 为空");
                return;
            }

            new DownLoaderTask(hotfixFileUrl, Environment.getExternalStorageDirectory().getAbsolutePath(), fileName, new DownLoaderTask.DownloadCallBack() {
                @Override
                public void onPostExecute() {
                    try {
                        //获取补丁文件路径
                        String path_all = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+ fileName;
                        FHotfixLog.d("addpatch", path_all);
                        mPatchManager.addPatch(path_all);
                    } catch (Throwable e) {
                        FHotfixLog.e(e);
                    }
                }
            }).execute();
        } catch (MalformedURLException e) {
            FHotfixLog.e(e);
        }
    }
}
