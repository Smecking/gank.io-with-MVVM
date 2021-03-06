package com.fall.gank.core;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.example.rxpermisson.PermissionAppCompatActivity;
import com.fall.gank.callback.BaseActivityCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by qqq34 on 2016/11/24.
 */

public abstract class BaseActivity extends PermissionAppCompatActivity implements BaseActivityCallback, BaseView {
    public static final String KEY_VIEW_MODEL = "BaseActivity.viewmodel";
    private View view;
    private BaseObservable baseObservable;
    private AttachPresenterHelper mAttachPresenterHelper;
    protected List<IPresenter> iPresenterList ;  //储存引用的所有presenter，一个界面可能会有多个Presenter的情况
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iPresenterList = new ArrayList<>();
        if (savedInstanceState != null) {
            baseObservable = (BaseObservable) savedInstanceState.get(KEY_VIEW_MODEL);
        }
        initBinding();
        if (baseObservable != null) {
            initOldData(baseObservable);
        } else {
            initData();
        }
        mAttachPresenterHelper = new AttachPresenterHelper(iPresenterList);
        attachViewModel();
        initToolbar(savedInstanceState);
        initView(savedInstanceState);
        view = findViewById(android.R.id.content);
        initListeners();
        mAttachPresenterHelper.initPresenter(baseObservable == null, this);

    }

    protected abstract void initBinding();


    @Override
    protected void onDestroy() {
 mAttachPresenterHelper.destroyPresenter();
        super.onDestroy();
    }


    protected abstract void initToolbar(Bundle savedInstanceState);


    public void attachViewModel() {
   mAttachPresenterHelper.attachl();
    }

    @Override
    public void onShowSnackBar(String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onStartActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (getViewModel() != null) {
            outState.putSerializable(KEY_VIEW_MODEL, (Serializable) getViewModel());
        }
        super.onSaveInstanceState(outState);
    }

    public boolean isDarkTheme() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }
}
