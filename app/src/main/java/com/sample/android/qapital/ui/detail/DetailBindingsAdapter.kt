package com.sample.android.qapital.ui.detail

import android.text.format.DateUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.android.qapital.data.Feed


object DetailBindingsAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, imageUrl: String) {

        Glide.with(imageView.context)
            .load(imageUrl)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("items")
    fun bindItems(recyclerView: RecyclerView, feeds: List<Feed>) {
        val feedAdapter = FeedAdapter(feeds)
        recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = feedAdapter
        }
    }

    @JvmStatic
    @BindingAdapter("descriptive_date")
    fun setDescriptiveDate(view: TextView, timestamp: Long) {
        val now = System.currentTimeMillis()
        val relativeDate = DateUtils.getRelativeTimeSpanString(
            timestamp,
            now,
            DateUtils.SECOND_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        )
        view.text = relativeDate
    }

    @JvmStatic
    @BindingAdapter("android:max")
    fun setProgressBarMax(view : ProgressBar, value : Float) {
        view.max = value.toInt()
    }

    @JvmStatic
    @BindingAdapter("android:progress")
    fun setProgressBarProgress(view : ProgressBar, value : Float) {
        view.progress = value.toInt()
    }
}