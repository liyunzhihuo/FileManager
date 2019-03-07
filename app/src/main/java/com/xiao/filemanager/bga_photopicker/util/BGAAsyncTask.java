package com.xiao.filemanager.bga_photopicker.util;

import android.os.AsyncTask;

public abstract class BGAAsyncTask<Params, Result> extends AsyncTask<Params, Void, Result> {

    private Callback<Result> mCallBack;

    public BGAAsyncTask(Callback<Result> callback) {
        this.mCallBack = callback;
    }


    public void cancelTask() {
        if (getStatus() != Status.FINISHED) {
            cancel(true);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mCallBack != null) {
            mCallBack.onTaskCancelled();
        }
        //无法放到 cancelTask()中，因为此方法会在cancelTask()后执行，所以如果放到cancelTask()中，则此字段永远是空，也就不会调用 onCancel()方法了
        mCallBack = null;
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (mCallBack != null) {
            mCallBack.onPostExecute(result);
        }
    }

    public interface Callback<Result> {
        /**
         * 当结果返回的时候执行
         *
         * @param result 返回的结果
         */
        void onPostExecute(Result result);

        /**
         * 当请求被取消的时候执行
         */
        void onTaskCancelled();
    }
}
