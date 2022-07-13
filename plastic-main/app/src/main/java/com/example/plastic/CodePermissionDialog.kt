package com.example.plastic

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CodePermissionDialog : DialogFragment() {
    @SuppressLint("UseCompatLoadingForDrawables", "RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val res = resources
            val code = arguments?.getInt("codeid")
            val name = ClassNames.getImage(code!!)
            val resID: Int = res.getIdentifier(name, "drawable", context?.packageName)

            builder.setTitle("Подтверждение кода")
                .setMessage("Код переработки предсказан верно?")
                .setCancelable(true)
                .setIcon(resID)
                .setPositiveButton("Да") {_, _ -> run{
                    CodesResultId.addCode(code)

                    val dbService = ServerConn.getInstance()
//                    val createCall = dbService.getCodes(Code(code, "", ""))
//                    createCall.enqueue(object: Callback<CodeList> {
//                        override fun onFailure(call: Call<CodeList>, t: Throwable) {
//                            Log.d("mlog", t.toString())
//                        }
//                        override fun onResponse(call: Call<CodeList>, resp: Response<CodeList>) {
//                            CodeResultCode.addCode(resp.body()!!.items[0])
//                            //CodeResultCode.decode()
//                            Log.d("mlog", resp.body()!!.items[0].toString())
//
//
//                        }
//                    })
                    val createCallPlace = dbService.getPlacesByCode(Code(code, "", ""))
                    createCallPlace.enqueue(object: Callback<PlaceList>{
                        override fun onFailure(call: Call<PlaceList>, t: Throwable) {
                            Log.d("mlog", t.toString())
                        }

                        override fun onResponse(call: Call<PlaceList>, resp: Response<PlaceList>) {
                            Log.d("mlog", resp.body().toString())
                            PlaceResultId.addPlaces(resp.body()!!.items.map{it.place_id} as MutableList<Int>)
                        }
                    })


                }

                    }

                .setNegativeButton("Нет"){ _, _ ->

                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}