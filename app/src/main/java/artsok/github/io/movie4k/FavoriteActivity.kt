//package artsok.github.io.movie4k
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.ItemTouchHelper
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
//import artsok.github.io.movie4k.DataStore.Companion.movies
//
//
//class FavoriteActivity : AppCompatActivity() {
//
//    private lateinit var favoriteRecycler: RecyclerView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.favorite_movies)
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        initFavoriteRecycler()
//
//    }
//
//    private fun initFavoriteRecycler() {
//        favoriteRecycler = findViewById(R.id.favorite_rc)
//        favoriteRecycler.layoutManager =
//            GridLayoutManager(this, GridLayoutManager.VERTICAL)
//
//        val favoriteAdapter = FavoriteAdapter(
//            favoriteRecycler.rootView as ViewGroup,
//            this,
//            movies,
//            this@FavoriteActivity::personItemClicked
//        )
//
//        val itemTouchHelper = object :
//            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//                movies.filter { it.favorite }[position].favorite = false
//                favoriteRecycler.adapter?.notifyItemRemoved(position)
//            }
//        }
//        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(favoriteRecycler)
//        favoriteRecycler.adapter = favoriteAdapter
//        favoriteAdapter.registerAdapterDataObserver(EmptyObserver(favoriteRecycler))
//    }
//
//    private fun personItemClicked(movie: Movie) {
//        val intent = Intent(this, MovieActivity::class.java)
//        with(intent) {
//            putExtra(MainActivityOLD.MARKER, movie)
//        }
//        startActivity(intent)
//    }
//
//    inner class EmptyObserver(private val recyclerView: RecyclerView) : AdapterDataObserver() {
//
//        init {
//            isFavoriteRecyclerEmpty()
//        }
//
//        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
//            super.onItemRangeRemoved(positionStart, itemCount)
//            isFavoriteRecyclerEmpty()
//        }
//
//        private fun isFavoriteRecyclerEmpty() {
//            val emptyViewVisible = movies.count(Movie::favorite) == 0
//            if (recyclerView.adapter != null && emptyViewVisible) {
//                val layout = inflateEmptyView(recyclerView)
//                layout.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
//                recyclerView.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
//            }
//        }
//
//        private fun inflateEmptyView(view: View): View {
//            return LayoutInflater.from(view.context).inflate(
//                R.layout.empty_favorite_list, view.parent as ViewGroup
//            )
//        }
//    }
//}
