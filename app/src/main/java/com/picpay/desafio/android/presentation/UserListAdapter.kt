package com.picpay.desafio.android.presentation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.picpay.desafio.android.domain.User
import com.picpay.desafio.android.framework.network.UserNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserListAdapter(private val context: Context) : ListAdapter<User, RecyclerView.ViewHolder>(
    UserListDiffCallback()
) {

    private val adapterScope = CoroutineScope(Dispatchers.Main)

    fun submitNewList(list: List<User>) {
        adapterScope.launch {
            submitList(list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.list_item_user, parent, false)
        return UserListItemViewHolder(
            binding as ListItemUserBinding
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is UserListItemViewHolder -> {viewHolder.bind(getItem(position))}
        }
    }

}