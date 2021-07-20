package ru.nobird.app.kmm_test.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nobird.app.kmm_test.android.databinding.ItemUserBinding
import ru.nobird.app.kmm_test.data.model.User

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserVH>() {

    private val users = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH =
        UserVH(ItemUserBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.onBind(users[position])
    }

    override fun getItemCount(): Int =
        users.size

    fun updateList(items: List<User>?) {
        with(users) {
            clear()
            addAll(items ?: listOf())
        }
        notifyDataSetChanged()
    }

    inner class UserVH(private val itemUser: ItemUserBinding) : RecyclerView.ViewHolder(itemUser.root) {
        fun onBind(user: User) {
            itemUser.userName.text = user.login
        }
    }
}