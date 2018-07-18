package info.nixon3.nfcplug

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Bundle
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.view.FlutterView


class NfcplugPlugin: MethodCallHandler {



//        if(intent!=null){
//            var message = ""
//            if(writeMode){
//                try{
//                    if(NFCUtil.createNFCMessage(msgToWrite,intent)){
//                        message="write Nfc done"
//                        channel!!.invokeMethod("wroteNfc",message)
//                        writeMode=false
//                    }
//                }
//                catch(e:Exception){
//                    message= e.message.toString()
//                }
//            }
//            else if(readMode)
//            {
//                var message = NFCUtil.retrieveNFCMessage(intent)
//                channel!!.invokeMethod("gotNfc",message)
//                readMode = false
//            }
//            return true
//        }
//        return false


    private var mActivity: Activity
    private var registrar: Registrar
    private var view: FlutterView
    private var channel: MethodChannel
    private var nfcAdapter:NfcAdapter? =null
    private var readMode:Boolean =true
    private var writeMode:Boolean =false
    private var msgToWrite:String =""


  private constructor(registrar:Registrar, view: FlutterView, activity:Activity,channel: MethodChannel) {
    this.registrar = registrar
    this.mActivity=activity
    this.view = view
    this.channel=channel
    activity
            .application
            .registerActivityLifecycleCallbacks(
                    object : Application.ActivityLifecycleCallbacks {
                      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {

                      }

                      override fun onActivityStarted(activity: Activity) {
                          if(nfcAdapter==null) nfcAdapter = NfcAdapter.getDefaultAdapter(activity)

                      }

                      override fun onActivityResumed(activity: Activity) {
                          if (activity == mActivity) {
                              if (readMode || writeMode) {
                                  nfcAdapter?.let {
                                      NFCUtil.enableNFCInForeground(it, activity, javaClass)
                                  }
                              }
                          }
                      }

                      override fun onActivityPaused(activity: Activity) {
                                  nfcAdapter?.let {
                                      NFCUtil.disableNFCInForeground(it, activity)
                          }
                      }

                      override fun onActivityStopped(activity: Activity) {}

                      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

                      override fun onActivityDestroyed(activity: Activity) {}
                    })
  }

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar): Unit {
      val channel = MethodChannel(registrar.messenger(), "nfcplug")
       val instance =  NfcplugPlugin(registrar,registrar.view(),registrar.activity(),channel)
        channel.setMethodCallHandler( instance)
        var intentFilter:IntentFilter = IntentFilter()
        intentFilter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        registrar.context().registerReceiver(createReceiver(),intentFilter)

    }

  }



  override fun onMethodCall(call: MethodCall, result: Result): Unit {
    when(call.method){
        "initialize" ->{
            if(nfcAdapter!=null) nfcAdapter = NfcAdapter.getDefaultAdapter(mActivity)

        }
        "read"-> {
            nfcAdapter?.let {
                NFCUtil.enableNFCInForeground(it, mActivity, javaClass)}
            readMode = true
            result.success("Tap Card to read")
        }
        "write"-> result.success("not implemented")
        else -> result.notImplemented()

    }
  }
}


class createReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        handleIntent(intent)
}

    fun handleIntent(intent: Intent?) {
        if (intent != null) {
            var message = NFCUtil.retrieveNFCMessage(intent)
            println(message)
        }
    }
}