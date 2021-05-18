package com.pro.bityard.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.SelectAdapter;
import com.pro.bityard.adapter.StyleListAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.StyleEntity;
import com.pro.bityard.entity.TagEntity;
import com.pro.bityard.utils.Util;
import com.pro.bityard.viewutil.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
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

public class StyleSelectActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.text_save)
    TextView text_save;
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.recyclerView_select)
    RecyclerView recyclerView_select;
    @BindView(R.id.recyclerView_style)
    RecyclerView recyclerView_style;


    private StyleListAdapter styleAdapter;


    private SelectAdapter selectAdapter;
    private List<TagEntity> styleList, daysRateList, daysDrawList, daysBetList;
    private List<TagEntity> allList;

    private Map<String, TagEntity> tag_select;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setTheme(this);

    }

    public static void enter(Context context) {
        Intent intent = new Intent(context, StyleSelectActivity.class);
        context.startActivity(intent);
    }

    public static void enter(Activity context, List<TagEntity> value) {
        Intent intent = new Intent(context, StyleSelectActivity.class);
        intent.putExtra("VALUE", (Serializable) value);
        context.startActivity(intent);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_style_select;
    }

    @Override
    protected void initPresenter() {

    }

    private String selectValue;

    @Override
    protected void initView(View view) {
        text_title.setText(R.string.text_account_setting);
        text_save.setOnClickListener(this);
        text_save.setVisibility(View.VISIBLE);
        findViewById(R.id.img_back).setOnClickListener(this);

        recyclerView_style.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_select.setLayoutManager(new GridLayoutManager(this, 4));

        styleAdapter = new StyleListAdapter(this);
        recyclerView_style.setAdapter(styleAdapter);


        selectAdapter = new SelectAdapter(this);
        recyclerView_select.setAdapter(selectAdapter);


        tag_select = new HashMap<>();

        styleAdapter.setOnItemChaneClick((isChecked, data) -> {
            Log.d("print", "initView:110: " + tag_select);
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


        selectAdapter.setOnItemDeleteClick((position, data) -> {
            if (tag_select.size() <= 1) {
                return;
            }
            tag_select.remove(data.getContent() + data.getType());
            selectList.remove(data);
            selectAdapter.notifyDataSetChanged();
            styleList = new ArrayList<>();
            for (TagEntity tagEntity : allList) {
                if (tagEntity.getContent().equals(data.getContent()) && tagEntity.getType().equals(data.getType())) {
                    tagEntity.setChecked(false);
                }
                if (tagEntity.getType().equals(AppConfig.type_style)) {
                    styleList.add(tagEntity);
                }
            }

            styleAdapter.setDatas(styleList);
            styleAdapter.notifyDataSetChanged();


        });
    }


    private void addTag() {


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
        Intent intent = getIntent();

        selectList = (List<TagEntity>) intent.getSerializableExtra("VALUE");
        selectAdapter.setDatas(selectList);
        getStyleList();

    }

    private void getStyleList() {

        allList = new ArrayList<>();
        NetManger.getInstance().styleList("2", (state, response) -> {
            if (state.equals(BUSY)) {
            } else if (state.equals(SUCCESS)) {
                StyleEntity styleEntity = (StyleEntity) response;
                //  styleList.add("0," + content.getContent() + type_style);
                for (StyleEntity.DataBean content : styleEntity.getData()) {
                    allList.add(new TagEntity(false, content.getContent(), content.getCode(), AppConfig.type_style));
                }

                setData();

            } else if (state.equals(FAILURE)) {
            }
        });


    }

    private void setData() {
        styleList = new ArrayList<>();
        for (TagEntity data : allList) {
            if (data.getType().equals(AppConfig.type_style)) {
                styleList.add(data);
            }
        }
        //遍历是否是选中的
        for (TagEntity tag : selectList) {
            for (TagEntity data : styleList) {
                if (data.getCode().equals(tag.getCode())) {
                    data.setChecked(true);
                }
            }
        }
        styleAdapter.setDatas(styleList);

    }

    @Override
    protected void initEvent() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.text_save:
                if (tag_select.size() > 4) {
                    Toast.makeText(this, getResources().getString(R.string.text_four_much), Toast.LENGTH_SHORT).show();
                    return;
                }
                NetManger.getInstance().addTag("2", Util.styleValue(tag_select), (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        Toast.makeText(this, this.getResources().getString(R.string.text_success), Toast.LENGTH_SHORT).show();

                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });
                break;

        }
    }


}
