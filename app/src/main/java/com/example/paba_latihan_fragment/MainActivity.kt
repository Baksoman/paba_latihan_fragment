package com.example.paba_latihan_fragment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.paba_latihan_fragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    // List untuk menyimpan history selama aplikasi berjalan
    private val historyKali = ArrayList<String>()
    private val historyBagi = ArrayList<String>()
    private val historyTambah = ArrayList<String>()
    private val historyKurang = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft + systemBars.left,
                systemBars.top,
                v.paddingRight + systemBars.right,
                v.paddingBottom + systemBars.bottom
            )
            insets
        }

        binding!!.btnKali.setOnClickListener { prosesOperasi("*") }
        binding!!.btnBagi.setOnClickListener { prosesOperasi("/") }
        binding!!.btnTambah.setOnClickListener { prosesOperasi("+") }
        binding!!.btnKurang.setOnClickListener { prosesOperasi("-") }
    }

    private fun prosesOperasi(operator: String) {
        val teksAngka1 = binding!!.etAngka1.text.toString()
        val teksAngka2 = binding!!.etAngka2.text.toString()

        if (teksAngka1.isEmpty() || teksAngka2.isEmpty()) {
            Toast.makeText(this, "Harap isi kedua angka terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val angka1 = teksAngka1.toDouble()
        val angka2 = teksAngka2.toDouble()

        if (operator == "/" && angka2 == 0.0) {
            Toast.makeText(this, "Tidak dapat membagi dengan nol", Toast.LENGTH_SHORT).show()
            return
        }

        val hasil = when (operator) {
            "*" -> angka1 * angka2
            "/" -> angka1 / angka2
            "+" -> angka1 + angka2
            "-" -> angka1 - angka2
            else -> 0.0
        }

        val catatan = "${formatAngka(angka1)} $operator ${formatAngka(angka2)} = ${formatAngka(hasil)}"

        val (fragmentTujuan, teksHistory) = when (operator) {
            "*" -> {
                historyKali.add(catatan)
                Pair(fKali(), historyKali.joinToString("\n"))
            }
            "/" -> {
                historyBagi.add(catatan)
                Pair(fBagi(), historyBagi.joinToString("\n"))
            }
            "+" -> {
                historyTambah.add(catatan)
                Pair(fTambah(), historyTambah.joinToString("\n"))
            }
            else -> {
                historyKurang.add(catatan)
                Pair(fKurang(), historyKurang.joinToString("\n"))
            }
        }

        val dataKirim = Bundle().apply {
            putString("HISTORY", teksHistory)
            putDouble("HASIL", hasil)
        }
        fragmentTujuan.arguments = dataKirim

        replaceFragment(fragmentTujuan)
    }

    private fun formatAngka(nilai: Double): String {
        return if (nilai % 1.0 == 0.0) {
            nilai.toLong().toString()
        } else {
            nilai.toString()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}