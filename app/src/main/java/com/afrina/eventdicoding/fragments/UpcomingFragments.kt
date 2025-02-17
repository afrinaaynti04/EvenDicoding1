
package com.afrina.eventdicoding.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrina.eventdicoding.network.Network
import com.afrina.eventdicoding.data.response.DetailData
import com.afrina.eventdicoding.databinding.FragmentUpcomingFragmentsBinding
import com.afrina.eventdicoding.ui.DetailActivity
import com.afrina.eventdicoding.ui.viewmodel.MainViewModel
import com.afrina.eventdicoding.ui.adapter.VerticalListAdapter

class UpcomingFragments : Fragment() {
    private lateinit var rvVerticalAdapter: VerticalListAdapter
    private lateinit var binding : FragmentUpcomingFragmentsBinding

    //createview
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingFragmentsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        val mainViewVertical = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        mainViewVertical.listEvent.observe(viewLifecycleOwner) { value ->
            setEventDataVertical(value)
        }
        mainViewVertical.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        if(!Network.isNetworkAvailable(context)) {
            Toast.makeText(context,"No internet connection. Please turn on your network", Toast.LENGTH_LONG).show()
            mainViewVertical.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }
    }
    private fun setEventDataVertical(event : List<DetailData>) {
        rvVerticalAdapter.submitList(event)
    }
    private fun setupRecyclerView() {

        rvVerticalAdapter = VerticalListAdapter(requireContext()) { detailData ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("EXTRA_DETAIL", detailData)
            startActivity(intent)
        }
        binding.verticalOnly.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        binding.verticalOnly.adapter = rvVerticalAdapter
    }

    private fun showLoading(isLoading : Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}