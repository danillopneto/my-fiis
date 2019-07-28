package br.go.dpn.meusfiis.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import br.go.dpn.meusfiis.R

class LineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var filterItem: TextView = itemView.findViewById(R.id.filterItemTxt)
    var deleteButton: ImageButton = itemView.findViewById(R.id.deleteFilterItem)
}