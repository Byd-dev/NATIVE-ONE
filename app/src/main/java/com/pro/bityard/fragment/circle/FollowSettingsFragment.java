package com.pro.bityard.fragment.circle;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.adapter.AmountListAdapter;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.view.CircleImageView;
import com.pro.bityard.view.DecimalEditText;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class FollowSettingsFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.img_head)
    CircleImageView img_head;

    @BindView(R.id.layout_copy_amount)
    LinearLayout layout_copy_amount;
    @BindView(R.id.layout_copy_proportion)
    LinearLayout layout_copy_proportion;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.radio_fixed_margin)
    RadioButton radio_fixed_margin;

    @BindView(R.id.radio_proportional_margin)
    RadioButton radio_proportional_margin;

    @BindView(R.id.text_userName)
    TextView text_userName;
    @BindView(R.id.text_type)
    TextView text_type;
    @BindView(R.id.text_copy_trade_amount)
    TextView text_copy_trade_amount;


    //金额跟单
    @BindView(R.id.text_trade_position)
    TextView text_trade_position;
    @BindView(R.id.text_maximum_position_amount)
    TextView text_maximum_position_amount;
    @BindView(R.id.text_stop_loss_ratio)
    TextView text_stop_loss_ratio;
    @BindView(R.id.layout_unfold_settings)
    LinearLayout layout_unfold_settings;
    private boolean isShow_amount = false;
    @BindView(R.id.recyclerView_amount)
    RecyclerView recyclerView_amount;
    @BindView(R.id.text_advanced_settings)
    TextView text_advanced_settings;
    @BindView(R.id.edit_amount)
    DecimalEditText edit_amount;
    @BindView(R.id.text_amount_add)
    TextView text_amount_add;
    @BindView(R.id.text_amount_sub)
    TextView text_amount_sub;

    private AmountListAdapter amountListAdapter;
    private List<String> dataList;


    //比例跟单
    @BindView(R.id.text_copy_proportion)
    TextView text_copy_proportion;
    @BindView(R.id.text_trade_position_proportion)
    TextView text_trade_position_proportion;
    @BindView(R.id.layout_unfold_settings_proportion)
    LinearLayout layout_unfold_settings_proportion;
    private boolean isShow_proportion = false;
    @BindView(R.id.text_advanced_settings_proportion)
    TextView text_advanced_settings_proportion;

    public FollowSettingsFragment newInstance(FollowEntity.DataBean value) {
        FollowSettingsFragment fragment = new FollowSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable("DATA_VALUE", value);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_settings_follow;
    }

    @Override
    protected void onLazyLoad() {

    }

    private String amount;

    @Override
    protected void initView(View view) {
        text_title.setText(R.string.text_copy_trade_settings);

        layout_copy_proportion.setVisibility(View.GONE);
        view.findViewById(R.id.img_back).setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(this);
        radio_fixed_margin.setBackground(getResources().getDrawable(R.mipmap.bg_blue_left));
        radio_proportional_margin.setBackground(getResources().getDrawable(R.mipmap.bg_normal_right));
        //金额跟单

        layout_unfold_settings.setVisibility(View.GONE);

        String strMsg = getString(R.string.text_copy_trade_amount);
        text_copy_trade_amount.setText(Html.fromHtml(strMsg));

        String strMsg2 = getString(R.string.text_copy_trade_position);
        text_trade_position.setText(Html.fromHtml(strMsg2));
        text_trade_position_proportion.setText(Html.fromHtml(strMsg2));

        String strMsg3 = getString(R.string.text_maximum_position_amount);
        text_maximum_position_amount.setText(Html.fromHtml(strMsg3));
        String strMsg4 = getString(R.string.text_stop_loss_ratio);
        text_stop_loss_ratio.setText(Html.fromHtml(strMsg4));

        view.findViewById(R.id.layout_advanced_settings).setOnClickListener(this);
        view.findViewById(R.id.layout_advanced_settings_proportion).setOnClickListener(this);

        amountListAdapter = new AmountListAdapter(getActivity());
        recyclerView_amount.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView_amount.setAdapter(amountListAdapter);
        dataList = new ArrayList<>();
        dataList.add("20");
        dataList.add("50");
        dataList.add("100");
        dataList.add("500");
        amountListAdapter.setDatas(dataList);

        amountListAdapter.setOnItemClick(data -> {
            this.amount = data;
            edit_amount.setText(data);
        });

        String value_amount = edit_amount.getText().toString();
        edit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    if (Integer.parseInt(s.toString())<0){
                        edit_amount.setText(String.valueOf(0));
                    }else if (Integer.parseInt(s.toString())>500){
                        edit_amount.setText(String.valueOf(500));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        text_amount_add.setOnClickListener(v -> {
            int a;
            if (value_amount.equals("")) {
                a = 5;
            } else {
                a = Integer.parseInt(edit_amount.getText().toString()) + 5;
            }

            edit_amount.setText(String.valueOf(a));
        });
        text_amount_sub.setOnClickListener(v -> {
            int a;
            if (value_amount.equals("")) {
                a = 5;
            } else {
                a = Integer.parseInt(edit_amount.getText().toString()) - 5;
            }
            edit_amount.setText(String.valueOf(a));
        });
        //比例跟单
        layout_unfold_settings_proportion.setVisibility(View.GONE);
        String strMsg5 = getString(R.string.text_copy_proportion);
        text_copy_proportion.setText(Html.fromHtml(strMsg5));


    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        FollowEntity.DataBean dataBean = (FollowEntity.DataBean) getArguments().getSerializable("DATA_VALUE");

        Glide.with(getActivity()).load(dataBean.getAvatar()).error(R.mipmap.icon_my_bityard).into(img_head);
        text_userName.setText(dataBean.getUsername());

        String value_type = null;
        int type = dataBean.getType();
        switch (type) {
            case 1:
                value_type = getString(R.string.text_normal_user);
                text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_normal_user), null, null, null);

                break;
            case 2:
                value_type = getString(R.string.text_normal_trader);
                text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_normal_trader), null, null, null);
                break;
            case 3:
                value_type = getString(R.string.text_pro_trader);
                text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_pro_trader), null, null, null);

                break;
        }

        text_type.setText(value_type);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.layout_advanced_settings:
                if (isShow_amount) {
                    layout_unfold_settings.setVisibility(View.GONE);
                    isShow_amount = false;
                    text_advanced_settings.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(R.mipmap.icon_triangle_right_big), null);

                } else {
                    layout_unfold_settings.setVisibility(View.VISIBLE);
                    isShow_amount = true;

                    text_advanced_settings.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(R.mipmap.icon_triangle_down_big), null);
                }
                break;

            case R.id.layout_advanced_settings_proportion:
                if (isShow_proportion) {
                    layout_unfold_settings_proportion.setVisibility(View.GONE);
                    isShow_proportion = false;
                    text_advanced_settings_proportion.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(R.mipmap.icon_triangle_right_big), null);
                } else {
                    layout_unfold_settings_proportion.setVisibility(View.VISIBLE);
                    isShow_proportion = true;

                    text_advanced_settings_proportion.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(R.mipmap.icon_triangle_down_big), null);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_fixed_margin:
                radio_fixed_margin.setBackground(getResources().getDrawable(R.mipmap.bg_blue_left));
                radio_proportional_margin.setBackground(getResources().getDrawable(R.mipmap.bg_normal_right));

                layout_copy_amount.setVisibility(View.VISIBLE);
                layout_copy_proportion.setVisibility(View.GONE);

                break;
            case R.id.radio_proportional_margin:

                radio_proportional_margin.setBackground(getResources().getDrawable(R.mipmap.bg_red_right));
                radio_fixed_margin.setBackground(getResources().getDrawable(R.mipmap.bg_normal_left));

                layout_copy_amount.setVisibility(View.GONE);
                layout_copy_proportion.setVisibility(View.VISIBLE);

                break;
        }
    }
}
