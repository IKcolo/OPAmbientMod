package com.retrox.aodmod.app

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import com.retrox.aodmod.app.pref.AppPref
import com.retrox.aodmod.app.state.AppState
import com.retrox.aodmod.app.state.AppState.expApps
import com.retrox.aodmod.app.state.AppState.isActive
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

/**
 * 不要在这里使用Xposed api相关的类
 * 会导致 NoClassDef
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        MainHook.logD(service.getActiveSessions(null).toString())
        isActive.value = XposedUtils.isExpModuleActive(this@MainActivity)
        expApps.value = XposedUtils.getExpApps(this@MainActivity)

        verticalLayout {
            cardView {
                setCardBackgroundColor(Color.parseColor("#A0A0A0"))
                radius = dip(12).toFloat()
                verticalPadding = dip(12)

                textView {
                    textColor = Color.WHITE
                    textSize = 17f
                    isActive.observe(this@MainActivity, Observer {
                        it?.let { active ->
                            if (active) {
                                text = "模块已激活"
                                this@cardView.setCardBackgroundColor(Color.parseColor("#1B73E8"))
                                alpha = 0.85f
                            } else {
                                text = "模块尚未激活 请在太极·Magisk中激活模块"
                                this@cardView.setCardBackgroundColor(Color.parseColor("#A0A0A0"))
                                alpha = 1f
                            }
                        }

                    })
                    horizontalPadding = dip(16)
                    gravity = Gravity.CENTER_HORIZONTAL
                }.lparams {
                    gravity = Gravity.CENTER
                }

                setOnClickListener {
                    val t = Intent("me.weishu.exp.ACTION_MODULE_MANAGE")
                    t.data = Uri.parse("package:" + "com.retrox.aodmod")
                    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        startActivity(t)
                    } catch (e: ActivityNotFoundException) {
                        // TaiChi not installed.
                    }
                }
            }.lparams(matchParent, dip(60)) {
                horizontalMargin = dip(8)
                verticalMargin = dip(8)
            }

            cardView {
                setCardBackgroundColor(Color.parseColor("#A0A0A0"))
                radius = dip(12).toFloat()
                verticalPadding = dip(12)

                textView {
                    textColor = Color.WHITE
                    textSize = 17f
                    expApps.observe(this@MainActivity, Observer {
                        it?.let { list ->
                            if (list.contains("com.oneplus.aod")) {
                                text = "主动显示APP已添加"
                                this@cardView.setCardBackgroundColor(Color.parseColor("#1B73E8"))
                                alpha = 0.85f
                            } else {
                                text = "主动显示APP未添加 点击添加主动显示APP \n 需要太极·Magisk"
                                this@cardView.setCardBackgroundColor(Color.parseColor("#A0A0A0"))
                                alpha = 1f
                            }
                        }

                    })
                    horizontalPadding = dip(16)
                    gravity = Gravity.CENTER_HORIZONTAL
                }.lparams {
                    gravity = Gravity.CENTER
                }

                setOnClickListener {
                    val t = Intent("me.weishu.exp.ACTION_ADD_APP")
                    t.data = Uri.parse("package:" + "com.oneplus.aod")
                    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        startActivity(t)
                    } catch (e: ActivityNotFoundException) {
                        // TaiChi not installed or version below 4.3.4.
                    }
                }


            }.lparams(matchParent, dip(60)) {
                horizontalMargin = dip(8)
                verticalMargin = dip(8)
            }


            cardView {
                setCardBackgroundColor(Color.parseColor("#F5A623"))
                radius = dip(12).toFloat()
                verticalPadding = dip(12)

                textView {
                    textColor = Color.WHITE
                    textSize = 17f
                    text = "设置息屏音乐提醒"
                    horizontalPadding = dip(16)
                    gravity = Gravity.CENTER_HORIZONTAL
                }.lparams {
                    gravity = Gravity.CENTER
                }

                setOnClickListener {
                    startActivity<MusicSettingsActivity>()
                }

            }.lparams(matchParent, dip(60)) {
                horizontalMargin = dip(8)
                verticalMargin = dip(8)
            }


            cardView {
                setCardBackgroundColor(Color.parseColor("#F5A623"))
                radius = dip(12).toFloat()
                verticalPadding = dip(12)

                textView {
                    textColor = Color.WHITE
                    textSize = 17f
                    text = "设置息屏模式: 系统模式/常亮模式 \n当前模式: ${AppPref.aodMode}"
                    horizontalPadding = dip(16)
                    gravity = Gravity.CENTER_HORIZONTAL
                }.lparams {
                    gravity = Gravity.CENTER
                }

                setOnClickListener {
                    startActivity<AodModeActivity>()
                }

            }.lparams(matchParent, dip(60)) {
                horizontalMargin = dip(8)
                verticalMargin = dip(8)
            }

            button("检查添加app") {
                setOnClickListener {
                    val apps = XposedUtils.getExpApps(this@MainActivity)
                    Log.d("AOD", apps.toString())
                }
            }

            button("Sleep On") {
                setOnClickListener {
                    val intent = Intent("com.aodmod.sleep.on")
                    sendBroadcast(intent)
                }
            }

            button("Sleep OFF") {
                setOnClickListener {
                    val intent = Intent("com.aodmod.sleep.off")
                    sendBroadcast(intent)
                }
            }

            button("Kill") {
                setOnClickListener {
                    val intent = Intent("com.retrox.aod.killmyself")
                    sendBroadcast(intent)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        AppState.refreshActiveState(this)
        AppState.refreshExpApps(this)
    }
}