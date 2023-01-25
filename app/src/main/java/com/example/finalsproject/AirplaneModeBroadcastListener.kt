package com.example.finalsproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeBroadcastListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val isAirplaneModeOn = intent?.getBooleanExtra("state", false) ?: return

        if (isAirplaneModeOn){
            Toast.makeText(context, "Airplane Mode Is Enabled", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context, "Airplane Mode Is Disabled", Toast.LENGTH_SHORT).show()
        }
    }
}