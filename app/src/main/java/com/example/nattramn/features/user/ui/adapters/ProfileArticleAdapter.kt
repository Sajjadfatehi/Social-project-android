package com.example.nattramn.features.user.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nattramn.R
import com.example.nattramn.core.utils.Constants
import com.example.nattramn.core.utils.setOnSingleClickListener
import com.example.nattramn.databinding.ProfileArticleRowBinding
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.user.ui.OnProfileArticleListener

class ProfileArticleAdapter(
    var profileArticleViews: List<ArticleView>,
    private val onProfileArticleListener: OnProfileArticleListener
) :
    RecyclerView.Adapter<ProfileArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: ProfileArticleRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.profile_article_row, parent, false
        )

        return ViewHolder(binding)

    }

    override fun getItemCount() = profileArticleViews.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.profileData = profileArticleViews[position]
        holder.binding.likes = profileArticleViews[position].likes.toString()
    }

    inner class ViewHolder(val binding: ProfileArticleRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.profileArticleCard.setOnSingleClickListener {
                onProfileArticleListener.onProfileArticleCardClick(
                    profileArticleViews[layoutPosition].slug
                )
            }
            binding.itemArticleAuthorImage.setOnSingleClickListener {
                onProfileArticleListener.onAuthorIconClick(
                    profileArticleViews[layoutPosition].userView.name
                )
            }
            binding.itemArticleAuthorName.setOnSingleClickListener {
                onProfileArticleListener.onAuthorNameClick(
                    profileArticleViews[layoutPosition].userView.name
                )
            }
            binding.itemArticleBookmark.setOnSingleClickListener {
                onProfileArticleListener.onBookmarkClick(
                    profileArticleViews[layoutPosition].slug,
                    profileArticleViews[layoutPosition].bookmarked,
                    layoutPosition,
                    Constants.PROFILE
                )
            }
            binding.itemArticleDescription.setOnSingleClickListener {
                onProfileArticleListener.onArticleDescriptionClick(
                    profileArticleViews[layoutPosition].slug
                )
            }
            binding.itemArticleTitle.setOnSingleClickListener {
                onProfileArticleListener.onArticleTitleClick(
                    profileArticleViews[layoutPosition].slug
                )
            }
            binding.itemCommentsButton.setOnSingleClickListener {
                onProfileArticleListener.onArticleCommentsClick(
                    layoutPosition
                )
            }
            binding.itemArticleOptions.setOnSingleClickListener {
                onProfileArticleListener.onMoreOptionsClick(
                    profileArticleViews[layoutPosition].slug,
                    layoutPosition
                )
            }

        }

    }
}
