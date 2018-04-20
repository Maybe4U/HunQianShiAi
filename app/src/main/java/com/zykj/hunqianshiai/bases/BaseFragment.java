package com.zykj.hunqianshiai.bases;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lzy.okgo.model.HttpParams;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.home.OnDoubleClickListener;
import com.zykj.hunqianshiai.tools.CustomDialog;
import com.zykj.hunqianshiai.tools.T;

import butterknife.ButterKnife;


/**
 * Created by ${xu} on 2017/10/18.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    public Context mContext;
    public View mView;
    public FragmentActivity mActivity;
    private CustomDialog mDialog;
    public static HttpParams mParams;

    private int time;

    public abstract int getLayoutId();

    public abstract void initView();

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mActivity = getActivity();
        if (null == mParams) {
            mParams = new HttpParams();
        }

        if (null == mView) {
            mView = LayoutInflater.from(mContext).inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, mView);
            initView();
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，
            // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }

        return mView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mView) {
            mView = null;
        }
        ButterKnife.unbind(this);
    }

    public void openActivity(Class clazz) {
        openActivity(clazz, null);
    }

    public void openActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(mContext, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            startActivity(intent);
        }
    }

    public void toastShow(String content) {
        T.showShort(mContext, content);

    }

    public void showDialog() {
        if (null == mDialog) {
            mDialog = new CustomDialog(mActivity, R.style.CustomDialog);
        }
        mDialog.show();
    }

    public void hideDialog() {
        if (null != mDialog) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void onClick(View view) {

    }


    /*===================新增====================*/
    /**
     * method desc.  : 传递当前RecyclerView的Item值，回滚到顶部
     *                 修改休眠值可以改变滚动时间
     * params        :
     * return        :
     */
    protected class GoTopTask extends AsyncTask<Integer, Integer, String> {
        private RecyclerView recyclerView;
        public GoTopTask(View view) {
            recyclerView = (RecyclerView) view;
        }

        @Override
        protected void onPreExecute() {
            //回到顶部时间置0  此处的时间不是狭义上的时间
            time=0;
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub

            for(int i=params[0];i>=0;i--){
                publishProgress(i);
                //返回顶部时间耗费15个item还没回去，则直接去顶部
                //目的：要产生滚动的假象，但也不能耗时过多
                time++;
                if(time>15){
                    publishProgress(0);
                    return null;
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        public void onProgressUpdate(Integer... values) {
            recyclerView.smoothScrollToPosition(values[0]);
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

    }
}
