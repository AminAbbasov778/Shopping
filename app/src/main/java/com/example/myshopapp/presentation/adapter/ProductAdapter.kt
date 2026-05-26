package com.example.myshopapp.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopapp.util.LogTags
import com.example.myshopapp.data.local.entity.ProductEntity
import com.example.myshopapp.databinding.ItemProductBinding
import com.example.myshopapp.presentation.state.ProductWithQty

class ProductAdapter(
    private val onIncrease: (ProductEntity) -> Unit,
    private val onDecrease: (ProductEntity) -> Unit,
    private val onEdit: (ProductEntity) -> Unit,
    private val onDelete: (ProductEntity) -> Unit,
) : ListAdapter<ProductWithQty, ProductAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ProductWithQty>() {
            override fun areItemsTheSame(old: ProductWithQty, new: ProductWithQty) =
                old.product.id == new.product.id

            override fun areContentsTheSame(old: ProductWithQty, new: ProductWithQty) =
                old == new
        }
    }

    inner class VH(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Log.d(LogTags.PRODUCT, "onBindViewHolder: $position")

        val item = getItem(position)
        val product = item.product
        val qty = item.qty

        holder.binding.tvName.text = product.name
        holder.binding.tvPrice.text = "%.2f ₼".format(product.salePrice)

        if (qty > 0) {
            holder.binding.tvQty.text = "Miqdar: ${qty.toInt()}"
            holder.binding.tvAmount.visibility = View.VISIBLE
            holder.binding.tvAmount.text = "Cəmi: %.2f ₼".format(item.totalAmount)
        } else {
            holder.binding.tvQty.text = ""
            holder.binding.tvAmount.visibility = View.GONE
        }

        holder.binding.btnPlus.setOnClickListener { onIncrease(product) }
        holder.binding.btnMinus.setOnClickListener { onDecrease(product) }
        holder.binding.btnEdit.setOnClickListener { onEdit(product) }
        holder.binding.btnDelete.setOnClickListener { onDelete(product) }
    }
}
