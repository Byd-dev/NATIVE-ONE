package com.pro.bityard.fragment.circle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.IdeaAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.StyleEntity;
import com.pro.bityard.entity.TagEntity;
import com.pro.bityard.utils.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class IdeaSelectFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_save)
    TextView text_save;
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.recyclerView_idea)
    RecyclerView recyclerView_idea;

    private IdeaAdapter ideaAdapter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Activity activity;

    private String valueCode=null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_save.setVisibility(View.VISIBLE);
        text_save.setOnClickListener(this);
        text_title.setText(activity.getResources().getString(R.string.text_follow_think));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        ideaAdapter = new IdeaAdapter(getActivity());
        recyclerView_idea.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_idea.setAdapter(ideaAdapter);
        Util.colorSwipe(getActivity(), swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        ideaAdapter.setDetailClick(data -> {
            valueCode=data.getCode();
            for (TagEntity tag : allList) {
                if (!data.getCode().equals(tag.getCode())) {
                    tag.setChecked(false);
                }else {
                    tag.setChecked(true);
                }
            }
            ideaAdapter.setDatas(allList);
            ideaAdapter.notifyDataSetChanged();
        });

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_idea_select;
    }

    @Override
    protected void intPresenter() {

    }

    private List<TagEntity> allList;

    @Override
    protected void initData() {


        allList = new ArrayList<>();
        styleList = new ArrayList<>();
        NetManger.getInstance().styleList("1", (state, response) -> {
            if (state.equals(SUCCESS)) {
                StyleEntity styleEntity = (StyleEntity) response;
                for (StyleEntity.DataBean content : styleEntity.getData()) {
                    allList.add(new TagEntity(false, content.getContent(), content.getCode(), AppConfig.type_style));
                }
                NetManger.getInstance().myStyleList("1", new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)) {
                            swipeRefreshLayout.setRefreshing(true);

                        } else if (state.equals(SUCCESS)) {
                            swipeRefreshLayout.setRefreshing(false);

                            StyleEntity styleEntity = (StyleEntity) response;
                            for (StyleEntity.DataBean content : styleEntity.getData()) {
                                styleList.add(new TagEntity(true, content.getContent(), content.getCode(), AppConfig.type_style));
                            }
                            setData();

                        } else if (state.equals(FAILURE)) {
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    }
                });


            }
        });

    }

    private List<TagEntity> styleList, daysRateList, daysDrawList, daysBetList;

    private void setData() {

        //遍历是否是选中的
        for (TagEntity tag : allList) {
            for (TagEntity data : styleList) {
                if (data.getCode().equals(tag.getCode())) {
                    tag.setChecked(true);
                }
            }
        }
        ideaAdapter.setDatas(allList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.text_save:
                NetManger.getInstance().addTag("1",valueCode , (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        Toast.makeText(getActivity(), this.getResources().getString(R.string.text_success), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });
                break;
        }
    }
}
