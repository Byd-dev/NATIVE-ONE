package com.pro.bityard.fragment.circle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.view.CircleImageView;

import java.io.Serializable;

import butterknife.BindView;

public class FollowSettingsFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.img_head)
    CircleImageView img_head;

    @BindView(R.id.text_userName)
    TextView text_userName;
    @BindView(R.id.text_type)
    TextView text_type;


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
}
