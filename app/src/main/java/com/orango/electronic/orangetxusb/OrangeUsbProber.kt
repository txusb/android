package com.orango.electronic.orangetxusb

import com.hoho.android.usbserial.driver.CdcAcmSerialDriver
import com.hoho.android.usbserial.driver.FtdiSerialDriver
import com.hoho.android.usbserial.driver.ProbeTable
import com.hoho.android.usbserial.driver.UsbSerialProber

class OrangeUsbProber {

    companion object {
        val VENDOR_ID = 0x0403
        val PRODUCT_ID = 0x6015
        fun getCustomProber() : UsbSerialProber {
            val customTable = ProbeTable()
            customTable.addProduct(VENDOR_ID, PRODUCT_ID, FtdiSerialDriver::class.java )
            return UsbSerialProber(customTable)
        }
    }

}
