package com.pro.bityard.fragment.circle;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.view.CircleImageView;

import java.io.Serializable;

import butterknife.BindView;

public class FollowSettingsFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.img_head)
    CircleImageView img_head;

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


    public FollowSettingsFragment newInstance(FollowEntity.DataBean value) {
        FollowSettingsFragment fragment = new FollowSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable("DATA_VALUE", (Serializable) value);
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

    @Override
    protected void initView(View view) {
        text_title.setText(R.string.text_copy_trade_settings);
        view.findViewById(R.id.img_back).setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(this);
        radio_fixed_margin.setBackground(getResources().getDrawable(R.mipmap.bg_blue_left));
        radio_proportional_margin.setBackground(getResources().getDrawable(R.mipmap.bg_normal_right));

        String strMsg = getString(R.string.text_copy_trade_amount);
        text_copy_trade_amount.setText(Html.fromHtml(strMsg));

    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        FollowEntity.DataBean dataBean = (FollowEntity.DataBean) getArguments().getSerializable("DATA_VALUE");
        Log.d("print", "initData:60: " + dataBean);

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

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_fixed_margin:
                radio_fixed_margin.setBackground(getResources().getDrawable(R.mipmap.bg_blue_left));
                radio_proportional_margin.setBackground(getResources().getDrawable(R.mipmap.bg_normal_right));

                break;
            case R.id.radio_proportional_margin:

                radio_proportional_margin.setBackground(getResources().getDrawable(R.mipmap.bg_red_right));
                radio_fixed_margin.setBackground(getResources().getDrawable(R.mipmap.bg_normal_left));

                break;
        }
    }
}
