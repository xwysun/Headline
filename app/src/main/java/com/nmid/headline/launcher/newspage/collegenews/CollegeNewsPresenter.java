package com.nmid.headline.launcher.newspage.collegenews;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.nmid.headline.data.NewsDataSource;
import com.nmid.headline.data.NewsRepository;
import com.nmid.headline.data.bean.New;
import com.nmid.headline.launcher.newspage.NewsPageContract;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by xwysu on 2017/4/9.
 */

public class CollegeNewsPresenter implements NewsPageContract.Presenter {

    private final NewsRepository mNewsRepository;
    private final NewsPageContract.View mNewsView;
    private boolean mFirstLoad = true;
    //recordId用来记录最后的PID，是实际上的lastId，因为MVP的特性，这样写好一些
    int recordId=0;

    public CollegeNewsPresenter(@NonNull NewsRepository newsRepository,@NonNull NewsPageContract.View view){
        mNewsRepository=checkNotNull(newsRepository);
        mNewsView=checkNotNull(view);
        mNewsView.setPresenter(this);
    }
    @Override
    public void start() {
        loadNews();
    }

    @Override
    public void loadNews() {
        loadNews(NewsDataSource.FIRST_REQUEST);
        recordId=0;
    }
    private void loadNews(int lastId){
        mNewsRepository.getNews(new NewsDataSource.LoadNewsCallback() {
            @Override
            public void onNewsLoaded(List<New> news) {
                checkNotNull(news);
                recordId=news.get(news.size()-1).getNewsPid();
                if (mNewsView.isActive()){
                    if (lastId>0){
                        mNewsView.showMoreNews(news);
                    }else {
                        mNewsView.showNews(news);
                    }
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mNewsView.isActive()){
                    mNewsView.showError();
                }
            }
        },NewsDataSource.TYPE_JINGWEI,lastId);
    }

    @Override
    public void loadOldNews() {

    }

    @Override
    public void loadMoreNews() {
        loadNews(recordId);
    }

    @Override
    public void openNewDetail(int pos) {

    }
}
