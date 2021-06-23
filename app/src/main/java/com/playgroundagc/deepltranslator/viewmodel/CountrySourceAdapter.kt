package com.playgroundagc.deepltranslator.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.model.SourceLang

/**
 * Created by Amadou on 23/06/2021, 16:46
 *
 * TODO: File Description
 *
 */


class CountrySourceAdapter(context: Context, var countryList: Array<SourceLang>) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val item: ItemHolder
        val country = countryList[position]
        if (convertView == null) {
            view = inflater.inflate(R.layout.simple_layout_file, parent, false)
            item = ItemHolder(view)
            view?.tag = item
        } else {
            view = convertView
            item = view.tag as ItemHolder
        }

        item.countryName.text = country.language

        item.countryImage.setImageResource(R.drawable.ic_account)

//        item.clubAddress.text = country.address

//        try {
//            GlideApp.with(view).apply {
//                load(AffiliationRequestService.getImageReference(club.logo))
//                    .placeholder(R.drawable.ic_account)
//                    .centerCrop()
//                    .into(item.clubImage)
//            }
//        } catch (e: Exception) {
//            Log.e("ClubDropDownAdapter", "Image could not load: $e")
//        }

        return view

    }

    override fun getItem(position: Int): Any {
        return countryList[position]
    }

    override fun getCount(): Int {
        return countryList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private class ItemHolder(row: View?) {
        val countryName: TextView = row?.findViewById(R.id.country_text) as TextView
        val countryImage: ImageView = row?.findViewById(R.id.country_image) as ImageView
    }
}