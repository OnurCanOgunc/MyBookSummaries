package com.decode.mybooksummaries.presentation.profile

import android.net.Uri
import androidx.annotation.DrawableRes

sealed class ProfileImageState {
    data class UriImage(val uri: Uri) : ProfileImageState()
    data class ResourceImage(@DrawableRes val resId: Int) : ProfileImageState()
    object Default : ProfileImageState()
}