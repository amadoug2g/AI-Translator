package com.playgroundagc.deepltranslator.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.databinding.FragmentTranslationBinding
import com.playgroundagc.deepltranslator.util.selectImageCategory
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast

class TranslationFragment : Fragment() {

    //region Variables
    val viewModel: FragmentViewModel by activityViewModels()

    companion object {
        private lateinit var binding: FragmentTranslationBinding
    }
    //endregion

    //region Override Methods
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_translation, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupViews()
        setupObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.api_usage -> {
                        showApiUsage()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val navController = findNavController()

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
                inputTranslateBtn.apply {
                    background = null
                    setOnClickListener {
                        translate()
                    }
                }

                inputEdittext.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        viewModel?.setInputText(p0.toString())
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })
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
                state.sourceLang.selectImageCategory()
                    .let {
                        binding.sourceLangSpinner.sourceFlagImg.setImageResource(it)
                    }
                state.targetLang.selectImageCategory()
                    .let {
                        binding.targetLangSpinner.targetFlagImg.setImageResource(it)
                    }

                binding.progressBar.visibility = if (state.isFetchingTranslation) View.VISIBLE else View.INVISIBLE
            }
        }
    }
    //endregion

    //region Translation
    private fun translate() {
        lifecycleScope.launch {
            viewModel.translation()
        }
    }

    private fun checkSwitchLang() {
        //TODO("Implement language switching")
        toast("To be implemented!")
    }

    private fun showApiUsage() {
        viewModel.getUsage()
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