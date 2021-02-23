package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.SelectAdapter;
import com.pro.bityard.adapter.StyleListAdapter;
import com.pro.bityard.adapter.TagsAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.StyleEntity;
import com.pro.bityard.entity.TagEntity;
import com.pro.bityard.viewutil.StatusBarUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class AccountSettingsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.text_submit)
    TextView text_submit;
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.recyclerView_style)
    RecyclerView recyclerView_style;


    private StyleListAdapter styleAdapter;



    private List<TagEntity> styleList;
    private List<TagEntity> allList;

    @Override
    protected void onResume() {
        super.onResume();
        getStyleList();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);

    }

    public static void enter(Context context) {
        Intent intent = new Intent(context, AccountSettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_account_setting;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(R.string.text_account_setting);
        findViewById(R.id.img_back).setOnClickListener(this);

        findViewById(R.id.layout_style).setOnClickListener(this);
        findViewById(R.id.layout_sys).setOnClickListener(this);

        recyclerView_style.setLayoutManager(new GridLayoutManager(this, 4));

        styleAdapter = new StyleListAdapter(this);
        recyclerView_style.setAdapter(styleAdapter);
        styleAdapter.isStyleTag(true);
        styleAdapter.setEnable(false);









    }




    @Override
    protected void initData() {


    }

    private void getStyleList() {

        allList = new ArrayList<>();
        NetManger.getInstance().myStyleList("2", (state, response) -> {
            if (state.equals(BUSY)) {
            } else if (state.equals(SUCCESS)) {
                StyleEntity styleEntity = (StyleEntity) response;
                Log.d("print", "getStyleList:143:  "+styleEntity);
                for (StyleEntity.DataBean content : styleEntity.getData()) {
                    //  styleList.add("0," + content.getContent() + type_style);
                    allList.add(new TagEntity(false, content.getContent(), content.getCode(), AppConfig.type_style));
                }

                tagEntityList=new ArrayList<>();
                tagEntityList.addAll(allList);

                styleAdapter.setDatas(allList);


               // setData();

            } else if (state.equals(FAILURE)) {
            }
        });


    }

    private void setData(){
        styleList = new ArrayList<>();
        for (TagEntity data : allList) {
            if (data.getType().equals(AppConfig.type_style)) {
                styleList.add(data);
            }
        }
        styleAdapter.setDatas(styleList);



    }

    @Override
    protected void initEvent() {

    }

    List<TagEntity> tagEntityList;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_style:
                StyleSelectActivity.enter(this, allList);
                break;
            case R.id.layout_sys:
                UserActivity.enter(this, IntentConfig.Keys.KEY_SYS);
                break;

        }
    }



}
