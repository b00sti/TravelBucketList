package com.b00sti.travelbucketlist.ui.public_bucket_list

import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.b00sti.travelbucketlist.base.AdapterNavigator
import com.b00sti.travelbucketlist.base.BaseAdapter
import com.b00sti.travelbucketlist.base.BaseVH
import com.b00sti.travelbucketlist.base.BaseVM
import com.b00sti.travelbucketlist.databinding.ItemBucketToDosBinding
import com.b00sti.travelbucketlist.model.Bucket

/**
 * Created by b00sti on 27.07.2018
 */
private val ITEM_CALLBACK = object : DiffUtil.ItemCallback<Bucket.ToDo>() {
    override fun areItemsTheSame(oldItem: Bucket.ToDo, newItem: Bucket.ToDo): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Bucket.ToDo, newItem: Bucket.ToDo): Boolean = oldItem == newItem
}

interface BucketToDosNavigator : AdapterNavigator<Bucket.ToDo>

class BucketToDosAdapterVM(val item: Bucket.ToDo) : BaseVM<BucketToDosNavigator>() {
    fun onItemClicked() = getNavigator().onItemClicked(item)
}

class BucketToDosAdapter(val callback: BucketToDosNavigator) : BaseAdapter<Bucket.ToDo, BucketToDosAdapter.VH>(ITEM_CALLBACK) {
    override fun getViewHolder(parent: ViewGroup): VH = VH(ItemBucketToDosBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    inner class VH(binding: ItemBucketToDosBinding) : BaseVH<ItemBucketToDosBinding, BucketToDosAdapterVM>(binding), BucketToDosNavigator {

        override fun onItemClicked(item: Bucket.ToDo) = callback.onItemClicked(item)

        override fun getViewModel(position: Int): BucketToDosAdapterVM {
            val viewModel = BucketToDosAdapterVM(getItem(adapterPosition))
            viewModel.setNavigator(this)
            return viewModel
        }

        override fun getBindingVariable(): Int = BR.vm
    }
}