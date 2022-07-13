package com.example.plastic


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plastic.databinding.ActivityPhotoBinding
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class Photo : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityPhotoBinding

    //lateinit var dbService: DBService


    private lateinit var mBitmap: Bitmap
    private lateinit var mModule: Module

    private lateinit var adapter: CodeListAdapter

    private var mImgScaleX = 0f
    private  var mImgScaleY = 0f
    private  var mIvScaleX= 0f
    private  var mIvScaleY= 0f
    private  var mStartX = 0f
    private  var mStartY = 0f


    @Throws(IOException::class)
    fun assetFilePath(context: Context, assetName: String?): String? {
        val file = File(context.filesDir, assetName)
        if (file.exists() && file.length() > 0) {
            return file.absolutePath
        }
        context.assets.open(assetName!!).use { `is` ->
            FileOutputStream(file).use { os ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (`is`.read(buffer).also { read = it } != -1) {
                    os.write(buffer, 0, read)
                }
                os.flush()
            }
            return file.absolutePath
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)





        //Server connection
//        val httpLoggingInterceptor = HttpLoggingInterceptor()
//        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
//
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(httpLoggingInterceptor)
//            .build()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://plastic-server.herokuapp.com")
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//        dbService = retrofit.create(DBService::class.java)

        //result of open camera
        // this is new way to handle intent
        // onActivityResult is deprecated now
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    handleCameraImage(result.data)
                }
            }
        try {
            mModule =
                LiteModuleLoader.load(assetFilePath(getApplicationContext(), "best12064.ptl"))

            //val br = BufferedReader(InputStreamReader(assets.open("plastic.txt")))


            val inputStream: InputStream = File(assetFilePath(getApplicationContext(), "plastic.txt")).inputStream()
            val classes = mutableListOf<String>()

            inputStream.bufferedReader().forEachLine { classes.add(it) }
            PrePostProcessor.mClasses = classes.toTypedArray()

        } catch (e: IOException) {
            Log.e("Object Detection", "Error reading assets", e)
            finish()
        }



        binding.btnOpenCamera.setOnClickListener {

            //intent to open camera app
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)

        }
        binding.btnSubmitCodes.setOnClickListener{
            Log.d("mlog", PlaceResultId.getInstance().toString())
            val intent = Intent(this, Map::class.java)
            this.startActivity(intent)
//            val compositeDisposable = CompositeDisposable()
//            placeService.let{
//                compositeDisposable.add(placeService.getAllPlaces(1)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({
//                        database.setdata(it)
//
//                        println("Get response")
//
//                    }, {
//                        println("Gson error")
//                    }))
//            }
        }

        adapter = CodeListAdapter(CodesResultId.getInstance())
        binding.rvCodes.adapter = adapter
        binding.rvCodes.layoutManager= LinearLayoutManager(this)
    }


    private fun handleCameraImage(intent: Intent?) {
        mBitmap = intent?.extras?.get("data") as Bitmap

        mImgScaleX = mBitmap.width.toFloat() / PrePostProcessor.mInputWidth
        mImgScaleY = mBitmap.height.toFloat() / PrePostProcessor.mInputHeight

        mIvScaleX =
            if (mBitmap.width > mBitmap.height) mBitmap.getWidth().toFloat() / mBitmap.width else mBitmap.getHeight().toFloat() / mBitmap.height
        mIvScaleY =
            if (mBitmap.height > mBitmap.width) mBitmap.getHeight().toFloat() / mBitmap.height else mBitmap.getWidth().toFloat() / mBitmap.width

        mStartX =  (mBitmap.getWidth() - mIvScaleX * mBitmap.width) / 2
        mStartY =  (mBitmap.getHeight() - mIvScaleY * mBitmap.height) / 2

        val resizedBitmap = Bitmap.createScaledBitmap(
            mBitmap,
            PrePostProcessor.mInputWidth,
            PrePostProcessor.mInputHeight,
            true
        )
        val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
            resizedBitmap,
            PrePostProcessor.NO_MEAN_RGB,
            PrePostProcessor.NO_STD_RGB
        )
        val outputTuple = mModule.forward(IValue.from(inputTensor)).toTuple()
        val outputTensor = outputTuple[0].toTensor()
        val outputs = outputTensor.dataAsFloatArray
        val results = PrePostProcessor.outputsToNMSPredictions(
            outputs,
            mImgScaleX,
            mImgScaleY,
            mIvScaleX,
            mIvScaleY,
            mStartX,
            mStartY
        )
        //val coderes = results.map{it.classIndex}

        //Log.d("mlog", coderes.toString())

        for (code in arrayOf(1, 4)){
            val myDialogFragment = CodePermissionDialog()
            val manager = supportFragmentManager
            val args = Bundle()
            args.putInt("codeid", code)

            myDialogFragment.arguments = args
            myDialogFragment.show(manager, "myDialog")



        }






    }
    fun openMap(view: View) {

        val intent = Intent(this, com.example.plastic.Map::class.java).apply {

        }
        startActivity(intent)
    }
}
