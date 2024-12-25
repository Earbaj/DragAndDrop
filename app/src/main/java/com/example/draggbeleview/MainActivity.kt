package com.example.draggbeleview

import android.content.ClipData
import android.content.ClipDescription
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.draggbeleview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val imageUrls = arrayListOf(
        "https://next-images.123rf.com/index/_next/image/?url=https://assets-cdn.123rf.com/index/static/assets/top-section-bg.jpeg&w=3840&q=75",
        "https://www.shutterstock.com/image-photo/calm-weather-on-sea-ocean-600nw-2212935531.jpg",
        "https://media.istockphoto.com/id/1317323736/photo/a-view-up-into-the-trees-direction-sky.jpg?s=612x612&w=0&k=20&c=i4HYO7xhao7CkGy7Zc_8XSNX_iqG0vAwNsrH1ERmw2Q=",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQ1ipeQbFseUM_GzxwMoQj45w9HtAnu4eu5w&s",
        "https://images.pexels.com/photos/355508/pexels-photo-355508.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.unsplash.com/photo-1462536943532-57a629f6cc60?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3",
        "https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg?cs=srgb&dl=pexels-hsapir-1054655.jpg&fm=jpg",
        "https://media.istockphoto.com/id/1322277517/photo/wild-grass-in-the-mountains-at-sunset.jpg?s=612x612&w=0&k=20&c=6mItwwFFGqKNKEAzv0mv6TaxhLN3zSE43bWmFN--J5w=",
        "https://images.pexels.com/photos/1165991/pexels-photo-1165991.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val imageContainer: LinearLayout = findViewById(R.id.imageContainer)
        val dropContainer: LinearLayout = findViewById(R.id.dropContainer)

        // Populate the horizontal list with ImageViews
        for (url in imageUrls) {
            val imageView = createImageView(url)
            imageContainer.addView(imageView)
            setupDrag(imageView) // Make the ImageView draggable
        }

        // Set up drop targets for all ImageViews in the drop container
        for (i in 0 until dropContainer.childCount) {
            val dropTarget = dropContainer.getChildAt(i)
            setupDrop(dropTarget)
        }

    }

    private fun createImageView(url: String): ImageView {
        val imageView = ImageView(this)
        val params = LinearLayout.LayoutParams(200, 200)
        params.setMargins(16, 16, 16, 16)
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        // Load the image from the URL using Glide
        Glide.with(this)
            .load(url)
            .into(imageView)

        // Tag the ImageView with its URL for drag-and-drop functionality
        imageView.tag = url
        return imageView
    }

    private fun setupDrag(draggableView: View) {
        draggableView.setOnLongClickListener { v ->
            val clipItem = ClipData.Item(v.tag as? CharSequence)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val draggedData = ClipData("Dragged Image URL", mimeTypes, clipItem)

            // Start drag-and-drop
            v.startDragAndDrop(
                draggedData,
                View.DragShadowBuilder(v),
                null,
                0
            )
            true // Return true to indicate the drag started
        }
    }

    private fun setupDrop(dropTarget: View) {
        dropTarget.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    Log.d(TAG, "ON DRAG STARTED")
                    if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        (v as? ImageView)?.apply {
                            alpha = 0.5F // Highlight the drop target
                            invalidate()
                        }
                        true
                    } else {
                        false
                    }
                }

                DragEvent.ACTION_DROP -> {
                    Log.d(TAG, "ON DROP")
                    val item: ClipData.Item = event.clipData.getItemAt(0)
                    val dragData = item.text.toString() // Extract the dragged URL

                    // Load the dropped image into the target ImageView
                    if (v is ImageView) {
                        Glide.with(v.context)
                            .load(dragData)
                            .into(v)
                        v.alpha = 1.0F // Restore full opacity
                    }
                    true
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.d(TAG, "ON DRAG ENTERED")
                    (v as? ImageView)?.apply {
                        alpha = 0.3F // Highlight the drop target more
                        invalidate()
                    }
                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    Log.d(TAG, "ON DRAG EXITED")
                    (v as? ImageView)?.apply {
                        alpha = 0.5F // Restore semi-transparency
                        invalidate()
                    }
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.d(TAG, "ON DRAG ENDED")
                    (v as? ImageView)?.apply {
                        alpha = 1.0F // Restore full opacity
                    }
                    true
                }

                else -> false // Return false for unhandled actions
            }
        }
    }

}