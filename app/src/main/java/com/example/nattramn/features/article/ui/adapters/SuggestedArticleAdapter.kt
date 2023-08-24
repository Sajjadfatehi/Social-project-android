package com.example.nattramn.features.article.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nattramn.core.utils.Constants
import com.example.nattramn.core.utils.setOnSingleClickListener
import com.example.nattramn.databinding.HorizontalArticleRowBinding
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.article.ui.OnArticleListener

class SuggestedArticleAdapter(
    var suggestions: List<ArticleView>,
    private val onArticleListener: OnArticleListener
) :
    RecyclerView.Adapter<SuggestedArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = HorizontalArticleRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.article = suggestions[position]

    }

    override fun getItemCount() = suggestions.size

    inner class ViewHolder(val binding: HorizontalArticleRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.horizontalArticleCard.setOnSingleClickListener {
                onArticleListener.onCardClick(
                    suggestions[layoutPosition].slug
                )
            }
            binding.itemBookmark.setOnSingleClickListener {
                onArticleListener.onArticleSaveClick(
                    suggestions[layoutPosition].slug,
                    suggestions[layoutPosition].bookmarked,
                    layoutPosition,
                    Constants.SUGGESTION
                )
            }
            binding.itemArticlePreview.setOnSingleClickListener {
                onArticleListener.onArticleTitleClick(
                    suggestions[layoutPosition].slug
                )
            }
            binding.itemAuthorImage.setOnSingleClickListener {
                onArticleListener.onAuthorIconClick(
                    suggestions[layoutPosition].userView.name
                )
            }
            binding.itemAuthorName.setOnSingleClickListener {
                onArticleListener.onAuthorNameClick(
                    suggestions[layoutPosition].userView.name
                )
            }

        }

    }

}