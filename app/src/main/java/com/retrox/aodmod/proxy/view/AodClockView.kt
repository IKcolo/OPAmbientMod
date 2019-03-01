package com.retrox.aodmod.proxy.view

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.VectorDrawable
import android.support.constraint.ConstraintLayout.LayoutParams.PARENT_ID
import android.view.View
import android.widget.LinearLayout
import com.retrox.aodmod.MainHook
import com.retrox.aodmod.extensions.setGoogleSans
import com.retrox.aodmod.service.notification.NotificationManager
import com.retrox.aodmod.state.AodClockTick
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import java.text.SimpleDateFormat
import java.util.*

fun Context.aodClockView(lifecycleOwner: LifecycleOwner): View {
    return constraintLayout {
        textView {
            id = Ids.tv_clock
            textColor = Color.WHITE
            textSize = 50f
            letterSpacing = 0.1f
            setGoogleSans()

            text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date())
            AodClockTick.tickLiveData.observe(lifecycleOwner, Observer {
                text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date())
            })
        }.lparams(width = wrapContent, height = wrapContent) {
            endToEnd = PARENT_ID
            startToStart = PARENT_ID
            topToTop = PARENT_ID
        }
        textView("Wed  Nov. 28") {
            id = Ids.tv_today
            textColor = Color.WHITE
            textSize = 18f
            setGoogleSans()
            text = SimpleDateFormat("EEEE MM. dd", Locale.ENGLISH).format(Date())
            AodClockTick.tickLiveData.observe(lifecycleOwner, Observer {
                text = SimpleDateFormat("EEEE MM. dd", Locale.ENGLISH).format(Date())
            })
        }.lparams(width = wrapContent, height = wrapContent) {
            endToEnd = PARENT_ID
            startToStart = PARENT_ID
            topToBottom = Ids.tv_clock
            topMargin = dip(12)
        }
        view {
            backgroundColor = Color.WHITE
            id = Ids.view_divider
        }.lparams(width = dip(12), height = dip(2)) {
            endToEnd = PARENT_ID
            startToStart = PARENT_ID
            topToBottom = Ids.tv_today
            topMargin = dip(18)
        }
        linearLayout {
            id = Ids.ll_icons
            orientation = LinearLayout.HORIZONTAL

            val refreshBlock = {
                val icons = NotificationManager.notificationMap.values.asSequence()
                    .map { it.notification }
//                    .filter { it. }
                    .map {
                        try {
                            it.smallIcon.loadDrawable(context)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            return@map null
                        }
                    }
                    .filterNot { it == null || it is VectorDrawable }
                    .toList()

                MainHook.logD("icons: $icons")

                removeAllViews()
                icons.take(6).forEach {
                    imageView {
                        setImageDrawable(it)
                    }.lparams(width = dip(24), height = dip(24)) {
                        horizontalMargin = dip(4)
                    }
                }
            }
            refreshBlock.invoke()
            NotificationManager.notificationStatusLiveData.observe(lifecycleOwner, Observer {
                refreshBlock.invoke()
            })

        }.lparams(width = wrapContent, height = wrapContent) {
            endToEnd = PARENT_ID
            startToStart = PARENT_ID
            topToBottom = Ids.view_divider
            topMargin = dip(24)
        }
    }
}

