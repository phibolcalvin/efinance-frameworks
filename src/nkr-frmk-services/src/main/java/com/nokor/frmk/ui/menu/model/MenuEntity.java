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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Menu domain object. Menu contains MenuItems.
 *
 * @author prasnar
 */
@Entity
@Table(name = "tu_menu")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MenuEntity extends EntityRefA {
    /** */
	private static final long serialVersionUID = 5434705930935315258L;

    private TypeMenu menuType;
    private List<MenuItemEntity> menuItems = new ArrayList<MenuItemEntity>();

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mnu_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    @Column(name = "mnu_code", nullable = false, length = 50)
	@Override
	public String getCode() {
		return code;
	}
    
    @Column(name = "mnu_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @return the menuType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typ_mnu_id", nullable = false)
	public TypeMenu getMenuType() {
		return menuType;
	}

	/**
	 * @param menuType the menuType to set
	 */
	public void setMenuType(TypeMenu menuType) {
		this.menuType = menuType;
	}

	
	/**
	 * @return the menuItems
	 */
    @OneToMany(mappedBy = "menu")
    @OrderBy(value = "sort_index, code")
    //@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public List<MenuItemEntity> getMenuItems() {
		return menuItems;
	}

	/**
	 * @param menuItems the menuItems to set
	 */
	public void setMenuItems(List<MenuItemEntity> menuItems) {
		this.menuItems = menuItems;
	}
}
