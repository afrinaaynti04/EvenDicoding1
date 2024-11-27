package com.afrina.eventdicoding.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.afrina.eventdicoding.data.response.DetailData
import com.afrina.eventdicoding.databinding.ActivityDetailBinding



class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailData: DetailData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        detailData = intent.getParcelableExtra("EXTRA_DETAIL") ?: return

        displayEventDetails(detailData)

        binding.filledButton.setOnClickListener {
            // Ambil nilai quota, cek nullability, dan kurangi
            val currentQuota = detailData.quota ?: 0  // Jika null, dianggap 0
            if (currentQuota > 0) {
                // Kurangi kuota
                val newQuota = currentQuota - 1
                detailData = detailData.copy(quota = newQuota)  // Buat copy data baru dengan quota yang diupdate

                // Perbarui tampilan kuota
                binding.quota.text = newQuota.toString()

                // Buka link pendaftaran
                val link: String = detailData.link.toString()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            } else {
                // Tampilkan pesan jika kuota sudah habis
                Toast.makeText(this, "Kuota sudah habis", Toast.LENGTH_SHORT).show()
            }
        }
        binding.back.setOnClickListener{
            finish()
        }
    }

    private fun displayEventDetails(detailData: DetailData) {
        binding.name.text = detailData.name
        binding.ownerName.text = detailData.ownerName
        binding.quota.text = detailData.quota.toString()

        val remainingQuota = (detailData.quota ?: 0) - (detailData.registrans ?: 0)
        binding.remainingQuota.text = remainingQuota.toString()

        binding.beginTime.text = detailData.beginTime
        binding.category.text = detailData.category
        binding.description.text = Html.fromHtml(detailData.description,Html.FROM_HTML_MODE_LEGACY)
        binding.registrant.text = detailData.registrans.toString()
        Glide.with(this)
            .load(detailData.imageLogo)
            .into(binding.image)
    }
}
