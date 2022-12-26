package com.playgroundagc.deepltranslator.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.playgroundagc.core.data.SourceLang
import com.playgroundagc.core.data.TargetLang
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.databinding.FragmentLanguageSelectBinding
import com.playgroundagc.deepltranslator.presentation.adapter.LanguageSelect
import com.playgroundagc.deepltranslator.presentation.adapter.SourceSelectAdapter
import com.playgroundagc.deepltranslator.presentation.adapter.TargetSelectAdapter
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "origin"

class LanguageSelectFragment : Fragment(), LanguageSelect {

    //region Variables
    //private val args by navArgs<TranslationFragmentArgs>()
    val viewModel: FragmentViewModel by activityViewModels()

    private lateinit var binding: FragmentLanguageSelectBinding
    private lateinit var sourceSelectAdapter: SourceSelectAdapter
    private lateinit var targetSelectAdapter: TargetSelectAdapter
    private var origin: String? = null
    //endregion

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            origin = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_language_select, container, false)

        when (origin) {
            "source" -> setupRecyclerSource()
            else -> setupRecyclerTarget()
        }

        setupViews()

        // navController.previousBackStackEntry?.savedStateHandle?.set("key", result)

        binding

        return binding.root
    }

    override fun onClick(sourceLang: SourceLang) {
        viewModel.setSourceLanguage(sourceLang)
        requireActivity().onBackPressed()
    }

    override fun onClick(targetLang: TargetLang) {
        viewModel.setTargetLanguage(targetLang)
        requireActivity().onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.languageSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                when (origin) {
                    "source" -> {
                        if (filterSource(newText) != 0) {
                            binding.emptyCardView.visibility = View.GONE
                            binding.allLanguageRecycler.visibility = View.VISIBLE
                        } else {
                            binding.emptyCardView.visibility = View.VISIBLE
                            binding.allLanguageRecycler.visibility = View.GONE
                        }
                    }
                    else -> {
                        if (filterTarget(newText) != 0) {
                            binding.emptyCardView.visibility = View.GONE
                            binding.allLanguageRecycler.visibility = View.VISIBLE
                        } else {
                            binding.emptyCardView.visibility = View.VISIBLE
                            binding.allLanguageRecycler.visibility = View.GONE
                        }
                    }
                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }
    //endregion

    //region Setups
    private fun setupViews() {
        binding.apply {
            recentLangTxt.visibility = View.GONE
            recentLanguageRecycler.visibility = View.GONE

            backArrow.apply {
                background = null
                setOnClickListener {
                    requireActivity().onBackPressed()
                }
            }

            languageSearchView.queryHint = when (origin) {
                "source" -> "Search source language"
                else -> "Search target language"
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                binding.allLanguageRecycler
            }
        }
    }
    //endregion

    //region RecyclerView
    private fun setupRecyclerTarget() {
        val targetLangList = TargetLang.values().toMutableList()
        targetSelectAdapter = TargetSelectAdapter(targetLangList, this)
        binding.allLanguageRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.allLanguageRecycler.adapter = targetSelectAdapter
    }

    private fun setupRecyclerSource() {
        val sourceLangList = SourceLang.values().toMutableList()
        sourceSelectAdapter = SourceSelectAdapter(sourceLangList, this, viewModel.uiState.value)
        binding.allLanguageRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.allLanguageRecycler.adapter = sourceSelectAdapter
    }

    private fun filterSource(text: String): Int {
        val filteredList = mutableListOf<SourceLang>()

        for (item in SourceLang.values()) {
            if (item.language.lowercase().contains(text.lowercase())
            ) {
                filteredList.add(item)
            }
        }

        if (filteredList.isEmpty()) {
            sourceSelectAdapter.filterList(mutableListOf())
        } else {
            sourceSelectAdapter.filterList(filteredList)
        }

        return filteredList.size
    }

    private fun filterTarget(text: String): Int {
        val filteredList = mutableListOf<TargetLang>()

        for (item in TargetLang.values()) {
            if (item.language.lowercase().contains(text.lowercase())
            ) {
                filteredList.add(item)
            }
        }

        if (filteredList.isEmpty()) {
            targetSelectAdapter.filterList(mutableListOf())
        } else {
            targetSelectAdapter.filterList(filteredList)
        }

        return filteredList.size
    }
    //endregion
}