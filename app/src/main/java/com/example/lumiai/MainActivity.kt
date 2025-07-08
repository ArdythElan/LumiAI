package com.example.lumiai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.lumiai.data.database.ChatDatabase
import com.example.lumiai.data.repository.ChatRepository
import com.example.lumiai.viewmodel.ChatViewModel
import com.example.lumiai.ui.navigation.LumiNavGraph
import com.example.lumiai.data.network.RetrofitInstance

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // Bouw de Room database
            val database = Room.databaseBuilder(
                applicationContext,
                ChatDatabase::class.java,
                "chat_db"
            ).build()

            // Bouw de repository met dao + retrofit api
            val repository = ChatRepository(
                dao = database.chatDao(),
                api = RetrofitInstance.api
            )

            // ViewModel met repository
            val viewModel = ChatViewModel(repository)

            // Start NavGraph
            LumiNavGraph(navController, viewModel)
        }
    }
}
