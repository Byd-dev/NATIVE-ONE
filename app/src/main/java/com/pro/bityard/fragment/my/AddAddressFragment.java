package com.pro.bityard.fragment.my;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.activity.WebActivity;
import com.pro.bityard.adapter.ChainListAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.AddAddressItemEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class AddAddressFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.img_service)
    ImageView img_service;

    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.edit_address)
    EditText edit_address;

    @BindView(R.id.edit_remark)
    EditText edit_remark;

    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.text_err)
    TextView text_err;

    private ChainListAdapter chainListAdapter;//杠杆适配器
    private List<String> dataList;

    private String chain = "OMNI";
    private LoginEntity loginEntity;

    @Override
    public void onResume() {
        super.onResume();
        loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        img_service.setVisibility(View.VISIBLE);
        img_service.setOnClickListener(this);
        text_title.setText(getResources().getString(R.string.text_add_withdrawal_address));

        view.findViewById(R.id.img_back).setOnClickListener(this);
        chainListAdapter = new ChainListAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(chainListAdapter);

        dataList = new ArrayList<>();
        dataList.add("OMNI");
        dataList.add("ERC20");
        dataList.add("TRC20");
        chainListAdapter.setDatas(dataList);
        chainListAdapter.select("OMNI");
        edit_address.setHint(getString(R.string.text_enter) + " OMNI " + getString(R.string.text_add_address));
        chainListAdapter.setOnItemClick((position, data) -> {
            String value_address = edit_address.getText().toString();
            chainListAdapter.select(data);
            recyclerView.setAdapter(chainListAdapter);
            edit_address.setHint(getString(R.string.text_enter) + " " + data + " " + getString(R.string.text_add_address));
            chain = data;
            if (value_address.equals("")){
                text_err.setVisibility(View.GONE);
            }else {
                switch (chain) {
                    case "OMNI":
                        if (value_address.startsWith("1")) {
                            text_err.setVisibility(View.GONE);
                        } else if (value_address.startsWith("3")) {
                            text_err.setVisibility(View.GONE);
                        } else {
                            text_err.setVisibility(View.VISIBLE);
                            text_err.setText(getResources().getText(R.string.text_omni_err));
                        }
                        break;
                    case "ERC20":
                        if (value_address.startsWith("0")) {
                            text_err.setVisibility(View.GONE);
                        } else if (value_address.startsWith("x")) {
                            text_err.setVisibility(View.GONE);
                        } else {
                            text_err.setVisibility(View.VISIBLE);
                            text_err.setText(getResources().getText(R.string.text_erc_err));
                        }
                        break;
                    case "TRC20":
                        if (value_address.startsWith("T")) {
                            text_err.setVisibility(View.GONE);
                        } else {
                            text_err.setVisibility(View.VISIBLE);
                            text_err.setText(getResources().getText(R.string.text_trc_err));
                        }
                        break;
                }
            }



        });

        btn_submit.setOnClickListener(this);
        Util.setTwoUnClick(edit_address, edit_remark, btn_submit);
        Util.setTwoUnClick(edit_remark, edit_address, btn_submit);

        edit_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    switch (chain) {
                        case "OMNI":
                            if (s.toString().startsWith("1")) {
                                text_err.setVisibility(View.GONE);
                            } else if (s.toString().startsWith("3")) {
                                text_err.setVisibility(View.GONE);
                            } else {
                                text_err.setVisibility(View.VISIBLE);
                                text_err.setText(getResources().getText(R.string.text_omni_err));
                            }
                            break;
                        case "ERC20":
                            if (s.toString().startsWith("0")) {
                                text_err.setVisibility(View.GONE);
                            } else if (s.toString().startsWith("x")) {
                                text_err.setVisibility(View.GONE);
                            } else {
                                text_err.setVisibility(View.VISIBLE);
                                text_err.setText(getResources().getText(R.string.text_erc_err));
                            }
                            break;
                        case "TRC20":
                            if (s.toString().startsWith("T")) {
                                text_err.setVisibility(View.GONE);
                            } else {
                                text_err.setVisibility(View.VISIBLE);
                                text_err.setText(getResources().getText(R.string.text_trc_err));
                            }
                            break;
                    }
                } else {
                    text_err.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_add_address;
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

            case R.id.btn_submit:

                NetManger.getInstance().addAddress("USDT", chain, edit_address.getText().toString(), edit_remark.getText().toString(), (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        AddAddressItemEntity addAddressItemEntity = (AddAddressItemEntity) response;
                        if (addAddressItemEntity.getCode() == 200) {
                            Toast.makeText(getActivity(), R.string.text_add_success, Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    } else if (state.equals(FAILURE)) {
                        if (response != null) {
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                        dismissProgressDialog();
                    }
                });
                break;

            case R.id.img_service:
                String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, Util.ZH_SIMPLE);
                String url;
                if (isLogin()) {
                    url = String.format(NetManger.SERVICE_URL, language, loginEntity.getUser().getUserId(), loginEntity.getUser().getAccount());
                } else {
                    url = String.format(NetManger.SERVICE_URL, language, "", "游客");
                }
                WebActivity.getInstance().openUrl(getActivity(), url, getResources().getString(R.string.text_my_service));
                break;

        }
    }
}
