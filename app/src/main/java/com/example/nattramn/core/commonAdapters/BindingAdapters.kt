package com.example.nattramn.core.commonAdapters

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nattramn.R
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.core.resource.Status
import com.example.nattramn.core.utils.faNumbers
import com.example.nattramn.core.utils.load
import com.google.android.material.button.MaterialButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageSource")
fun setImageUrl(imageView: ImageView, imageSource: String?) {

    imageSource?.let {
        /*val imgUri = it.toUri().buildUpon().scheme("https://ibb.co/19Z3rbG").build()*/
        Glide.with(imageView.context)
            .load(imageSource)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.default_profile_picture)
            )
            .into(imageView)
    }
}

@BindingAdapter("app:isBookmarked")
fun bookmarkSrc(view: View, isBookmarked: Boolean) {
    if (view is ImageView) {
        if (isBookmarked) {
            load(view, R.drawable.ic_bookmark_article_fragment)
        } else {
            load(view, R.drawable.ic_bookmark)
        }
    }
}

@BindingAdapter("app:bookmarkSrcRecyclerItem")
fun bookmarkSrcRecyclerItem(view: View, isBookmarked: Boolean) {
    if (view is ImageView) {
        if (isBookmarked) {
            load(view, R.drawable.ic_bookmark_checked)
        } else {
            load(view, R.drawable.ic_bookmark)
        }
    }
}

@BindingAdapter("app:setLikeIcon")
fun setLikeIcon(view: View, liked: Boolean) {
    if (view is ImageView) {
        if (liked) {
            load(view, R.drawable.ic_thumb_up_green)
        } else {
            load(view, R.drawable.ic_thumb_up_grey)
        }
    }
}

@BindingAdapter("app:visibleOnResult")
fun visibleOnResult(view: View, resource: Resource<*>?) {
    view.isVisible = resource?.status == Status.LOADING
}

@BindingAdapter("app:checkState")
fun checkState(view: View, following: Boolean) {
    if (view is MaterialButton) {
        if (following) {
            view.text = "در حال دنبال کردن"
            view.setTextColor(Color.parseColor("#ffffff"))
            view.setBackgroundColor(Color.parseColor("#63b47c"))
        } else {
            view.text = "دنبال کردن"
            view.setTextColor(Color.parseColor("#ffffff"))
            view.setBackgroundColor(Color.parseColor("#286de6"))
        }
    }
}

@BindingAdapter("app:convertDate")
fun convertDate(view: View, string: String) {

    if (view is TextView) {
        val date: Date?
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        try {
            date = format.parse(string)
            val dateFormat = SimpleDateFormat("MM dd")
            var dateTime = dateFormat.format(date!!)
            dateTime = toMonthName(dateTime.substring(0, 2)) + dateTime.substring(2, 5)

            var out: String? = ""
            val length = dateTime.length

            for (i in 0 until length) {
                val c = dateTime[i]
                if (c in '0'..'9') {
                    val number = c.toString().toInt()
                    out += faNumbers[number]
                } else if (c == '٫' || c == ',') {
                    out += '،'
                } else {
                    out += c
                }
            }

            view.text = out

        } catch (e: ParseException) {
            view.text = "خطا در محاسبه تاریخ"
        }
    }

}

@BindingAdapter("persianText")
fun convert(textView: TextView, text: String) {

    var out: String? = ""
    val length = text.length

    for (i in 0 until length) {
        val c = text[i]
        if (c in '0'..'9') {
            val number = c.toString().toInt()
            out += faNumbers[number]
        } else if (c == '٫' || c == ',') {
            out += '،'
        } else {
            out += c
        }
    }

    textView.text = out

}

private fun toMonthName(monthNumber: String): String {
    when (monthNumber) {
        "01" -> {
            return "Jan"
        }
        "02" -> {
            return "Feb"
        }
        "03" -> {
            return "Mar"
        }
        "04" -> {
            return "Apr"
        }
        "05" -> {
            return "May"
        }
        "06" -> {
            return "Jun"
        }
        "07" -> {
            return "Jul"
        }
        "08" -> {
            return "Aug"
        }
        "09" -> {
            return "Sep"
        }
        "10" -> {
            return "October"
        }
        "11" -> {
            return "November"
        }
        "12" -> {
            return "December"
        }
    }
    return "Jan"
}
