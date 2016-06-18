# hotfix

封装了热修复框架andfix，方便使用



# 使用方法

    compile 'com.baiwanlu.android:hotfix:1.0.0'


    //在application的onCreate里调用
　　//测试情况可以开启log,默认关闭
    FHotfixLog.LOG_DEBUG = true;
    //context: 用applicationContext
    //hotfileUrl: apatch的下载地址
    //targetVersionCode:  根据versioncode来判断是否要加载
    FHotfixHelper.init(context,  hotfileUrl, versioncode);


#andfix 的apatch生成指导见 [文档](http://ie8384.com/blog/?p=1251)


　　
    
