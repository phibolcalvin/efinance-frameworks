package com.nokor.frmk.ui.menu.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Menu item domain object.
 *
 * @author prasnar
 */
@Entity
@Table(name = "tu_menu_item")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MenuItemEntity extends EntityRefA {
    /** */
	private static final long serialVersionUID = 5890304602684729645L;

    private MenuEntity menu;
    private String action;
    private String iconPath;
    private MenuItemEntity parent;
    private List<MenuItemEntity> children = new ArrayList<MenuItemEntity>();

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mnu_ite_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    @Column(name = "mnu_ite_code", nullable = false, length = 50)
	@Override
	public String getCode() {
		return code;
	}
    
    @Column(name = "mnu_ite_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }

    
	/**
	 * @return the menu
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mnu_id", nullable = false)
	public MenuEntity getMenu() {
		return menu;
	}

	/**
	 * @param menu the menu to set
	 */
	public void setMenu(MenuEntity menu) {
		this.menu = menu;
	}

	/**
	 * @return the state
	 */

    @Column(name = "mnu_ite_action", nullable = true)
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	
	

	/**
	 * @return the iconPath
	 */
    @Column(name = "mnu_ite_icon_path", nullable = true)
	public String getIconPath() {
		return iconPath;
	}

	/**
	 * @param iconPath the iconPath to set
	 */
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	/**
	 * @return the parent
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_mnu_ite_id", nullable = true)
	public MenuItemEntity getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(MenuItemEntity parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
    @OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	//@OrderColumn(name="sort_index")
    //@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public List<MenuItemEntity> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<MenuItemEntity> children) {
		this.children = children;
	}
}
