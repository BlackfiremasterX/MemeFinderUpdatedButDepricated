package com.arbonik.project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.activity_editor.*

class EditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        val editor = PhotoEditor.Builder(this, photoEditorView).build()
        editor.setBrushDrawingMode(true)
        val color = resources.getColor(android.R.color.holo_red_dark)
        editor.brushColor = color
        editor.brushSize = 10F
        editor.brushEraser()
        editor.setOpacity(100)
        val url = intent.getStringExtra("image_src")
        runOnUiThread { Picasso.get().load(url).into(photoEditorView.source) }
    }
}