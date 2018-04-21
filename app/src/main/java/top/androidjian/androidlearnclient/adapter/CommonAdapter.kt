package top.androidjian.androidlearnclient.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import top.androidjian.androidlearnclient.R
import top.androidjian.androidlearnclient.bean.FriendListResponse

class CommonAdapter(val context: Context, datas: MutableList<FriendListResponse.Data>) :
    BaseQuickAdapter<FriendListResponse.Data, BaseViewHolder>(R.layout.common_list_item, datas) {
    override fun convert(helper: BaseViewHolder, item: FriendListResponse.Data?) {
        item ?: return
        helper.setText(R.id.commonItemTitle, item.name.trim())
    }
}