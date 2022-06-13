package com.exercise.cuanpah.ui.point

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.exercise.cuanpah.data.UserPreference
import com.exercise.cuanpah.databinding.FragmentPointBinding
import com.exercise.cuanpah.ui.ViewModelFactory
import com.exercise.cuanpah.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PointFragment : Fragment() {

    private lateinit var pointViewModel: PointViewModel
    private var _binding: FragmentPointBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPointBinding.inflate(inflater, container, false)

        setupAction()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction() {
        Thread {
            while (_binding != null) {
                val binding = _binding ?:break
                val a = activity as? MainActivity ?:break

                a.runOnUiThread {
                    pointViewModel = ViewModelProvider(
                        this,
                        ViewModelFactory(UserPreference.getInstance(requireContext().dataStore), "")
                    )[PointViewModel::class.java]

                    pointViewModel.getUser().observe(requireActivity()) { user ->
                        Log.e("POINT", user.toString())
                        pointViewModel.getPoint(user.id).observe(requireActivity()) {
                            when (it) {
                                "200" -> {
                                    Log.e("PointFragment", "OK")
                                    binding.point.text = user.point.toString()
                                }
                                else -> {
                                    Log.e("PointFragment", "Error")
                                }
                            }
                        }
                    }
                }
                Thread.sleep(30000)
            }
        }.start()
    }

}