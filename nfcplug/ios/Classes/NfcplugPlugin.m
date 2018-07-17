#import "NfcplugPlugin.h"
#import <nfcplug/nfcplug-Swift.h>

@implementation NfcplugPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftNfcplugPlugin registerWithRegistrar:registrar];
}
@end
