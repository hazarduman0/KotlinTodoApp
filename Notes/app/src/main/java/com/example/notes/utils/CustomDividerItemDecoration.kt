package com.example.notes.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R

class CustomDividerItemDecoration(context: Context, orientation: Int) :
    DividerItemDecoration(context, orientation) {

    private val divider: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.custom_divider)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val position = parent.getChildAdapterPosition(child)

            if (position != parent.adapter?.itemCount?.minus(1)) {
                val top = child.bottom + params.bottomMargin
                val bottom = top + (divider?.intrinsicHeight ?: 0)
                divider?.setBounds(left, top, right, bottom)
                divider?.draw(c)
            }
        }
    }
}