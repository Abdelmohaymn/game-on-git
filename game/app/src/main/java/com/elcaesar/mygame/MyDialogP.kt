package com.elcaesar.mygame

import android.R
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.elcaesar.mygame.QuestionsActivity.Companion.loadRewardedVideoAd
import com.elcaesar.mygame.QuestionsActivity.Companion.mRewardedVideoAd
import com.elcaesar.mygame.QuestionsActivity.Companion.mediaPlayerClick
import com.elcaesar.mygame.QuestionsActivity.Companion.points


@Suppress("DEPRECATION")
open class MyDialogP  {

    companion object {
        var playAds: Boolean = false

        ////////////////// save Data

        fun saveInt(context: Context, key: String, value: Int) {
            val pref = context.getSharedPreferences("Data", MODE_PRIVATE)
            val editor = pref.edit()
            editor.putInt(key, value)
            editor.apply()
        }
        fun getInt(context: Context, key: String, value:Int): Int {
            val pr = context.getSharedPreferences("Data", MODE_PRIVATE)
            return pr.getInt(key, value)
        }

        fun saveBool(context: Context, key: String, value: Boolean) {
            val pref = context.getSharedPreferences("Data", MODE_PRIVATE)
            val edit = pref.edit()
            edit.putBoolean(key, value)
            edit.apply()
        }
        fun getBool(context: Context, key: String,value: Boolean): Boolean {
            val pref = context.getSharedPreferences("Data", MODE_PRIVATE)
            return pref.getBoolean(key, value)
        }
        fun remove(context: Context,key:String){
            val preferences: SharedPreferences = context.getSharedPreferences("Data", MODE_PRIVATE)
            preferences.edit().remove(key).apply()
        }
    }

    fun openDialog(context: Context ,textView: TextView , enable: () -> Unit) {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.alert_dialog_freepoints, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setCancelable(false)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mDialogView.ivExit.setOnClickListener {
            //mediaPlayerClick!!.start()
            mAlertDialog.dismiss()
        }

        mDialogView.switch_playAds.isChecked= getBool(context,"is checked ads",false)
        mDialogView.switch_playAds.isClickable = getBool(context,"click play ads",true)

        mDialogView.switch_playAds.setOnCheckedChangeListener { _, isChecked: Boolean ->
            mediaPlayerClick!!.start()
            if (isChecked) {
                playAds = true
                mDialogView.switch_playAds.isClickable = false
                saveBool(context,"is checked ads",true)
                saveBool(context,"click play ads",false)
                saveBool(context,"play Ads", playAds)
                //saveInt(context,"points", points)
            }
            if (points == 0) {
                points += 25
                textView.text = points.toString()
                enable()
            } else {
                points += 25
                textView.text = points.toString()
            }

            saveInt(context,"points", points)
        }

        mDialogView.watch_vid.setOnClickListener() {
            mediaPlayerClick!!.start()
            if (mRewardedVideoAd!!.isLoaded){
                mRewardedVideoAd!!.show()
            }else{
                loadRewardedVideoAd()
                Toast.makeText(context,"لا يتوفر فديو الان حاول لاحقا !!",Toast.LENGTH_SHORT).show()
            }

        }

    }

}