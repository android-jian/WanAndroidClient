package top.androidjian.androidlearnclient.view

import top.androidjian.androidlearnclient.bean.FriendListResponse
import top.androidjian.androidlearnclient.bean.HotKeyResponse

/**
 * CommonUse Fragment View interface
 */
interface CommonUseFragmentView {
    /**
     * get Friend list Success
     */
    fun getFriendListSuccess(
        bookmarkResult: FriendListResponse?,
        commonResult: FriendListResponse,
        hotResult: HotKeyResponse
    )

    /**
     * get Friend list Failed
     */
    fun getFriendListFailed(errorMessage: String?)

    /**
     * get Friend list data size equal zero
     */
    fun getFriendListZero()
}