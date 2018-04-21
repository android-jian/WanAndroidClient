package top.androidjian.androidlearnclient.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import top.androidjian.androidlearnclient.R
import top.androidjian.androidlearnclient.bean.BannerResponse

class BannerAdapter(val context: Context, datas: MutableList<BannerResponse.Data>) :
    BaseQuickAdapter<BannerResponse.Data, BaseViewHolder>(R.layout.banner_item, datas) {
    override fun convert(helper: BaseViewHolder, item: BannerResponse.Data?) {
        item ?: return
        helper.setText(R.id.bannerTitle, item.title.trim())
        val imageView = helper.getView<ImageView>(R.id.bannerImage)
        Glide.with(context).load(item.imagePath).placeholder(R.drawable.logo).into(imageView)
    }
}