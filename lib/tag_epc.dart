import 'dart:convert';


class TagEpc {
    final int id;
    final String epc;
    final int count;
    final int rssi;

    TagEpc({
        this.id,
        this.epc,
        this.count,
        this.rssi,
    });

    factory TagEpc.fromMap(Map<String, dynamic> json) => TagEpc(
        id: json["KEY_ID"],
        epc: json["KEY_EPC"],
        count: json["KEY_COUNT"],
        rssi: json["KEY_RSSI"],
    );

    Map<String, dynamic> toMap() => {
        "KEY_ID": id,
        "KEY_EPC": epc,
        "KEY_COUNT": count,
        "KEY_RSSI": rssi,
    };
    
static List<TagEpc> parseTags(String str) => List<TagEpc>.from(json.decode(str).map((x) => TagEpc.fromMap(x)));

static String tagEpcToJson(List<TagEpc> data) => json.encode(List<dynamic>.from(data.map((x) => x.toMap())));
}
