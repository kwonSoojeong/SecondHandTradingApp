package com.crystal.tradingapp.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.crystal.tradingapp.DBKey
import com.crystal.tradingapp.databinding.ActivityAddArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class AddArticleActivity : AppCompatActivity() {
    companion object {
        const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1010
        const val CONTENT_PROVIDER_IMAGE_REQUEST_CODE = 2020
    }
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    private val articleDB: DatabaseReference by lazy{
        Firebase.database.reference.child(DBKey.DB_ARTICLES)
    }

    private val binding: ActivityAddArticleBinding by lazy{
        ActivityAddArticleBinding.inflate(layoutInflater)
    }
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val binding = ActivityAddArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addImageButton.setOnClickListener {
            //permission 요청
            when {
                ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    //앱에 이미 권한이 부여되었는지 확인
                    //사진 프로바이더로 넘겨줌
                    startContentProvider()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    //앱에 권한이 필요한 이유 설명
                    //이 메서드가 ture 를 반환하면 교육용 UI를 사용자에게 표시합니다
                    showPermissionContextPopup()
                }
                else -> {
                    // 권한 요청
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }

        initSubmitButton()
    }

    //권한 요청에 대한 콜백
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContentProvider()
                } else {
                    Toast.makeText(this, "권한을 거부했습니다", Toast.LENGTH_SHORT).show()
                }
                return
            }

        }

    }

    private fun startContentProvider() {
        //content provider 실행
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, CONTENT_PROVIDER_IMAGE_REQUEST_CODE)
//        registerForActivityResult()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            CONTENT_PROVIDER_IMAGE_REQUEST_CODE -> {
                //사진을 받아와서
                val uri = data?.data
                if (uri != null) {
                    binding.imageView.setImageURI(uri)
                    imageUri = uri
                }else{
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다")
            .setMessage("사진을 가져오기위해 권한이 필요합니다.")
            .setPositiveButton("확인") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                )
            }
            .create()
            .show()
    }

    private fun initSubmitButton() {
     binding.submitButton.setOnClickListener {
         //DB저장
         val title = binding.titleEditText.text.toString()
         val price = binding.priceEditText.text.toString()
         val seller = auth.currentUser?.uid.orEmpty()

         val model = ArticleModel(seller, title, System.currentTimeMillis(), "${price}원", "")
         articleDB.push().setValue(model)

         this.finish()
     }
    }
}
