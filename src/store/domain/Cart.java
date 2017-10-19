package store.domain;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 购物车
 * @author Leo
 *
 */
public class Cart {
	private Double total = 0d ;  //总计
	//购物项的集合，使用商品id作为key
	private Map<String,CartItem> map = new LinkedHashMap<String,CartItem>() ;
	public Double getTotal() {
		return total;
	}
	public Map<String, CartItem> getMap() {
		return map;
	}
	
	//将购物项添加到购物车
	public void addCartItem(CartItem cartItem) {
		//判断该商品是否存在
		String pid = cartItem.getProduct().getPid() ;
		if(map.containsKey(pid)) {
			//存在
				CartItem _cartItem = map.get(pid) ;
				//商品存在，改变购买的数量就可
				_cartItem.setCount(_cartItem.getCount() + cartItem.getCount());
		}else {
			//不存在
			map.put(pid, cartItem) ;
		}
		//改变小计
		total += cartItem.getSubtotal() ; 
	}
	//在购物车购物车中更改某个商品项的数量，触发的刷新
	public void updateCartItem(CartItem cartItem) {
		//将改动后的商品项添加到
		String pid = cartItem.getProduct().getPid() ;
		CartItem oldCartItem = map.get(pid) ;
		
		map.put(pid, cartItem) ;
		
		//改变小计
		total += cartItem.getSubtotal() - oldCartItem.getSubtotal() ; 
	}
	//从购物车中移除购物项
	public void removeCartItem(String pid) {
		//map中删除
		CartItem cartItem = map.remove(pid) ;
		//总计改动
		total = total - cartItem.getSubtotal() ;
	}
	//清空购物车
	public void clearCart() {
		//清空Map集合
		map.clear(); 
		//总计归零
		total = 0d ;
	}
}
