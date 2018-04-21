package top.androidjian.androidlearnclient.presenter

import top.androidjian.androidlearnclient.bean.ArticleListResponse
import top.androidjian.androidlearnclient.bean.HomeListResponse
import top.androidjian.androidlearnclient.model.CollectArticleModel
import top.androidjian.androidlearnclient.model.HomeModelImpl
import top.androidjian.androidlearnclient.model.TypeArticleModel
import top.androidjian.androidlearnclient.model.TypeArticleModelImpl
import top.androidjian.androidlearnclient.view.CollectArticleView
import top.androidjian.androidlearnclient.view.TypeArticleFragmentView

class TypeArticlePresenterImpl(
    private val typeArticleFragmentView: TypeArticleFragmentView,
    private val collectArticleView: CollectArticleView
) : TypeArticlePresenter, TypeArticlePresenter.OnTypeArticleListListener,
    HomePresenter.OnCollectArticleListener {

    private val typeArticleModel: TypeArticleModel = TypeArticleModelImpl()
    private val collectArticleModel: CollectArticleModel = HomeModelImpl()

    /**
     * get Type Article list
     * @param page page
     * @param cid cid
     */
    override fun getTypeArticleList(page: Int, cid: Int) {
        typeArticleModel.getTypeArticleList(this, page, cid)
    }

    /**
     * get Type Article list success
     * @param result ArticleListResponse
     */
    override fun getTypeArticleListSuccess(result: ArticleListResponse) {
        if (result.errorCode != 0) {
            typeArticleFragmentView.getTypeArticleListFailed(result.errorMsg)
            return
        }
        val total = result.data.total
        if (total == 0) {
            typeArticleFragmentView.getTypeArticleListZero()
            return
        }
        if (total < result.data.size) {
            typeArticleFragmentView.getTypeArticleListSmall(result)
            return
        }
        typeArticleFragmentView.getTypeArticleListSuccess(result)
    }

    /**
     * get Type Article list failed
     * @param errorMessage error message
     */
    override fun getTypeArticleListFailed(errorMessage: String?) {
        typeArticleFragmentView.getTypeArticleListFailed(errorMessage)
    }

    /**
     *  add or remove collect article
     *  @param id article id
     *  @param isAdd true add, false remove
     */
    override fun collectArticle(id: Int, isAdd: Boolean) {
        collectArticleModel.collectArticle(this, id, isAdd)
    }

    /**
     * add collect article success
     * @param result HomeListResponse
     * @param isAdd true add, false remove
     */
    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        if (result.errorCode != 0) {
            collectArticleView.collectArticleFailed(result.errorMsg, isAdd)
        } else {
            collectArticleView.collectArticleSuccess(result, isAdd)
        }
    }

    /**
     * add collect article failed
     * @param errorMessage error message
     * @param isAdd true add, false remove
     */
    override fun collectArticleFailed(errorMessage: String?, isAdd: Boolean) {
        collectArticleView.collectArticleFailed(errorMessage, isAdd)
    }

    fun cancelRequest() {
        typeArticleModel.cancelRequest()
        collectArticleModel.cancelCollectRequest()
    }
}