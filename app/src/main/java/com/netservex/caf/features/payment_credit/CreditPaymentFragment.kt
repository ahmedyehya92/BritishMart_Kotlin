package com.netservex.caf.features.payment_credit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.myfatoorah.sdk.model.executepayment.MFExecutePaymentRequest
import com.myfatoorah.sdk.model.executepayment_cardinfo.MFCardInfo
import com.myfatoorah.sdk.model.executepayment_cardinfo.MFDirectPaymentResponse
import com.myfatoorah.sdk.utils.MFAPILanguage
import com.myfatoorah.sdk.views.MFResult
import com.myfatoorah.sdk.views.MFSDK
import com.netservex.caf.R
import com.netservex.caf.core.isValidEditText
import com.netservex.caf.core.textValue
import com.netservex.caf.features.home.HomeFragment
import com.netservex.caf.features.main.MainActivity
import com.netservex.entities.BASE_URL_DIRECT_PAYMENT
import com.netservex.entities.OrderData
import com.netservex.entities.TOKEN_DIRECT_PAYMENT
import kotlinx.android.synthetic.main.fragment_credit_payment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreditPaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreditPaymentFragment : Fragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credit_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MFSDK.init(BASE_URL_DIRECT_PAYMENT, TOKEN_DIRECT_PAYMENT)

        btPayWithDirectPayment.setOnClickListener {

            pbLoading.visibility= View.VISIBLE

           /* if (fragmentManager!!.getFragments() != null && fragmentManager!!.getFragments()
                    .size > 0
            ) {
                for (i in 0 until fragmentManager!!.getFragments().size) {
                    val mFragment: Fragment =
                        fragmentManager!!.getFragments().get(i)
                    if (mFragment != null) {
                        fragmentManager!!.beginTransaction().remove(mFragment).commit()
                        fragmentManager!!.beginTransaction()
                            .add(R.id.main_fragment_container, HomeFragment(), "men").commit()
                    }
                }
            }*/
            val request = MFExecutePaymentRequest(2, 0.100)

// val mfCardInfo = MFCardInfo("Your token here")

            if(etCardNumber.isValidEditText()&& etExpiryMonth.isValidEditText() && etExpiryYear.isValidEditText() && etSecurityCode.isValidEditText())
            {
                val mfCardInfo = MFCardInfo(etCardNumber.textValue(), "%02d".format(etExpiryMonth.textValue().toInt()), etExpiryYear.textValue(), etSecurityCode.textValue(), true, false)

                MFSDK.executeDirectPayment(activity!!, request, mfCardInfo, MFAPILanguage.EN){ invoiceId: String, result: MFResult<MFDirectPaymentResponse> ->
                    when(result){
                        is MFResult.Success ->  {
                            pbLoading.visibility = View.GONE
                            Log.d("TAG", "Response: " + Gson().toJson(result.response))
                            Toast.makeText(activity, "Order placed!",Toast.LENGTH_LONG).show()
                            startActivity(Intent(context, MainActivity::class.java))
                            activity?.finish()




                            /*if (fragmentManager!!.getFragments() != null && fragmentManager!!.getFragments()
                                    .size > 0
                            ) {
                                for (i in 0 until fragmentManager!!.getFragments().size) {
                                    val mFragment: Fragment =
                                        fragmentManager!!.getFragments().get(i)
                                    if (mFragment != null) {
                                        fragmentManager!!.beginTransaction().remove(mFragment).commit()
                                        fragmentManager!!.beginTransaction()
                                            .add(R.id.main_fragment_container, HomeFragment(), "men").commit()
                                    }
                                }
                            }*/
                            Log.d("TAG", "invoiceId: $invoiceId")
                        }


                        is MFResult.Fail -> {
                            //Toast.makeText(activity, "Try again!",Toast.LENGTH_LONG).show()
                            pbLoading.visibility = View.GONE
                            val errorJson= Gson().toJson(result.error)
                            Log.d("TAG", "Fail: " + Gson().toJson(result.error))
                            Toast.makeText(activity, result.error.message,Toast.LENGTH_LONG).show()
                        }

                    }
                }
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
         * @return A new instance of fragment CreditPaymentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(orderData: OrderData) =
            CreditPaymentFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, orderData)
                }
            }
    }
}
