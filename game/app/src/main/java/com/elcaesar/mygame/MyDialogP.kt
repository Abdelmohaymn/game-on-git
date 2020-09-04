package com.elcaesar.mygame

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.elcaesar.mygame.QuestionsActivity.Companion.loadRewardedVideoAd
import com.elcaesar.mygame.QuestionsActivity.Companion.mRewardedVideoAd
import com.elcaesar.mygame.QuestionsActivity.Companion.mediaPlayerClick
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.android.synthetic.main.alert_dialog_freepoints.view.*


@Suppress("DEPRECATION")
open class MyDialogP : AppCompatActivity() {

    companion object {
        var playAds: Boolean = false
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
            mediaPlayerClick.start()
            mAlertDialog.dismiss()
        }

        mDialogView.switch_playAds.setOnCheckedChangeListener { _, isChecked: Boolean ->
            mediaPlayerClick.start()
            if (isChecked) {
                playAds = true
                mDialogView.switch_playAds.isClickable = false
            }
            if (QuestionsActivity.points == 0) {
                QuestionsActivity.points += 25
                textView.text = QuestionsActivity.points.toString()
                enable()
            } else {
                QuestionsActivity.points += 25
                textView.text = QuestionsActivity.points.toString()
            }
        }

        mDialogView.watch_vid.setOnClickListener() {
            mediaPlayerClick.start()
            if (mRewardedVideoAd!!.isLoaded){
                mRewardedVideoAd!!.show()
            }else{
               loadRewardedVideoAd()
            }

        }

    }

}
