package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.AnnouncementAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.AnnouncementEntity;
import com.pro.bityard.utils.TradeUtil;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class AnnouncementFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.view_line)
    View view_line;
    @BindView(R.id.layout_view)
    LinearLayout layout_view;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    // 声明平移动画
    private TranslateAnimation animation;
    @BindView(R.id.recyclerView_address)
    RecyclerView recyclerView_address;

    private AnnouncementAdapter announcementAdapter;


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_announcements));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        view_line.setVisibility(View.GONE);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));

        announcementAdapter = new AnnouncementAdapter(getActivity());
        recyclerView_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_address.setAdapter(announcementAdapter);
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(this::initData);

        announcementAdapter.setDetailClick(data -> {
            showDetailPopWindow(data);
        });


    }

    /*显示删除的按钮*/
    private void showDetailPopWindow(AnnouncementEntity.NoticesBean data) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_announcement_detail_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_announcement_detail);
        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(data.getTitle());
        TextView text_time = view.findViewById(R.id.text_time);
        text_time.setText(TradeUtil.dateToStamp(data.getTime()));

        WebView webView = view.findViewById(R.id.webview);
        webView.setBackgroundColor(0);
        String CSS_STYLE ="<style>* {font-size:16px;line-height:20px;}p {color:#ffffff;}</style>";
        webView.loadDataWithBaseURL(null, CSS_STYLE+data.getContent(), "text/html", "utf-8", null);

        view.findViewById(R.id.img_back).setOnClickListener(v -> {
            popupWindow.dismiss();
        });


        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_announcement;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        NetManger.getInstance().discover("en-us", (state, response) -> {
            if (state.equals(BUSY)) {
                swipeRefreshLayout.setRefreshing(true);
            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout.setRefreshing(false);
                AnnouncementEntity entity = (AnnouncementEntity) response;
                if (entity != null) {
                    announcementAdapter.setDatas(entity.getNotices());
                }
            } else if (state.equals(FAILURE)) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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
