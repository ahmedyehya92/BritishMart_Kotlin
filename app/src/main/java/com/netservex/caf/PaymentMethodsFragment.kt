package com.netservex.caf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.netservex.entities.OrderData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentMethodsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentMethodsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var orderData: OrderData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderData = it.getSerializable(ARG_PARAM1) as OrderData

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_sec, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param orderData Parameter 1.
         * @return A new instance of fragment PaymentMethodsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(orderData: OrderData) =
            PaymentMethodsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, orderData)

                }
            }
    }
}
