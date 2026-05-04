package com.guilhermenettizp.redesocial.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilhermenettizp.redesocial.databinding.PostItemBinding
import com.guilhermenettizp.redesocial.model.Post

class PostAdapter(private val posts: MutableList<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        holder.binding.txtDescricao.text = post.descricao

        post.imagem?.let {
            holder.binding.imgPost.setImageBitmap(it)
        }

        holder.binding.txtCidade.text = "${post.cidade}"
    }

    fun adicionarPosts(novosPosts: List<Post>) {
        posts.addAll(novosPosts)
        notifyDataSetChanged()
    }
}