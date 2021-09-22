package com.picpay.desafio.android.presentation

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.DataConsumeType
import com.picpay.desafio.android.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserListAdapter
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupView()

        viewModel.userList.observe(this, Observer { list ->
            adapter.submitNewList(list)
        })

        viewModel.state.observe(this, Observer { state ->
            state?.let{
                with(state){
                    binding.userListProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    when {
                        msgEmpty.isNotEmpty() -> {
                            binding.txtStatus.text = msgEmpty
                            binding.txtStatus.visibility = View.VISIBLE
                        }
                        msgError.isNotEmpty() -> {
                            binding.txtStatus.text = msgError
                            binding.txtStatus.visibility = View.VISIBLE
                        }
                        else -> {
                            binding.txtStatus.visibility = View.GONE
                        }
                    }
                }
                viewModel.stateLoaded()
            }
        })

//        viewModel.loadData(DataConsumeType.BOTH)

    }

    private fun setupView(){
        adapter = UserListAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.onlyInternetOption -> {
                viewModel.loadData(DataConsumeType.INTERNET)
            }
            R.id.onlyDatabaseOption -> {
                viewModel.loadData(DataConsumeType.DATABASE)
            }
            R.id.bothOption -> {
                viewModel.loadData(DataConsumeType.BOTH)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
