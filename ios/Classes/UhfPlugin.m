#import "UhfPlugin.h"
#if __has_include(<uhf_plugin/uhf_plugin-Swift.h>)
#import <uhf_plugin/uhf_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "uhf_plugin-Swift.h"
#endif

@implementation UhfPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftUhfPlugin registerWithRegistrar:registrar];
}
@end
