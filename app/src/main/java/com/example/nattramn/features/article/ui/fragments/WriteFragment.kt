package com.example.nattramn.features.article.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.nattramn.R
import com.example.nattramn.core.resource.Status
import com.example.nattramn.core.utils.setOnSingleClickListener
import com.example.nattramn.core.utils.snackMaker
import com.example.nattramn.databinding.FragmentWriteBinding
import com.example.nattramn.features.article.ui.viewmodels.WriteViewModel
import com.google.android.material.chip.Chip

@Suppress("DEPRECATION")
class WriteFragment : Fragment() {

    private lateinit var binding: FragmentWriteBinding
    private lateinit var writeViewModel: WriteViewModel
    private val args: WriteFragmentArgs by navArgs()

    private var slug: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        writeViewModel = ViewModelProvider(this).get(WriteViewModel::class.java)
        slug = args.slug

        binding = FragmentWriteBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnDraftClick()
        setBackButtonClick()

        onPublishClick()

        loadArticleToEdit()

        onCreateTag()

    }

    private fun onPublishClick() {
        binding.publishButton.setOnSingleClickListener {
            binding.writeProgress.visibility = View.VISIBLE
            if (textsAreLongEnough()) {
                if (slug != null) {
                    writeViewModel.editArticle(binding.articleBody.text.toString(), slug!!)
                } else {
                    val tags = getAllChipsFromChipGroup()

                    writeViewModel.createArticle(
                        body = binding.articleBody.text.toString(),
                        title = binding.articleTitle.text.toString(),
                        tags = tags
                    )
                }
            }
        }

        observeNetResponses()

    }

    private fun observeNetResponses() {
        writeViewModel.editArticleResult.observe(viewLifecycleOwner, Observer { resource ->
            binding.writeProgress.visibility = View.GONE

            if (resource.status == Status.SUCCESS) {
                snackMaker(requireView(), getString(R.string.messageArticleEditSuccess))
            } else if (resource.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })

        writeViewModel.createArticleResult.observe(viewLifecycleOwner, Observer { resource ->
            binding.writeProgress.visibility = View.GONE

            if (resource.status == Status.SUCCESS) {
                /*AuthLocalDataSource().getUsername()?.let { username ->
                    Navigation.findNavController(requireView())
                        .navigate(
                            WriteFragmentDirections.actionWriteFragmentToProfileFragment(
                                username
                            )
                        )
                }*/
                snackMaker(requireView(), getString(R.string.messagePublishArticleSuccess))
            } else if (resource.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageServerConnectionError))
            }
        })
    }

    private fun getAllChipsFromChipGroup(): MutableList<String> {
        val tags: MutableList<String> = mutableListOf()

        (0 until binding.writeChipGroup.childCount).forEach {
            val chip = binding.writeChipGroup.getChildAt(it) as Chip
            tags.add(chip.text.toString())
        }
        return tags
    }

    private fun addChip(chipText: String) {
        val chip = Chip(requireContext())
        chip.text = chipText
        chip.isCloseIconEnabled = true
        chip.layoutDirection = View.LAYOUT_DIRECTION_LTR
        chip.setOnCloseIconClickListener {
            binding.writeChipGroup.removeView(chip)
        }
        binding.writeChipGroup.addView(chip)
    }

    private fun loadArticleToEdit() {
        slug?.let {
            writeViewModel.getSingleArticle(it)
        }

        val titleDraft = writeViewModel.getTitleDraft()
        val bodyDraft = writeViewModel.getBodyDraft()

        if (!titleDraft.isNullOrEmpty() && !bodyDraft.isNullOrEmpty()) {
            binding.articleTitle.setText(titleDraft)
            binding.articleBody.setText(bodyDraft)
        }

        writeViewModel.singleArticleResult.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                binding.articleTitle.setText(it.data?.title)
                binding.articleBody.setText(it.data?.body)

                it.data?.tags?.let { tags ->
                    (tags.indices).forEach { index ->
                        addChip(tags[index])
                    }
                }

                changeViewsWhenEditing()
            } else if (it.status == Status.ERROR) {
                snackMaker(requireView(), getString(R.string.messageGetArticleFailed))
            }
        })
    }

    private fun changeViewsWhenEditing() {
        binding.publishButton.text = getString(R.string.messageApplyChanges)
        binding.articleTitle.isEnabled = false
        binding.chipGroupScroll.isEnabled = false
        binding.writeChipGroup.visibility = View.GONE
        binding.tagsEditText.visibility = View.GONE
        binding.keyWordsLayout.visibility = View.GONE
        binding.keyWordsTitle.visibility = View.GONE
    }

    private fun onCreateTag() {
        binding.tagsEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var tag = p0.toString()

                if (tag.length > 1 && tag[tag.lastIndex - 1] == ' ') {
                    tag = ""
                }

                if (tag.isNotEmpty() && tag.last() != ' ') {
                    tag = tag.substringAfterLast(' ', tag)
                }

                if (tag.isNotEmpty() && tag.last() == ' ' && tag != " " && tag != "\n") {
                    addChip(tag.trim())
                    binding.tagsEditText.setText("")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun textsAreLongEnough(): Boolean {
        val titleLength = binding.articleTitle.text?.length
        val bodyLength = binding.articleBody.text?.length

        titleLength?.let { titleLen ->
            bodyLength?.let { bodyLen ->
                if (titleLen > 14 && bodyLen > 29) {
                    return true
                }
                if (titleLen < 15) {
                    binding.articleTitle.requestFocus()
                    binding.articleTitle.error = getString(R.string.messageArticleTitleLimit)
                    binding.writeProgress.visibility = View.GONE
                    return false
                }
                if (bodyLen < 30) {
                    binding.articleBody.requestFocus()
                    binding.articleBody.error = getString(R.string.messageArticleBodyLimit)
                    binding.writeProgress.visibility = View.GONE
                    return false
                }
            }
        }
        return false
    }

    private fun setOnDraftClick() {
        binding.writeDraftToolbar.setOnSingleClickListener {
            val body = binding.articleBody.text.toString()
            val title = binding.articleTitle.text.toString()
            if (body.length > 29 && title.length > 14) {
                writeViewModel.saveDraft(title, body)
                snackMaker(requireView(), getString(R.string.messageSaveDraftSuccess))
                Navigation.findNavController(requireView()).navigate(
                    WriteFragmentDirections.actionWriteFragmentToHomeFragment()
                )
            } else {
                snackMaker(requireView(), getString(R.string.messageSaveDraftFailed))
            }
        }
    }

    private fun setBackButtonClick() {
        binding.writeRightArrow.setOnSingleClickListener { view ->
            Navigation.findNavController(view).navigateUp()
        }
    }

}