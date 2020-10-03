package com.elcaesar.mygame

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.elcaesar.mygame.AdsClass.Companion.MyRewardedAd
import com.elcaesar.mygame.AdsClass.Companion.bannerAd
import com.elcaesar.mygame.AdsClass.Companion.loadRewardedVideoAd
import com.elcaesar.mygame.AdsClass.Companion.mAdView
import com.elcaesar.mygame.AdsClass.Companion.mInterstitialAd
import com.elcaesar.mygame.AdsClass.Companion.mRewardedVideoAd
import com.elcaesar.mygame.AdsClass.Companion.mobileAds
import com.elcaesar.mygame.Dialogs.Companion.getBool
import com.elcaesar.mygame.Dialogs.Companion.getInt
import com.elcaesar.mygame.Dialogs.Companion.playAds
import com.elcaesar.mygame.Dialogs.Companion.remove
import com.elcaesar.mygame.Dialogs.Companion.saveBool
import com.elcaesar.mygame.Dialogs.Companion.saveInt
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.android.synthetic.main.alert_dialog_freepoints.*
import kotlinx.android.synthetic.main.alert_dialog_freepoints.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.io.FileOutputStream
import java.io.OutputStream

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNREACHABLE_CODE", "DEPRECATION")

 class QuestionsActivity : AppCompatActivity(),RewardedVideoAdListener {

    private var mProductList: List<Questions>? = null
    private var mDBHelper: DatabaseHelper? = null
    lateinit var mediaPlayerC:MediaPlayer
    lateinit var mediaPlayerW:MediaPlayer
    lateinit var mediaPlayerClick:MediaPlayer
    var rand:Int?=null
    var whoClicked=false
    var cnt=0

    @SuppressLint("ResourceAsColor", "WrongConstant", "ResourceType", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        MyRewardedAd(this)
        mRewardedVideoAd!!.rewardedVideoAdListener = this

        // إعلان فيديو بمكافئ

        mAdView = findViewById(R.id.adView)
        bannerAd()


        mobileAds(this)


        mDBHelper = DatabaseHelper(this)

        //Check exists database
        val database = applicationContext.getDatabasePath(DatabaseHelper.DBNAME)
        if (!database.exists()) {
            mDBHelper!!.readableDatabase
            //Copy db
            copyDatabase(this)
        }

        //Get product list in db when db exists
        mProductList = mDBHelper!!.listProduct
        ///////////////////

        mediaPlayerC = MediaPlayer.create(this,R.raw.correctt)
        mediaPlayerW = MediaPlayer.create(this,R.raw.incorrectt)
        mediaPlayerClick = MediaPlayer.create(this,R.raw.click)


        tv_points.text= points.toString()
        tv_theGames.visibility=View.GONE
        count= getInt(this,"count",0)
        //count=192
        setQuestions(count)
        if (getBool(this,"bu Ads",false)){
            buAds.visibility=View.VISIBLE
        }else{
            buAds.visibility=View.GONE
        }
        answer1.setBackgroundResource(getInt(this,"back1",R.drawable.button_answer))
        answer2.setBackgroundResource(getInt(this,"back2",R.drawable.button_answer))
        answer3.setBackgroundResource(getInt(this,"back3",R.drawable.button_answer))
        answer4.setBackgroundResource(getInt(this,"back4",R.drawable.button_answer))


        bu_back.setOnClickListener(){
            mediaPlayerClick.start()
            val intent=Intent(this,GamesActivity::class.java)
            if (playAds){
                mInterstitialAd!!.adListener = object: AdListener() {
                    override fun onAdClosed() {
                        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
                        startActivity(intent)
                        //overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
                    }
                }
                if (mInterstitialAd!!.isLoaded) {
                    mInterstitialAd!!.show()
                } else {
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
                }
            }else{
                startActivity(intent)
                overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
            }
        }

        iv_points_toolbar.setOnClickListener(){
            mediaPlayerClick.start()
            openDialog(this,tv_points,mediaPlayerClick, {isEnable()})
        }

        buAds.setOnClickListener(){
            mediaPlayerClick.start()
            mRewardedVideoAd!!.show()
            whoClicked=true
        }

        buAskFriends.setOnClickListener(){
            mediaPlayerClick.start()
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"${Question.text}"+"؟"+"\n"
                    +"حمل لعبة أسئلة إسلامية من هنا"+"\n"+
                    "https://play.google.com/store/apps/com.elcaesar.mygame")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"اسأل أصدقائك"))
        }

        answer1.setOnClickListener {

            if(getInt(this,"back1",R.drawable.button_answer)==R.drawable.button_answer2){
                answer1.isClickable=false
            }else{
                Answers(answer1)
            }

        }
        answer2.setOnClickListener {

            if(getInt(this,"back2",R.drawable.button_answer)==R.drawable.button_answer2){
                answer2.isClickable=false
            }else{
                Answers(answer2)
            }

        }
        answer3.setOnClickListener {

            if(getInt(this,"back3",R.drawable.button_answer)==R.drawable.button_answer2){
                answer3.isClickable=false
            }else{
                Answers(answer3)
            }

        }
        answer4.setOnClickListener {

            if(getInt(this,"back4",R.drawable.button_answer)==R.drawable.button_answer2){
                answer4.isClickable=false
            }else{
                Answers(answer4)
            }

        }


    }

    private fun setQuestions(index:Int){
        Question.text = mProductList!![index].ques
        answer1.text = mProductList!![index].ans1
        answer2.text = mProductList!![index].ans2
        answer3.text = mProductList!![index].ans3
        answer4.text = mProductList!![index].ans4

    }
    private fun notEnable(){
        answer1.isClickable=false
        answer2.isClickable=false
        answer3.isClickable=false
        answer4.isClickable=false
    }
    private fun isEnable(){
        answer1.isClickable=true
        answer2.isClickable=true
        answer3.isClickable=true
        answer4.isClickable=true
    }
    private fun isVisible(){
        answer1.isEnabled=true
        answer2.isEnabled=true
        answer3.isEnabled=true
        answer4.isEnabled=true
    }

    private fun ansBackground(){
        answer1.setBackgroundResource(R.drawable.button_answer)
        answer2.setBackgroundResource(R.drawable.button_answer)
        answer3.setBackgroundResource(R.drawable.button_answer)
        answer4.setBackgroundResource(R.drawable.button_answer)
    }


    @SuppressLint("ResourceAsColor")
    private fun Answers(button: Button){
        if(points>0){
            if (button.text==mProductList!![count].correct) {
                mediaPlayerC.start()
                count++
                points++
                tv_points.text = points.toString()
                cnt=0
                whoClicked=false
                button.setBackgroundResource(R.drawable.button_answer3)
                when(button){
                    answer1 -> saveInt(this,"back1",R.drawable.button_answer3)
                    answer2 -> saveInt(this,"back2",R.drawable.button_answer3)
                    answer3 -> saveInt(this,"back3",R.drawable.button_answer3)
                    answer4 -> saveInt(this,"back4",R.drawable.button_answer3)
                }

                if (count==200){
                    setQuestions(count-1)
                    Dialogs().openSuccessDialog(this,mediaPlayerClick,{anim()})
                }else{
                    if (playAds){
                        mInterstitialAd!!.adListener = object: AdListener() {
                            override fun onAdClosed() {
                                mInterstitialAd!!.loadAd(AdRequest.Builder().build())
                                playAnim(Question,0)
                            }
                        }
                        if (mInterstitialAd!!.isLoaded) {
                            mInterstitialAd!!.show()
                        } else {
                            playAnim(Question,0)
                        }
                    }else{
                        playAnim(Question,0)
                    }
                }


            }else{
                button.isClickable=false
                mediaPlayerW.seekTo(0)
                mediaPlayerW.start()
                points--
                tv_points.text = points.toString()
                button.setBackgroundResource(R.drawable.button_answer2)
                when(button){
                    answer1 -> saveInt(this,"back1",R.drawable.button_answer2)
                    answer2 -> saveInt(this,"back2",R.drawable.button_answer2)
                    answer3 -> saveInt(this,"back3",R.drawable.button_answer2)
                    answer4 -> saveInt(this,"back4",R.drawable.button_answer2)
                }
            }

            if (points==0){
                buAds.isClickable=false
                openDialog(this,tv_points,mediaPlayerClick,{ isEnable() })
            }

        }else{
            Toast.makeText(this,"يجب عليك إضافة محاولة واحدة علي الأقل",Toast.LENGTH_LONG).show()
        }

        saveInt(this,"points",points)
        saveInt(this,"count",count)
    }

    fun deleteAnswer(){
        rand=(1 until 4).random()
        if(answer1.text==mProductList!![count].correct)
        {
            when(rand){
                1 -> {
                    answer2.isEnabled = false
                    answer3.isEnabled = false
                }
                2 -> {
                    answer3.isEnabled = false
                    answer4.isEnabled = false
                }
                3 -> {
                    answer4.isEnabled = false
                    answer2.isEnabled = false
                }
            }
        }

        if(answer2.text==mProductList!![count].correct)
        {
            when(rand){
                1 -> {
                    answer1.isEnabled = false
                    answer3.isEnabled = false
                }
                2 -> {
                    answer3.isEnabled = false
                    answer4.isEnabled = false
                }
                3 -> {
                    answer4.isEnabled = false
                    answer1.isEnabled = false
                }
            }
        }

        if(answer3.text==mProductList!![count].correct)
        {
            when(rand){
                1 -> {
                    answer2.isEnabled = false
                    answer1.isEnabled = false
                }
                2 -> {
                    answer1.isEnabled = false
                    answer4.isEnabled = false
                }
                3 -> {
                    answer4.isEnabled = false
                    answer2.isEnabled = false
                }
            }
        }

        if(answer4.text==mProductList!![count].correct)
        {
            when(rand){
                1 -> {
                    answer2.isEnabled = false
                    answer3.isEnabled = false
                }
                2 -> {
                    answer3.isEnabled = false
                    answer1.isEnabled = false
                }
                3 -> {
                    answer1.isEnabled = false
                    answer2.isEnabled = false
                }
            }
        }

    }

    private fun playAnim(view: View, value: Int) {
        view.animate().alpha(value.toFloat()).scaleX(value.toFloat()).scaleY(value.toFloat()).setDuration(500).setStartDelay(100)
                .setInterpolator(DecelerateInterpolator()).setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        if (value==0 && cnt<4){
                            playAnim(answers_container.getChildAt(cnt),0 )
                            cnt++
                        }
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (value == 0) {
                            playAnim(view, 1)
                        }
                        setQuestions(count)
                        isEnable()
                        isVisible()
                        buAds.isClickable=true
                        ansBackground()
                        removeValues(this@QuestionsActivity)

                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
    }


///////// Companion Object ///////////

    companion object {
            var points=10
            var count:Int=0


        fun removeValues(context: Context){
            remove(context,"back1")
            remove(context,"back2")
            remove(context,"back3")
            remove(context,"back4")
        }

    ///////// Dialog 1 //////////

        fun openDialog(context: Context, textView: TextView,mediaClick:MediaPlayer, enable: () -> Unit) {
            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.alert_dialog_freepoints, null)
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setCancelable(false)

            val mAlertDialog = mBuilder.create()
            mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mAlertDialog.window!!.attributes.windowAnimations=R.style.DialogAnimation3
            mAlertDialog.show()

            mDialogView.ivExit.setOnClickListener {
                mediaClick.start()
                mAlertDialog.dismiss()
            }

            mDialogView.switch_playAds.isChecked= getBool(context,"is checked ads",false)
            mDialogView.switch_playAds.isClickable = getBool(context,"click play ads",true)

            mDialogView.switch_playAds.setOnCheckedChangeListener { _, isChecked: Boolean ->
                mediaClick.start()
                if (isChecked) {
                    playAds = true
                    mDialogView.switch_playAds.isClickable = false
                    saveBool(context,"is checked ads",true)
                    saveBool(context,"click play ads",false)
                    saveBool(context,"play Ads", true)
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
                if (mRewardedVideoAd!!.isLoaded){
                    mediaClick.start()
                    mRewardedVideoAd!!.show()
                }else{
                    loadRewardedVideoAd()
                    Toast.makeText(context,"لا يتوفر فديو الان حاول لاحقا !!",Toast.LENGTH_SHORT).show()
                }

            }

        }

    }


    private fun copyDatabase(context: Context) {
        return try {
            val inputStream = context.assets.open(DatabaseHelper.DBNAME)
            val outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME
            val outputStream: OutputStream = FileOutputStream(outFileName)
            val buff = ByteArray(1024)
            var length: Int
            while (inputStream.read(buff).also { length = it } > 0) {
                outputStream.write(buff, 0, length)
            }
            outputStream.flush()
            outputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }



    override fun onRewardedVideoAdClosed() {
        loadRewardedVideoAd()
    }

    override fun onRewardedVideoAdLeftApplication() {

    }

    override fun onRewardedVideoAdLoaded() {
            buAds.visibility= View.VISIBLE
            saveBool(this,"bu Ads",true)
            if (whoClicked){
                whoClicked=false
                buAds.isClickable=false
            }
    }

    override fun onRewardedVideoAdOpened() {

    }

    override fun onRewardedVideoCompleted() {

    }

    override fun onRewarded(p0: RewardItem?) {

        if (whoClicked){
            deleteAnswer()
        }else{
            if (points == 0) {
                points+=2
                tv_points.text = points.toString()
                isEnable()
            } else {
                points+=2
                tv_points.text = points.toString()
            }

        }
        saveInt(this,"points",points)
        remove(this,"bu Ads")
        buAds.visibility= View.GONE

    }

    override fun onRewardedVideoStarted() {

    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent=Intent(this,GamesActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
    }

    fun anim(){
        overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
    }

}