package br.go.dpn.meusfiis.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputFilter
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import br.go.dpn.meusfiis.R
import br.go.dpn.meusfiis.adapter.ItemFilterAdapter
import br.go.dpn.meusfiis.model.MyPreferences
import br.go.dpn.meusfiis.util.Utils
import net.orange_box.storebox.StoreBox

class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ItemFilterAdapter
    private lateinit var newFilterTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        inicializeControls()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addFiiBtn -> handleAddFii()
        }
    }

    private fun handleAddFii() {
        if (Utils.isNullOrWhiteSpace(newFilterTxt.text.toString())) {
            Toast.makeText(this, getString(R.string.insert_filter_term), Toast.LENGTH_SHORT).show()
        } else {
            mAdapter.updateList(newFilterTxt.text.toString().trim())
            newFilterTxt.text = ""
        }
    }

    fun inicializeControls() {
        setupRecycler()

        newFilterTxt = findViewById(R.id.newFilterTxt)
        newFilterTxt.filters = mutableListOf<InputFilter>(InputFilter.AllCaps()).toTypedArray()

        findViewById<ImageButton>(R.id.addFiiBtn).setOnClickListener(this)
    }

    private fun setupRecycler() {
        mRecyclerView = findViewById(R.id.recyclerFilterItems)

        val layoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = layoutManager

        val list: MutableList<String> = mutableListOf()
        val currentFilters = getMyPreferences().getFiis()
        if (currentFilters != null && currentFilters.isNotEmpty()) {
            list.addAll(currentFilters)
        }

        mAdapter = ItemFilterAdapter(list)
        mRecyclerView.adapter = mAdapter
        mAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                mAdapter.getItens().sort()
                getMyPreferences().setFiis(mAdapter.getItens())
            }
        })

        mRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    fun getMyPreferences(): MyPreferences {
        return StoreBox.create(this, MyPreferences::class.java)
    }

}
