# Auxiliary

[![](https://jitpack.io/v/FMJZHF/Auxiliary.svg)](https://jitpack.io/#FMJZHF/Auxiliary)

Android开发辅助工具
 *****
## Setup

### Step 1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

```
### Step 2. Add the dependency
```
dependencies {
    implementation 'com.github.FMJZHF:Auxiliary:版本号'
}
```
## 1.实现TextView部分文字的点击事件
### 先看效果图
![TextView部分文字的点击事件效果图](https://raw.githubusercontent.com/FMJZHF/Auxiliary/master/img_folder/textview.png)

### 使用方法
```
/**
 * kotlin 语言
 */
class NoUnderLineTextActivity : AppCompatActivity(), I_ClickableSpan {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_under_line_text)

        var text = "我已阅读并同意用户协议和隐私政策\n以及官网认证服务条款"

        // PairEntity("点击部分文字"，"区分文字的点击事件")
        val pairEntity1 = PairEntity("用户协议", "1")
        val pairEntity2 = PairEntity("隐私政策", "2")
        val pairEntity3 = PairEntity("官网认证服务条款", "3")

        /**
         * 设置点击文字没有下划线
         * @param noUnderLineTextTv TextView 控件
         * @param text 显示的文字
         * @param Color.parseColor("#3BAC6A") 点击文字的字体色值
         * @param PairEntity 设置点击文字的文案
         */
        NoUnderLineTextUtil.setTextNoUnderLine(this,
            noUnderLineTextTv,
            text,
            Color.parseColor("#3BAC6A"),
            pairEntity1 , pairEntity2, pairEntity3
        )

        /**
         * 设置点击文字含有下划线
         * @param underLineTextTv TextView 控件
         * @param text 显示的文字
         * @param Color.parseColor("#3BAC6A") 点击文字的字体色值
         * @param pairEntity1 设置点击文字的文案
         */
        NoUnderLineTextUtil.setTextUnderLine(
            this,
            underLineTextTv,
            text,
            Color.parseColor("#3BAC6A"),
            pairEntity1
        )
    }

    /**
     * 不同类型的点击事件
     */
    override fun OnSpanClick(pairEntity: PairEntity) {

        var type = pairEntity.clickType
        if (type == "1") {
            showHint("点击了："+pairEntity.clickText)
        } else if (type == "2") {
            showHint("点击了："+pairEntity.clickText)
        } else if (type == "3") {
            showHint("点击了："+pairEntity.clickText)
        } else {
            showHint("未设置点击事件")
        }

    }

    private fun showHint(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
```
## 2.Android 选择器
### 先看效果图
![Android 选择器效果图](https://github.com/FMJZHF/Auxiliary/blob/master/img_folder/wheelpicker.png)

### 使用方法
#### 自定义属性
```
xmlns:app="http://schemas.android.com/apk/res-auto"
	
app:canLoop="true"    <!-- 是否循环滚动 -->
app:centerTextColor="#404040"   <!-- 选择字体的颜色 -->
app:drawItemCount="7"  <!-- 总展示数量(包含分割线) -->
app:initPosition="3"  <!-- 初识显示数量 -->
app:lineColor="#999999"  <!-- 分割线的颜色 -->
app:textSize="18sp"   <!-- 字体大小 -->
app:topBottomTextColor="#cccccc"    <!-- 上下文字的颜色 -->
```
#### 具体使用方法
```
wheelPickerYear.setDataList(mYearList)
wheelPickerYear.setInitPosition(yearPos) // 默认选中的年份
//年份滚动选择
wheelPickerYear.setLoopListener(object : WheelPickerScrollListener {
    override fun onItemSelect(item: Int) {
        yearPos = item
        Log.e("选择的年：", mYearList[item])
        titleView.text = mYearList[yearPos] + "-" + mMonthList[monthPos] + "-" + mDayList[dayPos]
    }
})	
```
## 3.时间操作类 [DateTime](https://github.com/FMJZHF/Auxiliary/blob/master/auxiliaryjar/src/main/java/com/zhf/auxiliaryjar/date/DateTime.kt)
## 4.SharedPreferences存放数据操作类 [SharedPreferencesUtil](https://github.com/FMJZHF/Auxiliary/blob/master/auxiliaryjar/src/main/java/com/zhf/auxiliaryjar/sharedPreferences/SharedPreferencesUtil.kt)
## 5.获取本机手机号码及运营商 [SIMCardInfoUtil](https://github.com/FMJZHF/Auxiliary/blob/master/auxiliaryjar/src/main/java/com/zhf/auxiliaryjar/simCard/SIMCardInfoUtil.kt)
```
<!-- 获取手机号码 -->
SIMCardInfoUtil(this).getPhoneLocalEncry(true)
<!-- 获取运营商 -->
SIMCardInfoUtil(this).providersName
```
## 6.操作状态栏 [StatusBarNavigationBarUtil](https://github.com/FMJZHF/Auxiliary/blob/master/auxiliaryjar/src/main/java/com/zhf/auxiliaryjar/statusBar/StatusBarNavigationBarUtil.kt)
```
在Activity or Fragment onCreate 方法中进行调用
// 默认状态栏字体为白色
StatusBarNavigationBarUtil.immersiveStatusBarNavigationBar(this, statusBar = true, navigationBar = true)
StatusBarNavigationBarUtil.setLightStatusBar(this, false)
```
## 7.RecyclerView操作类
####  适配器[RecyclerBaseAdapter](https://github.com/FMJZHF/Auxiliary/blob/master/auxiliaryjar/src/main/java/com/zhf/auxiliaryjar/recyclerview_adapter/RecyclerBaseAdapter.kt)
```
class NewsAdapter(context: Context, datas: MutableList<TestBean>) :RecyclerBaseAdapter<TestBean>(context, R.layout.item_layout, datas){
          override fun convert(holder: RecyclerBaseHolder, item: TestBean, position: Int) {
                val tv = holder.getView<TextView>(R.id.tv)
                      tv.text = item.name
                 val image = holder.getView<ImageView>(R.id.image)
                      image.setOnClickListener{
                           // 点击事件
                      }
           }
 }
```
#### 分割线 [DividerItemDecoration](https://github.com/FMJZHF/Auxiliary/blob/master/auxiliaryjar/src/main/java/com/zhf/auxiliaryjar/recyclerview_adapter/DividerItemDecoration.kt)
``` 
// 动态添加分割线
val itemDecoration = DividerItemDecoration( context!!, LinearLayoutManager.VERTICAL )
mRecyclerView!!.addItemDecoration(itemDecoration)
```

## 8.选择城市[CityActivity](https://github.com/FMJZHF/Auxiliary/blob/master/app/src/main/java/com/zhf/auxiliary/city/CityActivity.kt)
###![效果图](https://raw.githubusercontent.com/FMJZHF/Auxiliary/master/img_folder/city.jpg)
## 9.自定义输入金额框[MoneyEditText](https://github.com/FMJZHF/Auxiliary/blob/master/auxiliaryjar/src/main/java/com/zhf/auxiliaryjar/edittext/MoneyEditText.kt)
```
    <com.zhf.auxiliaryjar.edittext.MoneyEditText
        android:id="@+id/et_at_trad_money"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_gravity="center_vertical"
        android:background="#33000000"
        android:hint="输入金额的输入框"
        android:inputType="number"
        android:maxLength="12"
        android:padding="5dp"
        android:textColor="#000000"
        android:textCursorDrawable="@null"
        android:textSize="16sp" />

【注意】在布局文件中设置 maxLength=12，是不能限制输入字符的最大长度，需要在代码中设置
var etTradMoney:MoneyEditText
//即限定最大输入字符数为12
etTradMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)})
```

## 10.自定义验证码输入框[VerificationCodeView](https://github.com/FMJZHF/Auxiliary/blob/master/auxiliaryjar/src/main/java/com/zhf/auxiliaryjar/verification_code/VerificationCodeView.kt)
```
  <com.zhf.auxiliaryjar.verification_code.VerificationCodeView
	xmlns:app="http://schemas.android.com/apk/res-auto"
	android:id="@+id/view_verification"
	android:layout_width="match_parent"
	android:layout_marginLeft="35dp"
	android:layout_marginRight="35dp"
	android:layout_height="wrap_content"
	android:layout_marginTop="20dp"
	app:vcv_code_width="50dp"
	 app:vcv_code_input_style="true"
	app:vcv_code_size="12sp"/>
		
```

### 使用方法
```
输入完成后自动获取结果方法：
    viewVerification!!.setInputOver(this)
    
    实现 VerificationCodeView.I_inputOver 接口 ，调用 inputCodeOver(code: String) 方法

添加 ： xmlns:app="http://schemas.android.com/apk/res-auto"	
```
属性  | 值  | 含义 
------------- | ------------- | -------------
vcv_code_size  | 12sp | 输入框文字大小
vcv_code_bg_focus  | @drawable/bg_text_focused | 输入框获取焦点时边框 
vcv_code_bg_normal  | @drawable/bg_text_normal | 输入框没有焦点时边框 
vcv_code_color | @color/text_color  | 输入框文字颜色 
vcv_code_number | 4  | 输入框的数量 默认为6 
vcv_code_width | 50dp  | 单个验证码的宽度，默认40dp 
vcv_code_input_style  | true | 是否展示密码样式，默认false

