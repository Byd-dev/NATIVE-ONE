package com.pro.bityard.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.FollowAdapter;
import com.pro.bityard.adapter.StyleListAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.entity.StyleEntity;
import com.pro.bityard.entity.TagEntity;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.bityard.viewutil.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class FilterResultActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.text_title)
    TextView text_title;

    @BindView(R.id.text_filter)
    TextView text_filter;
    @BindView(R.id.layout_bar)
    RelativeLayout layout_bar;

    @BindView(R.id.swipeRefreshLayout_circle)
    SwipeRefreshLayout swipeRefreshLayout_circle;


    @BindView(R.id.recyclerView_circle)
    HeaderRecyclerView recyclerView_circle;

    private FollowAdapter followAdapter;

    @BindView(R.id.layout_circle_null)
    LinearLayout layout_circle_null;

    @BindView(R.id.text_style)
    TextView text_style;
    @BindView(R.id.text_days_rate)
    TextView text_days_rate;
    @BindView(R.id.text_days_draw)
    TextView text_days_draw;
    private StyleEntity styleEntity;
    private List<TagEntity> styleList, daysRateList, daysDrawList, daysBetList;
    private List<TagEntity> allList;
    private StyleListAdapter styleAdapter;
    private List<TagEntity> intent_value;
    private String tags;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);

    }

    public static void enter(Activity context, List<TagEntity> value) {
        Intent intent = new Intent(context, FilterResultActivity.class);
        intent.putExtra("VALUE", (Serializable) value);
        context.startActivityForResult(intent, 100);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_filter_result;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {


        text_title.setText(R.string.text_filter_result);
        text_filter.setVisibility(View.VISIBLE);

        findViewById(R.id.img_back).setOnClickListener(this);
        text_filter.setOnClickListener(this);

        followAdapter = new FollowAdapter(this);
        recyclerView_circle.setLayoutManager(new LinearLayoutManager(this));


        recyclerView_circle.setAdapter(followAdapter);


        followAdapter.setWarningClick(() -> {
            Util.lightOff(this);
            PopUtil.getInstance().showTip(this, layout_view, false, getString(R.string.text_circle_warning),
                    state -> {

                    });
        });

        swipeRefreshLayout_circle.setOnRefreshListener(() -> {
            getFollowList(tags, defeatGe, defeatLe, drawGe, drawLe, daysGe, daysLe);
            getStyleList();
        });
        swipeRefreshLayout_circle.setColorSchemeColors(getResources().getColor(R.color.maincolor));


        text_style.setOnClickListener(this);
        text_days_rate.setOnClickListener(this);
        text_days_draw.setOnClickListener(this);
    }


    List<TagEntity> tagSelect;

    private StringBuilder stringBuilder = null;
    String defeatGe = null;
    String defeatLe = null;
    String drawGe = null;
    String drawLe = null;
    String daysGe = null;
    String daysLe = null;

    @Override
    protected void initData() {

        Intent intent = getIntent();
        intent_value = (List<TagEntity>) intent.getSerializableExtra("VALUE");
        stringBuilder = new StringBuilder();
        tagSelect = new ArrayList<>();
        for (TagEntity tagentity : intent_value) {
            tagSelect.add(tagentity);
        }

        Log.d("print", "initData:165:  " + tagSelect);
        for (TagEntity tagentity : tagSelect) {
            String code = tagentity.getCode();
            String type = tagentity.getType();
            if (type.equals(AppConfig.type_style)) {
                stringBuilder.append(code).append(",");
            } else if (type.equals(AppConfig.type_rate)) {
                if (code.equals("null")) {
                    defeatGe = null;
                    defeatLe = null;
                } else {
                    String[] split = code.split(",");
                    defeatGe = split[0];
                    defeatLe = split[1];
                }
            } else if (type.equals(AppConfig.type_draw)) {
                if (code.equals("null")) {
                    drawGe = null;
                    drawLe = null;
                } else {
                    String[] split = code.split(",");
                    drawGe = split[0];
                    drawLe = split[1];
                }
            } else if (type.equals(AppConfig.type_day)) {
                if (code.equals("null")) {
                    daysGe = null;
                    daysLe = null;
                } else {
                    String[] split = code.split(",");
                    daysGe = split[0];
                    daysLe = split[1];
                }
            }
        }
        if (!stringBuilder.toString().equals("")) {
            tags = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
        } else {
            tags = null;
        }


        getFollowList(tags, defeatGe, defeatLe, drawGe, drawLe, daysGe, daysLe);
        getStyleList();
    }


    /*过滤*/
    private void getFollowList(String tags, String defeatGe, String defeatLe, String drawGe, String drawLe, String daysGe, String daysLe) {
        NetManger.getInstance().followList(null, null,
                null, null, tags, defeatGe, defeatLe, drawGe,
                drawLe, daysGe, daysLe, (state, response) -> {
                    if (state.equals(BUSY)) {
                        swipeRefreshLayout_circle.setRefreshing(true);
                    } else if (state.equals(SUCCESS)) {
                        swipeRefreshLayout_circle.setRefreshing(false);
                        layout_circle_null.setVisibility(View.GONE);
                        recyclerView_circle.setVisibility(View.VISIBLE);
                        FollowEntity followEntity = (FollowEntity) response;
                        if (followEntity.getTotal() != 0) {
                            followAdapter.setDatas(followEntity.getData());
                        } else {

                        }
                    } else if (state.equals(FAILURE)) {
                        swipeRefreshLayout_circle.setRefreshing(false);
                        layout_circle_null.setVisibility(View.VISIBLE);
                        recyclerView_circle.setVisibility(View.GONE);
                    }
                });
    }

    private void getStyleList() {
        allList = new ArrayList<>();
        NetManger.getInstance().styleList("2", (state, response) -> {
            if (state.equals(BUSY)) {
            } else if (state.equals(SUCCESS)) {
                styleEntity = (StyleEntity) response;
                for (StyleEntity.DataBean content : styleEntity.getData()) {
                    allList.add(new TagEntity(false, content.getContent(), content.getCode(), AppConfig.type_style));
                }


            } else if (state.equals(FAILURE)) {
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_filter:
            case R.id.img_back:
                Intent intent = getIntent();
                intent.putExtra(AppConfig.KEY_FILTER_RESULT, (Serializable) intent_value);
                setResult(AppConfig.CODE_FILTER, intent);
                finish();
                break;
            case R.id.text_style:
                Util.lightOff(this);
                text_style.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_white_down), null);
                styleList = new ArrayList<>();
                if (allList != null) {
                    for (TagEntity data : allList) {
                        if (data.getType().equals(AppConfig.type_style)) {
                            styleList.add(data);
                        }
                    }
                    for (TagEntity tagentity : styleList) {
                        for (TagEntity tagSelect : tagSelect) {
                            if (tagentity.getContent().equals(tagSelect.getContent())) {
                                tagentity.setChecked(true);
                            }
                        }
                    }
                    showStyleWindow(styleList);
                }
                break;

        }
    }

    private Map<String, String> style_like;

    /*行情选择*/
    private void showStyleWindow(List<TagEntity> styleList) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.item_style_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_style);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        styleAdapter = new StyleListAdapter(this);
        recyclerView.setAdapter(styleAdapter);
        styleAdapter.setDatas(styleList);

        popupWindow.setOnDismissListener(() -> {
            text_style.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_white_right), null);
            Util.lightOn(this);
        });

        style_like = new HashMap<>();
        styleAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                style_like.put(data.getCode(), data.getContent());
            } else {
                style_like.remove(data.getCode());
            }
        });


        view.findViewById(R.id.btn_submit).setOnClickListener(v ->
                Toast.makeText(this, style_like.toString(), Toast.LENGTH_SHORT).show()
        );

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_bar);
    }
}