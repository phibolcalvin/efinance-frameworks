package com.nokor.frmk.model.t9n;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.EntityA;

/**
 *
 * @author prasnar
 */
@Entity
@Table(name = "td_translation_text")
public class TranslationText extends EntityA {
	/** */
	private static final long serialVersionUID = 8924693771141837704L;

    private Translation translation;
    private LocaleType localeType;
    private String text;
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tln_val_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	
    

	/**
	 * @return the translation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tln_id", nullable = false)
	public Translation getTranslation() {
		return translation;
	}

	/**
	 * @param translation the translation to set
	 */
	public void setTranslation(Translation translation) {
		this.translation = translation;
	}

	/**
	 * @return the localeType
	 */
    @ManyToOne
    @JoinColumn(name = "typ_loc_id", nullable = false)
	public LocaleType getLocaleType() {
		return localeType;
	}

	/**
	 * @param localeType the localeType to set
	 */
	public void setLocaleType(LocaleType localeType) {
		this.localeType = localeType;
	}

	/**
	 * @return the text
	 */
    @Column(name = "tln_val_text")
	public String getText() {
		return text != null ? text : StringUtils.EMPTY;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

    
}
