package com.pro.bityard.fragment.circle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.SelectAdapter;
import com.pro.bityard.adapter.StyleListAdapter;
import com.pro.bityard.adapter.TagsAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.StyleEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class FilterSettingsFragment extends BaseFragment implements View.OnClickListener {
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
    private TagsAdapter rateTagsAdapter, drawTagsAdapter, daysAdapter;

    private SelectAdapter selectAdapter;
    private List<String> styleList, daysRateList, daysDrawList, daysBetList;

    private Map<String, String> tag_select;


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_submit.setVisibility(View.VISIBLE);
        text_title.setText(R.string.text_filter_settings);
        text_submit.setOnClickListener(this);
        view.findViewById(R.id.img_back).setOnClickListener(this);

        recyclerView_style.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView_days_rate.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView_days_draw.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView_bet_days.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView_select.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        styleAdapter = new StyleListAdapter(getActivity());
        recyclerView_style.setAdapter(styleAdapter);

        rateTagsAdapter = new TagsAdapter(getActivity(), 1);
        recyclerView_days_rate.setAdapter(rateTagsAdapter);


        drawTagsAdapter = new TagsAdapter(getActivity(), 2);
        recyclerView_days_draw.setAdapter(drawTagsAdapter);

        daysAdapter = new TagsAdapter(getActivity(), 3);
        recyclerView_bet_days.setAdapter(daysAdapter);


        selectAdapter = new SelectAdapter(getActivity());
        recyclerView_select.setAdapter(selectAdapter);


        tag_select = new HashMap<>();

        styleAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                tag_select.put(data, data);
            } else {
                tag_select.remove(data);
            }
            setSelect(tag_select);
        });


        rateTagsAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                tag_select.put(data, data);
            } else {
                tag_select.remove(data);
            }
            setSelect(tag_select);

        });

        drawTagsAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                tag_select.put(data, data);
            } else {
                tag_select.remove(data);
            }
            setSelect(tag_select);

        });
        daysAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                tag_select.put(data, data);
            } else {
                tag_select.remove(data);
            }
            setSelect(tag_select);

        });

        selectAdapter.setOnItemDeleteClick((position, data) -> {
            selectList.remove(data);
            selectAdapter.notifyDataSetChanged();
            Log.d("print", "initView:134:  "+data);

        });
    }

    private List<String> selectList;

    private void setSelect(Map<String, String> tag_select) {
        selectList = new ArrayList<>();
        for (String value : tag_select.values()) {
            selectList.add(value);
        }
        selectAdapter.setDatas(selectList);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_settings_filter;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        getStyleList();
    }

    private String type_style = ",style";
    private String type_rate = ",rate";
    private String type_draw = ",draw";
    private String type_day = ",day";

    private void getStyleList() {

        styleList = new ArrayList<>();
        NetManger.getInstance().styleList("2", (state, response) -> {
            if (state.equals(BUSY)) {
            } else if (state.equals(SUCCESS)) {
                StyleEntity styleEntity = (StyleEntity) response;
                for (StyleEntity.DataBean content : styleEntity.getData()) {
                    styleList.add("0," + content.getContent() + type_style);
                }
                styleAdapter.setDatas(styleList);

            } else if (state.equals(FAILURE)) {
            }
        });


        daysRateList = new ArrayList<>();
        daysRateList.add("0," + getString(R.string.text_unlimited) + type_rate);
        daysRateList.add("0," + "0-20%" + type_rate);
        daysRateList.add("0," + "20%-60%" + type_rate);
        daysRateList.add("0," +  "60%-100%" + type_rate);
        rateTagsAdapter.setDatas(daysRateList);

        daysDrawList = new ArrayList<>();
        daysDrawList.add("0," + getString(R.string.text_unlimited) + type_draw);
        daysDrawList.add("0," +  "0-10%" + type_draw);
        daysDrawList.add("0," +  "10%-50%" + type_draw);
        daysDrawList.add("0," +  "50%-100%" + type_draw);
        drawTagsAdapter.setDatas(daysDrawList);

        daysBetList = new ArrayList<>();
        daysBetList.add("0," + getString(R.string.text_unlimited) + type_day);
        daysBetList.add("0," + "0-30" + type_day);
        daysBetList.add("0," +  "31-60" + type_day);
        daysBetList.add("0," + "60-180" + type_day);
        daysAdapter.setDatas(daysBetList);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.text_submit:
                Log.d("print", "onClick:已选择:  " + tag_select.toString());
                Toast.makeText(getActivity(), tag_select.toString(), Toast.LENGTH_SHORT).show();
                // UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_CIRCLE_SEARCH_FILTER, value_rate + value_draw + value_days);
                break;
        }
    }
}
