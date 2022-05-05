package com.arbonik.project

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

internal object JSONHelper {
    private const val FILE_MEMES_NAME = "izbrannie-memes.json"
    fun exportToJSON(context: Context?, dataList: List<Meme>?) {
        val gson = Gson()
        var dataItems = DataItems()
        dataItems.setItems(dataList)
        val jsonString = gson.toJson(dataItems)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = context?.openFileOutput(FILE_MEMES_NAME, Context.MODE_PRIVATE)
            fileOutputStream?.write(jsonString.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun importFromJSON(context: Context?): List<Meme?>? {
        var streamReader: InputStreamReader? = null
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = context?.openFileInput(FILE_MEMES_NAME)
            streamReader = InputStreamReader(fileInputStream)
            val gson = Gson()
            val dataItems = gson.fromJson(streamReader, DataItems::class.java)
            return dataItems.getItems()
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            if (streamReader != null) {
                val page = Optional.ofNullable(streamReader)
                try {
                    streamReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return emptyList()
    }
    private class DataItems {
        private var memes: List<Meme>? = null

        fun getItems() : List<Meme>? {
            return memes
        }
        fun setItems(memes: List<Meme>?) {
            this.memes = memes
        }
    }
}