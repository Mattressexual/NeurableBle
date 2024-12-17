package com.example.neurableble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.random.Random

class FocusViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(FocusUiState())
    val uiState: StateFlow<FocusUiState> = _uiState.asStateFlow()

    private var scoringJob: Job? = null

    fun startScoring() {
        // Track Job for canceling
        scoringJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                fetchScore()
                yield()
                delay(5000)
            }
        }
    }

    fun stopScoring() {
        scoringJob?.cancel()
    }

    private fun fetchScore() {
        _uiState.update { current -> current.copy(
            score = Random.nextInt(0, 100))
        }
    }
}

data class FocusUiState(
    val score: Int = 0
)