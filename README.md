<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">MyDemo</h1>
<h4 align="center">android原生开发学习DEMO</h4>

## ✨项目简介
mydemo 项目主要用于学习


## 🔥模块
### mylibrary
微服务模块更新中，目前具备以下模块
* activity：activity父级基础窗口
* api：基于okhttp的数据访问接口
* common：通用类及接口
* config：配置信息，网络服务配置信息
* crash：APP崩溃信息收集
* entity：网络服务返回的结果类
* fragment：一些基础的fragment
* gps：定位
* icon：实现iconfont
* info：AppInfo,CodeInfo,DeviceInfo,UserInfo
* language：设置当前应用语言
* map：地图相关
* sensor：手机姿态
* uicomponent：自定义的控件
* utils：工具
* vibrator：震动
### myupdate
* app更新安装
### myweather
* 天气

#### 一些功能或BUG
1. 手机姿态监控，采用观察者设计模式实现-------------
2. activity 提前关闭，线程安全，不再请求数据，APP不会崩溃，java是线程安全的----------------------
3. 主题使用及变更，换皮肤------------------------待完善
4. 语言使用及变更------------------切换语言后，recreate所有activty页面可以实现不重启切换语言-------
5. listview---pading实现滑动----流式布局实现-------------
6. 按键震动效果--------------------------
7. kotlin页面双向绑定--------
8. 自定义implement--------------
9. 获取设备屏幕长宽---------------
10. token过期时，自动跳转登录页面
11. dex文件动态加载-----------------
12. ICON库---------------------------
13. 依赖注入
14. 支付宝、微信、支付
15. 手机模型3D姿态展示、3D指南针---------未成
16. 用户页面，可修改密码
17. tbl_codefactory样例页面
18. 本地数据库操作-----------------------
19. TDD测试驱动开发
20. widget系列控件--------学习中
21. 账户页面，滚动时，头部动画-----------------------
22. 解析.ttf文件，转 unicode码
23. 修改xml-scene文件中的值---------------------
24. 读取图片中的exif信息--------------------
25. 性能检测
26. 瘦身
    ### 演示图片
![Image text](https://gitee.com/weishuolin/androidmydemo/raw/master/app/src/main/assets/demo/Screenshot_2023-05-08-21-14-11-017_esa.mydemo.jpg)


## ☀️添砖加瓦
欢迎大家提issue一起完善，以及提供各种宝贵建议，持续做成商业化开发框架。
如果您感觉我们的代码有需要优化的地方或者有更好的方案欢迎随时提pr。
可添加微信进行交流，鸡翅老哥会拉你进入项目群。

如果您想看完整的教学视频，可以加入粉丝群了解学习。


# 🐾贡献代码的步骤
1. 在Gitee上fork项目到自己的repo
2. 把fork过去的项目也就是你的项目clone到你的本地
3. 修改代码
4. commit后push到自己的库
5. 登录Gitee在你仓库首页可以看到一个 pull request 按钮，点击它，填写一些说明信息，然后提交即可。 等待维护者合并
