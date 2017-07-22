# ProgressView
初次提交
ProgressView是一个镂空的进度条，可以设置按钮颜色，边框。
### 使用方法
---
root build.gradle
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
dependency
```
	dependencies {
	        compile 'com.github.piaofirst:ProgressView:v1.0'
	}

```
### 布局文件
```
 <com.piaofirst.progressviewlibrary.ProgressView
        android:id="@+id/progressView"
        android:layout_width="100dp"
        android:layout_height="50dp" />
```
### 代码设置(kotlin)
```
  //设置按钮主色
  progressView.color = Color.RED
  //设置边框宽带
  progressView.borderWidth = 3f;
  //设置按钮远郊区半径
  progressView.radius = 15f
  //设置按钮文字大小
  progressView.textSize = 30f
  
  progressView.onStart = {
     //点击开始回调
  }
  progressView.onPause = {
    //点击暂停回调
  }
  progressView.onResume = {
    //点击继续回调
  }
  progressView.onOpen = {
   //点击打开回调
  }
  
  //设置按钮状态
  progressView.state = ProgressView.ProgressState.Loading
  progressView.state = ProgressView.ProgressState.Paused
  progressView.state = ProgressView.ProgressState.Finished
  progressView.state = ProgressView.ProgressState.Idle

  //自定义状态文字
  progressView.idleText = "下载"
  progressView.loadingTextRender = { progress: Float -> "${(progress * 100).toInt()}%" }
  progressView.pauseText = "暂停"
  progressView.finishedText = "完成"
```
