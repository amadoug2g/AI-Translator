package com.playgroundagc.deepltranslator.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playgroundagc.deepltranslator.databinding.SimpleLayoutFileBinding
// import com.playgroundagc.deepltranslator.domain.SourceLang
import com.playgroundagc.core.data.SourceLang
import com.playgroundagc.deepltranslator.util.selectImageCategory
import timber.log.Timber

/**
 * Created by Amadou on 06/07/2022, 13:29
 *
 * Purpose:
 *
 */

class SourceSelectAdapter(
    private val sourceLangList: Array<SourceLang>,
    val action: LanguageSelect
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
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = sourceLangList.size

    inner class SourceViewHolder(val binding: SimpleLayoutFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sourceLang: SourceLang) {
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

        fun from(parent: ViewGroup): SourceViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                SimpleLayoutFileBinding.inflate(layoutInflater, parent, false)
            return SourceViewHolder(binding)
        }

    }
}