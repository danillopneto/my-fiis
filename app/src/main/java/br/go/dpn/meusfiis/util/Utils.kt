package br.go.dpn.meusfiis.util

class Utils {
    companion object {
        fun isNullOrWhiteSpace(value: String) : Boolean {
            return value.isEmpty() || value.trim().isEmpty()
        }
    }
}