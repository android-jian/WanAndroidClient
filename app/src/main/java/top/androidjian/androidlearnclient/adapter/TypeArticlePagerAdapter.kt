package top.androidjian.androidlearnclient.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import top.androidjian.androidlearnclient.bean.TreeListResponse.Data.Children
import top.androidjian.androidlearnclient.ui.fragment.TypeArticleFragment

class TypeArticlePagerAdapter(val list: List<Children>, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {
    private val listFragment = mutableListOf<Fragment>()

    init {
        list.forEach { listFragment.add(TypeArticleFragment.newInstance(it.id)) }
    }

    override fun getItem(position: Int): Fragment = listFragment[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence = list[position].name

    override fun getItemPosition(`object`: Any?): Int = PagerAdapter.POSITION_NONE
}