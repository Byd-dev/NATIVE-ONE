package com.pro.bityard.guide;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.MainActivity;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.GuideEntity;
import com.pro.switchlibrary.SPUtils;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class GuideActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.banner)
    XBanner banner;

    @BindView(R.id.btn_sure)
    Button btn_sure;
    private List<String> contractsList, getalllist;
    private List<String> foreignList, getForeignList;
    private List<String> stockindexList, getStockindexList;
    private List<String> domesList, getDomesList;

    private List<GuideEntity> data;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_guide;
    }

    public static void enter(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {

        String string = SPUtils.getString(AppConfig.FIRST_OPEN);
        if (!string.equals("")) {
            MainActivity.enter(GuideActivity.this, MainActivity.TAB_TYPE.TAB_HOME);
            GuideActivity.this.finish();


        } else {

           // getApi();

            MainActivity.enter(GuideActivity.this, MainActivity.TAB_TYPE.TAB_HOME);

            SPUtils.putString(AppConfig.FIRST_OPEN, "first");


            data = new ArrayList<>();
            data.add(new GuideEntity("实盘", "模拟", "助您验证理财方案", getResources().getDrawable(R.mipmap.guide_1)));
            data.add(new GuideEntity("闪电下单", "让您快人一步", "同样的操作 不同的收益", getResources().getDrawable(R.mipmap.guide_2)));
            data.add(new GuideEntity("每日签到", "领红包", "红包可用于实盘交易抵扣现金", getResources().getDrawable(R.mipmap.guide_3)));
            data.add(new GuideEntity("五重防护", "资金更安全", "资金 隐私 项目 合法 银行级别风控", getResources().getDrawable(R.mipmap.guide_4)));

            banner.setBannerData(R.layout.banner_guide_layout, data);
            banner.loadImage(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {
                    ImageView imageView = view.findViewById(R.id.img_banner);

                    imageView.setImageDrawable(data.get(position).getDrawable());

                    TextView text_left = view.findViewById(R.id.text_left);
                    TextView text_right = view.findViewById(R.id.text_right);
                    TextView text_bottom = view.findViewById(R.id.text_bottom);

                    text_left.setText(data.get(position).getTextLeft());
                    text_right.setText(data.get(position).getTextRight());
                    text_bottom.setText(data.get(position).getTextBottom());
                }
            });

            banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    if (i == 3) {
                        btn_sure.setVisibility(View.VISIBLE);
                    } else {
                        btn_sure.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
            findViewById(R.id.text_jump).setOnClickListener(this);
            btn_sure.setOnClickListener(this);

        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_jump:
            case R.id.btn_sure:
                MainActivity.enter(GuideActivity.this, MainActivity.TAB_TYPE.TAB_HOME);
                GuideActivity.this.finish();
                break;
        }
    }

   /* private void getApi() {
        OkGo.<String>get(OConstant.URL_API)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        if (!TextUtils.isEmpty(response.body())) {

                            OApiEntity oApiEntity = new Gson().fromJson(response.body(), OApiEntity.class);

                            QuoteProxy.getInstance().setoApiEntity(oApiEntity);

                            SPUtils.putData(UserConfig.API, oApiEntity);
                            String contracts = oApiEntity.getContracts().replaceAll("\"", "")
                                    .replaceAll("\\[", "").replaceAll("]", "");

                            String[] split = contracts.split(",");

                            contractsList = new ArrayList<>();

                            for (String x : split) {
                                contractsList.add(x);
                            }


                            foreignList = new ArrayList<>();
                            List<OApiEntity.ForeignCommdsBean> foreignCommds = oApiEntity.getForeignCommds();
                            for (int i = 0; i < foreignCommds.size(); i++) {
                                foreignList.add(foreignCommds.get(i).getCode());
                            }

                            QuoteProxy.getInstance().setForeignList(foreignList);


                            List<OApiEntity.StockIndexCommdsBean> stockIndexCommds = oApiEntity.getStockIndexCommds();
                            stockindexList = new ArrayList<>();
                            for (OApiEntity.StockIndexCommdsBean y :
                                    stockIndexCommds) {
                                stockindexList.add(y.getCode());
                            }

                            QuoteProxy.getInstance().setStockindexList(stockindexList);

                            domesList = new ArrayList<>();
                            List<OApiEntity.DomesticCommdsBean> domesticCommds = oApiEntity.getDomesticCommds();
                            for (OApiEntity.DomesticCommdsBean y :
                                    domesticCommds) {
                                domesList.add(y.getCode());
                            }

                            QuoteProxy.getInstance().setDomesList(domesList);

                            Log.d("SplashActivity", "onSuccess:guide 197: " + contractsList);
                            Log.d("SplashActivity", "onSuccess:guide 198:" + foreignList);
                            Log.d("SplashActivity", "onSuccess:guide 199: " + stockindexList);
                            Log.d("SplashActivity", "onSuccess:guide 200: " + domesList);
                            getForeignList = new ArrayList<>();
                            for (int i = 0; i < contractsList.size(); i++) {
                                for (int j = 0; j < foreignList.size(); j++) {
                                    if (contractsList.get(i).startsWith(foreignList.get(j))) {
                                        getForeignList.add(contractsList.get(i));
                                    }
                                }
                            }

                            getStockindexList = new ArrayList<>();
                            for (int i = 0; i < contractsList.size(); i++) {
                                for (int j = 0; j < stockindexList.size(); j++) {
                                    if (contractsList.get(i).startsWith(stockindexList.get(j))) {
                                        getStockindexList.add(contractsList.get(i));
                                    }
                                }
                            }

                            getDomesList = new ArrayList<>();
                            for (int i = 0; i < contractsList.size(); i++) {
                                for (int j = 0; j < domesList.size(); j++) {
                                    if (contractsList.get(i).startsWith(domesList.get(j))) {
                                        getDomesList.add(contractsList.get(i));
                                    }
                                }
                            }


                         *//*   Log.d("print", "onSuccess:234: " + getForeignList);
                            Log.d("print", "onSuccess:235:" + getStockindexList);
                            Log.d("print", "onSuccess:236: " + getDomesList);*//*
                            getalllist = new ArrayList<>();
                            getalllist.addAll(getForeignList);
                            getalllist.addAll(getStockindexList);
                            getalllist.addAll(getDomesList);

                            SPUtils.putString(UserConfig.ALLDEX, getalllist.toString());
                         *//*   SPUtils.putString(UserConfig.FOREIGN, getForeignList.toString());
                            SPUtils.putString(UserConfig.STOCKINDEX, getStockindexList.toString());
                            SPUtils.putString(UserConfig.DOMESTIC, getDomesList.toString());*//*


                        }

                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });

    }*/
}
