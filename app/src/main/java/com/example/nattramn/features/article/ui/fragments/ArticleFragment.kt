package com.example.nattramn.features.article.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nattramn.R
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.core.resource.Status
import com.example.nattramn.core.utils.setOnSingleClickListener
import com.example.nattramn.core.utils.snackMaker
import com.example.nattramn.databinding.FragmentArticleBinding
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.article.ui.CommentView
import com.example.nattramn.features.article.ui.OnArticleListener
import com.example.nattramn.features.article.ui.OnCommentListener
import com.example.nattramn.features.article.ui.adapters.CommentAdapter
import com.example.nattramn.features.article.ui.adapters.SuggestedArticleAdapter
import com.example.nattramn.features.article.ui.viewmodels.ArticleViewModel
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class ArticleFragment : Fragment(), OnCommentListener, OnArticleListener {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var suggestedArticleAdapter: SuggestedArticleAdapter
    private lateinit var dialog: Dialog
    private lateinit var articleViewArg: ArticleView
    private val args: ArticleFragmentArgs by navArgs()
    private val snapHorizontal = GravitySnapHelper(Gravity.CENTER)

    private var tags: MutableList<String>? = mutableListOf()
    private var comments: List<CommentView>? = mutableListOf()
    private lateinit var articleSlug: String
    private lateinit var suggestionArticles: MutableList<ArticleView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        articleViewArg = args.articleView
        articleSlug = articleViewArg.slug
        tags = articleViewArg.tags?.toMutableList()
        comments = articleViewArg.commentViews

        binding = FragmentArticleBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            articleView = articleViewArg
            isBookmarked = articleViewArg.bookmarked
            liked = isLiked()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snapHorizontal.attachToRecyclerView(binding.recyclerArticleRelated)
        setOnClickListeners()

        articleViewArg.commentViews?.let { showCommentsRecycler(it) }

        showTagsInChipGroup()

        searchTagsAndShowSuggestionsRecycler()

        sendCommentsRequest()

        updateCurrentOrOpenSuggestion(articleSlug)

    }

    private fun setOnClickListeners() {
        setOnLikeArticleClick()
        onBookmarkButtonClick()
        setOnShareArticleClick()
        setAddCommentAction()
        setOnProfileClick()
        setBackButtonClick()
    }

    private fun showTagsInChipGroup() {
        binding.chipGroupSA.removeAllViews()

        tags?.let {
            if (it.isEmpty()) {
                binding.keywordsTextSA.visibility = View.GONE
            } else {
                for (tag in it) {
                    val chip = Chip(requireContext())
                    chip.text = tag
                    chip.setOnSingleClickListener {
                        Navigation.findNavController(requireView()).navigate(
                            ArticleFragmentDirections.actionArticleFragmentToTagFragment(tag)
                        )
                    }
                    binding.chipGroupSA.addView(chip)
                }
            }
        }
    }

    private fun updateCurrentOrOpenSuggestion(slug: String) {
        articleViewModel.getSingleArticle(slug)

        articleViewModel.singleArticleResult.observe(viewLifecycleOwner, Observer { resource ->
            if (resource.status == Status.SUCCESS) {
                resource.data?.let { articleView ->

                    if (articleView.slug != articleSlug) {
                        Navigation.findNavController(requireView()).navigate(
                            ArticleFragmentDirections.actionArticleFragmentSelf(articleView)
                        )
                    } else {
                        applyArticleUpdatedData(resource, articleView)
                    }

                }
            } else if (resource.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageGetNewDataError))
            }
        })
    }

    private fun searchTagsAndShowSuggestionsRecycler() {

        tags?.remove("dragons")
        tags?.remove("training")

        tags?.forEach { tag -> articleViewModel.getTagArticles(tag) }

        articleViewModel.tagArticlesResult.observe(viewLifecycleOwner, Observer { resources ->
            if (resources.status == Status.SUCCESS) {
                resources.data?.let { articles ->
                    suggestionArticles = articles.distinct().toMutableList()
                    if (suggestionArticles.isNotEmpty()) {
                        binding.relatedArticlesText.visibility = View.VISIBLE
                    }
                    suggestedArticleAdapter =
                        SuggestedArticleAdapter(
                            suggestionArticles,
                            this
                        )

                    binding.recyclerArticleRelated.apply {
                        adapter = suggestedArticleAdapter
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            } else if (resources.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageSuggestionsArticleSearchError))
            }
            hideSuggestionsProgressBar()
        })

    }

    private fun showCommentsRecycler(comments: List<CommentView>) {
        var userNames = mutableListOf<String>()
        comments.forEach { userNames.add(it.username) }
        userNames = userNames.distinct().toMutableList()
        userNames.forEach { username -> articleViewModel.getUserArticles(username) }

        if (comments.isEmpty()) {
            hideCommentsProgressBar()
        }

        commentAdapter =
            CommentAdapter(
                comments,
                this
            )

        binding.recyclerArticleComments.apply {
            adapter = commentAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun openProfile(username: String) {
        Navigation.findNavController(requireView())
            .navigate(
                ArticleFragmentDirections.actionArticleFragmentToProfileFragment(username)
            )
    }

    private fun applyArticleUpdatedData(
        resourceArticle: Resource<ArticleView>, articleView: ArticleView
    ) {
        if (tags.isNullOrEmpty()) hideSuggestionsProgressBar()
        binding.articleView = resourceArticle.data
        articleView.commentViews?.let { showCommentsRecycler(it) }
        tags = articleView.tags?.toMutableList()
        showTagsInChipGroup()
        searchTagsAndShowSuggestionsRecycler()
    }

    private fun setAddCommentAction() {
        binding.articleCommentButton.setOnSingleClickListener {
            dialog = Dialog(requireContext(), 0)
            dialog.apply {

                setContentView(R.layout.dialog_comment)
                show()
                window?.apply {
                    attributes?.apply {
                        width = WindowManager.LayoutParams.MATCH_PARENT
                        height = WindowManager.LayoutParams.WRAP_CONTENT
                        gravity = Gravity.TOP
                        dimAmount = 0.5f
                        y = 140
                    }
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                }
                findViewById<MaterialButton>(R.id.dialogSendComment).setOnSingleClickListener {
                    sendComment(findViewById<EditText>(R.id.dialogCommentText).text.toString())
                }
            }
        }
    }

    private fun onBookmarkButtonClick() {
        binding.articleBookmark.setOnSingleClickListener {
            if (binding.isBookmarked!!) {
                articleViewModel.removeFromBookmarks(articleSlug)
            } else {
                articleViewModel.bookmarkArticle(articleSlug)
            }
        }

        articleViewModel.bookmarkResult.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                binding.isBookmarked = true
                snackMaker(requireView(), getString(R.string.messageBookmarkSuccess))
            } else if (result.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })

        articleViewModel.removeBookmark.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                binding.isBookmarked = false
                snackMaker(requireView(), getString(R.string.messageRemoveBookmarkSuccess))
            } else if (result.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })
    }

    private fun sendComment(comment: String) {
        articleViewModel.sendComment(articleSlug, comment)

        articleViewModel.sendCommentResult.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                snackMaker(requireView(), getString(R.string.messageCommentSubmitSuccess))
            } else if (it.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
            dialog.dismiss()
        })
    }

    private fun setOnProfileClick() {
        binding.authorImageSA.setOnSingleClickListener { openProfile(articleViewArg.userView.name) }
        binding.authorNameSA.setOnSingleClickListener { openProfile(articleViewArg.userView.name) }
    }

    private fun setOnLikeArticleClick() {
        binding.likeArticleButton.setOnSingleClickListener {
            articleViewModel.applyLike(articleSlug)
            binding.liked = isLiked()
        }
    }

    private fun setOnShareArticleClick() {
        binding.shareArticleSA.setOnSingleClickListener {
            articleViewModel.getSingleArticle(articleSlug)

            articleViewModel.singleArticleResult.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {

                    val textToShare = "" +
                            "${it.data?.title} \n\n" +
                            "${it.data?.body} \n\n" +
                            "${it.data?.userView?.name}"

                    shareArticle(textToShare)
                } else if (it.status == Status.ERROR) {
                    snackMaker(requireView(), getString(R.string.messageServerConnectionError))
                }
            })
        }
    }

    private fun sendCommentsRequest() {
        articleViewModel.getArticleComments(articleSlug)

        articleViewModel.articleCommentsResult.observe(viewLifecycleOwner, Observer { resource ->
            if (resource.status == Status.SUCCESS && !resource.data.isNullOrEmpty()) {
                showCommentsRecycler(resource.data)
            }
        })
    }

    private fun setBackButtonClick() {
        binding.articleRightArrow.setOnSingleClickListener { view ->
            Navigation.findNavController(view).navigateUp()
        }
    }

    private fun shareArticle(body: String?) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, body)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun isLiked() = articleSlug in articleViewModel.getLikedArticles()

    private fun hideSuggestionsProgressBar() {
        binding.progressSuggestions.visibility = View.GONE
    }

    private fun hideCommentsProgressBar() {
        binding.progressComments.visibility = View.GONE
    }

    /*      INTERFACES IMPLEMENTATION      */
    override fun onCardClick(slug: String) {
        updateCurrentOrOpenSuggestion(slug)
    }

    override fun onArticleTitleClick(slug: String) {
        updateCurrentOrOpenSuggestion(slug)
    }

    override fun onArticleSaveClick(
        slug: String, isBookmarked: Boolean, position: Int,
        item: String
    ) {
        if (isBookmarked) {
            articleViewModel.removeFromBookmarks(slug)
        } else {
            articleViewModel.bookmarkArticle(slug)
        }

        articleViewModel.bookmarkResult.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                suggestionArticles[position].bookmarked = true
                suggestedArticleAdapter.notifyItemChanged(position)
                snackMaker(requireView(), getString(R.string.messageBookmarkSuccess))
            } else if (result.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })

        articleViewModel.removeBookmark.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                suggestionArticles[position].bookmarked = false
                suggestedArticleAdapter.notifyItemChanged(position)
                snackMaker(requireView(), getString(R.string.messageRemoveBookmarkSuccess))
            } else if (result.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })
    }

    override fun onAuthorNameClick(username: String) {
        openProfile(username)
    }

    override fun onAuthorIconClick(username: String) {
        openProfile(username)
    }

    override fun onCommentIconClick(username: String) {
        openProfile(username)
    }

    override fun onCommentUsernameClick(username: String) {
        openProfile(username)
    }
}