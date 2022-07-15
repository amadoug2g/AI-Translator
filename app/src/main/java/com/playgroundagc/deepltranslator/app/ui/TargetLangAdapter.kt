package com.playgroundagc.deepltranslator.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playgroundagc.deepltranslator.databinding.SimpleLayoutFileBinding
import com.playgroundagc.deepltranslator.domain.SourceLang
import com.playgroundagc.deepltranslator.domain.TargetLang
import com.playgroundagc.deepltranslator.util.selectImageCategory

/**
 * Created by Amadou on 06/07/2022, 13:48
 *
 * Purpose:
 *
 */

class TargetLangAdapter(private val targetLangList: Array<TargetLang>): RecyclerView.Adapter<TargetLangAdapter.TargetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetViewHolder {
        return TargetViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TargetViewHolder, position: Int) {
        val currentItem = targetLangList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = targetLangList.size

    class TargetViewHolder(val binding: SimpleLayoutFileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(targetLang: TargetLang) {
            with(binding) {
                this.countryImage.setImageResource(targetLang.selectImageCategory())
                this.countryText.text = targetLang.language
            }
        }

        companion object {
            fun from(parent: ViewGroup): TargetViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    SimpleLayoutFileBinding.inflate(layoutInflater, parent, false)
                return TargetViewHolder(binding)
            }
        }
    }
}