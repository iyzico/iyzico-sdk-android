package com.android.iyzicosdk.util

import android.R
import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade


internal class IyziCoImageLoaderUtility {
    companion object {

        fun imageLoaderWithCacheFit(
            activity: Context,
            url: String? = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
            imageView: ImageView,
            placeHolder: Int
        ) {


            Glide.with(activity)
                .load(url)
                .fitCenter()
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(placeHolder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)

        }

        fun setImageForSvg(context: Context, url: String, imageView: ImageView) {
            val requestBuilder: RequestBuilder<PictureDrawable>
            requestBuilder = Glide.with(context)
                .`as`(PictureDrawable::class.java)
                .error(com.android.iyzicosdk.R.drawable.iyzico_ic_garanti_bank)
                .transition(withCrossFade())
                .centerCrop()
                .listener(SvgSoftwareLayerSetter())

            requestBuilder.load(url).into(imageView)

        }




    }

}