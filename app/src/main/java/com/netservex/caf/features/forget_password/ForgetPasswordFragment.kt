package com.netservex.caf.features.forget_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer

import com.netservex.caf.R
import com.netservex.caf.core.RequestIntervalHandler2
import com.netservex.caf.core.isValidEmail
import com.netservex.caf.core.textValue
import kotlinx.android.synthetic.main.fragment_forget_password_sec.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgetPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgetPasswordFragment : Fragment(), ForgetPasswordView {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var requestIntervalHandler: RequestIntervalHandler2? = null
    private val presenter: ForgetPasswordPresenter by lazy {
        ForgetPasswordImplPresenter(this)
    }

    private val tryAgainTriggerObserever = Observer<Int> {
        when (it) {
            //  1 -> categoryId?.let { presenter?.getSubCategories(currentPage, it) }
        }
    }


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
        return inflater.inflate(R.layout.fragment_forget_password_sec, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_recover_password!!.setOnClickListener {

            if(et_email.isValidEmail())
                presenter.forgetPassword(et_email.textValue())

            /* fragmentManager!!.beginTransaction()
                 .add(R.id.main_fragment_container, CartFragment(), "cart_fragment")
                 .addToBackStack(null).commit()*/
        }

        requestIntervalHandler =
            RequestIntervalHandler2(lout_loading_interval_view_container, context!!, false)
        requestIntervalHandler?.tryAgainTrigger?.observe(this, tryAgainTriggerObserever)
        requestIntervalHandler?.setMessageErrorTextColor(R.color.colorredMain)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForgetPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun successfulForgetPassword() {
        Toast.makeText(context,"check your email to recover password", Toast.LENGTH_LONG).show()
        activity?.onBackPressed()
    }

    override fun failedForgetPassword() {
        finishLoading()
        Toast.makeText(context,"this email is not found, try another", Toast.LENGTH_LONG).show()
    }

    override fun showLoading() {
        requestIntervalHandler?.showLoadingView()
    }

    override fun finishLoading() {
        requestIntervalHandler?.finishLoading()
    }

    override fun connectionError(message: String?) {
        finishLoading()
        Toast.makeText(context,message?:"Unknown error", Toast.LENGTH_LONG).show()
    }

    override fun faildLoading(message: Any) {
    }
}
