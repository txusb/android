package com.orango.electronic.orangetxusb.mmySql

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import com.orango.electronic.orangetxusb.UsbPad.Pad_Idcopy
import com.orango.electronic.orangetxusb.UsbPad.StartProgram
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.UsbCable.Id_copy
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import com.orango.electronic.orangetxusb.UsbCable.Cable_Program
import com.orango.electronic.orangetxusb.mainActivity.Relarm
import com.orango.electronic.orangetxusb.tool.FileDowload
import java.lang.Exception
import java.util.ArrayList

class ItemDAO(context: Context) {
    companion object {
        val TAG = "ItemDAO"
        // 編號表格欄位名稱，固定不變
        val KEY_ID = "_id"
        val notin="'NA'"
        // 其它表格欄位名稱
        val MAKE_COLUMN = "Make"
        val MODEL_COLUMN = "Model"
        val YEAR_COLUMN = "Year"
        val MAKE_IMG_COLUMN = "Make_Img"
    }

    //資料庫物件
    private var dbHelper = DatabaseHelper(context)
    private lateinit var db: SQLiteDatabase

    init {
        dbHelper.createDataBase()
        if(dbHelper.checkDataBase())
            Log.d(TAG, "checkDataBase: true")
            dbHelper.openDataBase()
            db = dbHelper.db
    }


    // 關閉資料庫，一般的應用都不需要修改
    fun close() {
        dbHelper.close()
    }



    fun getMakeString(): ArrayList<String>?{
        val makes = arrayListOf<String>()

        val result = db.rawQuery(
            "select distinct $MAKE_COLUMN from `Summary table` order by $MAKE_COLUMN asc",null)

        if(result.count > 0){
            result.moveToFirst()
            do{
                makes.add(result.getString(0))
            }while (result.moveToNext())
            // 關閉Cursor物件
            result.close()
            // 回傳結果
            return makes
        }else{
            result.close()
            return null
        }
    }
fun     GoOk(code:String,fragmentManager: FragmentManager){
    val sql="select  `Make`,`Model`,`Year`,`Make_Img`  from `Summary table` where `Direct Fit` not in($notin) and `$MAKE_IMG_COLUMN` not in($notin) and `MMY number`='$code' limit 0,1"
    val result = db.rawQuery(
        sql,null)
    if(result.count > 0){
        result.moveToFirst()
        do{
            val make=result.getString(0)
            val model=result.getString(1)
            val years=result.getString(2)
            val makeImg=result.getString(3)
            Relarm.position=1
            val fragment = Relarm()
            val args = Bundle()
            args.putString(Cable_Program.stringMake, make)
            args.putString(Cable_Program.stringMakeImg, makeImg)
            args.putString(Cable_Program.stringModel, model)
            args.putString(Cable_Program.stringYear, years)
            fragment.arguments=args
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment, "Relarm")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Relarm")
                // 提交事務
                .commit()
        }while (result.moveToNext())
        // 關閉Cursor物件
        result.close()
    }else{
        result.close()
    }
}
    fun getMake(act:Activity): ArrayList<Item>?{
        try{
            val makes = arrayListOf<Item>()
            var sql="select distinct $MAKE_COLUMN , $MAKE_IMG_COLUMN from `Summary table` where `Direct Fit` not in($notin) and `$MAKE_COLUMN`  IS NOT NULL and `$MAKE_IMG_COLUMN` not in('NA') order by $MAKE_COLUMN asc"
            if(!FileDowload.Internet){sql="select distinct $MAKE_COLUMN , $MAKE_IMG_COLUMN from `Summary table` where `Direct Fit` not in($notin) and `$MAKE_COLUMN`  IS NOT NULL and `$MAKE_IMG_COLUMN` not in('NA') order by $MAKE_COLUMN asc limit 0,1"}
            val result = db.rawQuery(
                sql,null)
            if(result.count > 0){
                result.moveToFirst()
                do{

                    val item = Item()
                    item.make = result.getString(0)
                    item.makeImg = result.getString(1)
                    Log.d("makeImg",item.makeImg)
                    makes.add(item)
                }while (result.moveToNext())
                // 關閉Cursor物件
                result.close()
                // 回傳結果
                return makes
            }else{
                result.close()
                return null
            }
        }catch (e:Exception){act.getSharedPreferences("Setting", Context.MODE_PRIVATE).edit().putString("mmyname","").commit();return null}
    }
    fun getModel(make: String): ArrayList<String>?{
        val models = arrayListOf<String>()
        val result = db. rawQuery(
            "select distinct $MODEL_COLUMN from `Summary table` where $MAKE_COLUMN = '$make' and  `Direct Fit` not in($notin) order by $MODEL_COLUMN asc",
            null)

        if(result.count > 0){
            result.moveToFirst()
            do{
                models.add(result.getString(0))
            }while (result.moveToNext())
            // 關閉Cursor物件
            result.close()
            // 回傳結果
            return models
        }else{
            result.close()
            return null
        }
    }
    fun getIdModel(make: String): ArrayList<String>?{
        val models = arrayListOf<String>()

        val result = db. rawQuery(
            "select distinct $MODEL_COLUMN from `Summary table`,idcopy where $MAKE_COLUMN = '$make' and `Summary table`.`Direct Fit` in(`idcopy`.`s19`) order by $MODEL_COLUMN asc",
            null)

        if(result.count > 0){
            result.moveToFirst()
            do{
                models.add(result.getString(0))
            }while (result.moveToNext())
            // 關閉Cursor物件
            result.close()
            // 回傳結果
            return models
        }else{
            result.close()
            return null
        }
    }
    fun getIdYear(make: String,model: String): ArrayList<String>?{
        val years = arrayListOf<String>()

        val result = db. rawQuery(
            "select distinct $YEAR_COLUMN from `Summary table`,idcopy where $MODEL_COLUMN = '$model' and $MAKE_COLUMN = '$make' and `Summary table`.`Direct Fit`in(`idcopy`.`s19`) order by $YEAR_COLUMN asc",
            null)

        if(result.count > 0){
            result.moveToFirst()
            do{
                years.add(result.getString(0))
            }while (result.moveToNext())
            // 關閉Cursor物件
            result.close()
            // 回傳結果
            return years
        }else{
            result.close()
            return null
        }
    }
    fun getLf(s19:String): String?{
        val result = db. rawQuery(
            "select `Lf` from `Summary table` where `Direct Fit`='$s19' order by $YEAR_COLUMN asc limit 0,1",
            null)
        if(result.count > 0){
            result.moveToFirst()
                val a=result.getString(0)
                result.close()
               return a
        }else{
            result.close()
            return "0"
        }
    }
    fun getYear(make: String,model: String): ArrayList<String>?{
        val years = arrayListOf<String>()

        val result = db. rawQuery(
            "select distinct $YEAR_COLUMN from `Summary table` where $MODEL_COLUMN = '$model' and $MAKE_COLUMN = '$make' and  `Direct Fit` not in($notin) order by $YEAR_COLUMN asc",
            null)

        if(result.count > 0){
            result.moveToFirst()
            do{
                years.add(result.getString(0))
            }while (result.moveToNext())
            // 關閉Cursor物件
            result.close()
            // 回傳結果
            return years
        }else{
            result.close()
            return null
        }
    }
    fun getS19(s19: String): Boolean{
        val result = db.rawQuery(
            "select count(1) from `Summary table` " +
                    "where `Direct Fit`='$s19'",
            null)

        if(result.count > 0 ){
            result.moveToFirst()
          val a= result.getInt(0)
           return a>0
        }else{
            result.close()
            return false
        }
    }
    fun getMMY(make: String, model: String, year:String): String{
        val result = db.rawQuery(
            "select  `Direct Fit` from `Summary table` " +
                    "where $MAKE_COLUMN = '$make' " +
                    "and $MODEL_COLUMN = '$model' " +
                    "and $YEAR_COLUMN = '$year' and  `Direct Fit` not in('NA') limit 0,1",
            null)

        if(result.count > 0 ){
            result.moveToFirst()
            return result.getString(0)
        }else{
            result.close()
            return ""
        }
    }

    // 把Cursor目前的資料包裝為物件
    fun getRecord(cursor: Cursor): Item {
        // 準備回傳結果用的物件
        val result = Item()

        //result.id = cursor.getLong(0)
        result.make = cursor.getString(0)
        result.model =cursor.getString(1)
        result.year = cursor.getString(2)
        result.orangePart = cursor.getString(3)
        result.makeImg = cursor.getString(4)

        // 回傳結果
        return result
    }
    fun GetCopyId(s19:String):Int{
        val result = db.rawQuery("select `ID_Count` from `Summary table` where `Direct Fit`='$s19' and `$MAKE_IMG_COLUMN` not in('NA') limit 0,1", null)
        if(result.count > 0 ){
            result.moveToFirst()
            do{
                val TmpWrite=result.getString(0)
                Log.d("Idcount",TmpWrite)
                return TmpWrite.toInt()
            }while (result.moveToNext())

        }else{
            result.close()
            return 8
        }
    }

    fun GetreLarm(make:String,model:String,year:String,act:Context):String{
        val profilePreferences = act.getSharedPreferences("Setting", Context.MODE_PRIVATE)
        val a= profilePreferences.getString("Language","English")
        var colname="English"
        when(a){
            "繁體中文"->{ colname="`Relearn Procedure (Traditional Chinese)`"}
            "简体中文"->{ colname="`Relearn Procedure (Jane)`"}
            "Deutsche"->{ colname="`Relearn Procedure (German)`"}
            "English"->{ colname="`Relearn Procedure (English)`"}
            "Italiano"->{ colname="`Relearn Procedure (Italian)`"}
        }
        val result = db.rawQuery(
            "select $colname from `Summary table` where make='$make' and model='$model' and year='$year' limit 0,1",
            null)

        if(result.count > 0 ){
            result.moveToFirst()
            if(result.getString(0).isEmpty()){  return act.resources.getString(R.string.norelarm)}else{  return result.getString(0)}

        }else{
            result.close()
            return act.resources.getString(R.string.norelarm)
        }
    }
}