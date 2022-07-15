package com.playgroundagc.deepltranslator.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playgroundagc.deepltranslator.databinding.SimpleLayoutFileBinding
import com.playgroundagc.deepltranslator.domain.SourceLang
import com.playgroundagc.deepltranslator.util.selectImageCategory

/**
 * Created by Amadou on 06/07/2022, 13:29
 *
 * Purpose:
 *
 */

class SourceLangAdapter(private val sourceLangList: Array<SourceLang>) :
    RecyclerView.Adapter<SourceLangAdapter.SourceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val currentItem = sourceLangList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = sourceLangList.size

    class SourceViewHolder(val binding: SimpleLayoutFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sourceLang: SourceLang) {
            with(binding) {
                this.apply {
                    countryImage.setImageResource(sourceLang.selectImageCategory())
                    countryText.text = sourceLang.language
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): SourceViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    SimpleLayoutFileBinding.inflate(layoutInflater, parent, false)
                return SourceViewHolder(binding)
            }
        }
    }
}