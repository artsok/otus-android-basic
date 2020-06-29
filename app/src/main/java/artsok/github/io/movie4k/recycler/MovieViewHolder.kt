package artsok.github.io.movie4k.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import artsok.github.io.movie4k.R

class MovieViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    var itemImage: ImageView = item.findViewById(R.id.card_image)
    var itemTitle: TextView = item.findViewById(R.id.card_title)
}
