package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;


import com.pro.bityard.R;
import com.pro.bityard.base.BaseActivity;

public class DetailActivity extends BaseActivity {
    private static final String TYPE = "DETAIL_TYPE";


    public static void enter(Context context, String type, String id) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("TYPE", type);
        intent.putExtra("DETAIL_VALUE", id);
        context.startActivity(intent);
    }

    public static void enter(Context context, String type) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        String type = getIntent().getStringExtra("TYPE");
        if (type.equals("DETAIL_TYPE")) {
            addDetailFragment();
        }

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);


    }

    private void addDetailFragment() {
      /*  String id = getIntent().getStringExtra("DETAIL_VALUE");
         DetailFragment detailFragment = DetailFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_fragment_containter, detailFragment).commit();*/
    }


}
