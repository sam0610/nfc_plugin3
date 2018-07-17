import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:nfcplug/nfcplug.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'NFC Demo',
      home: new MyHomePage(title: 'NFC Plugin Demo'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;
  //test

  @override
  _MyHomePageState createState() => new _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = const MethodChannel('sam0610.nixon.io/nfc');
  TextEditingController _controller = new TextEditingController();
  List<String> _message = [];

  @override
  void initState() {
    super.initState();
    platform.setMethodCallHandler(myUtilsHandler);
  }

  Future<dynamic> myUtilsHandler(MethodCall methodCall) async {
    if (methodCall.method == "gotNfc") {
      String msg = methodCall.arguments;
      setState(() {
        _message.add(msg);
      });
    } else if (methodCall.method == "wroteNfc") {
      String msg = methodCall.arguments;
      setState(() {
        _message.add(msg);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Center(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              TextFormField(
                controller: _controller,
                decoration: new InputDecoration(labelText: "msg"),
              ),
              Row(
                children: <Widget>[
                  Expanded(
                    flex: 1,
                    child: new FlatButton.icon(
                      label: new Text("write"),
                      icon: new Icon(Icons.nfc),
                      onPressed: _writeNfc,
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: new FlatButton.icon(
                      label: new Text("read"),
                      icon: new Icon(Icons.nfc),
                      onPressed: _readNfc,
                    ),
                  ),
                ],
              ),
              new Expanded(
                  child: new ListView.builder(
                itemCount: _message.length,
                itemBuilder: (ctx, index) => new Text(_message[index],
                    style: new TextStyle(fontSize: 24.0, color: Colors.red)),
              ))
            ],
          ),
        ),
      ),
    );
  }

  void _writeNfc() async {
    String message = await Nfcplug.readNfc;
    _message.add(message);
    setState(() {});
  }

  void _readNfc() async {
    String message = await Nfcplug.writeNfc;
    _message.add(message);
    setState(() {});
  }
}
