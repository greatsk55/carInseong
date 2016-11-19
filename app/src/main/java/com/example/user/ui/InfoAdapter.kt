package com.example.user.ui

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.bumptech.glide.Glide
import com.example.user.Application
import com.example.user.dto.Comment
import com.example.user.network.Server


/**
 * Created by user on 16. 11. 18.
 */
class InfoAdapter : BaseQuickAdapter<Comment, BaseViewHolder>(R.layout.row_info, Application.commentList) {


    override fun convert(viewHolder: BaseViewHolder, item: Comment) {
        viewHolder.setText(R.id.name, item.name)
                .setText(R.id.contents, item.comment)
    }
}