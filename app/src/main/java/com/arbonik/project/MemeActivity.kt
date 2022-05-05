package com.arbonik.project

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_meme.*


class MemeActivity : AppCompatActivity() {
    lateinit var favouriteList: List<Meme?>
    lateinit var meme: Meme
    var memes: ArrayList<Meme> = ArrayList()
    var prefs: SharedPreferences? = null
    var width: Int = 0
    var height: Int = 0
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x
        height = size.y

        try {
            prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            if (prefs?.getInt("theme", 0) == AppCompatDelegate.MODE_NIGHT_NO) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } catch (e: java.lang.Exception) {
        }
        setSupportActionBar(toolbar)

        val id: Int = intent.getIntExtra("id", 0)
        val title: String? = intent.getStringExtra("title")
        val description: String? = intent.getStringExtra("description")
        val url: String? = intent.getStringExtra("url")

        meme = Meme(id, title, description, url)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "MEME"

        MainActivity().parse(meme_image, meme.url)
        val k = meme_image.layoutParams.width / meme_image.layoutParams.height
        meme_image.layoutParams.width = size.x / 100 * 70
        meme_image.layoutParams.height = (height / 2.8).toInt()
        meme_image.scaleType = ImageView.ScaleType.FIT_XY

        if (meme_image.layoutParams.height > meme_image.layoutParams.width){
            meme_image.layoutParams.height = (height)
        }

        meme_title.text = meme.title
        try {
            favouriteList = JSONHelper.importFromJSON(this)!!
        } catch (e: Exception) {}

        val clickListenerAddToFavourite = View.OnClickListener {
            try {
                if (!favouriteList.contains(meme)) {
                    for (meme1 in favouriteList) {
                        memes.add(meme1!!)
                    }
                    memes.add(meme)
                    JSONHelper.exportToJSON(applicationContext, memes)
                    val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.favourites_anim)
                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            back_layer.layoutParams.width = height
                            back_layer.visibility = VISIBLE
                            izbrannoe_button.isClickable = false
                        }

                        override fun onAnimationRepeat(animation: Animation?) {
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            back_layer.visibility = GONE
                            recreate()
                        }
                    })
                    back_layer.startAnimation(animation)
                }
            } catch (e: Exception) {
                memes.add(meme)
                JSONHelper.exportToJSON(applicationContext, memes)
            }
        }

        val clickListenerRemoveFromFavourite = View.OnClickListener {
            for (meme1 in favouriteList) {
                memes.add(meme1!!)
            }
            memes.remove(meme)
            JSONHelper.exportToJSON(applicationContext, memes)
            val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.favourites_anim2)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    izbrannoe_button.setBackgroundColor(android.R.color.transparent)
                    back_layer.layoutParams.width = height
                    back_layer.visibility = VISIBLE
                    izbrannoe_button.isClickable = false
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    back_layer.visibility = GONE
                    recreate()
                }
            })
            back_layer.startAnimation(animation)
        }
        try {
            if (favouriteList.contains(meme)) {
                izbrannoe_button.text = "В избранном"
                val color = resources.getColor(R.color.izbrannoe)
                izbrannoe_button.setBackgroundColor(color)
                izbrannoe_button.setOnClickListener(clickListenerRemoveFromFavourite)
            } else {
                izbrannoe_button.setOnClickListener(clickListenerAddToFavourite)
            }
        } catch (e: Exception) {
            izbrannoe_button.setOnClickListener(clickListenerAddToFavourite)
        }
        save_button.setOnClickListener {
            meme_image.setDrawingCacheEnabled(true)
            val b: Bitmap = meme_image.getDrawingCache()
            if (MediaStore.Images.Media.insertImage(contentResolver, b, "title", "desc") != null) {
                Toast.makeText(applicationContext, "Сохранено", Toast.LENGTH_LONG).show()
            }
        }
        editor_button.setOnClickListener {
            intent = Intent(applicationContext, EditorActivity::class.java)
            intent.putExtra("image_src", meme.url)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu_meme, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MEME FROM MEME FINDER")
                shareIntent.putExtra(Intent.EXTRA_TEXT, meme.url)
                shareIntent.type = "text/plain"
                startActivity(shareIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}



