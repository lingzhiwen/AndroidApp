package com.ling.project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ling.base.viewmodel.BaseViewModel;
import com.ling.common.bean.ArticleEntity;
import com.ling.network.bean.BaseResponse;
import com.ling.network.https.RetrofitHelper;
import com.ling.network.observer.NetCallback;
import com.ling.network.observer.NetHelperObserver;
import com.ling.network.scheduler.IoMainScheduler;
import com.ling.project.api.ProjectService;

import java.util.List;

/**
 * Created by ling on 2020/7/1 10:50
 */
public class ProjectViewModel extends BaseViewModel {

    public MutableLiveData<List<ArticleEntity.DatasBean>> mArticleListMutable = new MutableLiveData<>();
    public MutableLiveData<BaseResponse> mCollectMutable = new MutableLiveData<>();
    public MutableLiveData<BaseResponse> mUnCollectMutable = new MutableLiveData<>();

    public ProjectViewModel(@NonNull Application application) {
        super(application);
    }

    public void getProjectList(int pageNum, int id) {
        RetrofitHelper.getInstance().create(ProjectService.class)
                .getProjectList(pageNum, id)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse<ArticleEntity>>() {
                    @Override
                    public void success(BaseResponse<ArticleEntity> response) {
                        ArticleEntity articleEntity = response.getData();
                        List<ArticleEntity.DatasBean> datas = articleEntity.getDatas();
                        if (datas != null &&datas.size() > 0) {
                            mArticleListMutable.postValue(datas);
                        } else {

                        }
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

    public void collect(int id) {
        RetrofitHelper.getInstance().create(ProjectService.class)
                .collect(id)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse response) {
                        mCollectMutable.postValue(response);
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

    public void uncollect(int id) {
        RetrofitHelper.getInstance().create(ProjectService.class)
                .unCollect(id)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse response) {
                        mUnCollectMutable.postValue(response);
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }
}
