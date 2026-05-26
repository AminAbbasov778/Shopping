package com.example.myshopapp.presentation.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshopapp.util.LogTags
import com.example.myshopapp.R
import com.example.myshopapp.databinding.FragmentProductListBinding
import com.example.myshopapp.presentation.adapter.ProductAdapter
import com.example.myshopapp.presentation.state.ProductWithQty
import com.example.myshopapp.presentation.util.collectEvents
import com.example.myshopapp.presentation.viewmodel.CartViewModel
import com.example.myshopapp.presentation.viewmodel.ProductListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    lateinit var  binding: FragmentProductListBinding



    private lateinit var adapter: ProductAdapter

    private val viewModel: ProductListViewModel by viewModels()

    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
     binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LogTags.PRODUCT, "ProductListFragment: created")

        setupRecycler()
        observeUI()
        setupSearch()
        collectEvents(viewModel)

        binding.btnAddProduct.setOnClickListener {
            Log.d(LogTags.PRODUCT, "Navigate AddProductFragment")
            findNavController().navigate(R.id.action_productListFragment_to_addProductFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProducts()
    }

    private fun setupRecycler() {
        adapter = ProductAdapter(
            onIncrease = { product ->
                cartViewModel.add(product)
            },
            onDecrease = { product ->

                cartViewModel.minus(product.id)
            },
            onEdit = { product ->
                val bundle = bundleOf("productId" to product.id)
                findNavController().navigate(
                    R.id.action_productListFragment_to_addProductFragment,
                    bundle
                )
            },
            onDelete = { product ->


                viewModel.deleteProduct(product)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.recyclerView.adapter = adapter
    }

    private fun observeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine(

                viewModel.state,
                cartViewModel.state
            ) { productState, cartState ->

                val cartQtyMap = cartState.items.associate { it.product.id to it.qty }
                productState.filteredProducts.map { product ->

                    ProductWithQty(
                        product = product,
                        qty = cartQtyMap[product.id] ?: 0.0
                    )
                }
            }.collect { combined ->
                adapter.submitList(combined)
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            viewModel.search(text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
