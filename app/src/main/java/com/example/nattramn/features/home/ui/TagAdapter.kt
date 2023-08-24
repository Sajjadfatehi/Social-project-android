package com.example.nattramn.features.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nattramn.R
import com.example.nattramn.core.utils.setOnSingleClickListener
import com.example.nattramn.databinding.TagRowBinding

class TagAdapter(
    var tags: List<String>,
    private val onTagsItemListener: OnTagsItemListener
) :
    RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: TagRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.tag_row, parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = tags.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tag = tags[position]
    }

    inner class ViewHolder(val binding: TagRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.commentCardView.setOnSingleClickListener {
                onTagsItemListener.onTagClick(tags[layoutPosition])
            }
        }

    }

}
