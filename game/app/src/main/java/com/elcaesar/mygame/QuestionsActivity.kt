package com.elcaesar.mygame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.io.FileOutputStream
import java.io.OutputStream

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNREACHABLE_CODE", "DEPRECATION")

 class QuestionsActivity : AppCompatActivity(),RewardedVideoAdListener {

    lateinit var mAdView : AdView

    private var mProductList: List<Questions>? = null
    private var mDBHelper: DatabaseHelper? = null
    var count:Int =0
    var isCorrect =false
    var rand:Int?=null
    var whoClicked=false


    @SuppressLint("ResourceAsColor", "WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd!!.rewardedVideoAdListener = this
        loadRewardedVideoAd()
        // إعلان فيديو بمكافئ

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

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


        var check: Boolean = intent.getBooleanExtra("check",false)

        mediaPlayerC = MediaPlayer.create(this,R.raw.correctt)
        mediaPlayerW = MediaPlayer.create(this,R.raw.incorrectt)
        mediaPlayerClick = MediaPlayer.create(this,R.raw.click)


        toolbar.title = ""
        setSupportActionBar(toolbar)

        bu_back.setOnClickListener(){
            mediaPlayerClick.start()
            val intent=Intent(this,MainActivity::class.java)
            if (MyDialogP.playAds){
                mInterstitialAd.adListener = object: AdListener() {
                    override fun onAdClosed() {
                        mInterstitialAd.loadAd(AdRequest.Builder().build())
                        startActivity(intent)
                    }
                }
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                } else {
                    startActivity(intent)
                }
            }else{
                startActivity(intent)
            }
        }

        iv_points_toolbar.setOnClickListener(){
            mediaPlayerClick.start()
            MyDialogP().openDialog(this,tv_points, {isEnable()})
        }
        tv_points.text=points.toString()


        setQuestions(count)
        playSounds(check)
        buAds.visibility=View.GONE

        buAds.setOnClickListener(){
            mediaPlayerClick.start()
            mRewardedVideoAd!!.show()
            whoClicked=true
        }

        answer1.setOnClickListener {
            Answers(answer1)
        }
        answer2.setOnClickListener {
            Answers(answer2)
        }
        answer3.setOnClickListener {
            Answers(answer3)
        }
        answer4.setOnClickListener {
            Answers(answer4)
        }


        bu_Next.setOnClickListener {
            mediaPlayerClick.seekTo(0)
            mediaPlayerClick.start()
            if (points>0){
                if(isCorrect){
                    count++
                    isCorrect=false
                    if (MyDialogP.playAds){
                        mInterstitialAd.adListener = object: AdListener() {
                            override fun onAdClosed() {
                                mInterstitialAd.loadAd(AdRequest.Builder().build())
                                setQuestions(count)
                                isEnable()
                                isVisible()
                                if (showButton){
                                    buAds.visibility=View.VISIBLE
                                }
                            }
                        }
                        if (mInterstitialAd.isLoaded) {
                            mInterstitialAd.show()
                        } else {
                            setQuestions(count)
                            isEnable()
                            isVisible()
                            if (showButton){
                                buAds.visibility=View.VISIBLE
                            }
                        }
                    }else{
                        setQuestions(count)
                        isEnable()
                        isVisible()
                        if (showButton){
                            buAds.visibility=View.VISIBLE
                        }
                    }
                }else{
                    Toast.makeText(this,"يجب عليك معرفة الجواب الصحيح",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"يجب عليك إضافة محاولة واحدة علي الأقل",Toast.LENGTH_LONG).show()
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
    private fun playSounds(check:Boolean){
        if (check){
            mediaPlayerC.setVolume(0F, 0F)
            mediaPlayerW.setVolume(0F, 0F)
            mediaPlayerClick.setVolume(0F, 0F)
        }else{
            mediaPlayerC.setVolume(1F, 1F)
            mediaPlayerW.setVolume(1F, 1F)
            mediaPlayerClick.setVolume(1F, 1F)
        }
    }

    private fun Answers(button: Button){
        if (button.text==mProductList!![count].correct) {
            isCorrect = true
            mediaPlayerC.start()
            notEnable()
            buAds.visibility=View.GONE
        }else{
            mediaPlayerW.start()
            points--
            tv_points.text = Integer.toString(points)
            button.isClickable=false
        }

        if (points==0){
            notEnable()
            buAds.visibility=View.GONE
            MyDialogP().openDialog(this,tv_points, { isEnable() })
        }
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


///////// Companion Object ///////////

    companion object {
            var points=15
            lateinit var mediaPlayerC:MediaPlayer
            lateinit var mediaPlayerW:MediaPlayer
            lateinit var mediaPlayerClick:MediaPlayer
            var showButton=false
            lateinit var mInterstitialAd: InterstitialAd
            var mRewardedVideoAd: RewardedVideoAd? = null

        fun loadRewardedVideoAd() {
            if (!mRewardedVideoAd!!.isLoaded) {
                mRewardedVideoAd!!.loadAd("ca-app-pub-3940256099942544/5224354917",
                        AdRequest.Builder().build())
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
        if (whoClicked){
            whoClicked=false
            showButton=false
            buAds.visibility=View.GONE
        }


    }

    override fun onRewardedVideoAdLeftApplication() {

    }

    override fun onRewardedVideoAdLoaded() {
        showButton=true
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


    }

    override fun onRewardedVideoStarted() {

    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {

    }
}