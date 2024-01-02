package com.rmd.business.newsapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rmd.business.newsapp.R
import com.rmd.business.newsapp.data.datasource.local.db.ArticleDB
import com.rmd.business.newsapp.databinding.ActivityMainBinding
import com.rmd.business.newsapp.domain.repository.ArticleRepository
import com.rmd.business.newsapp.presentation.viewmodel.ArticleViewModel
import com.rmd.business.newsapp.presentation.viewmodel.factory.ArticleViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var articleRepository: ArticleRepository
    private lateinit var articleViewModelFactory: ArticleViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articleRepository = ArticleRepository(ArticleDB(this))
        articleViewModelFactory = ArticleViewModelFactory(application, articleRepository)
        articleViewModel = ViewModelProvider(this, articleViewModelFactory)[ArticleViewModel::class.java]


        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.headlinesFragment, R.id.favouritesFragment, R.id.searchFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}