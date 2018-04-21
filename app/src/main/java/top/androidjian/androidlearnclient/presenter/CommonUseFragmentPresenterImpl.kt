package top.androidjian.androidlearnclient.presenter

import top.androidjian.androidlearnclient.bean.FriendListResponse
import top.androidjian.androidlearnclient.bean.HotKeyResponse
import top.androidjian.androidlearnclient.model.HomeModel
import top.androidjian.androidlearnclient.model.HomeModelImpl
import top.androidjian.androidlearnclient.view.CommonUseFragmentView

class CommonUseFragmentPresenterImpl(private val commonUseFragmentView: CommonUseFragmentView) :
    HomePresenter.OnFriendListListener {

    private val homeModel: HomeModel = HomeModelImpl()
    /**
     * get friend tree list
     */
    override fun getFriendList() {
        homeModel.getFriendList(this)
    }

    /**
     * get friend list success
     */
    override fun getFriendListSuccess(
        bookmarkResult: FriendListResponse?,
        commonResult: FriendListResponse,
        hotResult: HotKeyResponse
    ) {
        if (commonResult.errorCode != 0 || hotResult.errorCode != 0) {
            commonUseFragmentView.getFriendListFailed(commonResult.errorMsg)
            return
        }
        if (commonResult.data == null || commonResult.data == null) {
            commonUseFragmentView.getFriendListZero()
            return
        }
        if (commonResult.data?.size == 0 && hotResult.data?.size == 0) {
            commonUseFragmentView.getFriendListZero()
            return
        }
        commonUseFragmentView.getFriendListSuccess(bookmarkResult, commonResult, hotResult)
    }

    /**
     * get friend list failed
     * @param errorMessage error message
     */
    override fun getFriendListFailed(errorMessage: String?) {
        commonUseFragmentView.getFriendListFailed(errorMessage)
    }

    /**
     * cancel request
     */
    fun cancelRequest() {
        homeModel.cancelFriendRequest()
    }
}