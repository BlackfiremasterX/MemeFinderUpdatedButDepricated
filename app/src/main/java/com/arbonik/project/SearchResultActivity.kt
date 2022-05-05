package com.arbonik.project

import android.content.SharedPreferences
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_search_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity() {
    var search_query: String = ""
    var width: Int = 0
    var height: Int = 0
    val memes: ArrayList<Meme> = ArrayList()
    var elems: Int = 1
    var prefs: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        try {
            prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            if (prefs?.getInt("theme", 0) == AppCompatDelegate.MODE_NIGHT_NO) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } catch (e: java.lang.Exception) {
        }

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x
        height = size.y

        search_query = intent.getStringExtra("search_query").toString()
        supportActionBar!!.title = "Результат поиска: $search_query"
        load_data(elems)

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    elems += 1
                    load_data(elems)
                }
            }
        })
    }
    fun load_data(elems: Int) {
        val apiService = MemeApiService.create()
        val context = this
        val results = apiService.search_with_query(elems, 30, search_query, "popular", "all", "ru")
        results.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>?, response: Response<Data>?) {
                for (meme in response!!.body().memes) {
                    Log.d("meme: ", meme.url!!)
                    memes.add(meme)
                }
                if (recycler_view.adapter != null) {
                    recycler_view.adapter!!.notifyDataSetChanged()
                } else {
                    recycler_view.adapter = RecAdapter(context, width / 2.2, width / 2.2, memes)
                }
            }
            override fun onFailure(call: Call<Data>?, t: Throwable?) {
                t!!.printStackTrace()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}