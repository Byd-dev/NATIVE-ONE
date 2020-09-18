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
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.StyleEntity;
import com.pro.bityard.entity.TagEntity;

import java.util.ArrayList;
import java.util.HashMap;
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
    private List<TagEntity> styleList, daysRateList, daysDrawList, daysBetList;
    private List<TagEntity> allList;

    private Map<String, TagEntity> tag_select;


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
                tag_select.put(data.getContent(), data);
            } else {
                tag_select.remove(data.getContent());
            }
            setSelect(tag_select);
        });


        rateTagsAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                tag_select.put(data.getContent(), data);
            } else {
                tag_select.remove(data.getContent());
            }
            setSelect(tag_select);

        });

        drawTagsAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                tag_select.put(data.getContent(), data);
            } else {
                tag_select.remove(data.getContent());
            }
            setSelect(tag_select);

        });
        daysAdapter.setOnItemChaneClick((isChecked, data) -> {
            if (isChecked) {
                tag_select.put(data.getContent(), data);
            } else {
                tag_select.remove(data.getContent());
            }
            setSelect(tag_select);

        });

        selectAdapter.setOnItemDeleteClick((position, data) -> {
            selectList.remove(data);
            selectAdapter.notifyDataSetChanged();
            Log.d("print", "initView:134:  " + data);
            for (TagEntity tagEntity : allList) {
                if (tagEntity.getContent().equals(data.getContent())) {
                    tagEntity.setChecked(true);
                }
            }


        });
    }

    private List<TagEntity> selectList;

    private void setSelect(Map<String, TagEntity> tag_select) {
        selectList = new ArrayList<>();
        for (TagEntity value : tag_select.values()) {
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


    private void getStyleList() {

        allList = new ArrayList<>();
        NetManger.getInstance().styleList("2", (state, response) -> {
            if (state.equals(BUSY)) {
            } else if (state.equals(SUCCESS)) {
                StyleEntity styleEntity = (StyleEntity) response;
                for (StyleEntity.DataBean content : styleEntity.getData()) {
                    //  styleList.add("0," + content.getContent() + type_style);
                    allList.add(new TagEntity(false, content.getContent(), AppConfig.type_style));
                }

                allList.add(new TagEntity(false, getString(R.string.text_unlimited), AppConfig.type_rate));
                allList.add(new TagEntity(false, "0-20%", AppConfig.type_rate));
                allList.add(new TagEntity(false, "20%-60%", AppConfig.type_rate));
                allList.add(new TagEntity(false, "60%-100%", AppConfig.type_rate));

                allList.add(new TagEntity(false, getString(R.string.text_unlimited), AppConfig.type_draw));
                allList.add(new TagEntity(false, "0-10%", AppConfig.type_draw));
                allList.add(new TagEntity(false, "10%-50%", AppConfig.type_draw));
                allList.add(new TagEntity(false, "50%-100%", AppConfig.type_draw));

                allList.add(new TagEntity(false, getString(R.string.text_unlimited), AppConfig.type_day));
                allList.add(new TagEntity(false, "0-30", AppConfig.type_day));
                allList.add(new TagEntity(false, "30-60", AppConfig.type_day));
                allList.add(new TagEntity(false, "60-180", AppConfig.type_day));

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
                rateTagsAdapter.setDatas(daysRateList);


                daysDrawList = new ArrayList<>();
                for (TagEntity data : allList) {
                    if (data.getType().equals(AppConfig.type_draw)) {
                        daysDrawList.add(data);
                    }
                }
                drawTagsAdapter.setDatas(daysDrawList);


                daysBetList = new ArrayList<>();
                for (TagEntity data : allList) {
                    if (data.getType().equals(AppConfig.type_day)) {
                        daysBetList.add(data);
                    }
                }
                daysAdapter.setDatas(daysBetList);

            } else if (state.equals(FAILURE)) {
            }
        });


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
