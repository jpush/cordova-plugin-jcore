---
name: update-sdk
description: |
  更新 cordova-plugin-jcore 插件的 JCore SDK 版本。自动拉取极光官网 Changelog，分析新增/移除/变更 API，更新 plugin.xml 中 Android（Gradle framework）和 iOS（CocoaPods podspec）版本引用，同步更新 Native 层（Java）和 JS Bridge 层代码，展示变更摘要确认后发布到 npm。
  Use when: 更新 JCore SDK、升级极光核心库版本、cordova-plugin-jcore 发布新版本、Cordova 插件 SDK 更新。
allowed-tools:
  - Bash
  - Read
  - Edit
  - Write
  - WebFetch
---

你正在更新 **cordova-plugin-jcore** 插件。

**用户参数：** $ARGUMENTS

---

## 第一步：解析参数

从 `$ARGUMENTS` 中提取版本号：
- `--android X.X.X` → Android JCore SDK 目标版本
- `--ios X.X.X` → iOS JCore SDK 目标版本
- `--jpush-ref X.X.X` → 查找 JCore API 变更所对应的 JPush 版本号（选填）

如果某端版本号缺失，先询问用户再继续。

**关于 `--jpush-ref`：** JCore 官网不单独发布更新日志，JCore 的 API 变更记录在 JPush 的更新日志里。若参数中未提供此值，询问用户：

> JCore 的 API 变更记录在哪个 JPush 版本的更新日志中？请提供 JPush 版本号（如 `4.9.0`），或回复"跳过"以略过 Changelog 拉取步骤。

若用户回复"跳过"，记录 `jpush_ref = null`，直接跳到第三步末尾说明。

---

## 第二步：安装依赖

```bash
pip3 install requests beautifulsoup4 -q 2>&1 | tail -1
```

---

## 第三步：拉取 SDK Changelog

> JCore 的更新日志发布在 JPush 的 changelog 页面中，需以 JPush 版本号查找对应章节，再从中提取 JCore 相关变更。

若 `jpush_ref` 已提供（非 null），执行：
```bash
python3 .claude/skills/update-sdk/scripts/changelog_fetcher.py --android <JPUSH_REF> --ios <JPUSH_REF>
```
读取 `.claude/skills/update-sdk/scripts/.changelog_cache.json` 获取 Changelog 内容。

若 `jpush_ref = null`，**跳过此步骤**，第四步分析时无 changelog 可读，仅做版本号更新，跳过 API 变更分析。

---

## 第四步：AI 分析变更

分析 Changelog，整理：

> **注意**：Changelog 同时包含 JPush 和 JCore 的变更。只关注调用类以 `JCore` 开头的条目（如 `JCoreInterface`、`JCoreManager` 等），忽略类名以 `JPush` 开头的内容。

1. 新增 API（双端统一 or 单端独有）；仅单端有的 → **先检查另一端是否已有等价实现**（见下方说明），确认缺失才标注单端
2. 移除/废弃 API
3. 行为变更
4. 新插件版本号（plugin.xml 中的 version 属性）：始终升 patch（如 3.4.9 → 3.5.0，3.9.9 → 4.0.0）

> **跨平台等价检查**：当 Changelog 只在某一端出现新增 API 时，**不要直接标为单端 Only**。先检查 `src/android/JCorePlugin.java` 和 `www/jcore.js`，搜索功能相同或名称相近的方法。如果已有对应实现，则合并为统一 API；只有确认完全没有等价功能时，才标注 Android Only / iOS Only。

---

## 第五步：更新版本号引用

plugin.xml 中同时包含 SDK 版本引用和插件自身版本，由脚本统一更新：

```bash
python3 .claude/skills/update-sdk/scripts/plugin_updater.py \
  --android <ANDROID_VERSION> \
  --ios <IOS_VERSION> \
  --bump-patch \
  --changelog-summary "<ONE_LINE_SUMMARY>"
```

---

## 第六步：更新 Native 层代码

**编写代码前，先通过 WebFetch 查询官网 API 文档，确认新增方法的完整签名、参数类型和返回值：**
- Android 文档：`https://docs.jiguang.cn/jcore/client/android_api`
- iOS 文档：`https://docs.jiguang.cn/jcore/client/ios_api`

在文档中搜索第四步识别出的新增方法名，确认签名后再编写下方代码。

**Android** — `src/android/JCorePlugin.java`
- 在 `execute()` 方法的 action 分支中添加新方法处理

**iOS**：此插件 iOS 端通过 CocoaPods 引用，无原生源文件需要修改。若有新的 JCore 初始化参数或配置，更新 `plugin.xml` 中的相关配置项。

---

## 第七步：更新 JavaScript Bridge

**`www/jcore.js`**
- 添加新方法的 `cordova.exec()` 封装

---

## 第八步：展示变更摘要并请求确认

```
========== cordova-plugin-jcore 更新摘要 ==========
Android JCore SDK:  旧版本 → 新版本
iOS JCore SDK:      旧版本 → 新版本
插件版本（plugin.xml）：旧版本 → 新版本

新增 API：...
修改的文件：plugin.xml, src/android/JCorePlugin.java, www/jcore.js, CHANGELOG.md
===================================================

确认以上变更并发布到 npm? [y/N]
```

---

## 第九步：发布（确认后执行）

```bash
python3 .claude/skills/update-sdk/scripts/publisher.py
```
