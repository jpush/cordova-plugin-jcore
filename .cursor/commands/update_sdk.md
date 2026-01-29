# 更新 JCore SDK

根据输入的目标 SDK 版本号更新本插件。本插件 **Android 使用 Gradle 依赖**、**iOS 使用 CocoaPods 依赖**，无需本地 SDK 包，只需在 `plugin.xml` 中修改版本号并检查是否有新增 API。

## 更新步骤

### 1. 更新 Android SDK 版本

在 `plugin.xml` 的 Android 平台节点中，修改 Gradle 依赖的版本号：

```xml
<framework src="cn.jiguang.sdk:jcore:x.x.x" />
```

- 将 `x.x.x` 替换为目标版本（如 `5.3.0`）
- 格式为 `groupId:artifactId:版本号`，仅支持精确版本

### 2. 更新 iOS SDK 版本

在 `plugin.xml` 的 iOS 平台节点中，修改 CocoaPods 的 `spec` 版本：

```xml
<pod name="JCore" spec="x.x.x" />
```

<!-- ### 3. 查找 SDK 新增 API

**⚠️ 重要：必须逐项检查更新日志，不要因“更新各厂商 SDK”等描述就认为没有新增 API。**

#### Android SDK

- 访问 [极光 Android 文档 / 更新说明](https://docs.jiguang.cn/jpush/jpush_changelog/updates_Android)
- **检查方法**：
  1. 找到目标版本（如 v5.3.0）的更新内容
  2. **逐项阅读**每一条更新，不跳过
  3. 重点看包含“新增”“新增接口”“新增 API”“新增方法”或 `public static`、`public void` 等 Java 方法签名的条目
  4. 对每个疑似新增 API 记录：方法名、完整方法签名、功能说明
- 在极光 Android API 文档中确认用法、参数和示例

#### iOS SDK

- 访问 [极光 iOS 文档 / 更新说明](https://docs.jiguang.cn/jpush/jpush_changelog/updates_iOS)
- **检查方法**：
  1. 找到目标版本的更新内容
  2. **逐项阅读**每一条更新
  3. 重点看“新增”、Objective-C 方法签名（如 `- (void)xxx:completion:`）等
  4. 对每个疑似新增 API 记录：方法名、完整方法签名、功能说明
- 在极光 iOS API 文档中确认用法、参数和示例

**检查清单**（完成后勾选）：

- [ ] 已找到目标版本的更新日志
- [ ] 已逐项阅读所有更新条目（含次要更新）
- [ ] 已识别所有“新增”“API”“接口”“方法”相关条目
- [ ] 已记录所有新增 API 的方法名和签名
- [ ] 已在官方 API 文档中确认每个新增 API 的用法
- [ ] 已区分：需要在本插件封装的对外 API vs 仅内部更新（可不封装）

**常见误区**：

- ❌ 看到“更新各厂商 SDK”就认为没有新增 API  
- ✅ 即使主要是版本升级，也要逐条检查是否有新增 API  
- ❌ 只扫一眼主要更新，忽略其他条目  
- ✅ 必须逐项检查更新列表  
- ❌ 用搜索代替阅读官方更新日志  
- ✅ 直接看官方更新日志并逐项检查  

### 4. 封装新增 API（如有）

**⚠️ 若无新增 API，请明确写“经检查，该版本无新增对外 API”，再跳过本步。**

若有新增对外 API，需在本插件中封装：

- 在 `www/jcore.js` 中增加 JavaScript 方法
- 在 `src/android/JCorePlugin.java` 中实现 Android 逻辑
- 在 `src/ios/` 下对应 iOS 原生文件中实现 iOS 逻辑（若有 iOS 插件代码）

**封装原则**：

- 若 Android 与 iOS 新增的是同一功能，封装为一个插件方法
- 若功能不同，分开封装
- **直接调用 SDK API，不要用反射**
- 若无新增 API，**必须写明“已检查并确认无新增 API”** 后跳过

**封装步骤**：

1. 确认 API 的完整签名和参数类型  
2. 确认调用时机（是否需在 init 之前调用）  
3. 在对应平台实现方法  
4. 在 JS 层增加方法，风格与现有 API 一致  
5. 补充必要错误处理和日志  

### 5. 更新 API 文档（若有新增方法）

若在步骤 4 中新增了插件方法：

- 在 `README.md` 或项目中的 `doc/` 下补充新方法的说明

若无新增方法，跳过此步。 -->

### 6. 更新插件版本号

**⚠️ 需在以下两处同步更新，保持一致。**

- `plugin.xml` 根节点：`version="x.x.x"`
- `package.json`：`version` 字段

**建议规则**：

- 格式：`主.次.修订`（如 `5.2.0`）  
- 可与本次升级的 JCore SDK 版本对齐，或在当前插件版本上 +0.0.1（如 `5.2.0` → `5.2.1`）  

## 注意事项

- **必须逐项检查更新日志，避免遗漏新增 API**
- Android / iOS 的 JCore 版本建议与官方推荐组合一致
- 新增 API 的封装风格需与现有 `jcore.js` / `JCorePlugin.java` 一致
- 更新后建议做一次完整构建与功能验证
- 若更新日志有缺字、错链等问题，以极光官方文档为准
- **务必保证 `plugin.xml` 与 `package.json` 中的插件版本号一致**

## 本插件与 SDK 的对应关系

- **Android**：通过 Gradle 依赖 `cn.jiguang.sdk:jcore`，无需本地 jar，版本在 `plugin.xml` 的 `<framework src="...">` 中修改。
- **iOS**：通过 CocoaPods 依赖 `JCore`，无需本地 xcframework，版本在 `plugin.xml` 的 `<pod name="JCore" spec="...">` 中修改。
