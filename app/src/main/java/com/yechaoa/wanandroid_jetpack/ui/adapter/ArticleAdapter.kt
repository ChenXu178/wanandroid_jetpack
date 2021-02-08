package com.yechaoa.wanandroid_jetpack.ui.adapter

import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yechaoa.wanandroid_jetpack.R
import com.yechaoa.wanandroid_jetpack.data.bean.Article

/**
 * Created by yechaoa on 2021/2/4.
 * Describe :
 */
class ArticleAdapter : BaseQuickAdapter<Article.ArticleDetail, BaseViewHolder>(R.layout.item_article), LoadMoreModule {

    init {
        addChildClickViewIds(R.id.article_favorite)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: Article.ArticleDetail) {
        //fromHtml，因为搜索结果中的title中含有html标签
        holder.setText(R.id.article_title, Html.fromHtml(item.title,Html.FROM_HTML_MODE_COMPACT))
        holder.setText(R.id.article_chapter, item.chapterName)
        holder.setText(R.id.article_date, item.niceDate)

        if (item.collect) {
            Glide.with(context).load(R.mipmap.ic_like_checked).into(holder.getView(R.id.article_favorite))
        } else {
            Glide.with(context).load(R.mipmap.ic_like_normal).into(holder.getView(R.id.article_favorite))
        }
    }

}