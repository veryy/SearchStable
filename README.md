# SearchStable

OPPO / 一加全局搜索「应用建议」防跳动模块。

下拉打开搜索，刚准备点一个应用，图标却刷新换了位置。这个模块就是用来解决这个问题的。

空输入时固定当前显示的应用建议，停留期间不再换位。输入关键词后立即解除锁定，允许搜索结果正常更新；清空关键词后重新等待并锁定建议。离开搜索后解除锁定，不会一直禁用后台刷新，也不会把推荐列表永久固定。

## 使用

1. 在 [Releases](https://github.com/veryy/SearchStable/releases/latest) 下载并安装 APK。
2. 在 LSPosed / Vector 中启用模块，作用域只勾选 **全局搜索**（`com.heytap.quicksearchbox`）。
3. 强行停止全局搜索后重新打开，或重启手机。

没有设置界面，启用即可。不用勾选桌面和系统框架。

## 支持范围

适用于 OPPO / 一加的 ColorOS 全局搜索。

目前只测试了：

- ColorOS 16.1 / Android 16
- 全局搜索 11.63.4.20

其他版本不保证有效。搜索应用更新后也可能失效。

## 注意

- 切到其他页面会解除本轮锁定。
- 锁定期间，建议卡片里的下载状态等信息也可能暂时不更新。
- 再次打开时可以显示已有缓存，不代表每次都会获取最新推荐。
- 从早期 `dev.operit.searchstable` 版换到公开版时，请先停用旧版，不要同时启用两个版本。

遇到问题可以提 [Issue](https://github.com/veryy/SearchStable/issues)，附上机型、系统版本、全局搜索版本和模块日志。发日志前记得删掉个人信息；搜索异常时先停用模块，再重启搜索。

## LSPosed 仓库

[收录申请](https://github.com/Xposed-Modules-Repo/submission/issues/2072)已提交，等待审核。目前请从 GitHub Releases 下载。

## 构建

JDK 17 / Android SDK 35 / Gradle 8.9，使用传统 Xposed API 82。

```sh
gradle :app:assembleDebug
```

也可以从 GitHub Actions 下载测试构建。Actions 与 Release 的 APK 签名可能不同，无法直接覆盖安装。

## License

[MIT](LICENSE)。非 OPPO / 一加官方项目。
