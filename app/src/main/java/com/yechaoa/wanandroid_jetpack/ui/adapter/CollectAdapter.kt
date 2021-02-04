package com.yechaoa.wanandroid_jetpack.ui.adapter

import android.text.Html
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yechaoa.wanandroid_jetpack.R
import com.yechaoa.wanandroid_jetpack.data.bean.CollectDetail

/**
 * Created by yechaoa on 2021/2/4.
 * Describe :
 */
class CollectAdapter : BaseQuickAdapter<CollectDetail, BaseViewHolder>(R.layout.item_article), LoadMoreModule {

    init {
        addChildClickViewIds(R.id.article_favorite)
    }

    override fun convert(holder: BaseViewHolder, item: CollectDetail) {
        //fromHtml，因为搜索结果中的title中含有html标签
        holder.setText(R.id.article_title, Html.fromHtml(item.title))
        holder.setText(R.id.article_chapter, item.chapterName)
        holder.setText(R.id.article_date, item.niceDate)
        Glide.with(context).load(R.mipmap.ic_like_checked).into(holder.getView(R.id.article_favorite))
    }
}