import 'dart:async';

import 'package:flutter/services.dart';

class Nfcplug {
  static const MethodChannel _channel = const MethodChannel('nfcplug');

  static Future<String> get readNfc async {
    final String msg = await _channel.invokeMethod('read');
    return msg;
  }

  static Future<String> get writeNfc async {
    final String msg = await _channel.invokeMethod('write');
    return msg;
  }
}
