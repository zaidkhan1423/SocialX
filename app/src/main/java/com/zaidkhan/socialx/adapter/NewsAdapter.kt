package com.zaidkhan.socialx.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaidkhan.socialx.databinding.ItemRecyclerViewBinding
import com.zaidkhan.socialx.model.Article
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class NewsAdapter(private val context: Context): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsList: List<Article>? = null

    class NewsViewHolder(val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList?.get(position)

        val isoString = news?.publishedAt.toString()
        val dateTime = OffsetDateTime.parse(isoString)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val publishTime = dateTime.format(formatter)

        holder.binding.txtPublishAt.text = publishTime
        holder.binding.txtSource.text = news?.source?.name
        holder.binding.txtTitle.text = news?.title
        holder.binding.txtDescription.text = news?.description
        Glide.with(context).load(news?.urlToImage).into(holder.binding.imgNewsImage)
    }

    override fun getItemCount(): Int = newsList?.size ?: 0

    fun submitList(list: List<Article>?) {
        this.newsList = list
        notifyDataSetChanged()
    }
}