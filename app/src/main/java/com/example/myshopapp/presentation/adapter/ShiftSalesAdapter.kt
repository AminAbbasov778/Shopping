package com.example.myshopapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.databinding.ItemSaleBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ShiftSalesAdapter(
    private val onClick: (SaleEntity) -> Unit
) : ListAdapter<SaleEntity, ShiftSalesAdapter.VH>(DIFF) {



    companion object {


        val DIFF = object : DiffUtil.ItemCallback<SaleEntity>() {
            override fun areItemsTheSame(oldItem: SaleEntity, newItem: SaleEntity): Boolean {
                return oldItem.localId == newItem.localId
            }

            override fun areContentsTheSame(oldItem: SaleEntity, newItem: SaleEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class VH(val binding: ItemSaleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemSaleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val binding = holder.binding

        binding.tvShortDoc.text = "Çek: #${item.shortDocumentId}"
        binding.tvCashier.text = "• Kassir: ${item.cashier}"
        binding.tvTotal.text = "%.2f %s".format(item.total, if(item.currency == "AZN") "₼" else item.currency)

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        binding.tvTime.text = sdf.format(Date(item.createdAt))

        if (item.cashSum > 0.0) {
            binding.tvCashSum.visibility = View.VISIBLE
            binding.tvCashSum.text = "Nağd: %.2f ₼".format(item.cashSum)
        } else {
            binding.tvCashSum.visibility = View.GONE
        }

        if (item.cardSum > 0.0) {
            binding.tvCardSum.visibility = View.VISIBLE
            binding.tvCardSum.text = "Kart: %.2f ₼".format(item.cardSum)
        } else {
            binding.tvCardSum.visibility = View.GONE
        }

        if (item.bonusSum > 0.0) {

            binding.tvBonusSum.visibility = View.VISIBLE
            binding.tvBonusSum.text = "Bonus: %.2f ₼".format(item.bonusSum)
        } else {


            binding.tvBonusSum.visibility = View.GONE
        }

        if (item.changeSum > 0.0) {


            binding.tvChangeSum.visibility = View.VISIBLE
            binding.tvChangeSum.text = "Qalıq pul: %.2f ₼".format(item.changeSum)
        } else {
            binding.tvChangeSum.visibility = View.GONE
        }

        val rrnText = item.rrn?.let { "RRN: $it" } ?: ""
        val uuidText = item.uuid?.let { "UUID: ${it.take(6)}..." } ?: ""

        if (rrnText.isNotEmpty() || uuidText.isNotEmpty()) {


            binding.tvRrn.visibility = View.VISIBLE
            binding.tvRrn.text = "$rrnText  |  $uuidText".trim().removePrefix("|").removeSuffix("|")
        } else {
            binding.tvRrn.visibility = View.GONE
        }

        binding.btnReprint.setOnClickListener {
            onClick(item)
        }
    }
}