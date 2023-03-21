package jth.booksviewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import jth.booksviewpager.data.model.BookItem
import jth.booksviewpager.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val list: ArrayList<BookItem> = arrayListOf()
        //list.add(BookItem(R.drawable.a, R.drawable.all_a))
        //list.add(BookItem(R.drawable.b, R.drawable.all_b))
        //list.add(BookItem(R.drawable.c, R.drawable.all_c))
        //list.add(BookItem(R.drawable.d, R.drawable.all_d))
        binding.viewPager2.adapter = BookPagerAdapter(list)
        //todo 이미지 추가 로직 필요

        val pageMarginPx = convertDPtoPX(-200)
        val pagerWidth = convertDPtoPX(360)
        val screenWidth = resources.displayMetrics.widthPixels
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        binding.viewPager2.apply {
            adapter = BookPagerAdapter(list)
            offscreenPageLimit = 1
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            })
            setPageTransformer { page, position ->
                page.translationX = position * -offsetPx
            }
        }
    }

    private fun convertDPtoPX(dp: Int): Int {
        val density: Float = resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }
}