package com.example.draggbeleview

import android.content.ClipData
import android.content.ClipDescription
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.draggbeleview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.tvGreeting.text = getString(R.string.greeting)
        Glide.with(this).asBitmap()
            .load(getString(R.string.source_url))
            .into(binding.ivSource)
        Glide.with(this).asBitmap()
            .load(getString(R.string.target_url))
            .into(binding.ivTarget)
        setupDrag(binding.ivSource)
        setupDrop(binding.ivTarget)
    }

    private fun setupDrag(draggableView: View) {
        draggableView.setOnLongClickListener { v ->
            // Set a tag for the draggable view
            v.tag = getString(R.string.source_url)

            val label = "Dragged Image URL"
            val clipItem = ClipData.Item(v.tag as? CharSequence)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val draggedData = ClipData(label, mimeTypes, clipItem)

            // Start drag and drop
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
                            alpha = 0.5F // Make the target semi-transparent
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
                    val dragData = item.text.toString() // Extract the dragged data (URL or path)

                    // Load the new image into the target ImageView
                    if (v is ImageView) {
                        Glide.with(v.context)
                            .load(dragData) // Use the dragged URL or path
                            .into(v)
                        v.alpha = 1.0F // Restore the full opacity
                    }
                    true
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.d(TAG, "ON DRAG ENTERED")
                    (v as? ImageView)?.apply {
                        alpha = 0.3F // Highlight the target with more transparency
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