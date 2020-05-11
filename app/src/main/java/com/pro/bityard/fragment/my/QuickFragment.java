package com.pro.bityard.fragment.my;

import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;

import butterknife.BindView;

public class QuickFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_title)
    TextView text_title;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_coin_turn));
        view.findViewById(R.id.img_back).setOnClickListener(this);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_quick;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                getActivity().finish();
                break;
        }
    }
}
