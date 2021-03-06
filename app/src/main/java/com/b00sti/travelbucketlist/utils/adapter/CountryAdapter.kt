package com.b00sti.travelbucketlist.utils.adapter

import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.b00sti.travelbucketlist.base.AdapterNavigator
import com.b00sti.travelbucketlist.base.BaseAdapter
import com.b00sti.travelbucketlist.base.BaseVH
import com.b00sti.travelbucketlist.base.BaseVM
import com.b00sti.travelbucketlist.databinding.ItemCountryBinding


/**
 * Created by b00sti on 12.03.2018
 */

enum class CONTINENTS {
    AFRICA,
    ANTARCTICA,
    ASIA,
    EUROPE,
    NORTH_AMERICA,
    OCEANIA,
    SOUTH_AMERICA
}

data class CountryItem(val name: String = "", val continent: CONTINENTS, val visited: Boolean, val photoUri: String, val desc: String)

interface CountryNavigator : AdapterNavigator<CountryItem> {
    fun onDeleteClicked(countryItem: CountryItem)
}

class CountryViewModel(val countryItem: CountryItem) : BaseVM<CountryNavigator>() {
    fun onItemClicked() = getNavigator().onItemClicked(countryItem)
    fun onDeleteClicked() = getNavigator().onDeleteClicked(countryItem)
}

private val ITEM_CALLBACK = object : DiffUtil.ItemCallback<CountryItem>() {
    override fun areItemsTheSame(oldItem: CountryItem, newItem: CountryItem): Boolean = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: CountryItem, newItem: CountryItem): Boolean = oldItem == newItem
}

class CountryAdapter(val callback: CountryNavigator) : BaseAdapter<CountryItem, CountryAdapter.CountryHolder>(ITEM_CALLBACK) {
    override fun getViewHolder(parent: ViewGroup): CountryHolder = CountryHolder(ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    inner class CountryHolder(binding: ItemCountryBinding) : BaseVH<ItemCountryBinding, CountryViewModel>(binding), CountryNavigator {

        override fun onItemClicked(item: CountryItem) = callback.onItemClicked(item)
        override fun onDeleteClicked(countryItem: CountryItem) = callback.onDeleteClicked(countryItem)

        override fun getViewModel(position: Int): CountryViewModel {
            val viewModel = CountryViewModel(getItem(adapterPosition))
            viewModel.setNavigator(this)
            return viewModel
        }

        override fun getBindingVariable(): Int = BR.vm
    }
}