# uhf_plugin

A flutter plugin for UHF type C72 to read UHF Cards.

#### Library Pub link
https://pub.dev/packages/uhf_plugin


### Getting Started

- Import the library:
   `import 'package:uhf_c72_plugin/uhf_c72_plugin.dart';`

- Open connection to the UHF reader

    `await UhfPlugin.connect`

- Check if is the reader connected

    `await UhfPlugin.isConnected;`

- Start reading data a single UHF card

    `await UhfPlugin.startSingle;`

- Start reading data multi 'continuous' UHF cards

    `await UhfPlugin.startContinuous;`
    
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

- Set Power level (5 dBm : 30 dBm use string numbers)

   `await UhfPlugin.setPowerLevel;`

- Set Work area 
1 > China Area 920~925MHz
2 > Chin2a Area 840~845MHz
3 > ETSI Area 865~868MHz
4 > Fixed Area 915MHz
5 > United States Area 902~928MHz
-use string numbers
   `await UhfPlugin.setWorkArea;`

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
![alt text](https://github.com/amorenew/uhf_c72_plugin/raw/master/sample1.png)

