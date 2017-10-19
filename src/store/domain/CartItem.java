package store.domain;

/**
 * 购物项
 * @author Leo
 *
 */
public class CartItem {
	private Product product ; //商品
	private Integer count ; //购买数量
	private Double subtotal ; //小计
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getSubtotal() {
		return count * product.getShop_price();
	}
	
}
