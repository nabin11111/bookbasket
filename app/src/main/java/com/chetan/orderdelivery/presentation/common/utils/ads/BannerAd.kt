package com.chetan.jobnepal.utils.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize

import com.google.android.gms.ads.AdView

@Composable
fun BannerAd(
    bId: String,
    modifier: Modifier = Modifier
){
    AndroidView(factory = {context ->
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = bId
            loadAd(AdRequest.Builder().build())
        }
    },
        modifier = modifier
        )
}