package com.arbonik.project

import androidx.appcompat.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {
    var search_query: String = ""
    var width: Int = 0
    var height: Int = 0
    val memes: ArrayList<Meme> = ArrayList()
    var counter: Int = 1
    var elems: Int = 1
    var recyclerView: RecyclerView? = null
    var toolbar: ActionBar? = null
    val mergeAdapter = MergeAdapter()
    var recAdapter: RecAdapter? = null
    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, null)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    elems += 1
                    load_data(elems)
                }
            }
        })

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        load_data(elems)
    }
    fun load_data(elems: Int) {
        val apiService = MemeApiService.create()
        val results = apiService.search(elems, 30, "popular", "all", "ru")
        val context = getContext() as Context
        results.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>?, response: Response<Data>?) {
                try {
                    for (meme in response!!.body().memes) {
                        memes.add(meme)
                    }
                } catch (e: Exception) {
                    for (meme in response!!.body().memes) {
                        memes.add(meme)
                    }
                }

                if (recyclerView?.adapter != null) {
                    recAdapter!!.notifyDataSetChanged()
                } else {
                    recAdapter = RecAdapter(context, width / 2.3, width / 2.5, memes)
                    mergeAdapter.addAdapter(recAdapter!!)
                    recyclerView?.adapter = mergeAdapter
                }
            }
            override fun onFailure(call: Call<Data>?, t: Throwable?) {
                t!!.printStackTrace()
            }
        })
    }
}