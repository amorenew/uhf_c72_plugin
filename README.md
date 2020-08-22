# uhf_plugin

A flutter plugin for Tablet Alps ax6737 to read UHF Cards.

#### Library Pub link
https://pub.dev/packages/uhf_plugin


### Getting Started

##### In your Gradle exclude Flutter so files because libary is working in 32bit mode
##### Only use release apk because running from IDE will not exclude 64bit files
    `packagingOptions {
        exclude 'lib/arm64-v8a/libflutter.so'
        exclude 'lib/arm64-v8a/libapp.so'
    }`

- Import the library:
   `import 'package:uhf_rt510_plugin/uhf_rt510_plugin.dart';`

- Open connection to the UHF reader

    `await UhfPlugin.connect`

- Check if is the reader connected

    `await UhfPlugin.isConnected;`

- Start reading data

    `await UhfPlugin.start;`
    
- Is started reading

   `await UhfPlugin.isStarted;`

- Stop Reading

   `await UhfPlugin.stop;`

- Close the connection

   `await UhfPlugin.close;`

- Clear cached data for the reader

   `await UhfPlugin.clearData;`

- Is Empty Tags

   `await UhfPlugin.isEmptyTags;`

- Listen to connection status

   `UhfPlugin.connectedStatusStream.receiveBroadcastStream().listen(updateIsConnected);`
   updateIsConnected should listen to bool value

- Listen to tags status

   `UhfPlugin.tagsStatusStream.receiveBroadcastStream().listen(updateTags);`
   ```dart
      List<TagEpc> _data = [];
      void updateTags(dynamic result) {
       setState(() {
           _data = TagEpc.parseTags(result);
        });
      }
   ```
![alt text](https://github.com/amorenew/uhf_rt510_plugin/raw/master/sample1.png)

