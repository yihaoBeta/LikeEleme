package com.beta.yihao.likeeleme

import java.text.NumberFormat
import kotlin.random.Random

/**
 * @Author yihao
 * @Date 2018/12/30-22:35
 * @Email yihaobeta@163.com
 */
data class FoodBean(var image: Int, var name: String, var sales: String, var price: String, var category: String)

data class CategoryBean(var id: Int, var name: String, var total: Int = 0)
data class FoodBeanWrapper(var image: Int, var name: String, var sales: String, var price: String, var category: CategoryBean)

private val imageList = arrayListOf(
    R.mipmap.food1, R.mipmap.food2, R.mipmap.food3,
    R.mipmap.food4, R.mipmap.food5
)

fun provideRecommendData(): List<FoodBean> {
    val list = mutableListOf<FoodBean>()
    for (i in 0 until 10) {
        val image = imageList.random()
        val sales = Random.nextInt(1, 100)
        val price = Random.nextDouble(10.0, 40.0)
        val bean = FoodBean(image, "推荐美食:$i", "月售:$sales",
            NumberFormat.getCurrencyInstance().format(price), "")
        list.add(bean)
    }
    return list
}

fun providerDataSet(): List<FoodBean> {
    val list = mutableListOf<FoodBean>()
    for (i in 0..49) {
        val image = imageList.random()
        val sales = Random.nextInt(1, 100)
        val price = Random.nextDouble(10.0, 40.0)
        val bean = FoodBean(image, "美食:$i", "月售:$sales",
            NumberFormat.getCurrencyInstance().format(price), "食品分类:${i/5}")
        list.add(bean)
    }
    return list
}
