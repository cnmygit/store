package store.domain;

/* `menu_id` VARCHAR(20) NOT NULL COMMENT '序列号',
  `parent_id` VARCHAR(20) NOT NULL COMMENT '父ID，根菜单时为-1，缺省-1',
  `menu_bar` VARCHAR(100) NOT NULL COMMENT '菜单名',
  `menu_url` VARCHAR(100) NOT NULL COMMENT '对应URL，一级菜单时为#',
  `menu_icon` VARCHAR(100) DEFAULT NULL COMMENT '菜单图标class样式，按钮时置空',
 */
public class Menu {
	private String menu_id ;
	private String parent_id ;
	private String menu_bar ;
	private String menu_url ;
	private String menu_icon ;
	public String getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getMenu_bar() {
		return menu_bar;
	}
	public void setMenu_bar(String menu_bar) {
		this.menu_bar = menu_bar;
	}
	public String getMenu_url() {
		return menu_url;
	}
	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
	}
	public String getMenu_icon() {
		return menu_icon;
	}
	public void setMenu_icon(String menu_icon) {
		this.menu_icon = menu_icon;
	}
	@Override
	public String toString() {
		return "Menu [menu_id=" + menu_id + ", parent_id=" + parent_id + ", menu_bar=" + menu_bar + ", menu_url="
				+ menu_url + ", menu_icon=" + menu_icon + "]";
	}
	
}
