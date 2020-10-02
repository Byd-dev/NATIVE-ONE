package com.pro.bityard.fragment.circle;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.adapter.AmountListAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.view.CircleImageView;
import com.pro.bityard.view.DecimalEditText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

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
    @BindView(R.id.edit_copy_trade_position)
    DecimalEditText edit_copy_trade_position;
    @BindView(R.id.edit_max_trade_position)
    DecimalEditText edit_max_trade_position;
    @BindView(R.id.text_add_trade_position)
    TextView text_add_trade_position;
    @BindView(R.id.text_sub_trade_position)
    TextView text_sub_trade_position;
    @BindView(R.id.text_add_max_position)
    TextView text_add_max_position;
    @BindView(R.id.text_sub_max_position)
    TextView text_sub_max_position;
    @BindView(R.id.bar_amount)
    SeekBar bar_amount;
    @BindView(R.id.edit_amount_bar)
    DecimalEditText edit_amount_bar;


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
    @BindView(R.id.bar_proportion_rate)
    SeekBar bar_proportion_rate;
    @BindView(R.id.edit_copy_rate_proportion)
    DecimalEditText edit_copy_rate_proportion;
    @BindView(R.id.edit_warning_proportion)
    DecimalEditText edit_warning_proportion;

    @BindView(R.id.text_add_warning)
    TextView text_add_warning;
    @BindView(R.id.text_sub_warning)
    TextView text_sub_warning;
    @BindView(R.id.edit_day_amount_proportion)
    DecimalEditText edit_day_amount_proportion;

    @BindView(R.id.text_add_day_amount_proportion)
    TextView text_add_day_amount_proportion;
    @BindView(R.id.text_sub_day_amount_proportion)
    TextView text_sub_day_amount_proportion;

    @BindView(R.id.edit_max_amount_proportion)
    DecimalEditText edit_max_amount_proportion;
    @BindView(R.id.text_sub_max_proportion)
    TextView text_sub_max_proportion;
    @BindView(R.id.text_add_max_proportion)
    TextView text_add_max_proportion;

    @BindView(R.id.bar_stop_loss_proportion)
    SeekBar bar_stop_loss_proportion;
    @BindView(R.id.edit_stop_loss_rate)
    DecimalEditText edit_stop_loss_rate;



    private FollowEntity.DataBean followerUser;
    private String traderId;
    private String followVal;
    private String maxDay;
    private String maxHold;
    private String slRatio;


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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        amountListAdapter.setOnItemClick((position, data) -> {
            amountListAdapter.select(data);
            amountListAdapter.notifyDataSetChanged();
            edit_amount.setText(data);

        });


        String value_amount = edit_amount.getText().toString();
        setEdit("1", edit_amount, null,500, 1);
        setAdd(text_amount_add, edit_amount, value_amount, 5);
        setSub(text_amount_sub, edit_amount, value_amount, 5);

        String value_position = edit_copy_trade_position.getText().toString();
        setEdit("2", edit_copy_trade_position, null,10000, 1);
        setAdd(text_add_trade_position, edit_copy_trade_position, value_position, 5);
        setSub(text_sub_trade_position, edit_copy_trade_position, value_position, 5);

        String value_max = edit_max_trade_position.getText().toString();
        setEdit("2", edit_max_trade_position, null,10000, 1);
        setAdd(text_add_max_position, edit_max_trade_position, value_max, 5);
        setSub(text_sub_max_position, edit_max_trade_position, value_max, 5);


        setSeekBar(bar_amount,edit_amount_bar,90,0);
        setEdit("3", edit_amount_bar, bar_amount,90, 0);

        //比例跟单
        layout_unfold_settings_proportion.setVisibility(View.GONE);
        String strMsg5 = getString(R.string.text_copy_proportion);
        text_copy_proportion.setText(Html.fromHtml(strMsg5));

        setSeekBar(bar_proportion_rate,edit_copy_rate_proportion,1000,10);
        setEdit("3", edit_copy_rate_proportion, bar_proportion_rate,1000, 10);

        String value_warning = edit_warning_proportion.getText().toString();
        setEdit("2", edit_warning_proportion, null,10000, 5);
        setAdd(text_add_warning, edit_warning_proportion, value_warning, 5);
        setSub(text_sub_warning, edit_warning_proportion, value_warning, 5);

        String value_day_proportion = edit_day_amount_proportion.getText().toString();
        setEdit("2", edit_day_amount_proportion, null,10000, 1);
        setAdd(text_add_day_amount_proportion, edit_day_amount_proportion, value_day_proportion, 5);
        setSub(text_sub_day_amount_proportion, edit_day_amount_proportion, value_day_proportion, 5);

        String value_max_proportion = edit_max_amount_proportion.getText().toString();
        setEdit("2", edit_max_amount_proportion, null,10000, 1);
        setAdd(text_add_max_proportion, edit_max_amount_proportion, value_max_proportion, 5);
        setSub(text_sub_max_proportion, edit_max_amount_proportion, value_max_proportion, 5);

        setSeekBar(bar_stop_loss_proportion,edit_stop_loss_rate,90,0);
        setEdit("3", edit_stop_loss_rate, bar_stop_loss_proportion,90, 0);

        view.findViewById(R.id.btn_submit).setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setSeekBar(SeekBar seekBar, DecimalEditText edit_bar,int max,int min){
        seekBar.setMax(max);
        seekBar.setMin(min);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    edit_bar.setText(progress + "");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void setEdit(String isAmount, EditText edit,SeekBar seekBar, int max, int min) {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (Integer.parseInt(s.toString()) < min) {
                        edit.setText(String.valueOf(min));
                    } else if (Integer.parseInt(s.toString()) > max) {
                        edit.setText(String.valueOf(max));
                    }
                    if (isAmount.equals("1")) {
                        amountListAdapter.select(s.toString());
                        amountListAdapter.notifyDataSetChanged();
                    } else if (isAmount.equals("3")) {
                        seekBar.post(() -> {
                            seekBar.setProgress(Integer.parseInt(s.toString()));
                        });
                    }

                } else {
                    if (isAmount.equals("3")) {
                        seekBar.post(() -> {
                            seekBar.setProgress(0);
                        });
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setAdd(TextView add, EditText edit, String value, int count) {
        add.setOnClickListener(v -> {
            int a;
            if (value.equals("")) {
                a = count;
            } else {
                a = Integer.parseInt(edit.getText().toString()) + count;
            }

            edit.setText(String.valueOf(a));
        });
    }

    private void setSub(TextView sub, EditText edit, String value, int count) {
        sub.setOnClickListener(v -> {
            int a;
            if (value.equals("")) {
                a = count;
            } else {
                a = Integer.parseInt(edit.getText().toString()) - count;
            }

            edit.setText(String.valueOf(a));
        });
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        followerUser = (FollowEntity.DataBean) getArguments().getSerializable("DATA_VALUE");

        Glide.with(getActivity()).load(followerUser.getAvatar()).error(R.mipmap.icon_my_bityard).into(img_head);
        text_userName.setText(followerUser.getUsername());

        String value_type = null;
        int type = followerUser.getType();
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


    private String followWay = "1";

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
            case R.id.btn_submit:
                traderId = followerUser.getUserId();
                if (followWay.equals("1")){
                    followVal = edit_amount.getText().toString();
                    maxDay = edit_copy_trade_position.getText().toString();
                    maxHold = edit_max_trade_position.getText().toString();
                    slRatio = edit_amount_bar.getText().toString();


                }else {
                    followVal = edit_copy_rate_proportion.getText().toString();
                    maxDay = edit_day_amount_proportion.getText().toString();
                    maxHold = edit_max_amount_proportion.getText().toString();
                    slRatio = edit_stop_loss_rate.getText().toString();
                }
                if (slRatio.equals("")){
                    slRatio="-1";
                }
                NetManger.getInstance().follow(traderId, "USDT", followWay, followVal, maxDay, maxHold, slRatio, "true", (state, response) -> {
                            if (state.equals(BUSY)) {
                                showProgressDialog();
                            } else if (state.equals(SUCCESS)) {
                                dismissProgressDialog();
                                TipEntity tipEntity= (TipEntity) response;
                                if (tipEntity.getMessage().equals("")){
                                    Toast.makeText(getActivity(),getResources().getString(R.string.text_tip_success),Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getActivity(),tipEntity.getMessage(),Toast.LENGTH_SHORT);
                                }
                            } else if (state.equals(FAILURE)) {
                                dismissProgressDialog();
                            }
                        }


                );
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
                followWay = "1";
                break;
            case R.id.radio_proportional_margin:

                radio_proportional_margin.setBackground(getResources().getDrawable(R.mipmap.bg_red_right));
                radio_fixed_margin.setBackground(getResources().getDrawable(R.mipmap.bg_normal_left));

                layout_copy_amount.setVisibility(View.GONE);
                layout_copy_proportion.setVisibility(View.VISIBLE);
                followWay = "2";

                break;
        }
    }
}
