package com.example.myshopapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.databinding.SoldItemBinding

class SoldProductAdapter(
    private val onPlus: (SaleItemEntity) -> Unit,
    private val onMinus: (SaleItemEntity) -> Unit,
    private val onRemove: (SaleItemEntity) -> Unit
) : ListAdapter<SaleItemEntity, SoldProductAdapter.SoldProductViewHolder>(SaleItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldProductViewHolder {
        val binding = SoldItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SoldProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SoldProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SoldProductViewHolder(
        private val binding: SoldItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SaleItemEntity) {
            binding.apply {
                tvName.text = item.itemName

                tvPrice.text = "${item.salePrice} x"

                tvQty.text = if (item.quantity % 1.0 == 0.0) {
                    item.quantity.toInt().toString()
                } else {
                    item.quantity.toString()
                }

                btnPlus.setOnClickListener { onPlus(item) }
                btnMinus.setOnClickListener { onMinus(item) }
                btnDelete.setOnClickListener { onRemove(item) }
            }
        }
    }

    class SaleItemDiffCallback : DiffUtil.ItemCallback<SaleItemEntity>() {
        override fun areItemsTheSame(oldItem: SaleItemEntity, newItem: SaleItemEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SaleItemEntity, newItem: SaleItemEntity): Boolean {
            return oldItem.quantity == newItem.quantity &&
                    oldItem.sum == newItem.sum &&
                    oldItem.itemName == newItem.itemName
        }
    }
}