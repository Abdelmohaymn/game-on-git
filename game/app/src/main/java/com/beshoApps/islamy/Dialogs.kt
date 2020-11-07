package com.beshoApps.islamy

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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.beshoApps.islamy.Dialogs.Companion.saveBool
import com.beshoApps.islamy.QuestionsActivity.Companion.points
import com.beshoApps.islamy.QuestionsActivity.Companion.removeValues
import kotlinx.android.synthetic.main.dialog_again.view.*
import kotlinx.android.synthetic.main.dialog_coupon_code.*
import kotlinx.android.synthetic.main.dialog_coupon_code.view.*
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
        fun saveString(context: Context, key: String, value: String) {
            val pref = context.getSharedPreferences("Data", MODE_PRIVATE)
            val edit = pref.edit()
            edit.putString(key, value)
            edit.apply()
        }
        fun getString(context: Context, key: String,value: String): String? {
            val pref = context.getSharedPreferences("Data", MODE_PRIVATE)
            return pref.getString(key, value)
        }

        fun remove(context: Context,key:String){
            val preferences: SharedPreferences = context.getSharedPreferences("Data", MODE_PRIVATE)
            preferences.edit().remove(key).apply()
        }

    }


    fun openSuccessDialog(context: Context,mediaClick:MediaPlayer,anim:()->Unit) {
        var mediaPlayerSuccess=MediaPlayer.create(context,com.beshoApps.islamy.R.raw.success)
        mediaPlayerSuccess.start()
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_success, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setCancelable(false)

        val mAlertDialog = mBuilder.create()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlertDialog.window!!.attributes.windowAnimations= R.style.DialogAnimation
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
            val uri: Uri = Uri.parse("market://details?id=com.beshoApps.islamy")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY )
            try {
                context.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.beshoApps.islamy")))
            }
        }

    }

    fun openAgainDialog(context: Context,mediaClick:MediaPlayer,activity: Activity,anim:()->Unit,int: Int) {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_again, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setCancelable(false)

        val mAlertDialog = mBuilder.create()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlertDialog.window!!.attributes.windowAnimations= R.style.DialogAnimation2
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

                3 -> {
                    saveInt(context,"count3",0)
                }
            }
            val intent=Intent(context,activity::class.java)
            context.startActivity(intent)
            anim()
            mAlertDialog.dismiss()
        }

    }

    fun openCouponDialog(context: Context,mediaClick:MediaPlayer,textView: TextView) {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_coupon_code, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setCancelable(false)

        val mAlertDialog = mBuilder.create()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlertDialog.window!!.attributes.windowAnimations= R.style.DialogAnimation2
        mAlertDialog.show()

        mDialogView.tv_coupon.text="رمز القسيمة : هو رمز يقوم المستخدم بكتابته ويتيح له الحصول علي محاولات تتراوح قيمتها من 5 إلي 20 "+"\n"+
                "للحصول علي رمز قسيمة يجب عليك متابعتنا علي انستجرام حيث أننا ننزل رمز من حين لآخر علي صفحتنا "

        mDialogView.iv_Exit3.setOnClickListener{
            mediaClick.start()
            mAlertDialog.dismiss()
        }

        mDialogView.iv_ok.setOnClickListener(){
            var code=mDialogView.et_coupon_code.text.toString()
            var check1= getBool(context,"check1",true)
            var check2= getBool(context,"check2",true)
            var check3= getBool(context,"check3",true)
            var check4= getBool(context,"check4",true)

            pointsCoupon(code,"Bc10_X%q9_u7Nq_I01L",5,"check1",check1,textView,context,mAlertDialog)

            pointsCoupon(code,"Js42_PpT2_&0lV_10Fe",10,"check2",check2,textView,context,mAlertDialog)

            pointsCoupon(code,"Ss12_k*T3_#0lv_8qp5",15,"check3",check3,textView,context,mAlertDialog)

            pointsCoupon(code,"A2L0_Bdc3_h@00_N9Z2",20,"check4",check4,textView,context,mAlertDialog)
        }

        mDialogView.iv_code_insta.setOnClickListener(){
            mediaClick.start()
            mAlertDialog.dismiss()
            val uri = Uri.parse("https://www.instagram.com/besho_apps/")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)

            likeIng.setPackage("com.instagram.android")

            try {
                context.startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/besho_apps/")
                    )
                )
            }
        }

    }
    private fun pointsCoupon(string: String,code:String, value:Int, name:String, check:Boolean, textView: TextView, context: Context, dialog: AlertDialog){
        if (check&&string==code){
            points+=value
            textView.text= points.toString()
            saveBool(context, name, false)
            dialog.dismiss()
            saveInt(context,"points", points)
            Toast.makeText(context, "تم إضافة $value من المحاولات",Toast.LENGTH_LONG).show()
        }else{
            dialog.et_coupon_code.text = null
            dialog.et_coupon_code.hint="لقد أدخلت رمز خاطئ أو تم استخدامه سابقا"
            YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(dialog.et_coupon_code)
        }
    }
}
