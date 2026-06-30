package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.ProductModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

import android.content.Intent
import com.example.myapplication.ui.login.MovieDetailsActivity

class ProductAdapter(private var products: List<ProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvProductName)
        val price: TextView = view.findViewById(R.id.tvProductPrice)
        val image: ImageView = view.findViewById(R.id.ivProductImage)
        val card: View = view // The whole card
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.name.text = product.name
        holder.price.text = product.category

        // Log URL to help debug if needed (check Logcat)
        android.util.Log.d("ProductAdapter", "Loading image for ${product.name}: ${product.imageUrl}")

        if (product.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(product.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(android.R.drawable.ic_dialog_alert)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_foreground)
        }
        
        holder.card.setOnClickListener {
            val intent = Intent(holder.itemView.context, MovieDetailsActivity::class.java)
            intent.putExtra("movie", product)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = products.size

    fun updateData(newProducts: List<ProductModel>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
