package com.beshoApps.islamy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.beshoApps.islamy.AdsClass.Companion.bannerAd
import com.beshoApps.islamy.AdsClass.Companion.mAdView
import com.beshoApps.islamy.Dialogs.Companion.getInt
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_elahadith.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.io.FileOutputStream
import java.io.OutputStream


@Suppress("DEPRECATION")
class ElahadithActivity : AppCompatActivity() {

    private var mProductList: List<myAhadith>? = null
    private var mDBHelper: DatabaseHelper? = null
    lateinit var mediaPlayerClick: MediaPlayer

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elahadith)

        MobileAds.initialize(this)
        mAdView = findViewById(R.id.banner_ahadith)
        bannerAd()

        mDBHelper = DatabaseHelper(this)

        //Check exists database
        val database = applicationContext.getDatabasePath(DatabaseHelper.DBNAME)
        if (!database.exists()) {
            mDBHelper!!.readableDatabase
            //Copy db
            copyDatabase(this)
        }

        //Get product list in db when db exists
        mProductList = mDBHelper!!.listProduct4
        ///////////////////
        mediaPlayerClick = MediaPlayer.create(this, R.raw.click)
        iv_dark_mode.visibility= View.GONE
        iv_points_toolbar.visibility=View.GONE
        tv_points.visibility=View.GONE
        tv_theGames.text="الأحاديث"
        //toolBar_ahadith.setBackgroundColor(Color.parseColor("#AE7B42"))////814F16////D7BD6B
        changeColor(R.color.blue2)

        val users=ArrayList<Data>()
//1895
        for(i in 0 .. 1895){
            users.add(
                Data(
                    mProductList!![i].sanad + mProductList!![i].hadith,
                    "الحديث رقم ${i + 1}", mediaPlayerClick, bu_saved_hadith
                )
            )
        }
        //users.add(Data("احمد"))

        val adapter =CustomAdapter(users)
        my_recycler_view.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        my_recycler_view.adapter=adapter

        var pos= getInt(this, "position", 0)
        bu_saved_hadith.text="الإنتقال لآخر حديث مقروء ${pos+1}"
        bu_saved_hadith.setOnClickListener(){
            mediaPlayerClick.start()
            my_recycler_view.scrollToPosition(pos)
        }

        bu_back.setOnClickListener(){
            mediaPlayerClick.start()
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
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

    fun changeColor(resourseColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, resourseColor)
        }

    }

    override fun onBackPressed() {
        mediaPlayerClick.start()
        super.onBackPressed()
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
    }
}