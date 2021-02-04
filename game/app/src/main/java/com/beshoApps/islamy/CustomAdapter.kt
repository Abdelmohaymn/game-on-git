package com.beshoApps.islamy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.beshoApps.islamy.Dialogs.Companion.getBool
import com.beshoApps.islamy.Dialogs.Companion.getInt
import com.beshoApps.islamy.Dialogs.Companion.saveInt
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.recycler_elahadith.view.*

class CustomAdapter(val userList:ArrayList<Data>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.recycler_elahadith,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }



    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        val data:Data=userList[position]
        var mediaClick=data.mediaPlayer
        var button=data.button
        holder.button_hadith.text=data.hadithRecycler
        holder.text_hadith_num.text=data.hadithNum



        holder.card_ahadith.animation=AnimationUtils.loadAnimation(holder.card_ahadith.context,R.anim.slide_left_long)

        holder.card_ahadith.setOnClickListener(){
            mediaClick.seekTo(0)
            mediaClick.start()
           if (holder.button_hadith.visibility==View.VISIBLE){
                YoYo.with(Techniques.SlideOutUp).onEnd { holder.button_hadith.visibility=View.GONE }.duration(1000).playOn( holder.button_hadith)
            }else{
                holder.button_hadith.visibility=View.VISIBLE
                YoYo.with(Techniques.SlideInDown).duration(1000).playOn( holder.button_hadith)
            }
            if (position>getInt(holder.card_ahadith.context,"position",0)){
                saveInt(holder.card_ahadith.context,"position",position)
                button.text="الإنتقال لآخر حديث مقروء ${position+1}"
            }

        }

        holder.button_hadith.setOnClickListener(){
            mediaClick.start()
            YoYo.with(Techniques.SlideOutUp).onEnd { holder.button_hadith.visibility=View.GONE }.duration(1000).playOn( holder.button_hadith)
        }

        holder.image_share.setOnClickListener(){
            mediaClick.start()
            val intent= Intent()
            intent.action= Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT, "${holder.button_hadith.text}" + "\n"
                        + "حمل تطبيق إسلامي من هنا" + "\n" +
                        "https://play.google.com/store/apps/details?id=com.beshoApps.islamy"
            )
            intent.type="text/plain"
            holder.button_hadith.context.startActivity(Intent.createChooser(intent, "شارك الحديث مع أصدقائك"))

        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button_hadith=itemView.buttonRecycler as Button
        val text_hadith_num=itemView.hadith_number as TextView
        val image_share=itemView.iv_share_hadith as ImageView
        val card_ahadith=itemView.card_ahadith as CardView

    }



}