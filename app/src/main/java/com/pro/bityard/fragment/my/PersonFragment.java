package com.pro.bityard.fragment.my;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import butterknife.BindView;

public class PersonFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.layout_view)
    LinearLayout layout_view;


    @BindView(R.id.text_uid)
    TextView text_uid;

    @BindView(R.id.text_email_tip)
    TextView text_email_tip;


    private int lever = 0;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_person;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginEntity data = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        Log.d("print", "onResume:27:用户:  " + data);
        text_uid.setText(data.getUser().getUserId());


    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.img_back).setOnClickListener(this);


        view.findViewById(R.id.layout_one).setOnClickListener(this);
        view.findViewById(R.id.layout_two).setOnClickListener(this);
        view.findViewById(R.id.layout_three).setOnClickListener(this);
        view.findViewById(R.id.layout_four).setOnClickListener(this);


    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;

            case R.id.layout_two:
                Util.lightOff(getActivity());

                break;


        }
    }


}
