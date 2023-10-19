package de.appgewaltig.disk_space

import android.os.Environment
import android.os.StatFs
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class DiskSpacePlugin: MethodCallHandler, FlutterPlugin {
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "disk_space")
      channel.setMethodCallHandler(DiskSpacePlugin())
    }
  }

   @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    // TODO: your plugin is now attached to a Flutter experience.
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    // TODO: your plugin is no longer attached to a Flutter experience.
  }
  
  private fun getFreeDiskSpaceInBytes():Long {
    val stat = StatFs(Environment.getExternalStorageDirectory().path)

    var bytesAvailable: Long

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
      bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
    else
      bytesAvailable = stat.blockSize.toLong() * stat.availableBlocks.toLong()
    return bytesAvailable
  }

  private fun getFreeDiskSpace(): Double {
    return (getFreeDiskSpaceInBytes() / (1024f * 1024f)).toDouble()
  }
  
  private fun getTotalDiskSpaceInBytes():Long{
    val stat = StatFs(Environment.getExternalStorageDirectory().path)

    var bytesAvailable: Long

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
      bytesAvailable = stat.blockSizeLong * stat.blockCountLong
    else
      bytesAvailable = stat.blockSize.toLong() * stat.blockCount.toLong()
    return bytesAvailable
  }

  private fun getTotalDiskSpace(): Double {
    
    return (getTotalDiskSpaceInBytes() / (1024f * 1024f)).toDouble()
  }


  override fun onMethodCall(call: MethodCall, result: Result) {
    when(call.method) {
      "getFreeDiskSpace" -> result.success(getFreeDiskSpace())
      "getTotalDiskSpace" -> result.success(getTotalDiskSpace())
      "getFreeDiskSpaceInBytes" -> result.success(getFreeDiskSpaceInBytes())
      "getTotalDiskSpaceInBytes" -> result.success(getTotalDiskSpaceInBytes())
      else -> result.notImplemented()
    }
  }
}
