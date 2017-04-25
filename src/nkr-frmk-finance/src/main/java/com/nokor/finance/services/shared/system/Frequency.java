package com.nokor.finance.services.shared.system;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.sysref.ISysRefData;

/**
 * Frequency
 * @author ly.youhort
 *
 */
public enum Frequency implements ISysRefData {
    D("daily"),
    W("weekly"),
    B("bi.weekly"),
    M("monthly"),
    Q("quarterly"),
    H("half.year"),
    A("annually");
    
    private final String code;

    /**
     * 
     */
    private Frequency(final String code) {
        this.code = code;
    }

    /**
     * return code
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * return desc
     */
    @Override
    public String getDesc() {
        return I18N.value(code);
    }
    
    /**
     * List of frequencies
     * @return
     */
    public static List<Frequency> list() {
    	List<Frequency> frequencies  = new ArrayList<Frequency>();
    	frequencies.add(A);
    	//frequencies.add(D);
    	frequencies.add(H);
    	frequencies.add(M);
    	frequencies.add(Q);
    	frequencies.add(W);
    	frequencies.add(B);
    	return frequencies;
    }
    
    /**
     * @param frequency
     * @return
     */
    public static int getNbMonth(Frequency frequency) {
    	switch (frequency) {
		case A:
			return 12;
		case H:
			return 6;
		case M:
		case W:
		case B:
			return 1;
		case Q:
			return 3;
		default:
			throw new IllegalArgumentException("Unsuppoted frequency " + frequency);
		}
    }
    
    /**
     * @param frequency
     * @return
     */
    public static int getNbSchedulePerYear(Frequency frequency) {
    	return 12 / getNbMonth(frequency);
    }
}
