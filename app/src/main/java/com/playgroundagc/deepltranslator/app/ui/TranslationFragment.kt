package com.playgroundagc.deepltranslator.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.databinding.FragmentTranslationBinding

class TranslationFragment : Fragment() {

    companion object {
        private lateinit var binding: FragmentTranslationBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_translation, container, false)

        binding.apply {
            fromTranslationBtn.apply {
                background = null
                setOnClickListener {
                    val bundle = bundleOf("origin" to "fromBtn")
                    navigate(R.id.translationFragmentToLanguageSelectFragment, bundle)
                }
            }

            toTranslationBtn.apply {
                setOnClickListener {
                    val bundle = bundleOf("origin" to "toBtn")
                    navigate(R.id.translationFragmentToLanguageSelectFragment, bundle)
                }
            }

            toTranslationBtn.background = null
            copyContentBtn.background = null
            copyContentButtonTranslated.background = null
            changeBtn.background = null
            pasteContentBtn.background = null
        }

        return binding.root
    }

    private fun navigate(origin: String = "") {
        //val action = LanguageSelectFragment
        Navigation.findNavController(binding.fromTranslationBtn)
            .navigate(R.id.translationFragmentToLanguageSelectFragment)
    }


    //region Navigation
    private fun navigate(destination: Int, extra: Bundle? = null) {
        Navigation
            .findNavController(this.requireView())
            .navigate(destination, extra)
    }
    //endregion
}