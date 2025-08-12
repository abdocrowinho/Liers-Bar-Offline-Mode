package com.example.liersbarofflinemode.ui.Utltiy

import com.example.domain.Entitys.RoomEntity

class LanDialogResponse private constructor(
    var data: RoomEntity?,
    var isLoading: Boolean?, var error: String?,
    var timeOut: String?

) {
    data class Builder(
        private var data: RoomEntity? = null,
        private var isLoading: Boolean? = null,
        private var error: String? = null,
        private var timeOut: String? = null
    ) {
        fun setData(data: RoomEntity?) = apply { this.data = data }
        fun setIsLoading(isLoading: Boolean) = apply { this.isLoading = isLoading }
        fun setError(error: String) = apply { this.error = error }
        fun setTimeOutError(error: String) = apply { this.timeOut = error }
        fun build() = LanDialogResponse(data, isLoading, error, timeOut)

    }
}