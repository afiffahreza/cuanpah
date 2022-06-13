package com.exercise.cuanpah.ui.profile

import android.content.Context
import android.content.Intent
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
import com.exercise.cuanpah.databinding.FragmentProfileBinding
import com.exercise.cuanpah.ui.SplashActivity
import com.exercise.cuanpah.ui.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupAction()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction() {

        profileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore), "")
        )[ProfileViewModel::class.java]

        profileViewModel.getUser().observe(requireActivity()) {
            Log.e("PROFILE", it.name)
            binding.profileName.text = it.name
        }

        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
            startActivity(Intent(requireContext(), SplashActivity::class.java))
        }

    }
}