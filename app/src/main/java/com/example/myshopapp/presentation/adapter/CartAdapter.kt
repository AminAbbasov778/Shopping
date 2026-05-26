package com.example.myshopapp.presentation.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopapp.databinding.ItemCartBinding
import com.example.myshopapp.presentation.state.CartItem

class CartAdapter(
    val onPlus: (CartItem) -> Unit,
    val onMinus: (CartItem) -> Unit,
    val onRemove: (CartItem) -> Unit,
    val onDiscount: (Long, Double) -> Unit
) : ListAdapter<CartItem, CartAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem.product.id == newItem.product.id
            }

            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class VH(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        var textWatcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)

        holder.binding.tvName.text = item.product.name
        holder.binding.tvQty.text = item.qty.toString()

        holder.binding.btnPlus.setOnClickListener { onPlus(item) }
        holder.binding.btnMinus.setOnClickListener { onMinus(item) }
        holder.binding.btnDelete.setOnClickListener { onRemove(item) }

        holder.binding.etDiscount.removeTextChangedListener(holder.textWatcher)

        val currentText = holder.binding.etDiscount.text.toString()
        val currentDouble = currentText.toDoubleOrNull() ?: 0.0


        if (item.discount == 0.0) {
            if (currentText.isNotEmpty()) {
                holder.binding.etDiscount.setText("")
            }
        } else if (currentDouble != item.discount) {
            val newText = if (item.discount % 1.0 == 0.0) {
                item.discount.toLong().toString()
            } else {
                item.discount.toString()
            }
            holder.binding.etDiscount.setText(newText)
            holder.binding.etDiscount.setSelection(holder.binding.etDiscount.text?.length ?: 0)
        }

        holder.textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: ""

                if (text.endsWith(".")) return

                val d = text.toDoubleOrNull() ?: 0.0
                if (item.discount != d) {

                    onDiscount(item.product.id, d)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        holder.binding.etDiscount.addTextChangedListener(holder.textWatcher)
    }
    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)
        holder.binding.etDiscount.removeTextChangedListener(holder.textWatcher)
        holder.textWatcher = null
    }
}