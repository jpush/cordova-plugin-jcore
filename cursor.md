
使用方法：修改需求里的内容，将需求和步骤内容作为指令让cursor进行执行。


需求：
1. 更新iOS JCore SDK 到 x.x.x 版本。JCore SDK 包的路径是：xxx
2. 更新Android JCore SDK 到 x.x.x 版本, JCore SDK 包的路径是：xxx
3. 将原生Android SDK 新增的方法，封装在插件中。
   原生SDK新增方法一：
   
   Android:
   
   ```
   ```
   
    统一封装为 方法名为 "" 的对外方法。
    

请按照以下步骤完成：

1. 找到需要升级的iOS JCore SDK，替换src/ios/jcore-ios-x.x.x.xcframework 为需要更新的版本。
2. 将plugin.xml中关于jcore-ios-x.x.x.xcframework相关的引用，替换为需要更新的版本。
3. 找到需要升级的Android JCore SDK，替换src/android/jcore-android-x.x.x.jar 为需要更新的版本。
4. 将plugin.xml中关于jcore-android-x.x.x.jar相关的引用，替换为需要更新的版本。
5. 在jcore.js 和 JCorePlugin.java 中封装需要新增的方法。(如果没有新增的方法就不用执行这一步)
6. 在plugin.xml中更新插件版本号，使用安卓SDK包的版本号。
7. 在package.json中更新插件版本号，使用安卓SDK包的版本号。



