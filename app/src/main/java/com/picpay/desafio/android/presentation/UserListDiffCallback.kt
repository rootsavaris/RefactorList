package com.picpay.desafio.android.presentation

import androidx.recyclerview.widget.DiffUtil
import com.picpay.desafio.android.domain.User

class UserListDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.username.equals(newItem.username)
    }
    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}