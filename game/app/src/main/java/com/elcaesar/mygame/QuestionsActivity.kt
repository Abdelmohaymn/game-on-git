package com.elcaesar.mygame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
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
    private lateinit var mInterstitialAd: InterstitialAd
    var mRewardedVideoAd: RewardedVideoAd? = null

    private var mProductList: List<Questions>? = null
    private var mDBHelper: DatabaseHelper? = null
    var count:Int =0
    var isCorrect =false


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
            MyDialogP().openDialog(this,tv_points, {isEnable()})
            mediaPlayerClick.start()
        }
        tv_points.text=points.toString()


        setQuestions(count)
        playSounds(check)

        buAds.setOnClickListener(){
            if (mRewardedVideoAd!!.isLoaded) {
                mRewardedVideoAd!!.show()
            } else {
                Toast.makeText(this, "يرجى الانتظار 5 ثواني \n ثم اظغط على الزر ", Toast.LENGTH_SHORT).show()
                loadRewardedVideoAd()
            }
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
                            }
                        }
                        if (mInterstitialAd.isLoaded) {
                            mInterstitialAd.show()
                        } else {
                            setQuestions(count)
                            isEnable()
                        }
                    }else{
                        setQuestions(count)
                        isEnable()
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
        }else{
            mediaPlayerW.start()
            points--
            tv_points.text = Integer.toString(points)
            button.isClickable=false
        }

        if (points==0){
            notEnable()
            MyDialogP().openDialog(this,tv_points, { isEnable() })
        }
    }



///////// Companion Object ///////////

    companion object {
            var points=10
            lateinit var mediaPlayerC:MediaPlayer
            lateinit var mediaPlayerW:MediaPlayer
            lateinit var mediaPlayerClick:MediaPlayer
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
    private fun loadRewardedVideoAd() {
        if (!mRewardedVideoAd!!.isLoaded) {
            mRewardedVideoAd!!.loadAd("ca-app-pub-3940256099942544/5224354917",
                AdRequest.Builder().build())
        }
    }

    override fun onRewardedVideoAdClosed() {
        loadRewardedVideoAd()
    }

    override fun onRewardedVideoAdLeftApplication() {

    }

    override fun onRewardedVideoAdLoaded() {
        Toast.makeText(this, "لقد تم تهيئة الإعلان بنجاح", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoAdOpened() {

    }

    override fun onRewardedVideoCompleted() {

    }

    override fun onRewarded(p0: RewardItem?) {
        points++
        tv_points.text= points.toString()
    }

    override fun onRewardedVideoStarted() {

    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {

    }
}