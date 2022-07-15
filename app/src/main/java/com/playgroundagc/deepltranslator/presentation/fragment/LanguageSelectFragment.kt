package com.playgroundagc.deepltranslator.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.databinding.FragmentLanguageSelectBinding
import com.playgroundagc.deepltranslator.domain.SourceLang
import com.playgroundagc.deepltranslator.domain.TargetLang
import com.playgroundagc.deepltranslator.presentation.adapter.SourceLangAdapter
import com.playgroundagc.deepltranslator.presentation.adapter.TargetLangAdapter

private const val ARG_PARAM1 = "origin"

class LanguageSelectFragment : Fragment() {

    private lateinit var binding: FragmentLanguageSelectBinding
    private lateinit var sourceLangAdapter: SourceLangAdapter
    private var origin: String? = null
    private lateinit var targetLangAdapter: TargetLangAdapter

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_language_select, container, false)

        binding.recentLangTxt.visibility = View.GONE
        binding.recentLanguageRecycler.visibility = View.GONE

        when (origin) {
            "source" -> setupRecyclerSource()
            else -> setupRecyclerTarget()
        }

        return binding.root
    }

    //region RecyclerView
    private fun setupRecyclerTarget() {
        val targetLangList = TargetLang.values()
        targetLangAdapter = TargetLangAdapter(targetLangList)
        binding.allLanguageRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.allLanguageRecycler.adapter = targetLangAdapter
    }

    private fun setupRecyclerSource() {
        val sourceLangList = SourceLang.values()
        sourceLangAdapter = SourceLangAdapter(sourceLangList)
        binding.allLanguageRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.allLanguageRecycler.adapter = sourceLangAdapter
    }
    //endregion
}