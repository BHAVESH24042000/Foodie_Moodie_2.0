package com.example.foodie_moodie_20

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.foodie_moodie_20.databinding.FragmentOverviewBinding
import com.example.foodiepoodie.dataModels.Result
import com.example.foodie_moodie_20.utils.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foodiemoodie.adapters.loadImage
import org.jsoup.Jsoup


/**
 * A simple [Fragment] subclass.
 * Use the [OverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPE_RESULT_KEY) as Result

        binding.mainImageView.loadImage(myBundle.image)
        binding.titleTextView.text = myBundle.title
        binding.likesTextView.text = myBundle.aggregateLikes.toString()
        binding.timeTextView.text = myBundle.readyInMinutes.toString()
        parseHtml(binding.summaryTextView, myBundle.summary)

        updateColors(myBundle.vegetarian!!, binding.vegetarianTextView, binding.vegetarianImageView)
        updateColors(myBundle.vegan, binding.veganTextView, binding.veganImageView)
        updateColors(myBundle.cheap!!, binding.cheapTextView, binding.cheapImageView)
        updateColors(myBundle.dairyFree!!, binding.dairyFreeTextView, binding.dairyFreeImageView)
        updateColors(myBundle.glutenFree!!, binding.glutenFreeTextView, binding.glutenFreeImageView)
        updateColors(myBundle.veryHealthy!!, binding.healthyTextView, binding.healthyImageView)

        return binding.root
    }

    private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    fun parseHtml(textView: TextView, description: String?){
        if(description != null) {
            val desc = Jsoup.parse(description).text()
            textView.text = desc
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}