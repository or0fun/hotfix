package com.baiwanlu.android.hotfix.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by benren.fj on 6/18/16.
 */
public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {
    private final String TAG = "DownLoaderTask";
    private URL mUrl;
    private File mFile;
    private int mProgress = 0;
    private ProgressReportingOutputStream mOutputStream;
    private DownloadCallBack downloadCallBack;
    // 文件下载的url 保存路径 out

    public DownLoaderTask(String url, String out, String fileName, DownloadCallBack downloadCallBack) {
        super();
        try {
            mUrl = new URL(url);
            mFile = new File(out, fileName);
            FHotfixLog.d(TAG, "out=" + out + ", name=" + fileName + ",mUrl.getFile()=" + mUrl.getFile());
        } catch (MalformedURLException e) {
            FHotfixLog.e(e);
        }
        this.downloadCallBack = downloadCallBack;

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Long doInBackground(Void... params) {
        return download();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    //下载保存后执行
    @Override
    protected void onPostExecute(Long result) {
        if(isCancelled())
            return;
        if (null != downloadCallBack) {
            downloadCallBack.onPostExecute();
        }
    }

    private long download(){
        URLConnection connection = null;
        int bytesCopied = 0;
        try {
            connection = mUrl.openConnection();
            int length = connection.getContentLength();
            if(mFile.exists()&&length == mFile.length()){
                FHotfixLog.d(TAG, "file " + mFile.getName() + " already exits!!");
                return 0l;
            }
            mOutputStream = new ProgressReportingOutputStream(mFile);
            publishProgress(0,length);
            bytesCopied = copy(connection.getInputStream(),mOutputStream);
            if(bytesCopied != length && length != -1){
                Log.e(TAG, "Download incomplete bytesCopied=" + bytesCopied + ", length" + length);
            }
            mOutputStream.close();
        } catch (IOException e) {
            FHotfixLog.e(e);
        }
        return bytesCopied;
    }

    private int copy(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024 * 8];
        BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
        BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } catch (IOException e) {
            FHotfixLog.e(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                FHotfixLog.e(e);
            }
            try {
                in.close();
            } catch (IOException e) {
                FHotfixLog.e(e);
            }
        }
        return count;
    }

    private final class ProgressReportingOutputStream extends FileOutputStream {

        public ProgressReportingOutputStream(File file)
                throws FileNotFoundException {
            super(file);
        }

        @Override
        public void write(byte[] buffer, int byteOffset, int byteCount)
                throws IOException {
            super.write(buffer, byteOffset, byteCount);
            mProgress += byteCount;
            publishProgress(mProgress);
        }

    }

    public interface DownloadCallBack {
        void onPostExecute();
    }
}