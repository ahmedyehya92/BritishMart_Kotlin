package com.netservex.caf.features.register

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.netservex.caf.R
import com.netservex.caf.core.*
import com.netservex.caf.features.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_register_sec.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class RegisterFragment : BaseFragment(), RegisterView {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var requestIntervalHandler: RequestIntervalHandler2
    private val tryAgainTriggerObserever = Observer<Int> {
        when (it) {
          //  1 -> categoryId?.let { presenter?.getSubCategories(currentPage, it) }
        }
    }
    private val presenter: RegisterPresenter by lazy {
        RegisterImplPresenter(this)
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
        val rootView: View =
            inflater.inflate(R.layout.fragment_register_sec, container, false)

        return rootView
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requestIntervalHandler =
            RequestIntervalHandler2(lout_loading_interval_view_container, context!!, false)
        requestIntervalHandler.tryAgainTrigger.observe(this, tryAgainTriggerObserever)
        requestIntervalHandler.setMessageErrorTextColor(R.color.colorredMain)

        btn_register.setOnClickListener {
            if(et_first_name.isValidEditText() &&
                et_last_name.isValidEditText() &&
                et_email.isValidEmail() &&
                et_password.isPasswordValid() &&
                et_confirm_password.isConfirmPasswordValid(et_password.textValue())&&
                et_phone.isValidEditText())

                presenter.register(et_first_name.textValue(), et_last_name.textValue(), et_email.textValue(), et_password.textValue(), et_confirm_password.textValue(), et_phone.textValue())
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun successfulRegister() {
        Toast.makeText(context,"You will receive email to activate account!", Toast.LENGTH_LONG).show()
        activity?.onBackPressed()
    }

    override fun failedRegister() {

    }

    override fun showLoading() {
        requestIntervalHandler.showLoadingView()
    }

    override fun finishLoading() {
        requestIntervalHandler.finishLoading()
    }

    override fun connectionError(message: String?) {
        Toast.makeText(context,message?:"Unknown error", Toast.LENGTH_LONG).show()

    }

    override fun faildLoading(message: Any) {

    }

    override fun onDestroy() {
        presenter.onDestroy(this)
        super.onDestroy()
    }
}
