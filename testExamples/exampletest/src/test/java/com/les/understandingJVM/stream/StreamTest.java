package com.les.understandingJVM.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * http://www.importnew.com/26090.html
 * 
 * @author taomk 2017年8月9日 上午10:51:52
 *
 */
public class StreamTest {

	public static void main(String[] args) {
		
		List<StoreProperty> stores = init();
		
		// 使用了lambda表达式来完成，如果使用传统的集合方式的话，会更加冗长
//		Collections.sort(stores, (x,y)->x.distance.compareTo(y.distance));
//		String choosedStoreName = stores.get(0).getName();
		
//		效率更好的解决方式，使用stream
		String choosedStoreName = stores
				.stream()
				.sorted(Comparator.comparingInt(s -> s.distance))
				.findFirst()
				.get()
				.getName();

		System.out.println("选择距离自己最近的一家店铺点菜 , 这家店铺的名称是：" + choosedStoreName);
		
		long hotSotreCount = stores.stream().filter(p -> p.salesCount > 1000).count();
		System.out.println("月销量超过1000，销售火爆的店铺数量是：" + hotSotreCount);
		
		StoreProperty s = stores.stream().min(Comparator.comparingInt(p->p.priceLevel)).get();
		System.out.println("价格最低的那家店铺是：" + s.getName());
		
		List<StoreProperty> nearestStores = stores.stream().sorted(Comparator.comparingInt(p -> p.distance)).limit(2).collect(Collectors.toList());
		System.out.println("距离最近的两家店铺是：");
		nearestStores.forEach(p -> System.out.println("\t" + p.name ));
		
		List<String> names = stores.parallelStream()
			.filter(p -> p.priceLevel<30)
			.sorted(Comparator.comparingInt(StoreProperty::getDistance))
			.limit(3)
			.map(StoreProperty::getName)
			.collect(Collectors.toList());
		System.out.println("筛选出价格低于30，按照距离排序的3家店铺是：");
		names.forEach(p -> System.out.println("\t" + p));
	}

	/**
	 * 初始化4家店铺
	 * 
	 * @return
	 */
	private static List<StoreProperty> init(){
		StoreProperty s1 = new StoreProperty("叫了个鸡", 1000, 500, 28);
		StoreProperty s2 = new StoreProperty("张三丰饺子馆", 2300, 1500, 25);
		StoreProperty s3 = new StoreProperty("永和大王", 580, 3000, 32);
		StoreProperty s4 = new StoreProperty("肯德基", 6000, 200, 26);
		
		return Arrays.asList(s1,s2,s3,s4);
	}
}

/**
 * 店铺属性
 * 
 * @author taomk 2017年8月9日 上午10:54:30
 *
 */
class StoreProperty {

	/**
	 * 店铺名称
	 */
	String name;
	/**
	 * 距离（单位：米）
	 */
	Integer distance;
	/**
	 * 销量（月售）
	 */
	Integer salesCount;
	/**
	 * 价格（元）
	 */
	Integer priceLevel;
	
	public StoreProperty(String name, Integer distance, Integer salesCount, Integer priceLevel) {
		super();
		this.name = name;
		this.distance = distance;
		this.salesCount = salesCount;
		this.priceLevel = priceLevel;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public Integer getSalesCount() {
		return salesCount;
	}
	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}
	public Integer getPriceLevel() {
		return priceLevel;
	}
	public void setPriceLevel(Integer priceLevel) {
		this.priceLevel = priceLevel;
	}
	
}