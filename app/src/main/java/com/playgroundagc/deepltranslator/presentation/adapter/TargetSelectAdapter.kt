package com.playgroundagc.deepltranslator.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playgroundagc.core.data.SourceLang
import com.playgroundagc.deepltranslator.databinding.SimpleLayoutFileBinding
import com.playgroundagc.core.data.TargetLang
import com.playgroundagc.core.data.TranslationUiState
import com.playgroundagc.deepltranslator.util.selectImageCategory
import timber.log.Timber

/**
 * Created by Amadou on 06/07/2022, 13:48
 *
 * Purpose:
 *
 */

class TargetSelectAdapter(
    private var targetLangList: MutableList<TargetLang>,
    val action: LanguageSelect,
) : RecyclerView.Adapter<TargetSelectAdapter.TargetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            SimpleLayoutFileBinding.inflate(layoutInflater, parent, false)
        return TargetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TargetViewHolder, position: Int) {
        val currentItem = targetLangList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = targetLangList.size

    fun filterList(filteredList: MutableList<TargetLang>) {
        targetLangList = filteredList
        notifyDataSetChanged()
    }

    inner class TargetViewHolder(val binding: SimpleLayoutFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(targetLang: TargetLang) {
            with(binding) {
                languageFlag.setImageResource(targetLang.selectImageCategory())
                languageName.text = targetLang.language
                languageCardView.setOnClickListener {
                    action.onClick(targetLang)
                    Timber.d("Lang is: ${targetLang.language}")
                }
            }
        }

        fun from(parent: ViewGroup): TargetViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                SimpleLayoutFileBinding.inflate(layoutInflater, parent, false)
            return TargetViewHolder(binding)
        }
    }
}