package top.androidjian.androidlearnclient.model

import top.androidjian.androidlearnclient.presenter.HomePresenter

interface CollectArticleModel {

    /**
     * add or remove collect article
     */
    fun collectArticle(
        onCollectArticleListener: HomePresenter.OnCollectArticleListener,
        id: Int,
        isAdd: Boolean
    )

    /**
     * cancel collect article Request
     */
    fun cancelCollectRequest()
}