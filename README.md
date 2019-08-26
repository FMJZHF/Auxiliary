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
    implementation 'com.github.FMJZHF:Auxiliary:v1.0'
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
## 2.时间操作类 DateTime