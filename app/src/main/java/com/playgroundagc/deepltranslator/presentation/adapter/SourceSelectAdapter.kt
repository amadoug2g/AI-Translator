package com.playgroundagc.deepltranslator.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playgroundagc.core.data.SourceLang
import com.playgroundagc.core.data.TranslationUiState
import com.playgroundagc.deepltranslator.databinding.SimpleLayoutFileBinding
import com.playgroundagc.deepltranslator.util.selectImageCategory
import timber.log.Timber

/**
 * Created by Amadou on 06/07/2022, 13:29
 *
 * Purpose:
 *
 */

class SourceSelectAdapter(
    private var sourceLangList: MutableList<SourceLang>,
    val action: LanguageSelect,
    val uiState: TranslationUiState,
) :
    RecyclerView.Adapter<SourceSelectAdapter.SourceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            SimpleLayoutFileBinding.inflate(layoutInflater, parent, false)
        return SourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val currentItem = sourceLangList[position]
        if (uiState.sourceLang.language == "Any language" && currentItem.name == uiState.targetLang.name) {
            holder.bind(currentItem, true)
            holder.itemView.layoutParams.height = 0
        } else {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int = sourceLangList.size

    fun filterList(filteredList: MutableList<SourceLang>) {
        if ( filteredList.isNullOrEmpty()) {

        } else {
        sourceLangList = filteredList
        }
        notifyDataSetChanged()
    }

    inner class SourceViewHolder(val binding: SimpleLayoutFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sourceLang: SourceLang, hide: Boolean = false) {

            if (hide) {
                binding.root.visibility = View.GONE
            } else {
                with(binding) {
                    apply {
                        languageFlag.setImageResource(sourceLang.selectImageCategory())
                        languageName.text = sourceLang.language
                        languageCardView.setOnClickListener {
                            action.onClick(sourceLang)
                            Timber.d("Lang is: ${sourceLang.language}")
                        }
                    }
                }
            }
        }

        fun from(parent: ViewGroup): SourceViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                SimpleLayoutFileBinding.inflate(layoutInflater, parent, false)
            return SourceViewHolder(binding)
        }

    }
}