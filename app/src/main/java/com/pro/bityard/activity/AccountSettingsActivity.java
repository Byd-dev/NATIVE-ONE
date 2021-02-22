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
    @BindView(R.id.recyclerView_select)
    RecyclerView recyclerView_select;
    @BindView(R.id.recyclerView_style)
    RecyclerView recyclerView_style;
    @BindView(R.id.recyclerView_days_rate)
    RecyclerView recyclerView_days_rate;
    @BindView(R.id.recyclerView_days_draw)
    RecyclerView recyclerView_days_draw;
    @BindView(R.id.recyclerView_bet_days)
    RecyclerView recyclerView_bet_days;
    private StyleListAdapter styleAdapter;
    private TagsAdapter rateAdapter, drawAdapter, daysAdapter;

    private SelectAdapter selectAdapter;
    private List<TagEntity> styleList, daysRateList, daysDrawList, daysBetList;
    private List<TagEntity> allList;

    private Map<String, TagEntity> tag_select;

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
        return R.layout.activity_settings_filter;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(R.string.text_account_setting);
        findViewById(R.id.img_back).setOnClickListener(this);

        recyclerView_style.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_days_rate.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_days_draw.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_bet_days.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_select.setLayoutManager(new GridLayoutManager(this, 4));

        styleAdapter = new StyleListAdapter(this);
        recyclerView_style.setAdapter(styleAdapter);

        rateAdapter = new TagsAdapter(this, 1);
        recyclerView_days_rate.setAdapter(rateAdapter);


        drawAdapter = new TagsAdapter(this, 2);
        recyclerView_days_draw.setAdapter(drawAdapter);

        daysAdapter = new TagsAdapter(this, 3);
        recyclerView_bet_days.setAdapter(daysAdapter);


        selectAdapter = new SelectAdapter(this);
        recyclerView_select.setAdapter(selectAdapter);


        tag_select = new HashMap<>();
        styleAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                tag_select.put(data.getContent() + data.getType(), data);
                for (TagEntity tagentity : allList) {
                    if (tagentity.getContent().equals(data.getContent())) {
                        tagentity.setChecked(true);
                    }
                }
            } else {
                tag_select.remove(data.getContent() + data.getType());
                for (TagEntity tagentity : allList) {
                    if (tagentity.getContent().equals(data.getContent())) {
                        tagentity.setChecked(false);
                    }
                }
            }
            setSelect(tag_select);
        });


        rateAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                tag_select.put(data.getContent() + data.getType(), data);
                for (TagEntity tagentity : allList) {
                    if (tagentity.getContent().equals(data.getContent()) && tagentity.getType().equals(data.getType())) {
                        tagentity.setChecked(true);
                    }
                }
            } else {
                tag_select.remove(data.getContent() + data.getType());
                for (TagEntity tagentity : allList) {
                    if (tagentity.getContent().equals(data.getContent()) && tagentity.getType().equals(data.getType())) {
                        tagentity.setChecked(false);
                    }
                }
            }
            setSelect(tag_select);

        });

        drawAdapter.setOnItemChaneClick((isChecked, data) -> {

            if (isChecked) {
                tag_select.put(data.getContent() + data.getType(), data);
                for (TagEntity tagentity : allList) {
                    if (tagentity.getContent().equals(data.getContent()) && tagentity.getType().equals(data.getType())) {
                        tagentity.setChecked(true);
                    }
                }
            } else {
                tag_select.remove(data.getContent() + data.getType());
                for (TagEntity tagentity : allList) {
                    if (tagentity.getContent().equals(data.getContent()) && tagentity.getType().equals(data.getType())) {
                        tagentity.setChecked(false);
                    }
                }
            }
            setSelect(tag_select);

        });
        daysAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                tag_select.put(data.getContent() + data.getType(), data);
                for (TagEntity tagentity : allList) {
                    if (tagentity.getContent().equals(data.getContent()) && tagentity.getType().equals(data.getType())) {
                        tagentity.setChecked(true);
                    }
                }
            } else {

                tag_select.remove(data.getContent() + data.getType());
                for (TagEntity tagentity : allList) {
                    if (tagentity.getContent().equals(data.getContent()) && tagentity.getType().equals(data.getType())) {
                        tagentity.setChecked(false);
                    }
                }
            }
            setSelect(tag_select);

        });

        selectAdapter.setOnItemDeleteClick((position, data) -> {
            tag_select.remove(data.getContent() + data.getType());
            selectList.remove(data);
            selectAdapter.notifyDataSetChanged();

            styleList = new ArrayList<>();
            daysRateList = new ArrayList<>();
            daysDrawList = new ArrayList<>();
            daysBetList = new ArrayList<>();
            for (TagEntity tagEntity : allList) {
                if (tagEntity.getContent().equals(data.getContent()) && tagEntity.getType().equals(data.getType())) {
                    tagEntity.setChecked(false);
                }
                if (tagEntity.getType().equals(AppConfig.type_style)) {
                    styleList.add(tagEntity);
                }
                if (tagEntity.getType().equals(AppConfig.type_rate)) {
                    daysRateList.add(tagEntity);
                }
                if (tagEntity.getType().equals(AppConfig.type_draw)) {
                    daysDrawList.add(tagEntity);
                }
                if (tagEntity.getType().equals(AppConfig.type_day)) {
                    daysBetList.add(tagEntity);
                }
            }
            Log.d("print", "initView:165: " + allList);

            Log.d("print", "initView:166: " + styleList);
            styleAdapter.setDatas(styleList);
            styleAdapter.notifyDataSetChanged();
            rateAdapter.setDatas(daysRateList);
            rateAdapter.notifyDataSetChanged();
            drawAdapter.setDatas(daysDrawList);
            drawAdapter.notifyDataSetChanged();
            daysAdapter.setDatas(daysBetList);
            daysAdapter.notifyDataSetChanged();


        });
    }

    private List<TagEntity> selectList;

    private void setSelect(Map<String, TagEntity> tag_select) {

        selectList = new ArrayList<>();
        for (TagEntity value : tag_select.values()) {
            selectList.add(value);
        }
        selectAdapter.setDatas(selectList);
        selectAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        getStyleList();

    }

    private void getStyleList() {

        allList = new ArrayList<>();
        NetManger.getInstance().styleList("2", (state, response) -> {
            if (state.equals(BUSY)) {
            } else if (state.equals(SUCCESS)) {
                StyleEntity styleEntity = (StyleEntity) response;
                for (StyleEntity.DataBean content : styleEntity.getData()) {
                    //  styleList.add("0," + content.getContent() + type_style);
                    allList.add(new TagEntity(false, content.getContent(), content.getCode(), AppConfig.type_style));
                }

                allList.add(new TagEntity(false, getString(R.string.text_unlimited), "null", AppConfig.type_rate));
                allList.add(new TagEntity(false, "≤20%", "0,20", AppConfig.type_rate));
                allList.add(new TagEntity(false, "20%-60%", "20,60", AppConfig.type_rate));
                allList.add(new TagEntity(false, "≥60%", "60,100", AppConfig.type_rate));

                allList.add(new TagEntity(false, getString(R.string.text_unlimited), "null", AppConfig.type_draw));
                allList.add(new TagEntity(false, "≤10%", "0,10", AppConfig.type_draw));
                allList.add(new TagEntity(false, "10%-50%", "10,50", AppConfig.type_draw));
                allList.add(new TagEntity(false, "≥50%", "50,100", AppConfig.type_draw));

                allList.add(new TagEntity(false, getString(R.string.text_unlimited), "null", AppConfig.type_day));
                allList.add(new TagEntity(false, "≤30", "0,30", AppConfig.type_day));
                allList.add(new TagEntity(false, "30-60", "30,60", AppConfig.type_day));
                allList.add(new TagEntity(false, "≥60", "60,180", AppConfig.type_day));

                setData();

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

        daysRateList = new ArrayList<>();
        for (TagEntity data : allList) {
            if (data.getType().equals(AppConfig.type_rate)) {
                daysRateList.add(data);
            }
        }
        rateAdapter.setDatas(daysRateList);


        daysDrawList = new ArrayList<>();
        for (TagEntity data : allList) {
            if (data.getType().equals(AppConfig.type_draw)) {
                daysDrawList.add(data);
            }
        }
        drawAdapter.setDatas(daysDrawList);


        daysBetList = new ArrayList<>();
        for (TagEntity data : allList) {
            if (data.getType().equals(AppConfig.type_day)) {
                daysBetList.add(data);
            }
        }
        daysAdapter.setDatas(daysBetList);
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

        }
    }



}
