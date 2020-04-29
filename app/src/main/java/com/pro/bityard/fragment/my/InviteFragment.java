package com.pro.bityard.fragment.my;

import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;

import butterknife.BindView;

public class InviteFragment extends BaseFragment {
    @BindView(R.id.text_title)
    TextView text_title;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        text_title.setText(R.string.text_affiliate_stats);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_invite;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

    }
}
