##### Welcome to visit:
Github: https://github.com/lorienzhang

Blog: http://blog.csdn.net/h_zhang

---

# ScreenSlide
屏幕滑动，ViewPager的基本使用

### 效果图
<div class='row'>
	<img src='./screenslide/ScreenSlide.gif' width='300px'/>
</div>

### 相关知识点
* ViewPager + FragmentStatePagerAdapter + Fragment基本使用；
* 带参数的字符串资源的使用：R.string.title

# CrossFade
view切换时添加淡入淡出效果，增强用户体验

### 效果图
下面左图是不使用crosssfade的效果，右图是使用crossfade的效果；
<div class='row'>
	<img src='./crossfading/noCrossfade.gif' width='300px'/>
	<img src='./crossfading/crossfade.gif' width='300px'>
</div>

### 相关知识点
* 在view切换之间加入交叉淡入淡出效果，这样可以增强用户体验；
* crossfade效果利用属性动画实现，掌握属性动画的基本使用，View.anim().play().with()....;

# zoom
关于图片缩放的一个例子！！！

### 效果图
<div class='row'>
	<img src='./zoom/zoom.gif' width="300px">
</div>

### 相关知识点
* View.getGlobalVisibleRect(rect, globalOffset) 获取到view相对于屏幕的坐标，存于rect之中;

* 确定对thumbnail是水平放大，还是垂直放大;

* 使用CenterCrop技术对图片进行裁剪，防止动画过程中产生不必要的拉伸;

* 起始位置和终点位置坐标的计算---Math;

* 属性动画的基本使用;

---

# LayoutChange
为布局改变添加动画效果

### 效果图
<div class='row'>
	<img src="./layoutchange/LayoutChangeAnim.gif" width="300px">
</div>

### 相关知识点
* 整体效果类似ListView，但是使用orientation为Vertical的LinearLayout实现的，如果item不是很多，这种方式比ListView更加轻量级；
* LinearLayout属性：android:animateLayoutChanges="true"，设置这个属性后，如果LinearLayout的布局发生变化，系统会添加动画效果；
* 还有关于LinearLayout其它属性：android:showDividers="middle"表示子view之间添加分割线，android:divider="@android:drawable/divider_horizontal_bright"选择系统提供的分割线
