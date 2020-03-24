package com.pro.bityard.fragment.hold;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.PositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.api.OnNetTwoResult;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.OpenPositionEntity;
import com.pro.bityard.entity.TipCloseEntity;
import com.pro.bityard.quote.QuoteManger;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.DecimalEditText;
import com.pro.bityard.view.HeaderRecyclerView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.GET_QUOTE_SECOND;
import static com.pro.bityard.utils.TradeUtil.ProfitAmount;
import static com.pro.bityard.utils.TradeUtil.big;
import static com.pro.bityard.utils.TradeUtil.price;
import static com.pro.bityard.utils.TradeUtil.profitRate;
import static com.pro.bityard.utils.TradeUtil.small;

public class Position2Fragment extends BaseFragment {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.headerRecyclerView)
    HeaderRecyclerView headerRecyclerView;

    private PositionAdapter positionAdapter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tradeType;
    private OpenPositionEntity openPositionEntity;

    private TextView text_incomeAll;
    private View headView;
    private TextView text_price;
    private String contractCode;
    private int flag = 0;
    private  int count_amount=0;
    private  int count_price=0;


    public Position2Fragment newInstance(String type) {
        Position2Fragment fragment = new Position2Fragment();
        Bundle args = new Bundle();
        args.putString("tradeType", type);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_position;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tradeType = getArguments().getString("tradeType");
        }
    }


    @Override
    protected void onLazyLoad() {

    }


    @Override
    protected void initView(View view) {

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));


        positionAdapter = new PositionAdapter(getContext());

        headerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        headView = LayoutInflater.from(getContext()).inflate(R.layout.item_position_head_layout, null);

        text_incomeAll = headView.findViewById(R.id.text_total_profit_loss);
        /*一键平仓*/
        headView.findViewById(R.id.text_close_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetManger.getInstance().closeAll(TradeUtil.positionIdList(openPositionEntity), tradeType, new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipCloseEntity tipCloseEntity = (TipCloseEntity) response;
                            Toast.makeText(getContext(), tipCloseEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            initData();
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    }
                });
            }
        });


        headerRecyclerView.setAdapter(positionAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        positionAdapter.setOnItemClick(new PositionAdapter.OnItemClick() {
            @Override
            public void onClickListener(OpenPositionEntity.DataBean data) {

            }

            @Override
            public void onCloseListener(String id) {


                /*平仓*/
                NetManger.getInstance().close(id, tradeType, new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipCloseEntity tipCloseEntity = (TipCloseEntity) response;
                            Toast.makeText(getContext(), tipCloseEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            initData();

                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    }
                });


            }

            @Override
            public void onProfitLossListener(OpenPositionEntity.DataBean data) {
                Log.d("print", "onProfitLossListener:165:  " + data);
                showPopWindow(data);

            }


        });

        startScheduleJob(mHandler, GET_QUOTE_SECOND, GET_QUOTE_SECOND);


    }

    /*修改止盈止损*/
    private void showPopWindow(OpenPositionEntity.DataBean data) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_spsl_layout2, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        contractCode = data.getContractCode();

        final boolean isBuy = data.isIsBuy();
        final int priceDigit = data.getPriceDigit();
        final double price = data.getPrice();
        final double lever = data.getLever();
        final double margin = data.getMargin();
        double stopProfit = data.getStopProfit();
        double stopLoss = data.getStopLoss();

        double stopLossBegin = data.getStopLossBegin();
        double stopProfitBegin = data.getStopProfitBegin();
        //当前价格
        text_price = view.findViewById(R.id.text_price);

        //量
        TextView text_volume = view.findViewById(R.id.text_volume);
        text_volume.setText("×" + String.valueOf(data.getVolume()));
        //名称
        TextView text_name = view.findViewById(R.id.text_name);
        String[] split = Util.quoteList(contractCode).split(",");
        text_name.setText(split[0]);
        //开仓价
        TextView text_open_price = view.findViewById(R.id.text_open_price);
        text_open_price.setText(String.valueOf(data.getOpPrice()));


        double profit_max_amount = TradeUtil.mul(margin, 5);
        double profit_min_amount = TradeUtil.mul(margin, 0.05);

        double loss_max_amount = TradeUtil.mul(margin, 0.9);
        double loss_min_amount = TradeUtil.mul(margin, 0.05);


        double profit_max_price = Double.parseDouble(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, profit_max_amount));
        double profit_min_price = Double.parseDouble(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, profit_min_amount));

        double loss_max_price = Double.parseDouble(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, loss_max_amount));
        double loss_min_price = Double.parseDouble(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, loss_min_amount));


        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);

        //预计盈利和止盈价
        TextView text_profit_stop_price = view.findViewById(R.id.text_profit_stop_price);
        //止盈价和盈利金额
        TextView text_stop_price_profit = view.findViewById(R.id.text_stop_price_profit);
        //预计亏损和止损价
        TextView text_loss_forward_price = view.findViewById(R.id.text_loss_forward_price);
        //止损价和亏损金额
        TextView text_stop_loss_price_amount = view.findViewById(R.id.text_stop_loss_price_amount);


        //预计亏损和
        DecimalEditText edit_profit_stop_price = view.findViewById(R.id.edit_profit_stop_price);
        //预计盈利
        edit_profit_stop_price.setText(String.valueOf(stopProfit));
        edit_profit_stop_price.setSelection(String.valueOf(stopProfit).length());//默认光标在最后面
        edit_profit_stop_price.setDecimalEndNumber(2);
        //止盈价和盈利金额
        TextView text_stop_price_profit_content = view.findViewById(R.id.text_stop_price_profit_content);
        text_stop_price_profit_content.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, stopProfit));
        //预计亏损和止损价
        DecimalEditText edit_loss_amount_price = view.findViewById(R.id.edit_loss_amount_price);
        edit_loss_amount_price.setText(String.valueOf(Math.abs(stopLoss)));
        edit_loss_amount_price.setSelection(String.valueOf(Math.abs(stopLoss)).length());//默认光标在最后面
        edit_loss_amount_price.setDecimalEndNumber(2);
        //止损价和亏损金额
        TextView text_stop_loss_price_amount_content = view.findViewById(R.id.text_stop_loss_price_amount_content);
        text_stop_loss_price_amount_content.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Math.abs(stopLoss)));
        //盈利百分比
        TextView text_profit_rate=view.findViewById(R.id.text_profit_rate);
        text_profit_rate.setText(profitRate(stopProfit, margin));

        //亏损百分比
        TextView text_loss_rate=view.findViewById(R.id.text_loss_rate);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_0:
                        flag = 0;


                        text_profit_stop_price.setText(getResources().getText(R.string.text_profit_forward));
                        text_stop_price_profit.setText(getResources().getText(R.string.text_stop_profit_pop));
                        text_loss_forward_price.setText(getResources().getText(R.string.text_loss_forward));
                        text_stop_loss_price_amount.setText(getResources().getText(R.string.text_stop_loss_pop));
                        //预计盈利
                        edit_profit_stop_price.setText(String.valueOf(stopProfit));
                        edit_profit_stop_price.setSelection(String.valueOf(stopProfit).length());//默认光标在最后面
                        edit_profit_stop_price.setDecimalEndNumber(2);
                        //止盈价
                        text_stop_price_profit_content.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, stopProfit));
                        //预计亏损
                        edit_loss_amount_price.setText(String.valueOf(Math.abs(stopLoss)));
                        edit_loss_amount_price.setSelection(String.valueOf(Math.abs(stopLoss)).length());//默认光标在最后面
                        edit_loss_amount_price.setDecimalEndNumber(2);
                        //止损价
                        text_stop_loss_price_amount_content.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Math.abs(stopLoss)));
                        count_amount++;

                        break;
                    case R.id.radio_1:
                        flag = 1;

                        text_profit_stop_price.setText(getResources().getText(R.string.text_profit_price));
                        text_stop_price_profit.setText(getResources().getText(R.string.text_profit_amount));
                        text_loss_forward_price.setText(getResources().getText(R.string.text_stoploss_price));
                        text_stop_loss_price_amount.setText(getResources().getText(R.string.text_loss_amount));
                        //止盈价
                        edit_profit_stop_price.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, stopProfit));
                        //盈利金额
                        text_stop_price_profit_content.setText(String.valueOf(stopProfit));
                        //止损价
                        edit_loss_amount_price.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Math.abs(stopLoss)));
                        //亏损金额
                        text_stop_loss_price_amount_content.setText(String.valueOf(Math.abs(stopLoss)));


                        count_price++;

                        break;
                }
            }
        });


        //预计盈利和止盈价的监听
        edit_profit_stop_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (flag==0){
                    if (s.length() == 0) {
                        edit_profit_stop_price.setHint(profit_min_amount + "~" + profit_max_amount);
                        text_stop_price_profit_content.setText(ProfitAmount(isBuy, price, priceDigit, lever, margin, big(profit_min_price, profit_max_price)));
                        text_profit_rate.setText("0.00%");


                    } else {
                        if (!s.toString().startsWith(".")) {
                            if (Double.parseDouble(s.toString()) > profit_max_amount) {
                                edit_profit_stop_price.setText(String.valueOf(profit_max_amount));
                                text_stop_price_profit_content.setText(ProfitAmount(isBuy, price, priceDigit, lever, margin, big(profit_min_price, profit_max_price)));
                                text_profit_rate.setText(profitRate(profit_max_amount, margin));


                            } else if (Double.parseDouble(s.toString()) < profit_min_amount) {

                                edit_profit_stop_price.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (s.length() != 0 && Double.parseDouble(s.toString()) < profit_min_amount) {
                                            edit_profit_stop_price.setText(String.valueOf(profit_min_amount));
                                            text_profit_rate.setText(profitRate(profit_min_amount, margin));
                                            text_stop_price_profit_content.setText(ProfitAmount(isBuy, price, priceDigit, lever, margin, small(profit_min_price, profit_max_price)));



                                        }
                                    }
                                }, 1000);
                            } else {
                                //价格页面 盈利金额 直接输入获取
                                text_stop_price_profit_content.setText(s.toString());
                                //金额页面 盈利百分比
                                text_profit_rate.setText(profitRate(Double.parseDouble(s.toString()), margin));
                                //金额页面 止盈价
                                text_stop_price_profit_content.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString())));
                                //价格页面  止盈价输入框
                               /* edit_stop_profit_price.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString())));
                                //edit_stop_profit_price.setText(text_stop_profit_amount.getText().toString());

                                //价格页面 盈利百分比
                                text_profit_rate_price.setText(profitRate(Double.parseDouble(s.toString()), margin));*/


                            }
                        } else {
                            String s1 = s.toString().replace(".", "");
                            edit_profit_stop_price.setText(s1);
                            text_profit_rate.setText(profitRate(Double.parseDouble(s1.toString()), margin));



                        }


                    }
                }else if (flag==1){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        view.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                backgroundAlpha(1f);
                flag=0;
                count_amount=0;
                count_price=0;
            }
        });




        //金额 盈利 加号
        view.findViewById(R.id.profit_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    if (edit_profit_stop_price.getText().toString().length() > 0) {
                        double a = TradeUtil.add(Double.parseDouble(edit_profit_stop_price.getText().toString()), TradeUtil.scale(2));
                        edit_profit_stop_price.setText(String.valueOf(a));
                        edit_profit_stop_price.setSelection(String.valueOf(a).length());
                    }
                } else if (flag == 1) {
                    if (edit_profit_stop_price.getText().toString().length() > 0) {
                        double a = TradeUtil.add(Double.parseDouble(edit_profit_stop_price.getText().toString()), TradeUtil.scale(priceDigit));
                        edit_profit_stop_price.setText(String.valueOf(a));
                        edit_profit_stop_price.setSelection(String.valueOf(a).length());
                    }
                }


            }
        });
        //金额 盈利 减号
        view.findViewById(R.id.profit_sub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    if (edit_profit_stop_price.getText().toString().length() > 0) {
                        double a = TradeUtil.sub(Double.parseDouble(edit_profit_stop_price.getText().toString()), TradeUtil.scale(2));
                        edit_profit_stop_price.setText(String.valueOf(a));
                        edit_profit_stop_price.setSelection(String.valueOf(a).length());

                    }
                } else if (flag == 1) {
                    if (edit_profit_stop_price.getText().toString().length() > 0) {
                        double a = TradeUtil.sub(Double.parseDouble(edit_profit_stop_price.getText().toString()), TradeUtil.scale(priceDigit));
                        edit_profit_stop_price.setText(String.valueOf(a));
                        edit_profit_stop_price.setSelection(String.valueOf(a).length());

                    }
                }


            }
        });

        //金额 亏损 加号
        view.findViewById(R.id.loss_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    //当前得小与初始亏损比例
                    double a = TradeUtil.add(Double.parseDouble(edit_loss_amount_price.getText().toString()), TradeUtil.scale(2));
                    edit_loss_amount_price.setText(String.valueOf(a));
                    edit_loss_amount_price.setSelection(String.valueOf(a).length());
                } else if (flag == 1) {
                    if (edit_loss_amount_price.getText().toString().length() > 0) {
                        double a = TradeUtil.add(Double.parseDouble(edit_loss_amount_price.getText().toString()), TradeUtil.scale(priceDigit));
                        edit_loss_amount_price.setText(String.valueOf(a));
                        edit_loss_amount_price.setSelection(String.valueOf(a).length());

                    }
                }
            }
        });

        //金额 亏损 减号
        view.findViewById(R.id.loss_sub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    double a = TradeUtil.sub(Double.parseDouble(edit_loss_amount_price.getText().toString()), TradeUtil.scale(2));
                    edit_loss_amount_price.setText(String.valueOf(a));
                    edit_loss_amount_price.setSelection(String.valueOf(a).length());
                } else if (flag == 1) {
                    if (edit_loss_amount_price.getText().toString().length() > 0) {
                        double a = TradeUtil.sub(Double.parseDouble(edit_loss_amount_price.getText().toString()), TradeUtil.scale(priceDigit));
                        edit_loss_amount_price.setText(String.valueOf(a));
                        edit_loss_amount_price.setSelection(String.valueOf(a).length());

                    }
                }

            }
        });


        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.6f;
        getActivity().getWindow().setAttributes(params);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.pop_anim);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }


    public void backgroundAlpha(float bgalpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgalpha;
        getActivity().getWindow().setAttributes(lp);


    }

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<String> quoteList = QuoteManger.getInstance().getQuoteList();
            if (quoteList != null && openPositionEntity != null) {
                setIncome(quoteList, openPositionEntity);
                positionAdapter.setDatas(openPositionEntity.getData(), quoteList);
                //pop也是同步刷新
                if (text_price != null) {
                    price(quoteList, contractCode, new TradeResult() {
                        @Override
                        public void setResult(Object response) {
                            text_price.setText(response.toString());
                        }
                    });
                }
            }
        }
    };

    /*设置浮动盈亏*/
    private void setIncome(List<String> quoteList, OpenPositionEntity openPositionEntity) {
        TradeUtil.getIncome(quoteList, openPositionEntity, new TradeResult() {
            @Override
            public void setResult(Object response) {
                Double incomeAll = (Double) response;
                if (incomeAll > 0) {
                    text_incomeAll.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.text_quote_green));
                } else {
                    text_incomeAll.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.text_quote_red));
                }
                text_incomeAll.setText(response.toString());

            }
        });
    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {


        NetManger.getInstance().getHold(tradeType, new OnNetTwoResult() {
            @Override
            public void setResult(String state, Object response1, Object response2) {
                if (state.equals(BUSY)) {
                    swipeRefreshLayout.setRefreshing(true);
                } else if (state.equals(SUCCESS)) {
                    swipeRefreshLayout.setRefreshing(false);
                    openPositionEntity = (OpenPositionEntity) response1;
                    List<String> quoteList = (List<String>) response2;
                    positionAdapter.setDatas(openPositionEntity.getData(), quoteList);
                    //这里根据持仓来是否显示头部视图
                    if (openPositionEntity.getData().size() == 0) {
                        text_incomeAll.setText("");
                        headerRecyclerView.removeHeaderView(headView);
                    } else {
                        //防止刷新已经有头布局 继续添加出现的bug
                        if (headerRecyclerView.getHeadersCount() == 0) {
                            headerRecyclerView.addHeaderView(headView);
                        }
                    }

                } else if (state.equals(FAILURE)) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            }


        });


    }
}
