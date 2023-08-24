package com.example.nattramn.features.user.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nattramn.R
import com.example.nattramn.core.resource.Status
import com.example.nattramn.core.utils.Constants
import com.example.nattramn.core.utils.setOnSingleClickListener
import com.example.nattramn.core.utils.snackMaker
import com.example.nattramn.databinding.FragmentProfileBinding
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.user.data.AuthLocalDataSource
import com.example.nattramn.features.user.ui.OnBottomSheetItemsClick
import com.example.nattramn.features.user.ui.OnProfileArticleListener
import com.example.nattramn.features.user.ui.UserView
import com.example.nattramn.features.user.ui.adapters.ProfileArticleAdapter
import com.example.nattramn.features.user.ui.viewmodels.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout

class ProfileFragment : Fragment(),
    OnProfileArticleListener, OnBottomSheetItemsClick, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileArticleAdapter: ProfileArticleAdapter
    private val dialogFragment = ActionBottomDialogFragment.newInstance(this)
    private val args: ProfileFragmentArgs by navArgs()

    private lateinit var userViewDb: UserView
    private lateinit var recyclerArticlesList: MutableList<ArticleView>

    private var currentTab = Constants.TAB_USER_ARTICLES
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        username = args.username
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        userViewDb = profileViewModel.getUserDb(username)

        binding = FragmentProfileBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            user = userViewDb
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*binding.profileArticleCount.text =
            profileViewModel.getUserArticlesDb(username).size.toString()*/
        onClickListeners()

        sendProfileInfoRequest()

        showUserArticles()
    }

    private fun setTabItemsView() {
        binding.profileTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                binding.profileTab.getTabAt(0)?.customView = null
                binding.profileTab.getTabAt(1)?.customView = null
                showProgressBar()

                val tabSelectedView: View? =
                    LayoutInflater.from(context).inflate(R.layout.custom_tab_selected, null)

                if (tab?.position == 0 &&
                    binding.profileTab.getTabAt(0)?.customView != tabSelectedView
                ) {

                    currentTab = Constants.TAB_USER_ARTICLES
                    binding.profileTab.getTabAt(0)?.customView = tabSelectedView
                    showUserArticles()

                } else {
                    currentTab = Constants.TAB_USER_FAVORITE_ARTICLES
                    binding.profileTab.getTabAt(1)?.customView = tabSelectedView
                    showBookmarkedArticles()
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun showBookmarkedArticles() {

        profileViewModel.getBookmarkedArticlesDb()
        profileViewModel.userBookmarks.observe(viewLifecycleOwner, Observer {
            if (username == AuthLocalDataSource().getUsername()) {
                showRecycler(it)
            } else {
                showRecycler(listOf())
            }
        })

        profileViewModel.setBookmarkedArticles(username)
        profileViewModel.profileBookmarkedArticlesResult.observe(viewLifecycleOwner, Observer {
            binding.swipeLayout.isRefreshing = false
            if (it.status == Status.SUCCESS) {
                showRecycler(it.data)
            } else if (it.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageArticleSearchError))
            }
        })
    }

    private fun showUserArticles() {
        profileViewModel.getUserArticlesDb(username)
        profileViewModel.userArticles.observe(viewLifecycleOwner, Observer {
            showRecycler(it)
        })

        profileViewModel.getUserArticles(username)
        profileViewModel.userArticlesResult.observe(viewLifecycleOwner, Observer { resource ->
            binding.swipeLayout.isRefreshing = false
            if (resource.status == Status.SUCCESS) {
                binding.profileArticleCount.text = resource?.data?.size.toString()
                /*(resource.data?.indices)?.forEach {
                    resource.data[it].commentsNumber =
                        profileViewModel.getUserArticlesDb(username)[it].commentsNumber
                }*/
                showRecycler(resource.data)
            } else if (resource.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })
    }

    private fun showRecycler(articles: List<ArticleView>?) {

        setGifVisibility(articles)

        articles?.let { recyclerArticlesList = it.toMutableList() }

        articles?.let { articlesList ->

            profileArticleAdapter =
                ProfileArticleAdapter(
                    articlesList,
                    this
                )
        }

        binding.recyclerProfileArticles.apply {
            adapter = profileArticleAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        hideProgressBar()
    }

    private fun setGifVisibility(articles: List<ArticleView>?) {
        if (articles.isNullOrEmpty()) {
            binding.profileGif.visibility = View.VISIBLE
            binding.gifSubtitle.visibility = View.VISIBLE
        } else {
            binding.profileGif.visibility = View.GONE
            binding.gifSubtitle.visibility = View.GONE
        }
    }

    private fun sendProfileInfoRequest() {
        profileViewModel.setProfile(username)
        profileViewModel.profileResult.observe(viewLifecycleOwner, Observer { user ->
            if (user.status == Status.SUCCESS) {
                if (username == AuthLocalDataSource().getUsername()) {
                    user.data?.following = false
                }
                binding.user = user.data
            }
        })
    }

    private fun openArticle(slug: String) {
        Navigation.findNavController(requireView())
            .navigate(
                ProfileFragmentDirections.actionProfileFragmentToArticleFragment(
                    profileViewModel.getSingleArticleDb(slug)
                )
            )
    }

    private fun openProfile(username: String) {
        Navigation.findNavController(requireView())
            .navigate(
                ProfileFragmentDirections.actionProfileFragmentSelf(
                    username
                )
            )
    }

    private fun setOnFollowButtonAction() {
        if (username == AuthLocalDataSource().getUsername()) {
            binding.logoutButton.visibility = View.VISIBLE
            binding.logoutTextView.visibility = View.VISIBLE
            binding.followButton.visibility = View.GONE
        }

        binding.followButton.setOnSingleClickListener {
            binding.followButton.isClickable = false
            if (binding.followButton.text == getString(R.string.messageFollowing)) {
                profileViewModel.unFollowUser(username)
            } else {
                profileViewModel.followUser(username)
            }
        }

        profileViewModel.followUserResult.observe(viewLifecycleOwner, Observer { resource ->
            binding.followButton.isClickable = true
            if (resource.status == Status.SUCCESS) {
                binding.user = resource.data
            } else if (resource.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageFollowRequestFailedTa))
            }
        })

        profileViewModel.unFollowUserResult.observe(viewLifecycleOwner, Observer { resource ->
            binding.followButton.isClickable = true
            if (resource.status == Status.SUCCESS) {
                binding.user = resource.data
            }
            else if (resource.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageRequestFailedTa))
            }
        })
    }

    private fun setBackButtonClick() {
        binding.profileRightArrow.setOnSingleClickListener { view ->
            Navigation.findNavController(view).navigateUp()
        }
    }

    private fun setOnLogoutClick() {
        binding.logoutButton.setOnSingleClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.messageLogoutTitle))
                .setMessage(getString(R.string.messageLogoutConfirmTitle))
                .setNegativeButton(getString(R.string.messageNo)) { _, _ ->
                }
                .setPositiveButton(getString(R.string.messageYes)) { _, _ ->
                    logoutUser()
                }
                .show()
        }
    }

    private fun logoutUser() {
        binding.swipeLayout.isRefreshing = true
        profileViewModel.logout()
        AuthLocalDataSource().saveToken("")
        AuthLocalDataSource().saveUsername("")
        binding.swipeLayout.isRefreshing = false
        Navigation.findNavController(requireView())
            .navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
    }

    private fun shareArticle(body: String?) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, body)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        dialogFragment.dismiss()
        startActivity(shareIntent)
    }

    private fun onClickListeners() {
        binding.swipeLayout.setOnRefreshListener(this)
        setOnLogoutClick()
        setBackButtonClick()
        setOnFollowButtonAction()
        setTabItemsView()
    }

    private fun hideProgressBar() {
        binding.profileProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.profileProgressBar.visibility = View.VISIBLE
    }

    /*      INTERFACES IMPLEMENTATION       */
    override fun onProfileArticleCardClick(slug: String) {
        openArticle(slug)
    }

    override fun onBookmarkClick(slug: String, isBookmarked: Boolean, position: Int, item: String) {
        if (isBookmarked) {
            profileViewModel.removeFromBookmarks(slug)
        } else {
            profileViewModel.bookmarkArticle(slug)
        }

        profileViewModel.bookmarkResult.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                recyclerArticlesList[position].bookmarked = true
                profileArticleAdapter.notifyItemChanged(position)
                snackMaker(requireView(), getString(R.string.messageBookmarkSuccess))
            } else if (result.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })

        profileViewModel.removeBookmark.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                recyclerArticlesList[position].bookmarked = false
                profileArticleAdapter.notifyItemChanged(position)
                snackMaker(requireView(), getString(R.string.messageRemoveBookmarkSuccess))
            } else if (result.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })
    }

    override fun onMoreOptionsClick(slug: String, position: Int) {
        val bundle = Bundle()
        bundle.putString(Constants.ARG_SLUG, slug)
        bundle.putInt(Constants.ARG_POSITION, position)
        bundle.putString(Constants.ARG_CURRENT_TAB, currentTab)
        bundle.putBoolean(
            Constants.ARG_HAS_TOKEN,
            username == AuthLocalDataSource().getUsername()
        )
        dialogFragment.arguments = bundle

        dialogFragment.show(childFragmentManager, ActionBottomDialogFragment.TAG)
    }

    override fun onArticleTitleClick(slug: String) {
        openArticle(slug)
    }

    override fun onArticleDescriptionClick(slug: String) {
        openArticle(slug)
    }

    override fun onAuthorNameClick(username: String) {
        openProfile(username)
    }

    override fun onAuthorIconClick(username: String) {
        openProfile(username)
    }

    override fun onArticleCommentsClick(position: Int) {}

    override fun onShareArticle(slug: String) {
        profileViewModel.getSingleArticle(slug)

        profileViewModel.singleArticleResult.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {

                val textToShare = "" +
                        "${it.data?.title} \n\n" +
                        "${it.data?.body} \n\n" +
                        "${it.data?.userView?.name}"

                shareArticle(textToShare)
            } else if (it.status == Status.ERROR) {
                dialogFragment.dismiss()
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDeleteArticle(slug: String, position: Int) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.messageDelete))
            .setMessage(getString(R.string.messageDeleteArticleConfirm))
            .setNegativeButton(getString(R.string.messageNo)) { _, _ ->
            }
            .setPositiveButton(R.string.messageYes) { _, _ ->
                profileViewModel.deleteArticle(slug)
            }
            .show()

        // TODO: before edit it goes to success status? why? and if remove multiple times goes to sm
        profileViewModel.deleteArticleResult.observe(
            viewLifecycleOwner
        ) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    snackMaker(requireView(), getString(R.string.messageArticleDeleteSuccess))
                    Log.d("mamad1", "before ${recyclerArticlesList.size}, position:$position ")
                    recyclerArticlesList.remove(recyclerArticlesList.find { it.slug == slug })
                    profileArticleAdapter.notifyItemRemoved(position)
                    Log.d("mamad2", "after ${recyclerArticlesList.size} , position:$position ")
                    dialogFragment.dismiss()
                }

                Status.LOADING -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.messagePleaseWait),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.messageServerConnectionError),
                        Toast.LENGTH_SHORT
                    ).show()
                    dialogFragment.dismiss()
                }
            }
        }
    }

    override fun onEditArticle(slug: String) {
        dialogFragment.dismiss()
        Navigation.findNavController(requireView())
            .navigate(ProfileFragmentDirections.actionProfileFragmentToWriteFragment(slug))
    }

    override fun onRefresh() {
        if (currentTab == Constants.TAB_USER_ARTICLES) {
            profileViewModel.getUserArticles(username)
        } else if (currentTab == Constants.TAB_USER_FAVORITE_ARTICLES) {
            profileViewModel.setBookmarkedArticles(username)
        }
    }

}