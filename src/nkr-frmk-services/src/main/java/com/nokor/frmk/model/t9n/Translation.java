package com.nokor.frmk.model.t9n;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 *
 * @author prasnar
 */
@Entity
@Table(name = "td_translation")
public class Translation extends EntityRefA {
	/** */
	private static final long serialVersionUID = -3449995292900747233L;

    private List<TranslationText> translationTexts;



	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tln_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	
    @Column(name = "tln_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}
	
    @Column(name = "tln_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }


	/**
	 * @return the translationTexts
	 */
    @OneToMany(targetEntity = TranslationText.class, mappedBy = "translation", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	public List<TranslationText> getTranslationTexts() {
		return translationTexts;
	}

	/**
	 * @param translationTexts the translationTexts to set
	 */
	public void setTranslationTexts(List<TranslationText> translationTexts) {
		this.translationTexts = translationTexts;
	}


    


}
