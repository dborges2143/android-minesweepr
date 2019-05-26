package com.borges.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val numbers = listOf(
        "1a", "2a", "3a", "4a", "5a", "6a",
        "1b", "2b", "3b", "4b", "5b", "6b",
        "1c", "2c", "3c", "4c", "5c", "6c"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MainAdapter(this, numbers)
        grid_view.adapter = adapter

        grid_view.setOnItemClickListener { _, _, position, _ ->
            Log.d(TAG, "onItemClick: clicked position $position")
            Toast.makeText(this, "You clicked ${numbers[position]}", Toast.LENGTH_LONG).show()
        }
    }
}
