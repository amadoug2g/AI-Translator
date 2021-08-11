package com.playgroundagc.deepltranslator.extension

import android.view.View

/**
 * Created by Amadou on 07/08/2021, 02:37
 *
 * View Extension Class
 *
 */

fun View.setVisible(state: Boolean) {
    when (state) {
        true -> {
            this.visibility = View.VISIBLE
        }
        false -> {
            this.visibility = View.GONE
        }
    }
}