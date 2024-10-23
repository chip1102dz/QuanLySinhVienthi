package com.example.myapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemBinding

class UserAdapter(private val database: UserDatabase, private val itemOnClickListener: ItemOnClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(val itemBinding: ItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    private var list = mutableListOf<User>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: MutableList<User>){
        this.list = list
        notifyDataSetChanged()
    }
    fun deleteItem(position: Int){
        val id = list[position].id
        database.delete(id)
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemBinding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = list[position]
        holder.itemBinding.tvName.text = user.name
        holder.itemBinding.tvDate.text = user.date
        holder.itemBinding.imgView.setImageResource(user.image)
        holder.itemView.setOnLongClickListener {
            val id = holder.adapterPosition
            deleteItem(id)
            true
        }
        holder.itemBinding.btnDetails.setOnClickListener {
            itemOnClickListener.OnClickItem(user)
        }
        holder.itemView.setOnClickListener {
            itemOnClickListener.OnClickItem1(user)
        }
    }
}