package com.playgroundagc.deepltranslator.presentation.fragment

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.databinding.FragmentTranslationBinding
import com.playgroundagc.deepltranslator.util.selectImageCategory
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast

class TranslationFragment : Fragment() {
    val viewModel: FragmentViewModel by activityViewModels()

    companion object {
        private lateinit var binding: FragmentTranslationBinding
    }

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_translation, container, false)

        setupViews()
        setupObservers()

        return binding.root
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.api_usage -> showApiUsage()
        }
        return super.onContextItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController();
        // We use a String here, but any type that can be put in a Bundle is supported
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")?.observe(
            viewLifecycleOwner
        ) { result ->
            // Do something with the result.
            toast(result)
        }
    }
    //endregion

    //region Setup
    private fun setupViews() {
        binding.apply {
            sourceLangSpinner.apply {
                sourceSelectCardView.apply {
                    setOnClickListener {
                        val bundle = bundleOf("origin" to "source")
                        navigate(R.id.translationToLanguageSelect, bundle)
                    }
                }
                sourceFlagImg.background = null
            }

            changeBtn.apply {
                background = null
                setOnClickListener {
                    checkSwitchLang()
                }
            }

            targetLangSpinner.apply {
                targetSelectCardView.apply {
                    setOnClickListener {
                        val bundle = bundleOf("origin" to "target")
                        navigate(R.id.translationToLanguageSelect, bundle)
                    }
                }

            }

            inputLayout.apply {
                inputCopyContentBtn.background = null
                inputPasteContentBtn.background = null
            }

            outputLayout.apply {
                outputCopyContentBtn.background = null
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.uiState = state
                state.sourceLang?.selectImageCategory()
                    ?.let {
                        binding.sourceLangSpinner.sourceFlagImg.setImageResource(it)
                    }
                state.targetLang?.selectImageCategory()
                    ?.let {
                        binding.targetLangSpinner.targetFlagImg.setImageResource(it)
                    }
            }
        }
    }
    //endregion

    //region Translation
    private fun checkSwitchLang() {
        TODO("Implement language switching")
        toast("To be implemented!")
    }

    private fun showApiUsage() {
        TODO("Not yet implemented")
        toast("To be implemented!")
    }
    //endregion

    //region Navigation
    private fun navigate(destination: Int, extra: Bundle? = null) {
        Navigation
            .findNavController(this.requireView())
            .navigate(destination, extra)
    }
    //endregion
}