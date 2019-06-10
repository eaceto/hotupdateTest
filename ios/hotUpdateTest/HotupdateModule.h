#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface HotupdateModule : RCTEventEmitter <RCTBridgeModule>
@property (nonatomic, strong) NSString *downloadUrl;
@property (nonatomic, strong) NSString *bundlePath;

-(void)downLoadWithUrl:(NSString*)url;
-(void)deleteJSBundle;
-(void)reloadJSBundle;
@end

