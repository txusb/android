package com.orango.electronic.orangetxusb.mainActivity

import android.app.Instrumentation
import android.app.PendingIntent
import android.content.*
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.orange.etalkinglibrary.E_talking.E_Command
import com.orange.etalkinglibrary.E_talking.TalkingActivity
import com.orango.electronic.orangetxusb.*
import com.orango.electronic.orangetxusb.EventBus.ConnectBle
import com.orango.electronic.orangetxusb.OrangeUsbProber.Companion.getCustomProber
import com.orango.electronic.orangetxusb.SettingPagr.Update
import com.orango.electronic.orangetxusb.TxCommand.Command
import com.orango.electronic.orangetxusb.TxCommand.FormatConvert
import com.orango.electronic.orangetxusb.TxCommand.RxCommand
import com.orango.electronic.orangetxusb.blelibrary.EventBus.*
import com.orango.electronic.orangetxusb.blelibrary.Server.BleServiceControl
import com.orango.electronic.orangetxusb.mmySql.ItemDAO
import com.orango.electronic.orangetxusb.tool.FileDowload
import kotlinx.android.synthetic.main.activity_navigation.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.concurrent.schedule


class NavigationActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener,ServiceConnection,
    SerialListener {
    companion object {
        var Action="PROGRAM"
        var ActionPad="PROGRAM"
        var PAD_OR_USB="USB"
        var blename:String=""
    }
    var scanorselect=0
    var serialnum="99"
    lateinit var havemessage:TextView
    lateinit var command: Command
    val TAG = "NavigationActivity"
    val itemDAO: ItemDAO by lazy { ItemDAO(applicationContext) }
    lateinit var toolbar: Toolbar
    lateinit var timer: Timer
    lateinit var back:ImageView
    var HaveMMy=false
    var bleServiceControl = BleServiceControl()
    lateinit var RightTop:ImageView

    private var savedState: Bundle? = null
    enum class Connected {
        False, Pending, True
    }
      var broadcastReceiver=object : BroadcastReceiver() {
         override fun onReceive(context: Context, intent: Intent) {
             if (intent.action == INTENT_ACTION_GRANT_USB) {
                 val granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                 connect(granted)
             }} }
     var deviceId=0
     var portNum=0
     var baudRate=115200
     var socket: SerialSocket? = null
     var service: SerialService? = null
     var initialStart:Boolean= true
     var connected = Connected.False
    lateinit var load:RelativeLayout
    lateinit var loadtitle:TextView
    lateinit var programAnimator: LottieAnimationView
    val INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".GRANT_USB"
    override fun onSerialConnect() {
        Log.d("d","onSerialConnect")
        command.socket=socket
        connected=Connected.True
        var check=false
        if(FileDowload.Internet){
            Thread{
                if(command.HandShake("0")==0){
                    handler.post {
                        Toast.makeText(this,resources.getString(R.string.need_upadte),Toast.LENGTH_SHORT).show()
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.nav_host_fragment,
                            Update(), "Update")
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                            .addToBackStack("Update")
                            // 提交事務
                            .commit() }
                    return@Thread
                }
                while(command.AppverInspire.equals("nodata")){command.AppverInspire=FileDowload.CabelName()}
                command.AppverInspire=command.AppverInspire.substring(10,14)
                while (!check&&connected==Connected.True){
                    check=  command.Command01()
                    if(check){Log.d("Appversion",command.Appver)
                        if(command.Appver.equals(command.AppverInspire)){

                        }else{
                            handler.post {
                                Toast.makeText(this,resources.getString(R.string.need_upadte),Toast.LENGTH_SHORT).show()
                                val transaction = supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.nav_host_fragment,
                                    Update(), "Update")
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                    .addToBackStack("Update")
                                    // 提交事務
                                    .commit() }
                        }
                    }
                }
            }.start()
        }
    }

    override fun onSerialConnectError(e: Exception) {
        Log.d("d","onSerialConnectError${e.printStackTrace()}")
        disconnect()
        connected=Connected.False
    }

    override fun onSerialRead(data: ByteArray) {
        RxCommand.RX(data, this)
    }

    override fun onSerialIoError(e: Exception) {
        disconnect()
        connected=Connected.False
    }
fun onclick(view: View){
    when(view.id){
        R.id.imageView71->{
            startActivity(Intent(this, TalkingActivity::class.java))
        }
        R.id.imageView3->{
            Thread {
                try{
                    var inst = Instrumentation()
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
                }
                catch ( e:java.lang.Exception) {
                    Log.e("Exception when onBack", e.toString())
                }
            }.start()
        }
    }
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        havemessage=findViewById(R.id.textView78)
        bindService(Intent(this, SerialService::class.java), this, Context.BIND_AUTO_CREATE)
        EventBus.getDefault().register(this)
        RightTop=findViewById(R.id.imageView)
        loadtitle=findViewById(R.id.textView11)
        load=findViewById(R.id.load)
        programAnimator=findViewById(R.id.animation_view3)
        command=Command()
        command.act=this
        back=findViewById(R.id.imageView3)
        toolbar = findViewById(R.id.toolbar)
        loadingMMy()
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        savedState = savedInstanceState
        if(savedState != null) onBackStackChanged()
        supportFragmentManager.addOnBackStackChangedListener(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.nav_host_fragment, HomeFragment(), "Home")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
            // 提交事務
            .commit()

        timer=Timer()
        timer.schedule(0,5000){
            runOnUiThread(Runnable {
                GetMessage()
                if(NowFr.equals("PadSelect")||NowFr.equals("Select Area")){
                    if(NowFr.equals("PadSelect")){
                        if(bleServiceControl.isconnect){
                            RightTop.visibility=View.VISIBLE
                            RightTop.setBackgroundResource(R.mipmap.icon_link_pad)
                            RightTop.setOnClickListener { }
                        }else{  RightTop.visibility=View.GONE}
                    }
                    if(NowFr.equals("Select Area")){
                        if(connected==Connected.False){
                            RightTop.visibility=View.GONE
                        }else{
                            RightTop.visibility=View.VISIBLE
                            RightTop.setBackgroundResource(R.drawable.icon_link_sensor)
                        }
                    }
                }
                if(!bleServiceControl.isconnect&&!blename.equals("")){
                    bleServiceControl.connect(blename,this@NavigationActivity)
                    Log.d("情況","有")
                }
if(bleServiceControl.isconnect){
    Log.d("情況","connect$blename")
}else{     Log.d("情況","tryconnect$blename")}
                if(connected==Connected.False){
                Log.d("cabel","tryconnect")
                refresh()
            }else{
                Log.d("cabel","connect")
            } })
        }
        action_bar_title.setOnClickListener { command.Inapp() }
    }
    public fun GetMessage(){
        val profilePreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        E_Command.admin=profilePreferences.getString("admin","nodata")
        Thread{
            var messagecount=E_Command.GetTopMessage()
            handler.post {
                Log.d("message","$messagecount")
                if(messagecount==0||messagecount==-1){
                    havemessage.visibility=View.GONE
                }else{
                    havemessage.visibility=View.VISIBLE
                    havemessage.setText(""+messagecount)
                }  }
        }.start()
    }
    public override fun onDestroy() {
        if (connected != Connected.False){stopService(Intent(this, SerialService::class.java))}
        if(!bleServiceControl.first){
            unbindService(bleServiceControl.mServiceConnection)
            unregisterReceiver(bleServiceControl.mGattUpdateReceiver)
        }
        unbindService(this)
        disconnect()
        timer.cancel()
        super.onDestroy()
    }

    public override fun onStart() {
        super.onStart()
        if (service != null)
            service!!.attach(this)
        else
            startService(
                Intent(
                    this,
                    SerialService::class.java
                )
            ) // prevents service destroy on unbind from recreated activity caused by orientation change
    }

    public override fun onStop() {
        if (service != null && !this.isChangingConfigurations)
            service!!.detach()
        super.onStop()
    }



    public override fun onResume() {
        super.onResume()
        if(!EventBus.getDefault().isRegistered(this)){EventBus.getDefault().register(this)}
        registerReceiver(broadcastReceiver, IntentFilter(INTENT_ACTION_GRANT_USB))
        if (initialStart && service != null) {
            initialStart = false
            runOnUiThread(Runnable { this.connect() })
        }
    }

    public override fun onPause() {
        unregisterReceiver(broadcastReceiver)
        super.onPause()
    }

    override fun onServiceConnected(name: ComponentName, binder: IBinder) {
        if( (binder as SerialService.SerialBinder).service !=null){
            service =binder.service
        }

        if (initialStart) {
            initialStart = false
            runOnUiThread(Runnable { this.connect() })
        }
    }

    override fun onServiceDisconnected(name: ComponentName) {
        service = null
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyUp(keyCode, event)
    }
var NowFr="no"
    override fun onBackStackChanged() {
        if(supportFragmentManager.fragments.get(supportFragmentManager.fragments.size-1).tag!=null){
            val tname=supportFragmentManager.fragments.get(supportFragmentManager.fragments.size-1).tag!!
            if(tname.equals("PadSelect")||tname.equals("Select Area")||tname.equals("Home")||tname.equals("UsersManual")||tname.equals("Setting")){NowFr=tname
            }

//           if(tname.equals("Select Area")||tname.equals("PadSelect")){
//               val backStackCount = supportFragmentManager.backStackEntryCount
//               for (i in 0 until backStackCount) {
//                   val backStackId = supportFragmentManager.getBackStackEntryAt(i).name
//                   Log.d("Backname",backStackId)
////
//               }/* end of for */
//           }
        }

       Log.d("Fragement_Tag",NowFr)
    }

    fun setActionBarTitle(title: String){
        action_bar_title.text = title
    }

    private fun connect() {
        connect(null)
    }

    private fun connect(permissionGranted : Boolean?){
            var device : UsbDevice? = null
            val usbManager = this.getSystemService(Context.USB_SERVICE) as UsbManager
            for (v : UsbDevice in usbManager.deviceList.values){
                if(v.deviceId == deviceId)
                    device = v
            }
            if (device == null){

                return
            }

            var driver = UsbSerialProber.getDefaultProber().probeDevice(device)
            if (driver == null) {
                driver = OrangeUsbProber.getCustomProber().probeDevice(device)
            }
            if (driver == null) {

                return
            }
            if (driver.ports.size < portNum) {

                return
            }

            val usbSerialPort = driver.ports[portNum]
            val usbConnection = usbManager.openDevice(driver.device)
            if (usbConnection == null && permissionGranted == null) {
                if (!usbManager.hasPermission(driver.device)) {
                    val usbPermissionIntent = PendingIntent.getBroadcast(this, 0, Intent(INTENT_ACTION_GRANT_USB), 0)
                    usbManager.requestPermission(driver.device, usbPermissionIntent)
                    return
                }
            }
            if (usbConnection == null) {
                if (!usbManager.hasPermission(driver.device))
                else
                    return
            }
            connected = Connected.Pending
            socket = SerialSocket()
            try {
                Log.d("執行情況","到socket")
                service!!.connect(this, "Connected")
                Log.d("執行情況","到service")
                socket!!.connect(this, this, usbConnection, usbSerialPort, baudRate)
                Log.d("執行情況","執行完")
                onSerialConnect()
            } catch (e: Exception) {
                onSerialConnectError(e)
            }
    }
    class listDeivce(var device: UsbDevice, var port: Int, var driver: UsbSerialDriver?)
    lateinit var usbDevice : listDeivce
    private fun refresh() {
        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val usbDefaultProber : UsbSerialProber = UsbSerialProber.getDefaultProber()
        val usbCustomProber : UsbSerialProber = getCustomProber()

        for (device:UsbDevice in usbManager.deviceList.values){
            val driver : UsbSerialDriver? = usbCustomProber.probeDevice(device)
            if (driver != null ) {
                for (port in 0 until driver.ports.size) {
                    usbDevice = listDeivce(device, port, driver)
                    getDevice()
                }
            } else {
                return
            }
        }
    }

    private fun getDevice() {
        val device : String?
        if(usbDevice.driver != null){
            device = usbDevice.driver!!.javaClass.simpleName
                .replace("SerialDriver","") + ", Port "+usbDevice.port
            deviceId=usbDevice.device.deviceId
            portNum=usbDevice.port
            connect(null)
            Log.d("connectdevice",usbDevice.device.deviceId.toString())
        }else{
            return
        }
        val id : String = String.format(
            Locale.US,"Vendor %04X, Product %04X"
            ,usbDevice.device.vendorId,usbDevice.device.productId)

        Log.d(HomeFragment.TAG, "Connected: $device ,$id")
    }

    private fun disconnect() {
        try {
            bleServiceControl.disconnect()
            connected = Connected.False
            service!!.disconnect()
            socket!!.disconnect()
            socket = null
        }catch (e:java.lang.Exception){}

    }


    //----------------------------------------Use bleServiceControl.WriteCmd(HexString,uuid).to write Data ----------------------------------------
    @Subscribe
    fun Event(a: RebackData) {
        try {
            Log.w("WriteReback", "Data:" + FormatConvert.bytesToHex(a.reback))
            RxCommand.RX(a.reback, this)
        } catch (e: Exception) {
            Log.w("error", e.message)
        }

    }

    //----------------------------------------Use bleServiceControl.ReadCmd(uuid).to read Data----------------------------------------
    @Subscribe
    fun Event(a: ReadData) {
        try {
            Log.w("ReadReback", "Data:" + FormatConvert.bytesToHex(a.reback))
            RxCommand.RX(a.reback, this)
        } catch (e: Exception) {
            Log.w("error", e.message)
        }

    }

    //----------------------------------------Use bleServiceControl.WriteCmd will callbcak this method to confirm is writed susscessfully----------------------------------------
    @Subscribe
    fun Event(a: WriteData) {
        try {
            Log.w("Write", "Data:" + FormatConvert.bytesToHex(a.data()))
        } catch (e: Exception) {
            Log.w("error", e.message)
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(a: ConnectState) {
        if (a.reback) {
         Log.d("連線","連線ok")
//            LoadingSuccess()
        } else {
            Toast.makeText(this,"Bluetooth is disconnected",Toast.LENGTH_SHORT).show()
            LoadingSuccess()
            fal=10
            blename=""
            supportFragmentManager.addOnBackStackChangedListener(this)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, HomeFragment(), "Home")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                // 提交事務
                .commit()
        }
    }

    var handler= Handler()
    fun loadingMMy(){
        loading(0)
        Thread{
            val a= FileDowload.DownMMy(this)
            handler.post {
                if(a){
                    HaveMMy=true
                    LoadingSuccess()
                }else{   HaveMMy=false
                    loadingMMy()}
            }
        }.start()
    }
    fun update(pass:Int){
        if(pass==0){
            loadtitle.text=resources.getString(R.string.Updating)
            load.visibility=View.VISIBLE
            programAnimator.visibility=View.VISIBLE
        }else{
            loadtitle.text=resources.getString(R.string.Updating)+"..."+pass+"%"
            load.visibility=View.VISIBLE
            programAnimator.visibility=View.VISIBLE
        }

    }
    fun loading(pass:Int){
        if(pass==0){
            loadtitle.text=resources.getString(R.string.Data_Loading)
            load.visibility=View.VISIBLE
            programAnimator.visibility=View.VISIBLE
        }else{
            loadtitle.text=resources.getString(R.string.Data_Loading)+"..."+pass+"%"
            load.visibility=View.VISIBLE
            programAnimator.visibility=View.VISIBLE
        }
    }
    fun WaitBle(){
        loadtitle.text=resources.getString(R.string.paired_with_your_device)
        load.visibility=View.VISIBLE
        programAnimator.visibility=View.VISIBLE
    }
    fun LoadingSuccess(){
        load.visibility=View.GONE
        programAnimator.visibility=View.GONE
    }
    var fal=0
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(a: ConnectBle) {
        WaitBle()
        Thread{
             fal=0
            while(true){
                if(bleServiceControl.isconnect||fal==10){break}
                Thread.sleep(1000)
                fal++
            }
            handler.post {
            if(bleServiceControl.isconnect){
                if(scanorselect==0){
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.nav_host_fragment, QrcodeScanner(), "Scanner")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                        .addToBackStack("Scanner")
                        // 提交事務
                        .commit()
                }else{
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.add(R.id.nav_host_fragment, MakeFragment(), "Vehicle Selection")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                        .addToBackStack("Vehicle Selection")
                        .commit()
                }

            }else{ blename=""}
                LoadingSuccess()
            }
        }.start()
    }

}
