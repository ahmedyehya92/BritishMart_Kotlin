package com.netservex.caf.features.menu_dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import com.netservex.caf.R
import com.netservex.caf.core.PassedDataFromCategoriesToMenu
import com.netservex.caf.core.RxBus
import com.netservex.caf.features.login.LoginFragment
import com.netservex.entities.CategoryModel
import com.netservex.usecases.usecases.TokenUseCase
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_menu_dialog_fragment_sec.*
import java.util.*

class MenueDialogFragment : androidx.fragment.app.DialogFragment(),
    MenuListAdapter.CustomeListener {

    /*
    @BindView(R.id.btn_home)
    ImageView btnHome;

    @BindView(R.id.btn_login_reg)
    ImageView btnLoginReg;

    @BindView(R.id.btn_offers)
    ImageView btnOffers;

    @BindView(R.id.btnCategories)
    ImageView btnCategories;

    @BindView(R.id.btn_exit)
    ImageView btnExit;
*/
    var menuItemsArrayList: MutableList<CategoryModel>? = null
    var cartListAdapter: MenuListAdapter? = null
    var customListener: CustomButtonListener? = null
    var disposable: Disposable? = null
    var categoriesArrayList: ArrayList<CategoryModel>? = null
    var data: PassedDataFromCategoriesToMenu? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View =
            inflater.inflate(R.layout.fragment_menu_dialog_fragment_sec, container, false)

        disposable = RxBus.subscribe(Consumer<Any> { o ->
            if (o is PassedDataFromCategoriesToMenu) {
                data = o as PassedDataFromCategoriesToMenu?
                //do sth with the data .. you can populate a RecycleView for example
            }
        })
        disposable!!.dispose()

        /*
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customListener.onItemClickListner("home");
            }
        });
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customListener.onItemClickListner("categories");
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customListener.onItemClickListner("exit");
            }
        });
        btnLoginReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customListener.onItemClickListner("loginRegister");
            }
        });
        btnOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customListener.onItemClickListner("offers");
            }
        });
*/return rootView
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        btn_close!!.setOnClickListener { dismiss() }
        menuItemsArrayList = ArrayList<CategoryModel>()
        menuItemsArrayList!!.add(CategoryModel("Home", "-1", ""))
        for (categoryModel in data!!.getCategoriesArrayList()) {
            menuItemsArrayList!!.add(categoryModel)
        }
        menuItemsArrayList!!.add(CategoryModel("Offers", "-1", ""))
        menuItemsArrayList!!.add(CategoryModel("Exit", "-1", ""))
        cartListAdapter = MenuListAdapter(context, menuItemsArrayList!!)
        cartListAdapter?.setCustomButtonListner(this)
        lv_menu_item!!.adapter = cartListAdapter
        lout_login.setOnClickListener {
            dismiss()
            val tokenUseCase = TokenUseCase()
            if(!tokenUseCase.isLoggedIn)
                activity?.supportFragmentManager?.beginTransaction()?.add(R.id.main_fragment_container, LoginFragment(), "login_fragment")?.addToBackStack("")?.commit()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = LinearLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setGravity(Gravity.BOTTOM)
        dialog.window
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val params =
            dialog.window.attributes // change this to your dialog.
        params.y = -100 // Here is the param to set your dialog position. Same with params.x
        dialog.window.attributes = params
        return dialog
    }

    override fun onItemClickListener(
        title: String?,
        itemId: String?,
        position: Int
    ) {
        dismiss()
        customListener!!.onItemClickListner(title, itemId)
    }

    interface CustomButtonListener {
        fun onItemClickListner(title: String?, itemId: String?)
    }

    fun setCustomButtonListner(listener: CustomButtonListener?) {
        customListener = listener
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable!!.dispose() //very important
    }
}