package com.example.masterproject.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.TelephonyManager
import android.util.Log

class PhoneStateReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?)
    {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_IDLE && phoneNumber != null)
            {
                sendSMS(context, phoneNumber)
                Log.e("TAG", "onReceive: "+sendSMS(context, phoneNumber))
            }
        }
    }

    private fun sendSMS(context: Context?, phoneNumber: String)
    {
        val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$phoneNumber"))
        smsIntent.putExtra("sms_body","Your SMS message here.")
        Log.e("TAG", "sendSMS: "+smsIntent.putExtra("sms_body","Your SMS message here."))
        smsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(smsIntent)
    }
}