package com.example.nattramn.features.user.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nattramn.core.utils.Constants
import com.example.nattramn.core.utils.setOnSingleClickListener
import com.example.nattramn.databinding.ActionBottomSheetBinding
import com.example.nattramn.features.user.ui.OnBottomSheetItemsClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ActionBottomDialogFragment(val listener: OnBottomSheetItemsClick) :
    BottomSheetDialogFragment() {

    private lateinit var binding: ActionBottomSheetBinding

    private lateinit var slug: String
    private lateinit var currentTab: String
    private var position = 0

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(listener: OnBottomSheetItemsClick): ActionBottomDialogFragment {
            return ActionBottomDialogFragment(listener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slug = arguments?.getString(Constants.ARG_SLUG)!!
        position = arguments?.getInt(Constants.ARG_POSITION)!!
        currentTab = arguments?.getString(Constants.ARG_CURRENT_TAB)!!

        binding = ActionBottomSheetBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        setDeleteAndEditOptionsVisibility()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onEditArticleClick()

        onDeleteArticleClick()

        onShareArticleClick()

    }

    private fun onShareArticleClick() {
        binding.shareArticleButton.setOnSingleClickListener {
            listener.onShareArticle(slug)
        }
    }

    private fun onDeleteArticleClick() {
        binding.deleteArticleButton.setOnSingleClickListener {
            listener.onDeleteArticle(slug, position)
        }
    }

    private fun onEditArticleClick() {
        binding.editArticleButton.setOnSingleClickListener {
            listener.onEditArticle(slug)
        }
    }

    private fun setDeleteAndEditOptionsVisibility() {
        if (currentTab == Constants.TAB_USER_FAVORITE_ARTICLES || !requireArguments().getBoolean(
                Constants.ARG_HAS_TOKEN
            )
        ) {
            binding.deleteArticleButton.visibility = View.GONE
            binding.editArticleButton.visibility = View.GONE
        }
    }

}