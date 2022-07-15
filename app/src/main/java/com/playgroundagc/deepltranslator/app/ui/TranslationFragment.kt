package com.playgroundagc.deepltranslator.app.ui

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.databinding.FragmentTranslationBinding
import org.jetbrains.anko.support.v4.toast

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
            languageSelectFrom.apply {
                setOnClickListener {
                    val bundle = bundleOf("origin" to "fromBtn")
                    navigate(R.id.translationToLanguageSelect, bundle)
                }
            }

            languageSelectTo.apply {
                setOnClickListener {
                    val bundle = bundleOf("origin" to "toBtn")
                    navigate(R.id.translationToLanguageSelect, bundle)
                }
            }

            fromTranslationBtn.background = null
            toTranslationBtn.background = null
            copyContentBtn.background = null
            copyContentButtonTranslated.background = null
            changeBtn.background = null
            pasteContentBtn.background = null
        }

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

    private fun showApiUsage() {
        TODO("Not yet implemented")
        toast("To be implemented!")
    }

    private fun navigate(origin: String = "") {
        //val action = LanguageSelectFragment
        Navigation.findNavController(binding.fromTranslationBtn)
            .navigate(R.id.translationToLanguageSelect)
    }


    //region Navigation
    private fun navigate(destination: Int, extra: Bundle? = null) {
        Navigation
            .findNavController(this.requireView())
            .navigate(destination, extra)
    }
    //endregion
}