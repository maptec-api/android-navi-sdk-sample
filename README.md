# Navi SDK Sample

独立 Android 示例工程，演示 **导航 SDK 组件（Standalone）** 的典型接入。

依赖 Maven 制品：`com.maptec.applied:sdk-navi-standalone`（版本见 `gradle/libs.versions.toml`）。

> **说明**：导航 SDK 组件是 Navi SDK 的**接入方式之一**，以预置算路 / 引导页为主，**不是**全功能导航 SDK。Embedded、更细粒度 API 等其他形态尚在开发中。完整定位、适用场景与局限见 Maptec 开发者指南《概述》。

## 本 Sample 覆盖能力

Application 模块 `:navi-sdk-sample` 入口页可体验：

| 分类 | 示例 |
| ---- | ---- |
| 路径规划 | 起终点算路、仅终点（起点取当前定位）、途经点、算路偏好 |
| 引导导航 | `showGuideActivity` 直达引导（真实 / 模拟）；Sample 内 Activity 承载组件导航页 |
| 扩展能力 | 沿途搜索、途经点编辑（扩展槽 + 公开 API） |
| 个性化 | UI 显隐、播报语言、配色、视角与缩放等（`NaviSdkConfig`） |

## 适用与局限（摘要）

**适合**：需要标准算路 → 导航流程、快速验证鉴权与主链路、业务页一键打开组件页。

**不适合 / 请知悉**：

- 不能像自研导航 App 那样整体替换组件页信息架构
- 定制限于全局配置与引导页扩展槽，不等于任意换皮或自建全套导航壳
- 本仓库不演示尚未公开的 Embedded / 纯 API 接入

## 环境要求

| 项 | 要求 |
| ---- | ---- |
| Android Studio | 建议最新稳定版（含 AGP 8.x 支持） |
| JDK（跑构建） | ≥ 17 |
| Gradle | ≥ 8.11.1 |
| Kotlin | ≥ 2.0 |
| Android 设备 | arm64-v8a，Android 7.0（API 24）及以上 |

## 配置

1. **Maven 仓库**：已在 `settings.gradle.kts` 配置 releases 地址，一般无需修改：

   ```kotlin
   maven {
       url = uri("https://maven.maptec.com/repository/maven-releases/")
   }
   ```

2. **鉴权信息**：`navi-sdk-sample/src/main/AndroidManifest.xml` 中的两处 `meta-data` 需替换为你自己的值：

   ```xml
   <meta-data android:name="maptec_apiKey" android:value="YOUR_API_KEY" />
   <meta-data android:name="signature_sha1" android:value="YOUR_SIGNATURE_SHA1" />
   ```

   - `maptec_apiKey`：Maptec 控制台创建的应用 Key
   - `signature_sha1`：当前签名证书的 SHA-1（大写十六进制，冒号分隔）

   控制台创建 Key 时：
   - 包名填写 `com.maptec.navisdk.demo`
   - 签名 SHA-1 需与上述 `signature_sha1` 一致

   本工程使用 Android 默认 debug 签名，可用以下命令查看 SHA-1（密码 `android`）：

   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey
   ```

3. **SDK 版本**（可选）：修改 `gradle/libs.versions.toml` 中的 `navisdk`，建议与发版 Tag 对齐。

## 运行

**方式一：Android Studio**

1. 打开本目录，等待 Gradle 同步完成。
2. 选择运行配置 **navi-sdk-sample**，点击 Run。
3. 连接 arm64-v8a 真机或模拟器；首次进入页面时授予定位权限。

**方式二：命令行**

```bash
./gradlew :navi-sdk-sample:installDebug
```

安装后打开应用，从入口页选择场景即可。

## 常见问题

| 现象 | 处理 |
| ---- | ---- |
| 同步报错 / 无法下载依赖 | 确认可访问 `maven.maptec.com`，并检查 `navisdk` 版本是否已发布 |
| 打开算路 / 导航失败 | 确认 `maptec_apiKey` / `signature_sha1` 已替换，且与包名、签名一致 |
| 提示定位权限 | 在系统设置中为应用授予定位权限后重试 |
| 期望高度自定义导航主界面 | 组件方案可能不匹配；见上文「适用与局限」，并关注后续接入形态 |

## 相关文档

请参阅 Maptec Navi SDK 开发者指南（组件接入）：概述、环境搭建、鉴权配置、快速接入、组件页开发等。
