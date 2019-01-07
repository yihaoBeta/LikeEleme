package com.beta.yihao.likeeleme.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.beta.yihao.likeeleme.*
import com.beta.yihao.likeeleme.adapter.RecommendAdapter
import com.beta.yihao.likemeituan.adapter.CategoryAdapter
import com.beta.yihao.likemeituan.adapter.DetailAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.food_list_layout.view.*

/**
 * @Author yihao
 * @Date 2019/1/1-19:11
 * @Email yihaobeta@163.com
 */
class OrderLayout(context: Context) : LinearLayout(context){
    //food列表的数据源
    private var dataSet: List<FoodBeanWrapper> = arrayListOf()
    //分类列表的数据源
    private var categorySet: List<CategoryBean> = arrayListOf()
    //绘制food list header的数据源
    private var categoryListForFloatingBar: List<CategoryBean> = arrayListOf()
    private var categoryAdapter: CategoryAdapter? = null
    private var detailAdapter: DetailAdapter? = null
    //food list header的装饰类
    private var headerItemDecoration: FoodHeaderItemDecoration? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.food_list_layout, this)
        initViews()
    }

    private fun initViews() {
        setData(providerDataSet())

        recommendRv.apply {
            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
            RecommendAdapter(provideRecommendData()).bindToRecyclerView(this)
        }
        categoryRv.apply {
            layoutManager = LinearLayoutManager(context)
            categoryAdapter = CategoryAdapter(categorySet)
            categoryAdapter!!.bindToRecyclerView(this)
            addOnItemTouchListener(object : OnItemClickListener() {
                override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    categoryAdapter!!.changeCategory(categorySet[position])
                    if (headerItemDecoration != null) {
                        headerItemDecoration!!.setClickStatus(true)
                    }
                    if (detailAdapter != null) {
                        detailAdapter!!.smoothMoveToPosition(categorySet[position].total)
                    }
                }

            })
        }


        foodListRv.apply {
            val manager = LinearLayoutManager(context)
            manager.isSmoothScrollbarEnabled = true
            manager.isAutoMeasureEnabled = true
            layoutManager = manager
            this.setHasFixedSize(true)
            detailAdapter = DetailAdapter(dataSet)
            detailAdapter!!.bindToRecyclerView(this)

            headerItemDecoration =
                    FoodHeaderItemDecoration(context, categoryListForFloatingBar)
            addItemDecoration(headerItemDecoration!!)
            headerItemDecoration!!.setOnCategoryChangeListener {
                categoryAdapter!!.changeCategory(it)
            }

            setOnTouchListener { _, _ ->
                headerItemDecoration!!.setClickStatus(false)
                false
            }
        }
    }

    //处理数据信息
    private fun setData(data: List<FoodBean>) {
        dataSet = wrapperFoodBean(data)
        categoryListForFloatingBar = genCategoryListForFloatingBar(dataSet)
        categoryAdapter?.notifyDataSetChanged()
        detailAdapter?.notifyDataSetChanged()
    }

    //对原始数据源进行包装，添加分类信息
    private fun wrapperFoodBean(data: List<FoodBean>): List<FoodBeanWrapper> {
        if (data.isEmpty()) throw java.lang.IllegalArgumentException("dataSet is empty")
        if (categorySet.isEmpty()) categorySet = genCategorySet(data)
        val foodBeanWrapper = arrayListOf<FoodBeanWrapper>()
        data.forEach {
            val categoryId = genCategoryId(it.category)
            foodBeanWrapper.add(FoodBeanWrapper(it.image,it.name,it.sales,it.price, CategoryBean(categoryId, it.category)))
        }

        return foodBeanWrapper
    }

    //获取分类id
    private fun genCategoryId(category: String): Int {
        categorySet.forEach {
            if (it.name == category) {
                return it.id
            }
        }
        throw IllegalStateException("something is wrong")
    }

    //计算分类数据
    private fun genCategorySet(data: List<FoodBean>): List<CategoryBean> {
        if (data.isEmpty()) throw IllegalArgumentException("dataSet is empty")
        val categorySet = mutableListOf<CategoryBean>()
        data.groupBy { it.category }.map { it.key }.forEachIndexed { index, s ->
            categorySet.add(CategoryBean(index, s))
        }
        //存储前一个分类中的food个数
        val listSize = arrayListOf<Int>()
        listSize.add(0)

        data.groupBy { it.category }.flatMap {
            arrayListOf(it.value.size)
        }.reduce { acc, i ->
            listSize.add(acc)
            acc + i
        }
        categorySet.forEachIndexed { index, categoryBean ->
            categoryBean.total = listSize[index]
        }

        return categorySet
    }


    //获取用来显示header的分类信息
    private fun genCategoryListForFloatingBar(data: List<FoodBeanWrapper>): List<CategoryBean> {
        if (data.isEmpty()) throw IllegalArgumentException("dataSet is empty")
        return data.map { it.category }.toList()
    }
}