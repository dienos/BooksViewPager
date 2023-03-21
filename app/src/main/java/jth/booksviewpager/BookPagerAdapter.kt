package jth.booksviewpager

import android.animation.ObjectAnimator
import android.app.Application
import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import jth.booksviewpager.data.model.BookItem
import jth.booksviewpager.databinding.BookItemBinding
import jth.booksviewpager.view.Rotate
import kotlin.math.roundToInt

class BookPagerAdapter(private val list: List<BookItem>) :
    RecyclerView.Adapter<BookPagerAdapter.PagerViewHolder>() {

    private lateinit var binding: BookItemBinding

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: BookItem, position: Int) {
            val context = itemView.context

            var backWidth = 0
            var coverWidth = 0
            var height = 0

            when (position) {
                0 -> {
                    backWidth = 43
                    coverWidth = backWidth * 5
                    height = backWidth * 10
                }

                1 -> {
                    backWidth = 40
                    coverWidth = backWidth * 5
                    height = backWidth * 7
                }

                2 -> {
                    backWidth = 45
                    coverWidth = backWidth * 5
                    height = backWidth * 10
                }

                3 -> {
                    backWidth = 35
                    coverWidth = backWidth * 5
                    height = backWidth * 15
                }
            }

            rotate(
                view = binding.bookBackImage,
                duration = 100,
                propertyName = "rotationY",
                start = 0f,
                end =  -61f
            )
            rotate(
                view = binding.bookCoverImage,
                duration = 100,
                propertyName = "rotationY",
                start = 0f,
                end = 10f
            )

            binding.bookBackImage.layoutParams.height = convertDPtoPX(context, height)
            binding.bookBackImage.layoutParams.width = convertDPtoPX(context, backWidth)

            binding.bookCoverImage.layoutParams.height = convertDPtoPX(context, height)
            binding.bookCoverImage.layoutParams.width = convertDPtoPX(context, coverWidth)

            binding.bookBackImage.setBackgroundResource(item.bookBackResource)
            binding.bookCoverImage.setBackgroundResource(item.bookCoverResource)


        }
    }

    private fun getDeviceWidth(context: Context): Int {
        var width = 0
        val display = context.resources?.displayMetrics

        display?.widthPixels?.let {
            width = it
        }

        return width
    }

    private fun convertDPtoPX(context: Context, dp: Int): Int {
        val density: Float = context.resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }

    private fun rotate(
        view: ImageView,
        propertyName: String,
        duration: Long,
        start: Float,
        end: Float
    ) {
        val anim = ObjectAnimator.ofFloat(view, propertyName, start, end)
        anim.duration = duration
        anim.start()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding.root)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(list[position], position)
    }


    /**
     * Setup a new 3D rotation on the container view.
     *
     * @param position the item that was clicked to show a picture, or -1 to show the list
     * @param start the start angle at which the rotation must begin
     * @param end the end angle of the rotation
     */
    private fun applyRotation(position: Int, start: Float, end: Float) {
        // Find the center of the container
        val centerX: Float = binding.root.width / 2.0f
        val centerY: Float = binding.root.height / 2.0f

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        val rotation = Rotate(start, end, centerX, centerY, 310.0f, true)
        rotation.duration = 500
        rotation.fillAfter = true
        rotation.interpolator = AccelerateInterpolator()
        rotation.setAnimationListener(DisplayNextView(position))
        binding.root.startAnimation(rotation)
    }

    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    inner class DisplayNextView(private val mPosition: Int) :
        Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
            binding.root.post(SwapViews(mPosition))
            binding.root.clearAnimation()
        }

        override fun onAnimationRepeat(animation: Animation) {}
    }

    /**
     * This class is responsible for swapping the views and start the second
     * half of the animation.
     */
    inner class SwapViews(private val mPosition: Int) : Runnable {
        override fun run() {
            val centerX: Float = binding.root.width / 2.0f
            val centerY: Float = binding.root.height / 2.0f
            val rotation: Rotate = if (mPosition > -1) {
                binding.bookCoverImage.visibility = View.VISIBLE
                binding.bookCoverImage.requestFocus()
                Rotate(90f, 180f, centerX, centerY, 310.0f, false)
            } else {
                binding.bookCoverImage.visibility = View.VISIBLE
                binding.bookCoverImage.requestFocus()
                Rotate(90f, 0f, centerX, centerY, 310.0f, false)
            }

            rotation.duration = 5000
            rotation.fillAfter = true
            rotation.interpolator = DecelerateInterpolator()
            binding.root.startAnimation(rotation)
        }
    }
}