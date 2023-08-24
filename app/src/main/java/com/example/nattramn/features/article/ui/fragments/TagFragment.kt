package com.example.nattramn.features.article.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nattramn.R
import com.example.nattramn.core.commonAdapters.VerticalArticleAdapter
import com.example.nattramn.core.resource.Status
import com.example.nattramn.core.utils.setOnSingleClickListener
import com.example.nattramn.core.utils.snackMaker
import com.example.nattramn.databinding.FragmentTagBinding
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.article.ui.OnArticleListener
import com.example.nattramn.features.article.ui.viewmodels.TagViewModel

class TagFragment : Fragment(), OnArticleListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentTagBinding
    private lateinit var tagViewModel: TagViewModel
    private lateinit var tagAdapter: VerticalArticleAdapter
    private val args: TagFragmentArgs by navArgs()
    private lateinit var tagArg: String

    private lateinit var tagArticles: MutableList<ArticleView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tagArg = args.tag

        binding = FragmentTagBinding.inflate(
            inflater, container, false
        ).apply {
            tagToolbarTitle.text = tagArg
        }

        binding.lifecycleOwner = viewLifecycleOwner
        tagViewModel = ViewModelProvider(this).get(TagViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeLayout.setOnRefreshListener(this)
        setBackButtonClick()

        setRecyclers()
    }

    private fun setRecyclers() {
        initRecyclerWithDatabase()
        sendTagArticlesRequest()
        observeNetResponse()
    }

    private fun initRecyclerWithDatabase() {
        showArticlesRecycler(tagViewModel.getTagArticlesDb(tagArg))
        tagArticles = tagViewModel.getTagArticlesDb(tagArg)
    }

    private fun sendTagArticlesRequest() {
        tagViewModel.getTagArticles(tagArg)
    }

    private fun observeNetResponse() {
        tagViewModel.tagArticlesResult.observe(viewLifecycleOwner, Observer { resource ->
            binding.swipeLayout.isRefreshing = false
            if (resource.status == Status.SUCCESS) {
                resource.data?.let { articles ->
                    tagArticles = articles.toMutableList()
                    showArticlesRecycler(articles)
                }
            } else if (resource.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
            hideProgressBar()
        })
    }

    private fun showArticlesRecycler(articles: List<ArticleView>) {

        setGifVisibility(articles)

        tagAdapter = VerticalArticleAdapter(
            articles,
            this
        )

        binding.recyclerTagArticles.apply {
            adapter = tagAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setGifVisibility(articles: List<ArticleView>) {
        if (articles.isNullOrEmpty()) {
            binding.tagGif.visibility = View.VISIBLE
            binding.gifSubtitle.visibility = View.VISIBLE
        } else {
            binding.tagGif.visibility = View.GONE
            binding.gifSubtitle.visibility = View.GONE
        }
    }

    private fun setBackButtonClick() {
        binding.tagRightArrow.setOnSingleClickListener { view ->
            Navigation.findNavController(view).navigateUp()
        }
    }

    private fun openProfile(username: String) {
        Navigation.findNavController(requireView())
            .navigate(
                TagFragmentDirections.actionTagFragmentToProfileFragment(username)
            )
    }

    private fun openArticle(slug: String) {

        tagViewModel.getSingleArticle(slug)

        tagViewModel.singleArticleResult.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                Navigation.findNavController(requireView())
                    .navigate(
                        TagFragmentDirections.actionTagFragmentToArticleFragment(
                            tagViewModel.getSingleArticleDb(slug)
                        )
                    )
            } else if (it.status == Status.LOADING) {
                snackMaker(requireView(), getString(R.string.messagePleaseWait))
            } else if (it.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })
    }

    private fun hideProgressBar() {
        binding.progressTag.visibility = View.GONE
    }

    /*      INTERFACES IMPLEMENTATION       */
    override fun onCardClick(slug: String) {
        openArticle(slug)
    }

    override fun onArticleTitleClick(slug: String) {
        openArticle(slug)
    }

    override fun onArticleSaveClick(
        slug: String, isBookmarked: Boolean, position: Int,
        item: String
    ) {

        if (isBookmarked) {
            tagViewModel.removeFromBookmarks(slug)
        } else {
            tagViewModel.bookmarkArticle(slug)
        }

        tagViewModel.bookmarkResult.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                tagArticles[position].bookmarked = true
                tagAdapter.notifyItemChanged(position)
                snackMaker(requireView(), getString(R.string.messageBookmarkSuccess))
            } else if (result.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })

        tagViewModel.removeBookmark.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                tagArticles[position].bookmarked = false
                tagAdapter.notifyItemChanged(position)
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

    override fun onRefresh() {
        sendTagArticlesRequest()
    }
}