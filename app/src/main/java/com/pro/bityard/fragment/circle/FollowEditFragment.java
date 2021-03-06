package com.pro.bityard.fragment.circle;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.adapter.AmountListAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.FollowerDetailEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.manger.NoticeManger;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
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

public class FollowEditFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.text_cancel_follow)
    TextView text_cancel_follow;
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


    //????????????

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


    //????????????
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

    @BindView(R.id.btn_switch)
    Switch btn_switch;
    @BindView(R.id.text_copy_switch_tip)
    TextView text_copy_switch_tip;


    @BindView(R.id.checkbox_amount)
    CheckBox checkBox_amount;
    @BindView(R.id.checkbox_proportion)
    CheckBox checkbox_proportion;
    @BindView(R.id.layout_stop_loss_amount)
    LinearLayout layout_stop_loss_amount;
    @BindView(R.id.layout_stop_loss_proportion)
    LinearLayout layout_stop_loss_proportion;
    private FollowerDetailEntity.DataBean followerUser;
    private String traderId;
    private String followVal;
    private String maxDay;
    private String maxHold;
    private String slRatio;
    private String followMax = null;


    public FollowEditFragment newInstance(FollowerDetailEntity.DataBean value) {
        FollowEditFragment fragment = new FollowEditFragment();
        Bundle args = new Bundle();
        args.putSerializable("DATA_VALUE", value);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_edit_follow;
    }

    @Override
    protected void onLazyLoad() {

    }

    private String amount;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initView(View view) {
        text_title.setText(R.string.text_edit_follow);
        text_cancel_follow.setVisibility(View.VISIBLE);

        layout_copy_proportion.setVisibility(View.GONE);
        view.findViewById(R.id.img_back).setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(this);
        radio_fixed_margin.setBackground(getResources().getDrawable(R.mipmap.bg_blue_left));
        radio_proportional_margin.setBackground(getResources().getDrawable(R.mipmap.bg_normal_right));
        //????????????

        btn_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!btn_switch.isPressed()) {
                return;
            }
            String active;
            if (isChecked) {
                active = "true";
                text_copy_switch_tip.setText(getResources().getString(R.string.text_copy_switch_tip));
            } else {
                active = "false";
                text_copy_switch_tip.setText(getString(R.string.text_last_time) + ":" + Util.stampToDate(followerUser.getLastTime()) + "\n" + getString(R.string.text_close_reason) + ":");

            }
            NetManger.getInstance().followSwitch(active, followerUser.getId(), (state, response) -> {
                if (state.equals(SUCCESS)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.text_tip_success), Toast.LENGTH_SHORT).show();
                }
            });
        });

        text_cancel_follow.setOnClickListener(this);

        layout_unfold_settings.setVisibility(View.GONE);

        String strMsg = getString(R.string.text_copy_trade_amount);
        text_copy_trade_amount.setText(Html.fromHtml(strMsg));


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
        dataList.add("200");
        amountListAdapter.setDatas(dataList);

        amountListAdapter.setOnItemClick((position, data) -> {
            amountListAdapter.select(data);
            amountListAdapter.notifyDataSetChanged();
            edit_amount.setText(data);

        });


        String value_amount = edit_amount.getText().toString();
        setEdit("1", edit_amount, null, 200, 1);
        setAdd(text_amount_add, edit_amount, value_amount, 5);
        setSub(text_amount_sub, edit_amount, value_amount, 5);

        String value_position = edit_copy_trade_position.getText().toString();
        setEdit("2", edit_copy_trade_position, null, 4000, 1);
        setAdd(text_add_trade_position, edit_copy_trade_position, value_position, 5);
        setSub(text_sub_trade_position, edit_copy_trade_position, value_position, 5);

        String value_max = edit_max_trade_position.getText().toString();
        setEdit("2", edit_max_trade_position, null, 4000, 1);
        setAdd(text_add_max_position, edit_max_trade_position, value_max, 5);
        setSub(text_sub_max_position, edit_max_trade_position, value_max, 5);


        setSeekBar(bar_amount, edit_amount_bar, 90, 0);
        setEdit("3", edit_amount_bar, bar_amount, 90, 0);

        //????????????
        layout_unfold_settings_proportion.setVisibility(View.GONE);
        String strMsg5 = getString(R.string.text_copy_proportion);
        text_copy_proportion.setText(Html.fromHtml(strMsg5));

        setSeekBar(bar_proportion_rate, edit_copy_rate_proportion, 500, 0);
        setEdit("3", edit_copy_rate_proportion, bar_proportion_rate, 500, 0);

        String value_warning = edit_warning_proportion.getText().toString();
        setEdit("2", edit_warning_proportion, null, 4000, 5);
        setAdd(text_add_warning, edit_warning_proportion, value_warning, 5);
        setSub(text_sub_warning, edit_warning_proportion, value_warning, 5);

        String value_day_proportion = edit_day_amount_proportion.getText().toString();
        setEdit("2", edit_day_amount_proportion, null, 4000, 1);
        setAdd(text_add_day_amount_proportion, edit_day_amount_proportion, value_day_proportion, 5);
        setSub(text_sub_day_amount_proportion, edit_day_amount_proportion, value_day_proportion, 5);

        String value_max_proportion = edit_max_amount_proportion.getText().toString();
        setEdit("2", edit_max_amount_proportion, null, 4000, 1);
        setAdd(text_add_max_proportion, edit_max_amount_proportion, value_max_proportion, 5);
        setSub(text_sub_max_proportion, edit_max_amount_proportion, value_max_proportion, 5);

        setSeekBar(bar_stop_loss_proportion, edit_stop_loss_rate, 90, 0);
        setEdit("3", edit_stop_loss_rate, bar_stop_loss_proportion, 90, 0);

        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        layout_stop_loss_amount.setVisibility(View.GONE);
        layout_stop_loss_proportion.setVisibility(View.GONE);

        /*checkBox_amount.setChecked(true);
        checkbox_proportion.setChecked(true);*/

        checkBox_amount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_stop_loss_amount.setVisibility(View.GONE);
                    slRatio = "-1";
                } else {
                    layout_stop_loss_amount.setVisibility(View.VISIBLE);
                    Log.d("print", "onCheckedChanged:331:  "+slRatio);
                    if (slRatio .equals("-1")) {
                        bar_amount.post(() -> {
                            bar_amount.setProgress(Integer.parseInt(TradeUtil.getNumberFormat(90, 0)));
                        });
                        edit_amount_bar.setText(90 + "");
                    } else {
                        bar_amount.post(() -> {
                            bar_amount.setProgress(Integer.parseInt(TradeUtil.getNumberFormat(Double.parseDouble(slRatio), 0)));
                        });
                        edit_amount_bar.setText(slRatio);
                    }


                }
            }
        });

        checkbox_proportion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_stop_loss_proportion.setVisibility(View.GONE);
                    slRatio = "-1";
                } else {
                    layout_stop_loss_proportion.setVisibility(View.VISIBLE);
                    Log.d("print", "onCheckedChanged:360: " + slRatio);
                    if (slRatio.equals("-1")) {
                        bar_stop_loss_proportion.post(() -> {
                            bar_stop_loss_proportion.setProgress(Integer.parseInt(TradeUtil.getNumberFormat(90, 0)));
                        });
                        edit_stop_loss_rate.setText(90 + "");
                    } else {
                        bar_stop_loss_proportion.post(() -> {
                            bar_stop_loss_proportion.setProgress(Integer.parseInt(TradeUtil.getNumberFormat(Double.parseDouble(slRatio), 0)));
                        });
                        edit_stop_loss_rate.setText(slRatio);
                    }


                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setSeekBar(SeekBar seekBar, DecimalEditText edit_bar, int max, int min) {
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


    private void setEdit(String isAmount, DecimalEditText edit, SeekBar seekBar, int max, int min) {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (Double.parseDouble(s.toString()) < min) {
                        edit.setText(String.valueOf(min));
                    } else if (Double.parseDouble(s.toString()) > max) {
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

    private void setAdd(TextView add, DecimalEditText edit, String value, int count) {
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

    private void setSub(TextView sub, DecimalEditText edit, String value, int count) {
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        followerUser = (FollowerDetailEntity.DataBean) getArguments().getSerializable("DATA_VALUE");
        Log.d("print", "initData:403:  " + followerUser);
        if (followerUser.isActive()) {
            btn_switch.setChecked(true);
            text_copy_switch_tip.setText(getResources().getString(R.string.text_copy_switch_tip));
        } else {
            btn_switch.setChecked(false);
            text_copy_switch_tip.setText(getString(R.string.text_last_time) + ":" + Util.stampToDate(followerUser.getLastTime()) + "\n" + getString(R.string.text_close_reason) + ":" + followerUser.getLastData());
        }


        //??????

        //????????????????????????
        followWay = String.valueOf(followerUser.getFollowWay());
        double followVal = followerUser.getFollowVal();
        double slRatio = followerUser.getSlRatio();
        double maxDay = followerUser.getMaxDay();
        double maxHold = followerUser.getMaxHold();



        switch (followWay) {
            case "1":
                radio_fixed_margin.setBackground(getResources().getDrawable(R.mipmap.bg_blue_left));
                radio_proportional_margin.setBackground(getResources().getDrawable(R.mipmap.bg_normal_right));

                layout_copy_amount.setVisibility(View.VISIBLE);
                layout_copy_proportion.setVisibility(View.GONE);

                edit_amount.setText(TradeUtil.getNumberFormat(followVal, 0));
                edit_copy_trade_position.setText(TradeUtil.getNumberFormat(maxDay, 0));
                edit_max_trade_position.setText(TradeUtil.getNumberFormat(maxHold, 0));
                edit_amount_bar.setText(TradeUtil.getNumberFormat(slRatio, 0));
                if (slRatio!=-1){
                    checkBox_amount.setChecked(false);
                    layout_stop_loss_amount.setVisibility(View.VISIBLE);
                    bar_amount.post(() -> {
                        bar_amount.setProgress(Integer.parseInt(TradeUtil.getNumberFormat(slRatio, 0)));
                    });
                }else {
                    layout_stop_loss_amount.setVisibility(View.GONE);
                    checkBox_amount.setChecked(true);
                    bar_amount.post(() -> {
                        bar_amount.setProgress(Integer.parseInt(TradeUtil.getNumberFormat(90, 0)));
                    });
                    edit_amount_bar.setText(90 + "");
                }



                break;
            case "2":
                radio_proportional_margin.setBackground(getResources().getDrawable(R.mipmap.bg_red_right));
                radio_fixed_margin.setBackground(getResources().getDrawable(R.mipmap.bg_normal_left));

                layout_copy_amount.setVisibility(View.GONE);
                layout_copy_proportion.setVisibility(View.VISIBLE);

                edit_day_amount_proportion.setText(TradeUtil.getNumberFormat(maxDay, 0));
                edit_max_amount_proportion.setText(TradeUtil.getNumberFormat(maxHold, 0));
                edit_stop_loss_rate.setText(TradeUtil.getNumberFormat(slRatio, 0));
                if (slRatio!=-1){
                    checkbox_proportion.setChecked(false);
                    layout_stop_loss_proportion.setVisibility(View.VISIBLE);
                    bar_stop_loss_proportion.post(() -> {
                        bar_stop_loss_proportion.setProgress(Integer.parseInt(TradeUtil.getNumberFormat(slRatio, 0)));
                    });
                }else {
                    layout_stop_loss_proportion.setVisibility(View.GONE);
                    checkbox_proportion.setChecked(true);
                    bar_stop_loss_proportion.post(() -> {
                        bar_stop_loss_proportion.setProgress(Integer.parseInt(TradeUtil.getNumberFormat(90, 0)));
                    });
                    edit_stop_loss_rate.setText(90 + "");
                }

                double followVal_dou = TradeUtil.mul(followVal, 100);
                String numberFormat = TradeUtil.getNumberFormat(followVal_dou, 0);
                edit_copy_rate_proportion.setText(numberFormat);

                bar_proportion_rate.post(() -> {
                    bar_proportion_rate.setProgress(Integer.parseInt(numberFormat));
                });

                edit_warning_proportion.setText(followerUser.getFollowMax());
                break;

        }


        Glide.with(getActivity()).load(followerUser.getTraderAvatar()).error(R.mipmap.icon_my_bityard).into(img_head);
        text_userName.setText(followerUser.getTraderName());

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
                traderId = followerUser.getTraderId();
                if (followWay.equals("1")) {
                    followVal = edit_amount.getText().toString();
                    maxDay = edit_copy_trade_position.getText().toString();
                    maxHold = edit_max_trade_position.getText().toString();
                    followMax = "0";
                    if (checkBox_amount.isChecked()){
                        slRatio = "-1";
                    }else {
                        slRatio = edit_amount_bar.getText().toString();

                    }
                } else {
                    double v1 = Double.parseDouble(edit_copy_rate_proportion.getText().toString());
                    double div = TradeUtil.div(v1, 100, 2);
                    followVal = String.valueOf(div);
                    maxDay = edit_day_amount_proportion.getText().toString();
                    maxHold = edit_max_amount_proportion.getText().toString();
                    followMax = edit_warning_proportion.getText().toString();
                    if (checkbox_proportion.isChecked()){
                        slRatio="-1";
                    }else {
                        slRatio = edit_stop_loss_rate.getText().toString();
                    }
                }

                /*if (slRatio.equals("")) {
                    slRatio = "-1";
                }*/
                NetManger.getInstance().follow(traderId, "USDT", followWay, followVal, followMax, maxDay, maxHold, slRatio, "true", (state, response) -> {
                            if (state.equals(BUSY)) {
                                showProgressDialog();
                            } else if (state.equals(SUCCESS)) {
                                dismissProgressDialog();
                                TipEntity tipEntity = (TipEntity) response;
                                if (tipEntity.getMessage().equals("")) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.text_tip_success), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else if (state.equals(FAILURE)) {
                                dismissProgressDialog();

                            }
                        }


                );
                break;
            case R.id.text_cancel_follow:
                NetManger.getInstance().followCancel(followerUser.getId(), (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        Toast.makeText(getActivity(), getResources().getString(R.string.text_tip_success), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        NoticeManger.getInstance().notice();

                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });
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
