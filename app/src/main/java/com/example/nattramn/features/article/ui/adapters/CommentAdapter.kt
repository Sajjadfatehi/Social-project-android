package com.example.nattramn.features.article.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nattramn.R
import com.example.nattramn.core.utils.setOnSingleClickListener
import com.example.nattramn.databinding.CommentRowBinding
import com.example.nattramn.features.article.ui.CommentView
import com.example.nattramn.features.article.ui.OnCommentListener

class CommentAdapter(
    var comments: List<CommentView>,
    private val commentListener: OnCommentListener
) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: CommentRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.comment_row, parent, false
        )

        return ViewHolder(binding)

    }

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.comment = comments[position]

    }

    inner class ViewHolder(val binding: CommentRowBinding) : RecyclerView.ViewHolder(binding.root) {


        init {

            binding.itemCommentName.setOnSingleClickListener {
                commentListener.onCommentIconClick(
                    comments[layoutPosition].username
                )
            }
            binding.itemCommentImage.setOnSingleClickListener {
                commentListener.onCommentUsernameClick(
                    comments[layoutPosition].username
                )
            }

        }

    }

}
