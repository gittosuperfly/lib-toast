# lib-toast
简单好用的Toast工具类。特性：

1. 不分主次线程都可以弹出Toast，自动区分资源 id 和 int 类型
2. 支持自定义样式，可以在 Application 中初始化 Toast 样式，一劳永逸
3. 支持自定义扩展Toast，拦截器、弹出策略等
4. 处理原生 Toast 在 Android 7.1 产生崩溃的历史遗留问题
5. 已适配 Android R 之后不能弹出自定义样式的Toast

## 引入

[![](https://jitpack.io/v/gittosuperfly/lib-toast.svg)](https://jitpack.io/#gittosuperfly/lib-toast)

**Step 1**. 添加JitPack repository到你项目的build.gradle文件

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2**. 添加库依赖
```groovy
	dependencies {
	    implementation 'com.github.gittosuperfly:lib-toast:Version'
	}
```


## 使用

**Step 1**. 在Application中初始化ToastUtils：

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
    }
}
```

**Step 2**. 在代码中弹出Toast

```kotlin
fun testBtnClick(){
    button.setOnClickListener {
        //String
        ToastUtils.show("this is message!")
        //ID, 若不存在则直接打印ID的值
        ToastUtils.show(R.string.app_name)
        //对象，调用toString方法
        ToastUtils.show(SampleMessage())
    }
}
```



## 更多

* Style 可自定义Toast样式，支持动态切换
* Interceptor 自由设置Toast拦截器
* Strategy 自定义Toast弹出策略

