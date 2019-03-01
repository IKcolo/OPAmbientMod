package com.retrox.aodmod.app

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import com.retrox.aodmod.app.state.AppState
import org.jetbrains.anko.*
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri


class MusicSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            textView {
                text = "这是什么功能？"
                textColor = Color.parseColor("#F5A623")
                textSize = 18f
                gravity = Gravity.START
            }.lparams(width = matchParent, height = wrapContent) {
                verticalMargin = dip(8)
                horizontalMargin = dip(12)
            }

            textView {
                text = "这是一个息屏音乐提示的功能，界面效果类似于Google Pixel的NowPlaying，但是本质上是显示系统的正在播放，使用此功能，你需要把使用的音乐APP添加到太极的应用列表。"
                gravity = Gravity.START
                textColor = Color.BLACK
                textSize = 16f

            }.lparams(width = matchParent, height = wrapContent) {
                verticalMargin = dip(8)
                horizontalMargin = dip(12)
            }

            textView {
                text = "支持什么APP？"
                textColor = Color.parseColor("#F5A623")
                textSize = 18f
                gravity = Gravity.START
            }.lparams(width = matchParent, height = wrapContent) {
                verticalMargin = dip(8)
                horizontalMargin = dip(12)
            }

            textView {
                text = "目前测试了网易云音乐和QQ音乐，但是理论上不存在兼容性问题，记住要把自己用的音乐APP加入的太极并且杀掉并重新启动。"
                gravity = Gravity.START
                textColor = Color.BLACK
                textSize = 16f

            }.lparams(width = matchParent, height = wrapContent) {
                verticalMargin = dip(12)
                horizontalMargin = dip(8)
            }

            button {
                text = "点击这里帮你加入网易云和QQ音乐~"
                setOnClickListener {
                    val t = Intent("me.weishu.exp.ACTION_ADD_APP")
                    t.data = Uri.parse("package:" + "com.netease.cloudmusic" + "|" + "com.tencent.qqmusic")
                    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        startActivity(t)
                    } catch (e: ActivityNotFoundException) {
                        // TaiChi not installed or version below 4.3.4.
                    }

                }
            }.lparams(wrapContent, wrapContent) {
                gravity = Gravity.START
                verticalMargin = dip(12)
                horizontalMargin = dip(8)
            }


            textView {
                text = "这些是已经在太极中的APP"
                textColor = Color.parseColor("#F5A623")
                textSize = 18f
                gravity = Gravity.START
            }.lparams(width = matchParent, height = wrapContent) {
                verticalMargin = dip(8)
                horizontalMargin = dip(12)
            }

            verticalLayout {
                AppState.expApps.observe(this@MusicSettingsActivity, Observer {
                    it?.let { list ->
                        removeAllViews()
                        list.forEach {
                            textView {
                                text = it
                                gravity = Gravity.START
                                textSize = 16f
                            }.lparams(width = matchParent, height = wrapContent) {
                                verticalMargin = dip(6)
                                horizontalMargin = dip(8)
                            }
                        }

                    }
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AppState.refreshExpApps(this)
        AppState.refreshActiveState(this)
    }
}