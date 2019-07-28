package br.go.dpn.meusfiis.model

import br.go.dpn.meusfiis.adapter.FiltersTypeAdapter
import net.orange_box.storebox.annotations.method.ClearMethod
import net.orange_box.storebox.annotations.method.KeyByString
import net.orange_box.storebox.annotations.method.TypeAdapter

interface MyPreferences {
    @ClearMethod
    fun clear()

    @KeyByString("key_fiis")
    @TypeAdapter(FiltersTypeAdapter:: class)
    fun getFiis(): List<String>

    @KeyByString("key_fiis")
    @TypeAdapter(FiltersTypeAdapter:: class)
    fun setFiis(filters: List<String>)
}
