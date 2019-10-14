package com.orango.electronic.orangetxusb.TxCommand;

import android.content.Context;
import android.util.Log;
import com.orango.electronic.orangetxusb.SerialSocket;
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.orango.electronic.orangetxusb.TxCommand.FormatConvert.*;
import static com.orango.electronic.orangetxusb.TxCommand.FormatConvert.StringHexToByte;

public class Command {
    public NavigationActivity act;
    public SerialSocket socket;
    public  String SensorModel="nodata";
    public  String AppVersion="nodata";
    public  String Lib="nodata";
    public  String Rx="";
    public String Appver="";
    public String AppverInspire="nodata";
    public String Boover="101";
    public int IC=0;
    public String ID="";
    public ArrayList<String> FALSE_CHANNEL=new ArrayList<>();
    public ArrayList<String> BLANK_CHANNEL=new ArrayList<>();
    public ArrayList<String>  CHANNEL_BLE=new ArrayList<>();
    public boolean Command12(int ic,int channel,String id){
        try{
            int check=30;
            Rx="";
            String commeand="0ASS120008CCIDXXXXF5".replace("SS",bytesToHex(new byte[]{(byte)ic})).replace("CC",bytesToHex(new byte[]{(byte)channel})).replace("ID",id);
            SendData(getCRC16(commeand),check);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>2){
                    SendData(getCRC16(commeand),check);
                    past=sdf.parse(sdf.format(new Date()));
                    fal++;
                }
                if(fal>3){return false;}
                if(Rx.length()==check){
                    boolean g=checkcommand(Rx.substring(10,12));
                    if(g){ID=Rx.substring(14,22);}
                    return g;
                }
            }
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    public boolean Command03(){
        try{
            int check=22;
            Rx="";
            SendData((getCRC16("0AFE03000754504D539CC8F5")),check);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>1){
                    SendData((getCRC16("0AFE03000754504D539CC8F5")),check);
                    past=sdf.parse(sdf.format(new Date()));
                    fal++;
                }
                if(fal==1){return false;}
                if(Rx.length()==check){
                    IC=((int)StringHexToByte(Rx.substring(12,14))[0])/2;
                    return true;
                }
            }
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    public boolean Command_11(int ic,int channel){
        try{
            int check=30;
            Rx="";
            String commeand="0ASS110004CCXXXXF5".replace("SS",bytesToHex(new byte[]{(byte)ic})).replace("CC",bytesToHex(new byte[]{(byte)channel}));
            SendData((getCRC16(commeand)),check);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>2){
                    SendData((getCRC16(commeand)),check);
                    past=sdf.parse(sdf.format(new Date()));
                    fal++;
                }
                if(fal==1){return false;}
                if(Rx.length()==check){
                    boolean g=checkcommand(Rx.substring(10,12));
                    if(g){ID=Rx.substring(14,22);}
                    return g;
                }
            }
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    public boolean checkcommand(String a){
        return getBit(StringHexToByte(a)[0]).substring(7, 8).equals("0");
    }
    public String writeid="";
    public  boolean writesensorID(String id){
        try{
            Rx="nodata";
            SendData((getCRC16("0A0012000801IDXXXXF5".replace("ID",id))),30);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            while(true){
                if(Rx.length()==30&&Rx.substring(14,22).equals(id)){
                    writeid=Rx.substring(14,22);
                    break;}
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>5){
                    return false;
                }
            }
            return true;
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    public boolean Setserial(NavigationActivity act){
        try{
            Rx="nodata";

            SendData("0A0004000754504D535610F5",32);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            while(true){
                if(Rx.contains("F50004000B")){
                    act.setSerialnum(Rx.substring(14,26));
                    break;}
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>2){
                    return false;
                }
            }
            return true;
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    //    public  boolean mainflow(){
//        try{
//            SendData(StringHexToByte(getCRC16("0A0011000401XXXXF5")),30);
//            return true;
//        }catch (Exception e){e.printStackTrace();
//            return false;}
//    }
//read sensor ID amd main flow (READ)
    public  boolean ReadSensorId(){
        try{
            SendData((getCRC16("0A0010000754504D53XXXXF5")),42);
            return true;
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    //    public  boolean ReadSensorIdSingle(){
//        try{
//            SendData(StringHexToByte(getCRC16("0A0016000601C006XXXXF5")));
//            return true;
//        }catch (Exception e){e.printStackTrace();
//            return false;}
//    }
//Clear APP端 sensor code
    public  boolean ClearSensor(){
        try{
            SendData((getCRC16("0A0014000D4F52414E474554504D53XXXXF5")),34);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>1){
                    SendData((getCRC16("0A0014000D4F52414E474554504D53XXXXF5")),34);
                    past=sdf.parse(sdf.format(new Date()));
                    fal++;
                }
                if(fal>3){return false;}
                if(Rx.length()==34){if(Rx.substring(4,6).equals("14")){return true;}}
            }
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    //ProgramSensor
    public  boolean ProgramSensor(String filename,String Lf){
        try{
            SendData((getCRC16("0A00150008LF154504D53XXXXF5".replace("LF",Lf))),30);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            while(true){
                if(Rx.length()>8&&Rx.substring(Rx.length()-8,Rx.length()-8+2).equals("3E")){
                    writeid=Rx.substring(14,22);
                    return true;}
                if(Rx.length()>12&&Rx.substring(12,14).equals("01")||Rx.length()>12&&Rx.substring(12,14).equals("02")||Rx.length()>12&&Rx.substring(12,14).equals("03")){
                    ClearSensor();
                    return false;
                }
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>10){
                    return false;
                }
            }
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    public  String getCRC16(String source) {
        int crc = 0x0000; 		 	 // 初始值
        int polynomial = 0x1021;	         // 校验公式 0001 0000 0010 0001
        byte[] bytes = StringHexToByte(source.substring(2, source.length()-6));  //把普通字符串转换成十六进制字符串

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        StringBuffer result = new StringBuffer(Integer.toHexString(crc));
        while (result.length() < 4) {		 //CRC检验一般为4位，不足4位补0
            result.insert(0, "0");
        }
        return source.replace("XXXX", result.toString().toUpperCase());}

    public  String AddCommand(String data,int Long){
        StringBuffer length = new StringBuffer(Integer.toHexString(data.length()/2+5));
        while (length.length() < 4) {		 //CRC检验一般为4位，不足4位补0
            length.insert(0, "0");
        }
        StringBuffer row = new StringBuffer(Integer.toHexString(Long));
        while (row.length() < 2) {		 //CRC检验一般为4位，不足4位补0
            row.insert(0, "0");
        }
        String TmpCommand="0A0013"+length.toString()+row+data+data.substring(18, 20)+"XXXXF5";
        return getCRC16(TmpCommand);
    }

    public boolean  LogData(final String filename){
        Rx="nodata";
        try{
            InputStream fr = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fr));
            StringBuilder sb = new StringBuilder();
            int ln=2048;
            if(act.getBleServiceControl().isconnect){ln=512;}
            while (br.ready()) {
                String s=br.readLine();
                if(s!=null&&!s.equals("null")){sb.append(s);}
            }
            int Long=0;
            if(sb.length()%ln == 0){Long=sb.length()/ln;
            }else{Long=sb.length()/ln+1;}
            for(int i=0;i<Long;i++){
                Rx="nodata";
                if(i==Long-1){
                    String a=sb.toString().substring(i*ln, sb.length());
                    if(a.length()>=20){      SendData((AddCommand(a,i)),34);}
                }else{
                    SendData((AddCommand(sb.substring(i*ln, i*ln+ln),i)),34);
                }
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                Date past=sdf.parse(sdf.format(new Date()));
                while(true){
                    if(Rx.length()>12&&checkcommand(Rx.substring(10,12))){
                        break;
                    }
                    Date now=sdf.parse(sdf.format(new Date()));
                    double time=getDatePoor(now,past);
                    if(time>3){
                        return false;
                    }
                }
            }
            fr.close();
            return true;
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    //取得時間差
//    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    Date d1=sdf.parse("2019-06-12 16:26:10");
//    Date d2=sdf.parse(sdf.format(new Date()));
//     System.out.println(getDatePoor(d1,d2));
    public  int getDatePoor(Date endDate, Date nowDate) {
        long diff = endDate.getTime() - nowDate.getTime();
        long sec = diff/1000;
        return (int)sec;
    }
    public  boolean CheckS19(String filename){
        try{
            InputStream fr = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fr));
            String a=br.readLine();
            String b=br.readLine();
            if(SensorModel.equals(a.substring(4, 8))&&AppVersion.equals(a.substring(8, 10))&&Lib.equals(b.substring(0, 2))){
                fr.close();
                return true;
            }else{
                fr.close();
                return false;}
        }catch (Exception e){e.printStackTrace();return false;}
    }
    public boolean ProgramStep(final String filename,String Lf) {
        ClearChech();
        Rx="nodata";
        try {
            ReadSensorId();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(SensorModel.equals("nodata")&&AppVersion.equals("nodata")&&Lib.equals("nodata")){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>10){
                    return false;
                }
            }
            if (CheckS19(filename)) {
                return ProgramSensor(filename, Lf);
            } else {
                if(!ClearSensor()){return false;}
                if(!LogData(filename)){return false;}
                return ProgramSensor(filename, Lf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean Command14(){
        try{
            int check=(8+6*IC)*2;
            Log.d("WriteReback",check+""+"ic"+IC);
            SendData((getCRC16("0AFE14000D4F52414E474554504D53XXXXF5")),check);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>2){
                    SendData((getCRC16("0AFE14000D4F52414E474554504D53XXXXF5")),check);
                    past=sdf.parse(sdf.format(new Date()));
                    fal++;
                }
                if(fal>3){return false;}
                if(Rx.length()==check){
                    boolean g=true;
                    for(int i=0;i<IC;i++){
                        String tmp=Rx.substring(10,Rx.length()-6);
                        if(checkcommand(tmp.substring(i*12,i*12+2))){g=false;}
                    }
                    return g;
                }
            }
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    public boolean ProgramAll(final String filename,String Lf) {
        ClearChech();
        try {
            if(!Command03()){return false;}
            if(!Command10_FE()){return false;}
            if (CheckS19(filename)) {
                return Command15(Lf);
            } else {
                if(!Command14()){return false;}
                if(!LogData(filename)){return false;}
                if(!Command17()){return false;}
                return Command15(Lf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean Command17(){
        try{
            int check=(8+10*(IC-1))*2;
            SendData((getCRC16("0AFE1700094F52414E4745A7C4F5")),check);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>10){
                    SendData((getCRC16("0AFE1700094F52414E4745A7C4F5")),check);
                    past=sdf.parse(sdf.format(new Date()));
                    fal++;
                }
                if(fal>1){return false;}
                if(Rx.length()==check){
                    boolean g=true;
                    for(int i=0;i<IC-1;i++){
                        String tmp=Rx.substring(10,Rx.length()-6);
                        if(!checkcommand(tmp.substring(i*14,i*14+2))){
                            g=false;
                        }
                    }
                    return g;
                }
            }
        }catch (Exception e){e.printStackTrace();
            return false;}}

    public boolean Command15(String Lf){
        try{
            Rx="";
            FALSE_CHANNEL=new ArrayList<>();
            CHANNEL_BLE=new ArrayList<>();
            BLANK_CHANNEL=new ArrayList<>();
            int check=(8+(7*2*IC))*2;
            SendData((getCRC16("0AFE150008LF154504D53XXXXF5".replace("LF",Lf))),check);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>15){
                    return false;
                }
                if(Rx.length()==check){
                    boolean g=true;
                    for(int i=0;i<IC*2;i++){
                        String tmp=Rx.substring(10,Rx.length()-6);
                        if(!checkcommand(tmp.substring(i*14,i*14+2))){g=false;
                            Log.w("WriteReback","失敗channel"+tmp.substring(i*14+2,i*14+4));
                            if(tmp.substring(i*14+4,i*14+12).equals("00018001")){
                                BLANK_CHANNEL.add(tmp.substring(i*14+2,i*14+4));
                            }else{
                                FALSE_CHANNEL.add(tmp.substring(i*14+2,i*14+4));
                            }
                        }else{
                            CHANNEL_BLE.add(tmp.substring(i*14+2,i*14+4)+"."+tmp.substring(i*14+4,i*14+12));
                        }
                    }
                    return g;
                }
            }
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    public boolean Command10_FE(){
        try{
            int check=(14*IC+8)*2;
            SendData((getCRC16("0AFE10000754504D537331F5")),check);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>2){
                    SendData((getCRC16("0AFE10000754504D537331F5")),check);
                    past=sdf.parse(sdf.format(new Date()));
                    fal++;
                }
                if(fal>3){return false;}
                if(Rx.length()==check&&!SensorModel.equals("nodata")&&!AppVersion.equals("nodata")&&!Lib.equals("nodata")){
                    return true;
                }
            }
        }catch (Exception e){e.printStackTrace();
            return false;}
    }
    public boolean ProgramAll(final String filename,String ID1,String ID2,String ID3,String ID4,String Lf) {
        ClearChech();
        try {
            if(!Command03()){return false;}
            if(!Command10_FE()){return false;}
            if (CheckS19(filename)) {
                return Command25(ID1, ID2, ID3, ID4, Lf);
            } else {
                if(!Command14()){return false;}
                if(!LogData(filename)){return false;}
                if(!Command17()){return false;}
                return Command25(ID1, ID2, ID3, ID4, Lf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean Command01(){
        try{
            SendData("0A000100094F52414E4745ACF3F5",32);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>2){
                    SendData((getCRC16("0A000100094F52414E4745ACF3F5")),32);
                    past=sdf.parse(sdf.format(new Date()));
                    fal++;
                }
                if(fal>3){return false;}
                if(Rx.length()==32){
                    Appver=Rx.substring(20,24);
                    Boover=Rx.substring(16,20);
                    return true;
                }}
        }catch(Exception e){e.printStackTrace();return false;}
    }
    public boolean Command25(String ID1,String ID2,String ID3,String ID4,String Lf){
        try{
            Rx="";
            FALSE_CHANNEL=new ArrayList<>();
            CHANNEL_BLE=new ArrayList<>();
            BLANK_CHANNEL=new ArrayList<>();
            String command="0AFE250017LF1ID1LF2ID2LF3ID3LF4ID4XXXXF5".replace("LF",Lf);
            int check=72;
            command=command.replace("ID1",ID1).replace("ID2",ID2).replace("ID3",ID3).replace("ID4",ID4);
            SendData((getCRC16(command)),check);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>15){
                    return false;
                }
                if(Rx.length()==check){
                    boolean g=true;
                    for(int i=0;i<4;i++){
                        String tmp=Rx.substring(10,Rx.length()-6);
                        if(!checkcommand(tmp.substring(i*14,i*14+2))){g=false;
                            Log.w("WriteReback","失敗channel"+tmp.substring(i*14+2,i*14+4));
                            if(tmp.substring(i*14+4,i*14+12).equals("00018001")){
                                BLANK_CHANNEL.add(tmp.substring(i*14+2,i*14+4));
                            }else{
                                FALSE_CHANNEL.add(tmp.substring(i*14+2,i*14+4));
                            }
                        }else{
                            Log.w("WriteReback","成功channel"+tmp.substring(i*14+2,i*14+4));
                            CHANNEL_BLE.add(tmp.substring(i*14+2,i*14+4)+"."+tmp.substring(i*14+4,i*14+12));
                        }
                    }
                    return g;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public void ClearChech(){
        SensorModel="nodata";
        AppVersion="nodata";
        Lib="nodata";
        Rx="nodata";
    }
    public boolean A0xEB(){
        try{
            SendData("0AFEEB000D4F52414E474554504D533A72F5",0);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>5){return  false;}
                if((Rx.equals("F5FEEB0005C0003B480A")||Rx.equals("F5FEEB0007C000A001597F0A"))){
                    return true;
                }
            }
        }catch (Exception e){e.printStackTrace(); return false;}
    }
    public int HandShake(String mpass){
        //0代表失敗
        //1模組在Boot loader流程
        //2模組在主程式流程
        //3在app中
try{
    String command=addcheckbyte("0A"+mpass+"000030000F5");
    SendData(command,0);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    Date past=sdf.parse(sdf.format(new Date()));
    while(true){
        Date now=sdf.parse(sdf.format(new Date()));
        double time=getDatePoor(now,past);
        if(time>2){return  -1;}
        if(Rx.length()==14&&Rx.equals("F5"+mpass+"0000311E70A")){
          return 0;
            }
        if(Rx.length()==14&&Rx.equals(addcheckbyte("F5"+mpass+"0000321000A"))){
            return 1;
        }
        if(Rx.length()==22){
            return 2;
        }
    }
}catch (Exception e){e.printStackTrace();return  -1;}
    }
    public boolean ClearFlush(){
        try{
            String command=addcheckbyte("0A0000030000F5");
            SendData(command,14);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            while(true){

            }
        }catch (Exception e){e.printStackTrace();return false;}
    }
    public void SendData(String data,int check){
        Rx="";
        if(act.getBleServiceControl().isconnect){
            act.getBleServiceControl().WriteCmd((getCRC16(data)),check);
        }else{
            try{    socket.write((getCRC16(data)),check);}catch (Exception e){e.printStackTrace();}
        }
    }
    public static String addcheckbyte(String com){
        byte[] a = StringHexToByte(com);
        byte checkbyte= a[0];
        for(int i=1;i<a.length-2;i++){
            checkbyte ^= a[i];
        }
        a[a.length-2]=checkbyte;
        return bytesToHex(a);
    }
    public  boolean WriteFlash(final Context context,  final int Ind,final String filename,String mcpass){
        try{
            FileInputStream fo=new FileInputStream(context.getApplicationContext().getFilesDir().getPath()+"/"+filename+".s2");
            InputStreamReader fr = new InputStreamReader(fo);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            while (br.ready()) {
                String s=br.readLine();
                s=s.replace("null","");
                sb.append(s);
            }
            int Long=0;
            if(sb.length()%Ind == 0){Long=sb.length()/Ind;
            }else{Long=sb.length()/Ind+1;}
            Log.d("總行數",""+Long);
            for(int i=0;i<Long;i++){
                if(i==Long-1){
                    Log.d("行數",""+i);
                    String data=bytesToHex(sb.substring(i*Ind, sb.length()).getBytes());
                    int length=Ind+2;
                    check(Convvvert(data,Integer.toHexString(length),mcpass),mcpass);
                    act.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            act.update(100);
                        }
                    });
                    return true;
                }else{
                    String data=bytesToHex(sb.substring(i*Ind, i*Ind+Ind).getBytes());
                    Log.d("行數",""+i);
                    int length=Ind+2;
                    float finalI = i;
                    float finalLong = Long;
                    act.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            act.update((int)((finalI / finalLong)*100));
                        }
                    });
                    if(!check(Convvvert(data,Integer.toHexString(length),mcpass),mcpass)){
                        return false;
                    }
                }
            }
            fr.close();
            return true;
        }catch(Exception e){e.printStackTrace();return false;}
    }
    public  boolean check(String data,String mcpass){
        SendData(addcheckbyte(data),14);
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(fal<5){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>0.3){
                    past=sdf.parse(sdf.format(new Date()));
                    SendData(addcheckbyte(data),14);
                    fal++;
                }
                if(Rx.length()==14&&Rx.equals(addcheckbyte("F5"+mcpass+"2000300F40A"))||Rx.equals(addcheckbyte("F5"+mcpass+"B000300000A"))){
                    return true;
                }
            }
            return false;
        }catch (Exception e){e.printStackTrace();return false;}
    }
    public  String Convvvert(String data,String length,String mcpass){
        String command="0A"+mcpass+"2LX00F5";
        while(length.length()<4){
            length="0"+length;
        }
        command= addcheckbyte(command.replace("L",length).replace("X", data));
        return command;
    }
    //Reboot
    public  boolean Reboot(){
        try{
            String a="0A0D00030000F5";
            SendData(addcheckbyte(a),14);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>3){return false;}
                if(Rx.equals("F501000300F70A")){return true;}
            }
        }catch (Exception e){e.printStackTrace();return false;}
    }

    public boolean GoProgram(String mcpass){
        try{
            String a="0A"+mcpass+"300030000F5";
            SendData(addcheckbyte(a),14);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>3){return false;}
                if(Rx.length()==14&&Rx.equals(addcheckbyte("F5"+mcpass+"1000300CC0A"))){return true;}
            }
        }catch (Exception e){e.printStackTrace();return false;}
    }
    public void Inapp(){
            String a="0A0E00030007F5";
            SendData(addcheckbyte(a),28);
    }
}
