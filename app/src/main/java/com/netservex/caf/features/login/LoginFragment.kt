package com.netservex.caf.features.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.netservex.caf.R
import com.netservex.caf.features.cart.CartFragment
import kotlinx.android.synthetic.main.fragment_login_sec.*

class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        val rootView: View =
            inflater.inflate(R.layout.fragment_login_sec, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_login!!.setOnClickListener {
            fragmentManager!!.beginTransaction()
                .add(R.id.main_fragment_container, CartFragment(), "cart_fragment")
                .addToBackStack(null).commit()
        }
        new_account!!.setOnClickListener {
            /*fragmentManager!!.beginTransaction()
                .add(R.id.main_fragment_container, RegisterFragment(), "register_fragment")
                .addToBackStack("").commit()*/
        }
        btn_forget_password!!.setOnClickListener {
            /* fragmentManager!!.beginTransaction().add(
                 R.id.main_fragment_container,
                 ForgetPasswordFragment(),
                 "forget_password_fragment"
             ).addToBackStack("").commit()*/
        }
        dummy_click!!.isSoundEffectsEnabled = false
        dummy_click!!.setOnClickListener { }

    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
// TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}