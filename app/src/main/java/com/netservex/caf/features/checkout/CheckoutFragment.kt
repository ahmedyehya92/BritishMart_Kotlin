package com.netservex.caf.features.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.netservex.caf.R
import com.netservex.caf.core.isValidEditText
import com.netservex.caf.core.isValidEmail
import com.netservex.caf.core.textValue
import com.netservex.caf.features.payment_credit.CreditPaymentFragment
import com.netservex.entities.OrderData
import kotlinx.android.synthetic.main.fragment_checkout.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_checkout.setOnClickListener {
            if(et_first_name.isValidEditText() && et_second_name.isValidEditText()&&
                et_email.isValidEmail()&&
                et_Address.isValidEditText()&&
                et_mobile_number.isValidEditText())
            {
                getFragmentManager()?.beginTransaction()?.add(R.id.main_fragment_container, CreditPaymentFragment.newInstance(
                    OrderData(et_first_name.textValue(),
                        et_second_name.textValue(),
                        et_email.textValue(),
                        et_Address.textValue(),
                        et_mobile_number.textValue())
                ), "")?.addToBackStack("")?.commit()

            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckoutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
