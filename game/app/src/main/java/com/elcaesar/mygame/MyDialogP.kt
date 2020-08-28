package com.elcaesar.mygame

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.alert_dialog_freepoints.view.*


@Suppress("DEPRECATION")
open class MyDialogP : AppCompatActivity() {


    companion object {
        var playAds: Boolean = false
    }

    fun openDialog(context: Context, textView: TextView, enable: () -> Unit) {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.alert_dialog_freepoints, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setCancelable(false)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialogView.ivExit.setOnClickListener {
            QuestionsActivity.mediaPlayerClick.start()
            mAlertDialog.dismiss()
        }
        mDialogView.buy.setOnClickListener() {
            QuestionsActivity.mediaPlayerClick.start()
            if (QuestionsActivity.points == 0) {
                QuestionsActivity.points++
                textView.text = QuestionsActivity.points.toString()
                enable()
            } else {
                QuestionsActivity.points++
                textView.text = QuestionsActivity.points.toString()
            }

        }
        mDialogView.switch_playAds.setOnCheckedChangeListener { _, isChecked: Boolean ->

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


        }

    }


    /* private fun saveSating() {
     val saveChange =
         getSharedPreferences("saveChange", Context.MODE_PRIVATE)
     val editor = saveChange.edit()
     editor.putInt("Point", points)
     editor.putInt("share", share)
     editor.apply()
 }

 fun loadSating() {
     val saveChange =
         getSharedPreferences("saveChange", Context.MODE_PRIVATE)
     val point = saveChange.getInt("Point", points)
     points = point
     share = saveChange.getInt("share", share)

 }*/

}