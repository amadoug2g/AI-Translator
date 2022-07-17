package com.playgroundagc.deepltranslator.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.databinding.FragmentLanguageSelectBinding
import com.playgroundagc.deepltranslator.presentation.adapter.LanguageSelect
import com.playgroundagc.deepltranslator.presentation.adapter.SourceSelectAdapter
import com.playgroundagc.deepltranslator.presentation.adapter.TargetSelectAdapter
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "origin"

class LanguageSelectFragment : Fragment(), LanguageSelect {
    //private val args by navArgs<TranslationFragmentArgs>()
    val viewModel: FragmentViewModel by activityViewModels()

    private lateinit var binding: FragmentLanguageSelectBinding
    private lateinit var sourceSelectAdapter: SourceSelectAdapter
    private lateinit var targetSelectAdapter: TargetSelectAdapter
    private var origin: String? = null

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

        binding.recentLangTxt.visibility = View.GONE
        binding.recentLanguageRecycler.visibility = View.GONE

        when (origin) {
            "source" -> setupRecyclerSource()
            else -> setupRecyclerTarget()
        }

        // navController.previousBackStackEntry?.savedStateHandle?.set("key", result)

        binding

        return binding.root
    }

    override fun onClick(sourceLang: com.playgroundagc.core.data.SourceLang) {
        viewModel.setSourceLanguage(sourceLang)
        requireActivity().onBackPressed()
    }

    override fun onClick(targetLang: com.playgroundagc.core.data.TargetLang) {
        viewModel.setTargetLanguage(targetLang)
        requireActivity().onBackPressed()
    }
    //endregion

    //region Setups
    fun setupViews() {

    }

    fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                binding.allLanguageRecycler
            }
        }
    }
    //endregion

    //region RecyclerView
    private fun setupRecyclerTarget() {
        val targetLangList = com.playgroundagc.core.data.TargetLang.values()
        targetSelectAdapter = TargetSelectAdapter(targetLangList, this)
        binding.allLanguageRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.allLanguageRecycler.adapter = targetSelectAdapter
    }

    private fun setupRecyclerSource() {
        val sourceLangList = com.playgroundagc.core.data.SourceLang.values()
        sourceSelectAdapter = SourceSelectAdapter(sourceLangList, this)
        binding.allLanguageRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.allLanguageRecycler.adapter = sourceSelectAdapter
    }
    //endregion
}