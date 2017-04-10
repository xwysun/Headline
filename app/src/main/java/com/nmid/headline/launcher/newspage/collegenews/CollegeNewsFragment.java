package com.nmid.headline.launcher.newspage.collegenews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nmid.headline.R;
import com.nmid.headline.data.bean.New;
import com.nmid.headline.launcher.newspage.NewsListAdapter;
import com.nmid.headline.launcher.newspage.NewsPageContract;
import com.nmid.headline.util.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by xwysu on 2017/4/9.
 */

public class CollegeNewsFragment extends Fragment implements NewsPageContract.View {

    @BindView(R.id.news_list)
    RecyclerView newsList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    Unbinder unbinder;

    NewsListAdapter adapter;

    private NewsPageContract.Presenter mPresenter;

    public CollegeNewsFragment() {

    }

    public static CollegeNewsFragment newInstance() {
        return new CollegeNewsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这里的加载只是为了防止出现NULL，真正的加载是由presenter控制的
        adapter=new NewsListAdapter(getActivity(),new ArrayList<New>(0));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, root);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsList.setLayoutManager(layoutManager);
        newsList.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                mPresenter.loadMoreNews();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadNews();
            }
        });
        adapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                mPresenter.openNewDetail(position);
            }
        });
        newsList.setAdapter(adapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(@Nullable NewsPageContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        // Make sure setRefreshing() is called after the layout is done with everything else.
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showEmptyError() {

    }

    @Override
    public void showOldnews(List<New> oldNews) {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showNews(List<New> news) {
        adapter.notifyAll(news);
    }

    @Override
    public void showMoreNews(List<New> addNews) {
        adapter.notifyAdd(addNews);
    }

    @Override
    public void showNewDetail(New item) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
