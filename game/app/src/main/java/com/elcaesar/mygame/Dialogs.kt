package com.elcaesar.mygame

import android.R
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.elcaesar.mygame.QuestionsActivity.Companion.removeValues
import kotlinx.android.synthetic.main.dialog_again.view.*
import kotlinx.android.synthetic.main.dialog_success.view.*

@Suppress("DEPRECATION")
open class Dialogs:AppCompatActivity() {

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


    fun openSuccessDialog(context: Context,mediaClick:MediaPlayer,anim:()->Unit) {
        var mediaPlayerSuccess=MediaPlayer.create(context,com.elcaesar.mygame.R.raw.success)
        mediaPlayerSuccess.start()
        val mDialogView =
            LayoutInflater.from(context).inflate(com.elcaesar.mygame.R.layout.dialog_success, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setCancelable(false)

        val mAlertDialog = mBuilder.create()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlertDialog.window!!.attributes.windowAnimations= com.elcaesar.mygame.R.style.DialogAnimation
        mAlertDialog.show()

        mDialogView.bu_Return.setOnClickListener{
            mediaClick.start()
            val intent=Intent(context,GamesActivity::class.java)
            context.startActivity(intent)
            anim()
            mAlertDialog.dismiss()
        }

        mDialogView.bu_Rate2.setOnClickListener{
            mediaClick.start()
            val uri: Uri = Uri.parse("market://details?id=com.elcaesar.mygame")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY )
            try {
                context.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.elcaesar.mygame")))
            }
        }

    }

    fun openAgainDialog(context: Context,mediaClick:MediaPlayer,activity: Activity,anim:()->Unit,int: Int) {
        val mDialogView =
            LayoutInflater.from(context).inflate(com.elcaesar.mygame.R.layout.dialog_again, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setCancelable(false)

        val mAlertDialog = mBuilder.create()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlertDialog.window!!.attributes.windowAnimations= com.elcaesar.mygame.R.style.DialogAnimation2
        mAlertDialog.show()

        mDialogView.iv_Exit2.setOnClickListener{
            mediaClick.start()
            mAlertDialog.dismiss()
        }

        mDialogView.iv_playAgain.setOnClickListener{
            mediaClick.start()
            when(int){
                1 -> {
                    saveInt(context,"count",0)
                    removeValues(context)
                }

                2 -> {
                    saveInt(context,"count2",0)
                }
            }
            val intent=Intent(context,activity::class.java)
            context.startActivity(intent)
            anim()
            mAlertDialog.dismiss()
        }

    }

}