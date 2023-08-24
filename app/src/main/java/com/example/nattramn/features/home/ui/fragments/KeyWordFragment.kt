package com.example.nattramn.features.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nattramn.R
import com.example.nattramn.core.resource.Status
import com.example.nattramn.core.utils.snackMaker
import com.example.nattramn.databinding.FragmentKeyWordsBinding
import com.example.nattramn.features.home.ui.OnTagsItemListener
import com.example.nattramn.features.home.ui.TagAdapter
import com.example.nattramn.features.home.ui.viewmodels.KeyWordsViewModel

class KeyWordFragment : Fragment(), OnTagsItemListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentKeyWordsBinding
    private lateinit var keyWordsViewModel: KeyWordsViewModel
    private lateinit var tagAdapter: TagAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        keyWordsViewModel = ViewModelProvider(this).get(KeyWordsViewModel::class.java)

        binding = FragmentKeyWordsBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeLayout.setOnRefreshListener(this)

        showAllTagsRecycler(keyWordsViewModel.getAllTagsDb().toMutableList())
        sendAllTagsRequest()
        observeAllTagsResponse()

    }

    private fun observeAllTagsResponse() {
        keyWordsViewModel.allTagsResult.observe(viewLifecycleOwner, Observer { resource ->
            binding.swipeLayout.isRefreshing = false
            if (resource.status == Status.SUCCESS) {
                resource.data?.tags?.let { tags ->
                    showAllTagsRecycler(tags.toMutableList())
                }
            } else if (resource.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
                binding.progressAllTags.visibility = View.GONE
            }
        })
    }

    private fun sendAllTagsRequest() {
        keyWordsViewModel.getAllTags()
    }

    private fun showAllTagsRecycler(tags: MutableList<String>) {

        setGifVisibility(tags)

        tags.remove("dragons")
        tags.remove("training")

        tagAdapter = TagAdapter(
            tags,
            this
        )
        binding.recyclerAllTags.apply {
            adapter = tagAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
        binding.progressAllTags.visibility = View.GONE
    }

    private fun setGifVisibility(tags: List<String>) {
        if (tags.isNullOrEmpty()) {
            binding.keyWordsGif.visibility = View.VISIBLE
            binding.gifSubtitle.visibility = View.VISIBLE
        } else {
            binding.keyWordsGif.visibility = View.GONE
            binding.gifSubtitle.visibility = View.GONE
        }
    }

    override fun onTagClick(tag: String) {
        Navigation.findNavController(requireView())
            .navigate(HomeFragmentDirections.actionHomeFragmentToTagFragment(tag))
    }

    override fun onRefresh() {
        sendAllTagsRequest()
    }

}