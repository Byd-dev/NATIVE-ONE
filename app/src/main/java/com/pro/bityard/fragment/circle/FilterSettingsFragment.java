package com.pro.bityard.fragment.circle;

import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.adapter.StyleAdapter;
import com.pro.bityard.adapter.TagsAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.StyleEntity;

import java.util.ArrayList;
import java.util.List;

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

    @BindView(R.id.recyclerView_style)
    RecyclerView recyclerView_style;
    @BindView(R.id.recyclerView_days_rate)
    RecyclerView recyclerView_days_rate;
    @BindView(R.id.recyclerView_days_draw)
    RecyclerView recyclerView_days_draw;
    @BindView(R.id.recyclerView_bet_days)
    RecyclerView recyclerView_bet_days;
    private StyleAdapter styleAdapter;
    private TagsAdapter rateTagsAdapter, drawTagsAdapter, daysAdapter;
    private String value_rate, value_draw, value_days;

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

        styleAdapter = new StyleAdapter(getActivity());
        recyclerView_style.setAdapter(styleAdapter);

        rateTagsAdapter = new TagsAdapter(getActivity());
        recyclerView_days_rate.setAdapter(rateTagsAdapter);


        drawTagsAdapter = new TagsAdapter(getActivity());
        recyclerView_days_draw.setAdapter(drawTagsAdapter);

        daysAdapter = new TagsAdapter(getActivity());
        recyclerView_bet_days.setAdapter(daysAdapter);

        rateTagsAdapter.setOnItemClick((position, data) -> {
            value_rate = data;
            rateTagsAdapter.select(1, data);
            recyclerView_days_rate.setAdapter(rateTagsAdapter);
        });

        drawTagsAdapter.setOnItemClick((position, data) -> {
            value_draw = data;
            drawTagsAdapter.select(2, data);
            recyclerView_days_draw.setAdapter(drawTagsAdapter);
        });


        daysAdapter.setOnItemClick((position, data) -> {
            value_days = data;
            daysAdapter.select(3, data);
            recyclerView_bet_days.setAdapter(daysAdapter);
        });
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

    private List<String> daysRateList, daysDrawList, daysBetList;

    private void getStyleList() {
        NetManger.getInstance().styleList("2", (state, response) -> {
            if (state.equals(BUSY)) {
            } else if (state.equals(SUCCESS)) {
                StyleEntity styleEntity = (StyleEntity) response;
                styleAdapter.setDatas(styleEntity.getData());

            } else if (state.equals(FAILURE)) {
            }
        });


        daysRateList = new ArrayList<>();
        daysRateList.add(getString(R.string.text_unlimited));
        daysRateList.add("0-20%");
        daysRateList.add("20%-60%");
        daysRateList.add("60%-100%");
        rateTagsAdapter.setDatas(daysRateList);

        daysDrawList = new ArrayList<>();
        daysDrawList.add(getString(R.string.text_unlimited));
        daysDrawList.add("0-10%");
        daysDrawList.add("10%-50%");
        daysDrawList.add("50%-100%");
        drawTagsAdapter.setDatas(daysDrawList);

        daysBetList = new ArrayList<>();
        daysBetList.add(getString(R.string.text_unlimited));
        daysBetList.add("0-30");
        daysBetList.add("31-60");
        daysBetList.add("60-180");
        daysAdapter.setDatas(daysBetList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.text_submit:
                UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_CIRCLE_SEARCH_FILTER, value_rate + value_draw + value_days);
                break;
        }
    }
}
