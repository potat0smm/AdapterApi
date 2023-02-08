package com.example.adapterapi

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adapterapi.Api.PostItem
import com.example.adapterapi.databinding.ActivityMainBinding
import com.example.adapterapi.databinding.PostLayoutDialogBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRv()
        getAllPosts()

        binding.fabAddPost.setOnClickListener {
            addPostDialog()
        }
    }
    private fun setupRv(){
        postAdapter = PostAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
            setHasFixedSize(true)
        }
    }
    private fun getAllPosts() {
        lifecycleScope.launchWhenCreated {
            val response = RetrofitInstance.retrofit.getAllPosts()

            if (response.isSuccessful && response.body() !=null){
                postAdapter.differ.submitList(response.body())
                Log.d("response","getAllPosts:${response.body()}")
            }
            else{
                Toast.makeText(this@MainActivity, "Error code:${response.code()}", Toast.LENGTH_LONG ).show()
            }
        }
    }
    private fun addPostDialog(){
        val mDialog = Dialog(this)
        val mBinding = PostLayoutDialogBinding.inflate(layoutInflater)
        mDialog.setContentView(mBinding.root)

        mBinding.btnCancel.setOnClickListener {
            mDialog.dismiss()
        }
        mBinding.btnPost.setOnClickListener {
            if(mBinding.etPostTitle.text.toString().isNotEmpty() &&
                mBinding.etBodyPost.text.toString().isNotEmpty()){

            val title = mBinding.etPostTitle.text.toString()
            val body = mBinding.etBodyPost.text.toString()
            val userID = mBinding.etUserID.text.toString().toInt()

                makePost(title, body, userID)
                Toast.makeText(this,"Post done successfully",
                    Toast.LENGTH_SHORT).show()
                mDialog.dismiss()
            }
            else{
                Toast.makeText(this,"Title && Body cant be empty",
                Toast.LENGTH_SHORT).show()
            }
        }
        mDialog.show()
    }

    private fun makePost(title:String, body:String, userID:Int){
        lifecycleScope.launchWhenCreated {
            val post = PostItem(body,0,title ,userID)

            val response = RetrofitInstance.retrofit.posting(post)

            if (response.isSuccessful && response.body() !=null){
                Log.d("post response","OurPost: ${response.body()}")
            }
            else{
                Log.d("post Error","Error:${response.body()}")
            }
        }
    }
}





















