package com.orange.etalkinglibrary.E_talking;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import com.orange.etalkinglibrary.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class E_Command {
    public static Dialog mDialog=null;
    public static String admin="fb1662627190550319";
    public final static String ip="35.240.51.141";
    public static void dismiss(){
        try{mDialog.dismiss();}catch (Exception e){e.printStackTrace();}
    }
    public static boolean SendMail(String ad,String file,String Message){
        try{
            Socket s = new Socket(ip, 5021);
            s.setSoTimeout(10000);
            DataInputStream br = new DataInputStream(s.getInputStream());
            DataOutputStream ps = new DataOutputStream(s.getOutputStream());
            ps.writeUTF(admin);
            ps.writeUTF(admin);
            ps.writeInt(3);
            ps.writeUTF(ad);
            ps.writeUTF(file);
            ps.writeUTF(Message);
            if(br.readInt()==1){return true;}else{return false;}
        }catch (Exception e){sleep(1000);e.printStackTrace();
            return false;
        }
    }
    public static void show(Context context){
        try{
            if(mDialog==null){
                mDialog = new Dialog(context, R.style.MyDialog);
                mDialog.setContentView(R.layout.progress);
                mDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                mDialog.setCancelable(true);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();
            }else{
                if(!mDialog.isShowing()){
                    mDialog = new Dialog(context, R.style.MyDialog);
                    mDialog.setContentView(R.layout.progress);
                    mDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    mDialog.setCancelable(true);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                }
            }
        }catch (Exception e){sleep(1000);e.printStackTrace();}
    }
    public static void GetMail(String take,Messageitem it,String ad){
        int originsize=it.admin.size();
        try{
            Socket s = new Socket(ip, 5021);
            s.setSoTimeout(10000);
            DataInputStream br = new DataInputStream(s.getInputStream());
            DataOutputStream ps = new DataOutputStream(s.getOutputStream());
            ps.writeUTF(admin);
            ps.writeUTF(admin);
            ps.writeInt(2);
            ps.writeUTF(ad);
            ps.writeUTF(take);
            String a=br.readUTF();
            while(!a.equals("完畢")){
                it.add2(a, br.readUTF(),br.readUTF(),br.readUTF(),br.readUTF(),br.readUTF(),br.readUTF());
                a=br.readUTF();
            }
            if(originsize==it.admin.size()){it.button=true;}
            it.success=true;
        }catch (Exception e){sleep(1000);e.printStackTrace();
            it.success=false;}

    }
    public static int GetTopMessage(){
        try{
            Socket s = new Socket(ip, 5021);
            s.setSoTimeout(10000);
            DataInputStream br = new DataInputStream(s.getInputStream());
            DataOutputStream ps = new DataOutputStream(s.getOutputStream());
            ps.writeUTF(admin);
            ps.writeUTF(admin);
            ps.writeInt(5);;
            String a=br.readUTF();
            return Integer.parseInt(a);
        }catch (Exception e){sleep(1000);e.printStackTrace();
            return -1;}
    }
    public static void GetNewMail(String take,Messageitem it,String ad){
        try{
            Socket s = new Socket(ip, 5021);
            s.setSoTimeout(10000);
            DataInputStream br = new DataInputStream(s.getInputStream());
            DataOutputStream ps = new DataOutputStream(s.getOutputStream());
            ps.writeUTF(admin);
            ps.writeUTF(admin);
            ps.writeInt(4);
            ps.writeUTF(ad);
            ps.writeUTF(take);
            String a=br.readUTF();
            while(!a.equals("完畢")){
                it.addzero(a, br.readUTF(),br.readUTF(),br.readUTF(),br.readUTF(),br.readUTF(),br.readUTF());
                a=br.readUTF();
            }
            it.success=true;
        }catch (Exception e){sleep(1000);e.printStackTrace();
            it.success=false;}

    }
    public static void sleep(int a){
        try{
            Thread.currentThread().sleep(1000);
        }catch (Exception e){e.printStackTrace();}
    }
    public static void GetMessageItem(String take,Messageitem it){
        int originsize=it.admin.size();
        try{
            Socket s = new Socket(ip, 5021);
            s.setSoTimeout(10000);
            DataInputStream br = new DataInputStream(s.getInputStream());
            DataOutputStream ps = new DataOutputStream(s.getOutputStream());
            ps.writeUTF(admin);
            ps.writeUTF(admin);
            ps.writeInt(1);;
            ps.writeUTF(take);
            String a=br.readUTF();
            while(!a.equals("完畢")){
                it.add(a,br.readUTF(),br.readUTF(),br.readUTF(),br.readUTF(),br.readUTF(),br.readUTF());
                a=br.readUTF();
            }
            if(it.admin.size()==originsize){
                it.button=true;
            }
            it.success=true;
        }catch (Exception e){sleep(1000);e.printStackTrace();  it.success=false;}
    }
}
