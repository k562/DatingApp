package com.example.datingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.datingapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding
//    lateinit var drawerLayout: DrawerLayout
//    lateinit var navigationView: NavigationView
    var actionBarDrawerToggle : ActionBarDrawerToggle ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        actionBarDrawerToggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.Open_drawer,R.string.Close_drawer)

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        binding.navigationView.setNavigationItemSelectedListener (this)




        val navController = findNavController(R.id.fragment)


        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.favourite -> {
                Toast.makeText(this,"Favourite",Toast.LENGTH_SHORT).show()
            }
            R.id.rateus -> {
                Toast.makeText(this,"Rate us",Toast.LENGTH_SHORT).show()
            }
            R.id.shareapp -> {
                Toast.makeText(this,"Share App",Toast.LENGTH_SHORT).show()
            }
            R.id.termsConditions -> {
                Toast.makeText(this,"Terms and conditions",Toast.LENGTH_SHORT).show()
            }
            R.id.privacypolicy -> {
                Toast.makeText(this,"Privace Policy",Toast.LENGTH_SHORT).show()
            }
            R.id.developer -> {
                Toast.makeText(this,"Developer",Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {


        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.close()
        } else {
            super.onBackPressed()
        }


    }
}