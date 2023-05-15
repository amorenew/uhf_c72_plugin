import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:uhf_c72_plugin/uhf_c72_plugin.dart';
import 'package:uhf_c72_plugin/tag_epc.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  bool _isStarted = false;
  bool _isEmptyTags = false;
  bool _isConnected = false;
  TextEditingController powerLevelController =
      TextEditingController(text: '26');
  TextEditingController workAreaController = TextEditingController(text: '1');
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await UhfC72Plugin.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    UhfC72Plugin.connectedStatusStream
        .receiveBroadcastStream()
        .listen(updateIsConnected);
    UhfC72Plugin.tagsStatusStream.receiveBroadcastStream().listen(updateTags);
    await UhfC72Plugin.connect;
    await UhfC72Plugin.setWorkArea('2');
    await UhfC72Plugin.setPowerLevel('30');
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  List<String> _logs = [];
  void log(String msg) {
    setState(() {
      _logs.add(msg);
    });
  }

  List<TagEpc> _data = [];
  void updateTags(dynamic result) {
    log('update tags');
    setState(() {
      _data = TagEpc.parseTags(result);
    });
  }

  void updateIsConnected(dynamic isConnected) {
    log('connected $isConnected');
    //setState(() {
    _isConnected = isConnected;
    //});
  }

  @override
  Widget build(BuildContext context) {
    //_data.add(TagEpc(count: 10, epc: '5SETF7656GGY5578'));
    //_data.add(TagEpc(count: 10, epc: '6757568YG76658GH'));
    // _data.add(TagEpc(count: 10, epc: 'TNB75G568YG758GH'));
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('UHF PROGAZE'),
        ),
        body: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(3.0),
                  child: Image.asset(
                    'assets/logo.png',
                    width: double.infinity,
                    height: 80,
                    fit: BoxFit.contain,
                  ),
                ),
              ),
              /*Text('Running on: $_platformVersion'),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  MaterialButton(
                      child: Text('Call connect'),
                      onPressed: () async {
                        await UhfC72Plugin.connect;
                      }),
                  MaterialButton(
                      child: Text('Call is Connected'),
                      onPressed: () async {
                        bool isConnected = await UhfC72Plugin.isConnected;
                        setState(() {
                          this._isConnected = isConnected;
                        });
                      }),
                ],
              ),
              Text(
                'UHF Reader isConnected:$_isConnected',
                style: TextStyle(color: Colors.blue.shade800),
              ),*/
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  MaterialButton(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(18.0),
                      ),
                      color: Colors.blueAccent,
                      child: Text(
                        'Call Start Single',
                        style: TextStyle(color: Colors.white),
                      ),
                      onPressed: () async {
                        bool isStarted = await UhfC72Plugin.startSingle;
                        log('Start signle $isStarted');
                      }),
                  MaterialButton(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(18.0),
                      ),
                      color: Colors.blueAccent,
                      child: Text(
                        'Call Start Continuous Reading',
                        style: TextStyle(color: Colors.white),
                      ),
                      onPressed: () async {
                        bool isStarted = await UhfC72Plugin.startContinuous;
                        log('Start Continuous $isStarted');
                      }),
                  /* MaterialButton(
                      child: Text('Call isStarted'),
                      onPressed: () async {
                        bool isStarted = await UhfC72Plugin.isStarted;
                        setState(() {
                          this._isStarted = isStarted;
                        });
                      }),*/
                ],
              ),
              /*Text(
                'UHF Reader isStarted:$_isStarted',
                style: TextStyle(color: Colors.blue.shade800),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[*/
              MaterialButton(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(18.0),
                  ),
                  color: Colors.blueAccent,
                  child: Text(
                    'Call Stop',
                    style: TextStyle(color: Colors.white),
                  ),
                  onPressed: () async {
                    bool isStopped = await UhfC72Plugin.stop;
                    log('Stop $isStopped');
                  }),
              /*   MaterialButton(
                      child: Text('Call Close'),
                      onPressed: () async {
                        await UhfC72Plugin.close;
                      }),
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[*/
              MaterialButton(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(18.0),
                  ),
                  color: Colors.blueAccent,
                  child: Text(
                    'Call Clear Data',
                    style: TextStyle(color: Colors.white),
                  ),
                  onPressed: () async {
                    await UhfC72Plugin.clearData;
                    setState(() {
                      _data = [];
                    });
                  }),
              /* MaterialButton(
                      child: Text('Call is Empty Tags'),
                      onPressed: () async {
                        bool isEmptyTags = await UhfC72Plugin.isEmptyTags;
                        setState(() {
                          this._isEmptyTags = isEmptyTags;
                        });
                      }),
                ],
              ),
              Text(
                'UHF Reader isEmptyTags:$_isEmptyTags',
                style: TextStyle(color: Colors.blue.shade800),
              ),*/
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  Container(
                    width: 100,
                    child: TextFormField(
                      controller: powerLevelController,
                      keyboardType: TextInputType.number,
                      textAlign: TextAlign.center,
                      decoration: InputDecoration(labelText: 'Power Level'),
                    ),
                  ),
                  MaterialButton(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(18.0),
                      ),
                      color: Colors.green,
                      child: Text(
                        'Set Power Level',
                        style: TextStyle(color: Colors.white),
                      ),
                      onPressed: () async {
                        bool isSetPower = await UhfC72Plugin.setPowerLevel(
                            powerLevelController.text);
                        log('isSetPower $isSetPower');
                      }),
                ],
              ),
              Text(
                'powers {"5" : "30" dBm}',
                style: TextStyle(color: Colors.blue.shade800, fontSize: 12),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  Container(
                    width: 100,
                    child: TextFormField(
                      controller: workAreaController,
                      keyboardType: TextInputType.number,
                      textAlign: TextAlign.center,
                      decoration: InputDecoration(labelText: 'Work Area'),
                    ),
                  ),
                  MaterialButton(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(18.0),
                      ),
                      color: Colors.green,
                      child: Text(
                        'Set Work Area',
                        style: TextStyle(color: Colors.white),
                      ),
                      onPressed: () async {
                        bool isSetWorkArea = await UhfC72Plugin.setWorkArea(
                            workAreaController.text);
                        log('isSetWorkArea $isSetWorkArea');
                      }),
                ],
              ),
              Text(
                'Work Area 1 China 920MHz - 2 China 840 - 3 ETSI 865\n4 Fixed 915 - 5 USA 902',
                style: TextStyle(color: Colors.blue.shade800, fontSize: 12),
              ),
              Container(
                width: double.infinity,
                height: 2,
                margin: EdgeInsets.symmetric(vertical: 8),
                color: Colors.blueAccent,
              ),
              ..._logs.map((String msg) => Card(
                    color: Colors.blue.shade50,
                    child: Container(
                      width: 330,
                      alignment: Alignment.center,
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                        'Log: $msg',
                        style: TextStyle(color: Colors.blue.shade800),
                      ),
                    ),
                  )),
              ..._data.map((TagEpc tag) => Card(
                    color: Colors.blue.shade50,
                    child: Container(
                      width: 330,
                      alignment: Alignment.center,
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                        'Tag ${tag.epc} Count:${tag.count}',
                        style: TextStyle(color: Colors.blue.shade800),
                      ),
                    ),
                  )),
            ],
          ),
        ),
      ),
    );
  }
}
