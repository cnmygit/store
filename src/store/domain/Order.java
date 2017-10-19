package store.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*`oid` varchar(32) NOT NULL,
`ordertime` datetime DEFAULT NULL,
`total` double DEFAULT NULL,
`state` int(11) DEFAULT NULL,
`address` varchar(30) DEFAULT NULL,
`name` varchar(20) DEFAULT NULL,
`telephone` varchar(20) DEFAULT NULL,
`uid` varchar(32) DEFAULT NULL,
*/
public class Order {
	private String oid ;
	private Date orderTime ;
	private Double total ;
	private Integer state ; //1：未付款 2：已付款，未发货 3：已发货，未收货 3：已收货
	private String address ;
	private String name ;
	private String telephone ;
	private User user ;
	private List<OrderItem> list = new ArrayList<OrderItem>()  ;
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<OrderItem> getList() {
		return list;
	}
	public void setList(List<OrderItem> list) {
		this.list = list;
	}
	
}
