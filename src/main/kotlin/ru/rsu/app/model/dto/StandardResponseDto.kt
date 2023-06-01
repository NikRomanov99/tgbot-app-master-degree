package ru.rsu.app.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class StandardResponseDto(val SUCCESS: Boolean, val CODE: Int)
