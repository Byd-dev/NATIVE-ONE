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

import com.pro.bityard.R;
import com.pro.bityard.adapter.FollowAdapter;
import com.pro.bityard.adapter.SelectResultAdapter;
import com.pro.bityard.adapter.StyleListAdapter;
import com.pro.bityard.adapter.TagsAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.entity.StyleEntity;
import com.pro.bityard.entity.TagEntity;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
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
    RecyclerView recyclerView_circle;

    private FollowAdapter followAdapter;
    private int page = 1;

    @BindView(R.id.layout_circle_null)
    LinearLayout layout_circle_null;

    @BindView(R.id.text_style)
    TextView text_style;
    @BindView(R.id.text_days_rate)
    TextView text_days_rate;
    @BindView(R.id.text_bet_days)
    TextView text_bet_days;
    private StyleEntity styleEntity;
    private List<TagEntity> styleList, daysRateList, daysDrawList, daysBetList;
    private List<TagEntity> allList;
    private StyleListAdapter styleAdapter;
    private TagsAdapter rateAdapter, drawAdapter, daysAdapter;
    private SelectResultAdapter selectAdapter;
    @BindView(R.id.recyclerView_select)
    RecyclerView recyclerView_select;
    @BindView(R.id.layout_select)
    LinearLayout layout_select;
    private String tags;
    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;
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
        return R.layout.activity_filter_result;
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
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView_circle.setLayoutManager(linearLayoutManager);


        recyclerView_circle.setAdapter(followAdapter);


        followAdapter.setWarningClick(() -> {
            Util.lightOff(this);
            PopUtil.getInstance().showTip(this, layout_view, false, getString(R.string.text_circle_warning),
                    state -> {

                    });
        });
        //跟单监听
        followAdapter.setOnFollowClick((dataBean) -> {
            FollowDetailActivity.enter(this, AppConfig.FOLLOW,dataBean);

        });
        swipeRefreshLayout_circle.setOnRefreshListener(() -> {
            // getFollowList(tags, defeatGe, defeatLe, drawGe, drawLe, daysGe, daysLe);
            getStyleList();
        });
        Util.colorSwipe(this,swipeRefreshLayout_circle);


        text_style.setOnClickListener(this);
        text_days_rate.setOnClickListener(this);
        text_bet_days.setOnClickListener(this);

        recyclerView_select.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        selectAdapter = new SelectResultAdapter(this);
        recyclerView_select.setAdapter(selectAdapter);
        recyclerView_circle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeRefreshLayout_circle.isRefreshing()) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == followAdapter.getItemCount() - 1) {
                    followAdapter.startLoad();
                    page = page + 1;
                    filter(AppConfig.LOAD,page);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


        selectAdapter.setOnItemDeleteClick((position, data) -> {
            //删除之前要把它置为未选中
            for (TagEntity tagentity : tag_select.values()) {
                if (tagentity.getContent().equals(data.getContent()) && tagentity.getType().equals(data.getType())) {
                    tagentity.setChecked(false);
                }
            }
            tag_select.remove(data.getContent() + data.getType());

           // Log.d("print", "initView:删除之后:  " + tag_select.values());
            setSelect(tag_select);
            page=1;
            filter(AppConfig.FIRST,page);


        });
    }


    private List<TagEntity> selectList;
    private HashMap<String, TagEntity> tag_select;

    private StringBuilder stringBuilder = null;


    @Override
    protected void initData() {

        Intent intent = getIntent();
        selectList = (List<TagEntity>) intent.getSerializableExtra("VALUE");


        tag_select = new HashMap<>();

        for (TagEntity tagEntity : selectList) {
            tag_select.put(tagEntity.getContent() + tagEntity.getType(), tagEntity);
        }
        Log.d("print", "initView:删除之前:  " + tag_select.values());
        page=1;
        filter(AppConfig.FIRST,page);

        getStyleList();
    }


    private void setSelect(Map<String, TagEntity> tag_select) {

        if (tag_select.values().size() == 0) {
            layout_select.setVisibility(View.GONE);
        } else {
            layout_select.setVisibility(View.VISIBLE);
        }
        selectList = new ArrayList<>();
        for (TagEntity value : tag_select.values()) {
            selectList.add(value);
        }
        selectAdapter.setDatas(selectList);
        selectAdapter.notifyDataSetChanged();
    }

    /*过滤*/
    private void filter(String loadType,int page) {
        String rateGe = null;
        String rateLe = null;
        String drawGe = null;
        String drawLe = null;
        String daysGe = null;
        String daysLe = null;
        stringBuilder = new StringBuilder();
        Log.d("print", "filter:240:  " + tag_select.values().size());
        if (tag_select.values().size() == 0) {
            layout_select.setVisibility(View.GONE);
        } else {
            layout_select.setVisibility(View.VISIBLE);
        }
        setSelect(tag_select);
        for (TagEntity tagentity : tag_select.values()) {
            String code = tagentity.getCode();
            String type = tagentity.getType();
            if (type.equals(AppConfig.type_style)) {
                stringBuilder.append(code).append(",");
            } else if (type.equals(AppConfig.type_rate)) {
                if (code.equals("null")) {
                    rateGe = null;
                    rateLe = null;
                } else {
                    String[] split = code.split(",");
                    rateGe = String.valueOf(TradeUtil.div(Double.parseDouble(split[0]),100,2));
                    rateLe = String.valueOf(TradeUtil.div(Double.parseDouble(split[1]),100,2));
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

        getFollowList(loadType,tags, rateGe, rateLe, drawGe, drawLe, daysGe, daysLe,page);
    }


    private void getFollowList(String loadType,String tags, String rateGe, String rateLe, String drawGe, String drawLe, String daysGe, String daysLe,int page) {
        NetManger.getInstance().followList(null, null,
                null, null, tags, rateGe, rateLe, drawGe,
                drawLe, daysGe, daysLe,String.valueOf(page),"10", (state, response) -> {
                    if (state.equals(BUSY)) {
                        swipeRefreshLayout_circle.setRefreshing(true);
                    } else if (state.equals(SUCCESS)) {
                        swipeRefreshLayout_circle.setRefreshing(false);
                        FollowEntity followEntity = (FollowEntity) response;
                        if (loadType.equals(AppConfig.LOAD)){
                            followAdapter.addDatas(followEntity.getData());
                        }else {
                            if (followEntity.getData().size() == 0) {
                                layout_circle_null.setVisibility(View.VISIBLE);
                                recyclerView_circle.setVisibility(View.GONE);
                            } else {
                                layout_circle_null.setVisibility(View.GONE);
                                recyclerView_circle.setVisibility(View.VISIBLE);
                            }
                            followAdapter.setDatas(followEntity.getData());
                        }


                    } else if (state.equals(FAILURE)) {
                        swipeRefreshLayout_circle.setRefreshing(false);
                        layout_circle_null.setVisibility(View.VISIBLE);
                        recyclerView_circle.setVisibility(View.GONE);
                    }
                });
    }

    private void getStyleList() {
        //设置跟单的列表page为1
        page=1;
        allList = new ArrayList<>();
        NetManger.getInstance().styleList("2", (state, response) -> {
            if (state.equals(BUSY)) {

            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout_circle.setRefreshing(false);
                styleEntity = (StyleEntity) response;
                for (StyleEntity.DataBean content : styleEntity.getData()) {
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
                for (TagEntity tag : tag_select.values()) {
                    for (TagEntity tagAll : allList) {
                        if (tag.getContent().equals(tagAll.getContent()) && tag.getType().equals(tagAll.getType())) {
                            tagAll.setChecked(true);
                        }
                    }
                }
                intent.putExtra(AppConfig.KEY_FILTER_RESULT, (Serializable) allList);
                setResult(AppConfig.CODE_FILTER, intent);
                finish();
                break;
            case R.id.text_style:
                Util.lightOff(this);
                text_style.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_white_down), null);
                showStyleWindow();
                break;

            case R.id.text_days_rate:
                Util.lightOff(this);
                text_days_rate.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_white_down), null);
                showDaysRateWindow();
                break;
            case R.id.text_bet_days:
                Util.lightOff(this);
                text_bet_days.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_white_down), null);
                showDaysBetWindow();
                break;
        }
    }

    /*风格选择*/
    private void showStyleWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.item_style_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_style);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        styleAdapter = new StyleListAdapter(this);
        recyclerView.setAdapter(styleAdapter);

        styleList = new ArrayList<>();
        if (allList != null) {
            //加载风格的数据
            for (TagEntity data : allList) {
                if (data.getType().equals(AppConfig.type_style)) {
                    styleList.add(data);
                }
            }
            // Log.d("print", "showStyleWindow:风格初始化: " + tag_select.values());
            //设置风格已选择的为true
            for (TagEntity tag : tag_select.values()) {
                for (TagEntity tagentity : styleList) {
                    if (tagentity.getContent().equals(tag.getContent())) {
                        tagentity.setChecked(true);
                    }
                }
            }
        }

        styleAdapter.setDatas(styleList);
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
            Log.d("print", "showStyleWindow:风格:  " + tag_select.values());
            page=1;
            filter(AppConfig.FIRST,page);
            setSelect(tag_select);

        });


        popupWindow.setOnDismissListener(() -> {
            text_style.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_white_right), null);
            Util.lightOn(this);
        });


        view.findViewById(R.id.btn_submit).setOnClickListener(v -> {

            popupWindow.dismiss();

        });


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_bar);
    }

    /*收益率选择*/
    private void showDaysRateWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.item_rate_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_rate);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        rateAdapter = new TagsAdapter(this, 1);
        recyclerView.setAdapter(rateAdapter);

        daysRateList = new ArrayList<>();
        if (allList != null) {
            //加载风格的数据
            for (TagEntity data : allList) {
                if (data.getType().equals(AppConfig.type_rate)) {
                    daysRateList.add(data);
                }
            }
            //设置风格已选择的为true
            for (TagEntity tag : tag_select.values()) {
                for (TagEntity tagentity : daysRateList) {
                    if (tagentity.getContent().equals(tag.getContent()) & tagentity.getType().equals(tag.getType())) {
                        tagentity.setChecked(true);
                    }
                }
            }
        }
        rateAdapter.setDatas(daysRateList);
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
            Log.d("print", "showStyleWindow:收益率:  " + tag_select.values());
            page=1;
            filter(AppConfig.FIRST,page);

            setSelect(tag_select);

        });


        popupWindow.setOnDismissListener(() -> {
            text_days_rate.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_white_right), null);
            Util.lightOn(this);
        });


        view.findViewById(R.id.btn_submit).setOnClickListener(v -> {

            popupWindow.dismiss();

        });


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_bar);
    }

    /*最大回撤*/
    private void showDaysBetWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.item_day_bet_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_bet_days);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        daysAdapter = new TagsAdapter(this, 2);
        recyclerView.setAdapter(daysAdapter);

        daysBetList = new ArrayList<>();
        if (allList != null) {
            //加载风格的数据
            for (TagEntity data : allList) {
                if (data.getType().equals(AppConfig.type_day)) {
                    daysBetList.add(data);
                }
            }
            //设置风格已选择的为true
            for (TagEntity tag : tag_select.values()) {
                for (TagEntity tagentity : daysBetList) {
                    if (tagentity.getContent().equals(tag.getContent()) && tagentity.getType().equals(tag.getType())) {
                        tagentity.setChecked(true);
                    }
                }
            }
        }
        daysAdapter.setDatas(daysBetList);
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
            page=1;
            filter(AppConfig.FIRST,page);
            setSelect(tag_select);

        });


        popupWindow.setOnDismissListener(() -> {
            text_bet_days.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_white_right), null);
            Util.lightOn(this);
        });


        view.findViewById(R.id.btn_submit).setOnClickListener(v -> {
            popupWindow.dismiss();

        });


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_bar);
    }


}
