package com.zhf.auxiliary.city

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zhf.auxiliary.R
import com.zhf.auxiliary.city.adapter.HotCityListAdapter
import com.zhf.auxiliary.city.adapter.SearchCityListAdapter
import com.zhf.auxiliary.city.model.CityEntity
import com.zhf.auxiliary.city.util.JsonReadUtil
import com.zhf.auxiliary.city.view.LetterListView
import com.zhf.auxiliary.city.view.ScrollWithGridView
import com.zhf.auxiliary.util.ToastUtils
import kotlinx.android.synthetic.main.activity_city.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * 选择城市
 */
class CityActivity : AppCompatActivity(), AbsListView.OnScrollListener {
	
	//文件名称
	private val CityFileName = "allcity.json"
	
	
	private var handler: Handler? = null
	private var overlay: TextView? = null // 对话框首字母TextView
	private var overlayThread: OverlayThread? = null // 显示首字母对话框
	private var mReady = false
	private var isScroll = false
	
	private var alphaIndexer: HashMap<String, Int>? = null// 存放存在的汉语拼音首字母和与之对应的列表位置
	
	protected var hotCityList: MutableList<CityEntity> = ArrayList()
	protected var totalCityList: MutableList<CityEntity> = ArrayList()
	protected var curCityList: MutableList<CityEntity> = ArrayList()
	protected var searchCityList: MutableList<CityEntity> = ArrayList()
	protected lateinit var cityListAdapter: CityListAdapter
	protected lateinit var searchCityListAdapter: SearchCityListAdapter
	
	private var locationCity: String? = null
	private var curSelCity: String? = null
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// 默认软键盘不弹出
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
		setSystemBarTransparent()
		
		setContentView(R.layout.activity_city)
		
		initView()
		initData()
		initListener()
	}
	
	
	private fun initView() {
		handler = Handler()
		overlayThread = OverlayThread()
		searchCityListAdapter = SearchCityListAdapter(this, searchCityList)
		searchCityLv.setAdapter(searchCityListAdapter)
		locationCity = "杭州"
		curSelCity = locationCity
	}
	
	private fun initData() {
		initTotalCityList()
		cityListAdapter = CityListAdapter(this, totalCityList, hotCityList)
		totalCityLv.setAdapter(cityListAdapter)
		totalCityLv.setOnScrollListener(this)
		totalCityLv.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
			if (position > 1) {
				val cityEntity = totalCityList[position]
				showSetCityDialog(cityEntity.getName(), cityEntity.getCityCode())
			}
		})
		lettersLv.setOnTouchingLetterChangedListener(LetterListViewListener())
		initOverlay()
	}
	
	private fun initListener() {
		
		searchCityLv.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
			val cityEntity = searchCityList[position]
			showSetCityDialog(cityEntity.getName(), cityEntity.getCityCode())
		})
		searchContentEt.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
			
			}
			
			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
			
			}
			
			override fun afterTextChanged(s: Editable) {
				val content = searchContentEt.getText().toString().trim({ it <= ' ' }).toLowerCase()
				setSearchCityList(content)
			}
		})
		
		searchContentEt.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				hideSoftInput(searchContentEt.getWindowToken())
				val content = searchContentEt.getText().toString().trim({ it <= ' ' }).toLowerCase()
				setSearchCityList(content)
				return@OnEditorActionListener true
			}
			false
		})
	}
	
	
	/**
	 * 设置搜索数据展示
	 */
	private fun setSearchCityList(content: String) {
		searchCityList.clear()
		if (TextUtils.isEmpty(content)) {
			totalCityLv.setVisibility(View.VISIBLE)
			lettersLv.setVisibility(View.VISIBLE)
			searchCityLv.setVisibility(View.GONE)
			noSearchDataTv.setVisibility(View.GONE)
		} else {
			totalCityLv.setVisibility(View.GONE)
			lettersLv.setVisibility(View.GONE)
			for (i in curCityList.indices) {
				val cityEntity = curCityList[i]
				if (cityEntity.getName().contains(content) || cityEntity.getPinyin().contains(content)
						|| cityEntity.getFirst().contains(content)
				) {
					searchCityList.add(cityEntity)
				}
			}
			
			if (searchCityList.size != 0) {
				noSearchDataTv.setVisibility(View.GONE)
				searchCityLv.setVisibility(View.VISIBLE)
			} else {
				noSearchDataTv.setVisibility(View.VISIBLE)
				searchCityLv.setVisibility(View.GONE)
			}
			
			searchCityListAdapter.notifyDataSetChanged()
		}
	}
	
	/**
	 * 初始化全部城市列表
	 */
	fun initTotalCityList() {
		hotCityList.clear()
		totalCityList.clear()
		curCityList.clear()
		
		val cityListJson = JsonReadUtil.getJsonStr(this, CityFileName)
		val jsonObject: JSONObject
		try {
			jsonObject = JSONObject(cityListJson)
			val array = jsonObject.getJSONArray("City")
			for (i in 0 until array.length()) {
				val `object` = array.getJSONObject(i)
				val name = `object`.getString("name")
				val key = `object`.getString("key")
				val pinyin = `object`.getString("full")
				val first = `object`.getString("first")
				val cityCode = `object`.getString("code")
				
				val cityEntity = CityEntity()
				cityEntity.setName(name)
				cityEntity.setKey(key)
				cityEntity.setPinyin(pinyin)
				cityEntity.setFirst(first)
				cityEntity.setCityCode(cityCode)
				
				if (key == "热门") {
					hotCityList.add(cityEntity)
				} else {
					if (cityEntity.getKey() != "0" && cityEntity.getKey() != "1") {
						curCityList.add(cityEntity)
					}
					totalCityList.add(cityEntity)
				}
			}
			
		} catch (e: JSONException) {
			e.printStackTrace()
		}
		
	}
	
	override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
			isScroll = true
		} else {
			isScroll = false
		}
	}
	
	override fun onScroll(
			view: AbsListView, firstVisibleItem: Int,
			visibleItemCount: Int, totalItemCount: Int
	) {
		if (!isScroll) {
			return
		}
		
		if (mReady) {
			val key = getAlpha(totalCityList[firstVisibleItem].getKey())
			overlay!!.setText(key)
			overlay!!.setVisibility(View.VISIBLE)
			handler!!.removeCallbacks(overlayThread)
			// 延迟让overlay为不可见
			handler!!.postDelayed(overlayThread, 700)
		}
	}
	
	/**
	 * 总城市适配器
	 */
	inner class CityListAdapter internal constructor(
			private val context: Context,
			private val totalCityList: List<CityEntity>?,
			private val hotCityList: List<CityEntity>
	) : BaseAdapter() {
		private val inflater: LayoutInflater = LayoutInflater.from(context)
		private val VIEW_TYPE = 3
		
		init {

			alphaIndexer = HashMap()
			
			for (i in totalCityList!!.indices) {
				// 当前汉语拼音首字母
				val currentStr = totalCityList!!.get(i).getKey()
				
				val previewStr = if (i - 1 >= 0) totalCityList.get(i - 1).getKey() else " "
				if (previewStr != currentStr) {
					val name = getAlpha(currentStr)
					alphaIndexer!![name] = i
				}
			}
		}
		
		override fun getViewTypeCount(): Int {
			return VIEW_TYPE
		}
		
		override fun getItemViewType(position: Int): Int {
			return if (position < 2) position else 2
		}
		
		override fun getCount(): Int {
			return totalCityList?.size ?: 0
		}
		
		override fun getItem(position: Int): Any {
			return totalCityList!![position]
		}
		
		override fun getItemId(position: Int): Long {
			return position.toLong()
		}
		
		override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
			var convertView = convertView
			val curCityNameTv: TextView
			val holder: ViewHolder
			val viewType = getItemViewType(position)
			if (viewType == 0) { // 定位
				convertView = inflater.inflate(R.layout.select_city_location_item, null)
				
				val noLocationLl: LinearLayout = convertView.findViewById(R.id.cur_city_no_data_ll)
				val getLocationTv: TextView = convertView.findViewById(R.id.cur_city_re_get_location_tv)
				curCityNameTv = convertView.findViewById(R.id.cur_city_name_tv)
				
				if (TextUtils.isEmpty(locationCity)) {
					noLocationLl.setVisibility(View.VISIBLE)
					curCityNameTv.visibility = View.GONE
					getLocationTv.setOnClickListener(View.OnClickListener {
						//获取定位
					})
				} else {
					noLocationLl.setVisibility(View.GONE)
					curCityNameTv.visibility = View.VISIBLE
					
					curCityNameTv.text = locationCity
					curCityNameTv.setOnClickListener {
						if (locationCity != curSelCity) {
							//设置城市代码
							var cityCode = ""
							for (cityEntity in curCityList) {
								if (cityEntity.getName() == locationCity) {
									cityCode = cityEntity.getCityCode()
									break
								}
							}
							showSetCityDialog(locationCity!!, cityCode)
						} else {
							ToastUtils.show("当前定位城市" + curCityNameTv.text.toString())
						}
					}
				}
			} else if (viewType == 1) { //热门城市
				convertView = inflater.inflate(R.layout.recent_city_item, null)
				val hotCityGv: ScrollWithGridView = convertView.findViewById(R.id.recent_city_gv)
				hotCityGv.setAdapter(HotCityListAdapter(context, this.hotCityList))
				hotCityGv.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
					val cityEntity = hotCityList[position]
					showSetCityDialog(cityEntity.getName(), cityEntity.getCityCode())
				})
			} else {
				if (null == convertView) {
					convertView = inflater.inflate(R.layout.city_list_item_layout, null)
					holder = ViewHolder(convertView)
					convertView!!.tag = holder
				} else {
					holder = convertView.tag as ViewHolder
				}
				
				val cityEntity = totalCityList!![position]
				holder.cityKeyTv!!.visibility = View.VISIBLE
				holder.cityKeyTv!!.text = getAlpha(cityEntity.getKey())
				holder.cityNameTv!!.setText(cityEntity.getName())
				
				if (position >= 1) {
					val preCity = totalCityList[position - 1]
					if (preCity.getKey() == cityEntity.getKey()) {
						holder.cityKeyTv!!.visibility = View.GONE
					} else {
						holder.cityKeyTv!!.visibility = View.VISIBLE
					}
				}
			}
			return convertView!!
		}
		
	 inner class ViewHolder(var view:View) {
			 var cityNameTv: TextView = view.findViewById(R.id.city_name_tv)
			 var cityKeyTv: TextView =  view.findViewById(R.id.city_key_tv)
		}
	}

	/**
	 * 获得首字母
	 */
	private fun getAlpha(key: String): String {
		return if (key == "0") {
			"定位"
		} else if (key == "1") {
			"热门"
		} else {
			key
		}
	}
	
	
	/**
	 * 初始化汉语拼音首字母弹出提示框
	 */
	private fun initOverlay() {
		mReady = true
		val inflater = LayoutInflater.from(this)
		overlay = inflater.inflate(R.layout.overlay, null) as TextView
		overlay!!.setVisibility(View.INVISIBLE)
		val lp = WindowManager.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT
		)
		val windowManager = this
				.getSystemService(Context.WINDOW_SERVICE) as WindowManager
		windowManager.addView(overlay, lp)
	}
	
	
	private inner class LetterListViewListener : LetterListView.OnTouchingLetterChangedListener {
		
		override fun onTouchingLetterChanged(s: String) {
			isScroll = false
			if (alphaIndexer!![s] != null) {
				val position = alphaIndexer!![s]
				totalCityLv.setSelection(position!!)
				overlay!!.setText(s)
				overlay!!.setVisibility(View.VISIBLE)
				handler!!.removeCallbacks(overlayThread)
				// 延迟让overlay为不可见
				handler!!.postDelayed(overlayThread, 700)
			}
		}
	}
	
	/**
	 * 设置overlay不可见
	 */
	private inner class OverlayThread : Runnable {
		override fun run() {
			overlay!!.setVisibility(View.GONE)
		}
	}
	
	/**
	 * 展示设置城市对话框
	 */
	private fun showSetCityDialog(curCity: String, cityCode: String) {
		if (curCity == curSelCity) {
			ToastUtils.show("当前定位城市$curCity")
			return
		}
		
		val builder = AlertDialog.Builder(this)  //先得到构造器
		builder.setTitle("提示") //设置标题
		builder.setMessage("是否设置 $curCity 为您的当前城市？") //设置内容
		
		builder.setPositiveButton("确定") { dialog, which ->
			//设置确定按钮
			dialog.dismiss()
			//选中之后做你的方法
		}
		builder.setNegativeButton("取消") { dialog, which ->
			//设置取消按钮
			dialog.dismiss()
		}
		
		builder.create().show()
	}
	
	/**
	 * 隐藏软件盘
	 */
	fun hideSoftInput(token: IBinder?) {
		if (token != null) {
			val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			im.hideSoftInputFromWindow(
					token,
					InputMethodManager.HIDE_NOT_ALWAYS
			)
		}
	}
	
	
	/**
	 * 设置沉浸式状态栏
	 */
	private fun setSystemBarTransparent() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			// 5.0 LOLLIPOP解决方案
			window.decorView.systemUiVisibility =
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
			window.statusBarColor = Color.TRANSPARENT
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 4.4 KITKAT解决方案
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
		}
	}
}
