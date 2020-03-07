package com.pro.bityard.fragment.user;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.CountryCodeAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class MobileLoginFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.text_CountryName)
    TextView text_countryName;

    @BindView(R.id.text_countryCode)
    TextView text_countryCode;
    private ViewPager viewPager;
    private CountryCodeEntity data;

    //地区的适配器
    private CountryCodeAdapter countryCodeAdapter;

    private List<CountryCodeEntity.DataBean> searchData;


    public MobileLoginFragment() {

    }


    public MobileLoginFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    @Override
    protected void onLazyLoad() {

    }


    @Override
    protected void initView(View view) {

        view.findViewById(R.id.text_email_login).setOnClickListener(this);
        view.findViewById(R.id.layout_country).setOnClickListener(this);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_mobile_login;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        data = SPUtils.getData(AppConfig.COUNTRY_CODE, CountryCodeEntity.class);
        if (data.equals("")) {

        } else {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_email_login:
                viewPager.setCurrentItem(0);
                break;

            case R.id.layout_country:
                showEditPopWindow(data);
                break;
        }
    }


    private void showEditPopWindow(CountryCodeEntity data) {


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_country_code_layout, null);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        countryCodeAdapter = new CountryCodeAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(countryCodeAdapter);
        final List<CountryCodeEntity.DataBean> dataData = data.getData();
        countryCodeAdapter.setDatas(dataData);


        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        EditText edit_search = view.findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    searchData = new ArrayList<>();
                    for (int i = 0; i < dataData.size(); i++) {
                        if (dataData.get(i).getNameCn().contains(s)
                                || dataData.get(i).getCountryCode().contains(s)
                                || dataData.get(i).getNameEn().contains(s)
                        ) {
                            searchData.add(dataData.get(i));
                        }
                    }
                    countryCodeAdapter.setDatas(searchData);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        view.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        countryCodeAdapter.setOnItemClick(new CountryCodeAdapter.OnItemClick() {
            @Override
            public void onSuccessListener(CountryCodeEntity.DataBean dataBean) {
                // TODO: 2020/3/7   中英文切换
                text_countryName.setText(dataBean.getNameCn());
                text_countryCode.setText("+" + dataBean.getCountryCode());
                popupWindow.dismiss();

            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.pop_anim);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

}
