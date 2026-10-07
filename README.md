# SearchStable · 搜索建议防跳动

适用于 **OPPO / 一加 ColorOS 全局搜索** 的轻量 LSPosed / Xposed 模块，防止下拉搜索时“应用建议”图标突然刷新、换位。

## 下载与使用

[下载 APK（GitHub Releases）](https://github.com/veryy/SearchStable/releases)

1. 安装 APK，需要支持传统 Xposed API 的 LSPosed / Vector 等框架。
2. 启用模块，作用域只勾选 **全局搜索**（`com.heytap.quicksearchbox`）。
3. 强制停止全局搜索后重新打开，或重启手机。
4. 模块没有设置界面；不需要勾选桌面或系统框架。

## 行为

- 每次打开搜索，锁定第一份非空应用建议列表。
- 当前停留期间阻止列表替换及单项数据更新，并保护延迟重绘使用的列表顺序。
- 再次打开时，已显示的非空缓存列表同样立即锁定。
- 搜索 Activity 暂停时解锁，后台刷新不被全局禁止。

**注意：解锁以 SearchHomeActivity 的 onPause 为边界；打开其他页面也会结束本轮锁定。锁定期间单项更新也被阻止，下载状态等可能暂不更新。下次打开允许显示当时缓存，并非保证每次都强制联网取最新列表。**

## 兼容性

定位 OPPO / 一加 ColorOS 全局搜索，不是所有 Android 搜索工具的通用模块。

已实测：ColorOS 16.1 / Android 16，全局搜索 **11.63.4.20**；v0.3 经实际用户反馈“可以，还不错”。其他 OPPO / 一加机型和版本未验证。

模块依赖应用内部类与方法；系统或搜索应用升级后可能失效。遇到异常先停用模块并重启搜索，再在 Issues 提交系统、机型、搜索版本和脱敏后的模块日志。

## 原理

Hook `NewRecommendAppAdapter.P/J` 阻止当前会话的数据替换，保护 `B/E` 延迟重绘以及 `onBindViewHolder` 的列表顺序，通过 Activity 生命周期划分会话。不修改系统 APK，不关闭推荐数据源。

快照是列表级浅拷贝，不宣称能冻结所有图标位图或实体内部属性。源码不包含反编译的厂商代码。

## 构建

JDK 17、Android SDK 35、Gradle 8.9：

```sh
gradle :app:assembleDebug
```

依赖传统 Xposed API 82（compileOnly）。也可在 Actions 页面运行 Build，下载测试 APK。CI debug 签名与首个真机调试 Release 的签名不保证相同，不能混装更新；正式发布应固定并私下保管签名密钥。

## LSPosed 仓库

源码公开不等于已收录。Xposed-Modules-Repo 收录后，支持该源的管理器才能搜索和下载；以审核结果为准。

## License

MIT。非 OPPO / OnePlus 官方项目，与厂商无关联。请自行承担系统修改风险。
