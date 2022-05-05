package com.arbonik.project

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.preference.PreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    var search_query: String = ""
    var width: Int = 0
    var height: Int = 0
    val mainFragment = MainFragment()
    val favouriteFragment = FavouriteFragment()
    var prefs: SharedPreferences? = null
    var is_on_favourite = false
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            if (prefs?.getInt("theme", 0) == AppCompatDelegate.MODE_NIGHT_NO) {
                setTheme(AppCompatDelegate.MODE_NIGHT_NO)
                window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.navigationBarColor = Color.TRANSPARENT
            }
            else if (prefs?.getInt("theme", 0) == AppCompatDelegate.MODE_NIGHT_YES) {
                setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                window.navigationBarColor = Color.TRANSPARENT
            }
        } catch (e: Exception) {
            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.navigationBarColor = Color.TRANSPARENT
            setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x
        height = size.y

        val mainFragment = MainFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_viewer, mainFragment).commit()
        mainFragment.width = width
        mainFragment.toolbar = supportActionBar

        if (Intent.ACTION_SEARCH == intent.action) {
            Toast.makeText(applicationContext, "asd", Toast.LENGTH_LONG).show()
            search_query = intent.getStringExtra(SearchManager.QUERY).toString()
            val intent = Intent(this, SearchResultActivity::class.java)
            intent.putExtra("search_query", search_query)
            startActivity(intent)
        }
    }
    fun parse(image: ImageView, url: String?) {
        runOnUiThread { Picasso.get().load(url).into(image) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(true)
        return true
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_toolbar -> {
                intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.favourite_toolbar -> {
                if (!is_on_favourite) {
                    val favouriteFragment = FavouriteFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_viewer, favouriteFragment).commit()
                    favouriteFragment.width = width
                    favouriteFragment.mFragmentManager = supportFragmentManager
                    item.icon = getDrawable(R.drawable.ic_baseline_home_24)
                    is_on_favourite = true
                } else {
                    val mainFragment = MainFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_viewer, mainFragment).commit()
                    mainFragment.width = width
                    mainFragment.toolbar = supportActionBar
                    item.icon = getDrawable(R.drawable.ic_star_toolbar)
                    is_on_favourite = false
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
interface MemeApiService {

    @GET("api/templates-share")
    fun search(@Query("page") page: Int,
               @Query("items_on_page") items: Int,
               @Query("sort") sort: String,
               @Query("type") type: String,
               @Query("lang") lang: String): Call<Data>
    @GET("api/templates-share")
    fun search_with_query(@Query("page") page: Int,
               @Query("items_on_page") items: Int,
               @Query("query") query: String,
               @Query("sort") sort: String,
               @Query("type") type: String,
               @Query("lang") lang: String): Call<Data>

    companion object Factory {
        fun create(): MemeApiService {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.meme-arsenal.ru/")
                    .build()

            return retrofit.create(MemeApiService::class.java)
        }
    }
}
