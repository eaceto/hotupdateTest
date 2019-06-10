#import "HotupdateModule.h"
#import "AFURLSessionManager.h"
#include <sys/sysctl.h>


@implementation HotupdateModule

@synthesize bridge = _bridge;



RCT_EXPORT_MODULE()

- (NSDictionary *)constantsToExport {
  
  return @{
           @"APP_NAME": @"xxx"
           };
}

// 尝试重新加载 bundle
RCT_EXPORT_METHOD(reloadBundle)
{
  NSLog(@"saul 重新加载 bundle");
  if ([NSThread isMainThread]) {
    [self reloadJSBundle];
  } else {
    dispatch_sync(dispatch_get_main_queue(), ^{
      [self reloadJSBundle];
    });
  }
}

-(void)reloadJSBundle
{
  [_bridge reload];
  NSLog(@"saul reload ok");
}

// 发起热更新动作
RCT_EXPORT_METHOD(startHotupdate:(NSString *)downloadUrl)
{
  NSLog(@"start download from server with url %@", downloadUrl);
  self.downloadUrl = downloadUrl;
  
  // 获取 bundle 路径
  
  NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
  NSString* path = [paths objectAtIndex:0];
  NSLog(@"path %@", path);
  
  
  [self sendMessageToRN];
}

// 删除旧的更新包
RCT_EXPORT_METHOD(deleteJSBundle)
{
  NSLog(@"saul 正在查找就的更新包，如果找到就删除");
  NSString* documentsPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
  NSString* bundleFilePath = [documentsPath stringByAppendingPathComponent:@"ios.jsbundle"];
  NSLog(@"saul bundleFilePath %@", bundleFilePath);
  BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:bundleFilePath];
  if(fileExists) {
    NSLog(@"saul 存在旧的更新包，立即删除!!!!");
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSError *error;
    BOOL success = [fileManager removeItemAtPath:bundleFilePath error:&error];
    if(success) {
      NSLog(@"saul 成功删除旧的更新包!");
      /*
      UIAlertView *removedSuccessFullyAlert = [[UIAlertView alloc] initWithTitle:@"Congratulations:" message:@"Successfully removed" delegate:self cancelButtonTitle:@"Close" otherButtonTitles:nil];
      [removedSuccessFullyAlert show];
      */
    } else {
      NSLog(@"saul Could not delete file -:%@ ",[error localizedDescription]);
    }
    
    return;
  }
  
  NSLog(@"saul 不存在旧的更新包");
}




+ (BOOL)requiresMainQueueSetup
{
  return NO; // only do this if your module initialization relies on calling UIKit!
}

// Native 发送事件给 RN
- (NSArray<NSString *> *)supportedEvents
{
  return @[@"EventReminder", @"UpdateProcess"];
}

// - (void)sendMessageToRN:(NSNotification *)notification
- (void)sendMessageToRN
{
  NSString *eventName = @"sauuuul";
  NSLog(@"sending event to RN");
  [self sendEventWithName:@"EventReminder" body:@{@"name": eventName}];
  // 开始下载文件
  NSString *downloadUrl = self.downloadUrl;
  [self downLoadWithUrl:downloadUrl];
  
  [self sendEventWithName:@"UpdateProcess" body:@{@"downloadUrl": downloadUrl}];
  
  // [self sendEventWithName:@"UpdateProcess" body:@{@"process_percent": [NSString stringWithFormat:@"%lf", 23.3]}];
}

// 根据 url 下载 bundle 文件
- (void)downLoadWithUrl:(NSString *)url
{
  NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
  AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
  NSURL *URL = [NSURL URLWithString:url];
  NSURLRequest *request = [NSURLRequest requestWithURL:URL];
  
  NSURLSessionDownloadTask *downloadTask = [manager downloadTaskWithRequest:request progress:^(NSProgress * _Nonnull downloadProgress) {
    NSLog(@"saul progress of download jsbundle %@", [NSString stringWithFormat:@"%lf", downloadProgress.fractionCompleted]);
    // 将下载进度发送给 RN 去展示
    [self sendEventWithName:@"UpdateProcess" body:@{@"process_percent": [NSString stringWithFormat:@"%lf", downloadProgress.fractionCompleted]}];
    
  } destination:^NSURL *(NSURL *targetPath, NSURLResponse *response) {
    NSURL *documentsDirectoryURL = [[NSFileManager defaultManager] URLForDirectory:NSDocumentDirectory inDomain:NSUserDomainMask appropriateForURL:nil create:NO error:nil];
    NSURL *bundlePath = [documentsDirectoryURL URLByAppendingPathComponent:[response suggestedFilename]];
    NSLog(@"JJJJJJJJJJJ %@", bundlePath.absoluteString);
    
    return bundlePath;
  } completionHandler:^(NSURLResponse *response, NSURL *filePath, NSError *error) {
    NSLog(@"saul File downloaded to: %@", filePath);
  }];
  [downloadTask resume];
  
  
  /*
  NSURLSessionDownloadTask *downloadTask = [manager downloadTaskWithRequest:request progress:^(NSProgress * _Nonnull downloadProgress) {
    //获取下载进度
    NSLog(@"Progress is %f", downloadProgress.fractionCompleted);
  } destination:^NSURL *(NSURL *targetPath, NSURLResponse *response) {
    //有返回值的block，返回文件存储路径
    NSURL *documentsDirectoryURL = [[NSFileManager defaultManager] URLForDirectory:NSDocumentDirectory inDomain:NSUserDomainMask appropriateForURL:nil create:NO error:nil];
    return [documentsDirectoryURL URLByAppendingPathComponent:@"bundle.zip"];
  } completionHandler:^(NSURLResponse *response, NSURL *filePath, NSError *error) {
    if(error){
      //下载出现错误
      NSLog(@"download error%@",error);
    }else{
   
      //下载成功
      NSLog(@"File downloaded to: %@", filePath);
   
   
    }
  }];
   */
  
  

  
  
}

@end

