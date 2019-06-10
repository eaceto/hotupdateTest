/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import "AppDelegate.h"

#import <React/RCTBridge.h>
#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>

#import <React/RCTBridgeDelegate.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  // RCTBridge *bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:launchOptions];

  
  // 更新包所在位置
  NSString* documentsPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
  NSString* bundleFilePath = [documentsPath stringByAppendingPathComponent:@"ios.jsbundle"];
  NSURL *jsBundleLocation = [NSURL URLWithString:bundleFilePath];  // 自定义加载路径
  // NSString *jsBundleFilePath = [NSString stringWithFormat:@"file://%@", bundleFilePath];
  // NSLog(@"saul jsBundleLocation %@", [NSString stringWithFormat:@"file://%@", bundleFilePath]);
  

  // 本地包所在位置
  // NSURL *jsCodeLocation;
  // jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index" fallbackResource:nil];
  // NSLog(@"saul jsCodeLocation %@", jsCodeLocation.absoluteString);
  
  
  
  // 加载包
  // initWithBundleURL
  // 模式1 尝试替换
 // RCTBridge *bridge = [[RCTBridge alloc] initWithBundleURL:jsBundleLocation
    //                                       moduleProvider:nil
      //                                       launchOptions:launchOptions];
  
  // initWithDelegate
  // 模式2
  RCTBridge *bridge = [[RCTBridge alloc] initWithDelegate:self
                                           launchOptions:launchOptions];
  
  
  
  
  RCTRootView *rootView = [[RCTRootView alloc] initWithBridge:bridge
                                                   moduleName:@"hotUpdateTest"
                                            initialProperties:nil];

  
  rootView.backgroundColor = [[UIColor alloc] initWithRed:1.0f green:1.0f blue:1.0f alpha:1];
  
  self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
  UIViewController *rootViewController = [UIViewController new];
  rootViewController.view = rootView;
  self.window.rootViewController = rootViewController;
  [self.window makeKeyAndVisible];

  // 重置 app
  // [self resetApp];
  
  return YES;
}


- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
  NSLog(@"saul 应该不会被执行");
  NSString* documentsPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
  NSString* bundleFilePath = [documentsPath stringByAppendingPathComponent:@"ios.jsbundle"];
  NSURL *jsBundleLocation = [NSURL URLWithString:bundleFilePath];
  NSString *jsBundleFilePath = [NSString stringWithFormat:@"file://%@", bundleFilePath];
  return jsBundleLocation;
  
#if DEBUG
  NSLog(@"saul debug 应该不会被执行");
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index" fallbackResource:nil];
#else
  
  return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
  // return [[NSBundle mainBundle] URLForResource:@"bundle/main/index.ios" withExtension:@"bundle"];
#endif
}

// 定制加载模块
/*
- (void)loadSourceForBridge:(RCTBridge *)bridge
                 onProgress:(RCTSourceLoadProgressBlock)onProgress
                 onComplete:(RCTSourceLoadBlock)loadCallback
{
  NSLog(@"saul 定制加载模块");

  [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
}
 */

- (void)deleteLocalBundle:(NSString *)path
{
  NSLog(@"saul 正在准备删除本地包");
  BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:path];
  if(fileExists) {
    NSLog(@"saul 立即删除本地包!!!!");
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSError *error;
    BOOL success = [fileManager removeItemAtPath:path error:&error];
    if(success) {
      NSLog(@"saul 成功删除本地包!");
    } else {
      NSLog(@"saul Could not delete file -:%@ ",[error localizedDescription]);
    }
    
    return;
  }
  
  NSLog(@"saul 不存在本地包");
}


// 重置 app
- (void)resetApp
{
  // RCTBridge *bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:launchOptions];
  
  NSLog(@"saul 重置 app");
 
  
}


@end
