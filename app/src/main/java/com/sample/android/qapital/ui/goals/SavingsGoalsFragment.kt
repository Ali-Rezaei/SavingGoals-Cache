package com.sample.android.qapital.ui.goals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sample.android.qapital.BR
import com.sample.android.qapital.R
import com.sample.android.qapital.data.SavingsGoal
import com.sample.android.qapital.databinding.FragmentSavingsGoalsBinding
import com.sample.android.qapital.ui.detail.DetailActivity
import com.sample.android.qapital.util.CurrencyFormatter
import com.sample.android.qapital.util.Resource
import com.sample.android.qapital.viewmodels.SavingsGoalsViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_savings_goals.view.*
import javax.inject.Inject

class SavingsGoalsFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment(), SavingsGoalClickCallback {

    @Inject
    lateinit var viewModelFactory: SavingsGoalsViewModel.SavingsGoalsViewModelFactory

    @Inject
    lateinit var currencyFormatter: CurrencyFormatter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModel =
            ViewModelProviders.of(this, viewModelFactory)[SavingsGoalsViewModel::class.java]

        val root = inflater.inflate(R.layout.fragment_savings_goals, container, false)
        val binding = FragmentSavingsGoalsBinding.bind(root).apply {
            setVariable(BR.vm, viewModel)
            lifecycleOwner = viewLifecycleOwner
        }

        with(root) {
            swipe_refresh.apply {
                setColorSchemeColors(
                    ContextCompat.getColor(context, R.color.colorAccent),
                    ContextCompat.getColor(context, R.color.colorPrimary),
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
                )

                setOnRefreshListener {
                    binding.vm?.refresh()
                    binding.vm?.showSavingsGoals()
                }
            }

            retry_button.setOnClickListener {
                binding.vm?.showSavingsGoals()
            }
        }

        val controller = AnimationUtils
            .loadLayoutAnimation(
                context,
                R.anim.layout_animation_from_bottom
            )

        viewModel.liveData.observe(this, Observer {
            if (it is Resource.Success) {
                val adapter = SavingsGoalsAdapter(it.data!!, currencyFormatter, this)
                root.list.apply {
                    setHasFixedSize(true)
                    layoutAnimation = controller
                    list.adapter = adapter
                    scheduleLayoutAnimation()
                }
            }
        })

        return root
    }

    override fun onClick(savingsGoal: SavingsGoal, poster: ImageView) {
        val intent = Intent(context, DetailActivity::class.java).apply {
            putExtras(Bundle().apply {
                putParcelable(DetailActivity.EXTRA_SAVINGS_GOAL, savingsGoal)
            })
        }
        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            Pair<View, String>(poster, ViewCompat.getTransitionName(poster))
        )

        ActivityCompat.startActivity(requireContext(), intent, activityOptions.toBundle())
    }
}