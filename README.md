# SquareCellLayout

> **格子布局**，支持正方形的子view,可用来做九宫格，十六宫格，只需改变列数就行了，也支持非正方形子view.主要是可以自动分配宽度，这才是正点。

看下效果吧：</br>

<img src="https://github.com/XuNeverMore/SquareCellLayout/blob/master/image/device-2018-09-17-111359.png" width="25%" height="25%">

## How to use?
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```
dependencies {
	        implementation 'com.github.XuNeverMore:SquareCellLayout:v1.0.1'
	}
```
### attr
```
<declare-styleable name="CellLayout">
        <!--是否是正方形模式-->
        <attr name="isSquareCell" format="boolean"/>
        <!--各自之间的间隙-->
        <attr name="cellSpace" format="dimension"/>
        <!--列数-->
        <attr name="columCount" format="integer"/>
    </declare-styleable>
```
### layout
```
    <com.nevermore.celllayout.CellLayout
        android:paddingTop="5dp"
        app:cellSpace="5dp"
        app:columCount="3"
        app:isSquareCell="true"
        android:paddingBottom="5dp"
        android:background="#ffffff"
        android:paddingRight="5dp"
        android:paddingLeft="5dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/cellLayout">
        ...child
    </com.nevermore.celllayout.CellLayout>

```
