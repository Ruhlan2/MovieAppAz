package com.isteam.movieappaz.common.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.isteam.movieappaz.common.utils.Constants.BASE_URL_IMAGE

object BindingAdapters {

    @BindingAdapter("load_image")
    @JvmStatic
    fun loadImageUrl(imageView: ImageView, url: String?) {
        url?.let {
            imageView.loadImageUrl(it)
        }
    }


    @BindingAdapter("load_image__with_low_quality")
    @JvmStatic
    fun loadImageUrlWithLowQuality(imageView: ImageView, url: String?) {
        url?.let {
            imageView.loadImageUrl(it,300)
        }
    }

    @BindingAdapter("load_tmdb_image")
    @JvmStatic
    fun loadTmdbImage(imageView: ImageView, url: String?) {
        url?.let {
            imageView.loadImageUrl(BASE_URL_IMAGE + it)
        }
    }

    @BindingAdapter("set_rate_text")
    @JvmStatic
    fun setRateText(textView: TextView, rating: Double?) {
        textView.text = rating?.toRate()
    }

    @BindingAdapter("load_resource")
    @JvmStatic
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("text_res_id")
    fun setTextResourceId(textView: TextView, resource: Int) {
        textView.text = textView.context.getString(resource)
    }

    @BindingAdapter("firstFourChar")
    @JvmStatic
    fun firstFourChar(textView: TextView, text: String) {
        textView.text = text.substring(0, 4)
    }

}