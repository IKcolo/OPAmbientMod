package com.retrox.aodmod.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.retrox.aodmod.MainHook
import com.retrox.aodmod.state.AodClockTick


/**
 * 这个货太慢了 不靠谱 于是在Power那边做了一个LiveData发送处理
 * 但是为了保险 还是开着它吧
 */
class ClockTickReceiver : BroadcastReceiver() {
    companion object {
        const val CUSTOM_PING = "CLOCKTICK_CUSTOM_PING"
        const val CUSTOM_PING_RTC = "CLOCKTICK_CUSTOM_PING_RTC"
        const val CUSTOM_PING_RTC_WAKEUP = "CLOCKTICK_CUSTOM_PING_RTC_WAKEUP"
    }
    override fun onReceive(context: Context, intent: Intent) {
        AodClockTick.tickLiveData.postValue("Tick! action -> ${intent.action} ")
        MainHook.logD("Tick Intent Received. Action: ${intent.action} ")
    }

}