package com.netservex.caf.features.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.FragmentManager
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.netservex.caf.R
import com.netservex.caf.core.PassedDataFromCategoriesToMenu
import com.netservex.caf.features.base.BaseActivity
import com.netservex.caf.features.categories_fragment.CategoriesFragment
import com.netservex.caf.features.login.LoginFragment
import com.netservex.caf.features.menu_dialog.MenueDialogFragment
import com.netservex.caf.features.offers.OffersListFragment
import com.netservex.caf.features.subcategories.SubCategoriesFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MenueDialogFragment.CustomButtonListener {

    var textCartItemCount: TextView? = null
    var menueDialogFragment: MenueDialogFragment? = null
    var disposable: Disposable? = null
    var data: PassedDataFromCategoriesToMenu? = null
    private var mHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* disposable = RxBus.subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof PassedDataFromCategoriesToMenu) {
                    data= (PassedDataFromCategoriesToMenu) o;
                    //do sth with the data .. you can populate a RecycleView for example
                }
            }
        });

        RxBus.publish(data);

        disposable.dispose();
*/setupActionBar()
        getSupportFragmentManager().beginTransaction()
            .add(R.id.main_fragment_container, CategoriesFragment(), "men").commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.main_menu, menu)
        val cartItem = menu.findItem(R.id.action_cart)
        val actionView = MenuItemCompat.getActionView(cartItem)
        textCartItemCount = actionView.findViewById(R.id.cart_badge)
        setupBadge()
        actionView.setOnClickListener { onOptionsItemSelected(cartItem) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                if (getSupportFragmentManager().findFragmentByTag("cart_fragment") == null) {
                    getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_fragment_container, LoginFragment(), "login_fragment")
                        .addToBackStack("").commit()
                }
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupBadge() {}
    fun setupActionBar() {
        setSupportActionBar(main_toolbar)
        val actionBar: ActionBar? = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayUseLogoEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.icon_left)
        main_toolbar!!.setNavigationOnClickListener {
            if (getSupportFragmentManager().findFragmentByTag("menu_dialog") == null) {
                val fm: FragmentManager = getSupportFragmentManager()
                menueDialogFragment = MenueDialogFragment()
                menueDialogFragment?.setCustomButtonListner(this@MainActivity)
                menueDialogFragment?.show(fm, "menu_dialog")
            }
        }
    }


    override fun onItemClickListner(title: String?, itemId: String?) {
        menueDialogFragment?.dismiss()
        if (itemId == "-1") {
            when (title) {
                "Home" -> if (getSupportFragmentManager().getBackStackEntryCount() >= 1) { //getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, new CategoriesFragment(), "men").commit();
                    getSupportFragmentManager().popBackStack(
                        getSupportFragmentManager().getBackStackEntryAt(
                            0
                        ).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
                "Exit" -> {
                    val homeIntent = Intent(Intent.ACTION_MAIN)
                    homeIntent.addCategory(Intent.CATEGORY_HOME)
                    homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(homeIntent)
                }
                "Offers" ->
                    getSupportFragmentManager().beginTransaction().add(
                    R.id.main_fragment_container,
                    OffersListFragment(),
                    "offers_fragment"
                ).addToBackStack(null).commit()
            }
        } else getSupportFragmentManager().beginTransaction().add(
            R.id.main_fragment_container,
            SubCategoriesFragment.newInstance(itemId, title),
            "subcategories_fragment"
        ).addToBackStack(null).commit()
    }
}