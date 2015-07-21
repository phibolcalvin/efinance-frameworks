package com.nokor.finance.services.tools;

import com.nokor.finance.services.shared.system.Frequency;

/**
 * 
 * @author prasnar
 *
 */
public final class LoanUtils {
	/**
	 * Get Number Of Periods
	 * @param termInMonth
	 * @param frequency
	 * @return
	 */
	public static int getNumberOfPeriods(int termInMonth, Frequency frequency) {
		int numberOfPeriods = 0;
		switch (frequency) {
		case D:
			numberOfPeriods = termInMonth * 30;
			break;
		case W:
			numberOfPeriods = termInMonth * 4;
			break;
		case M:
			numberOfPeriods = termInMonth;
			break;
		case Q:
			numberOfPeriods = termInMonth / 3;
			break;
		case H:
			numberOfPeriods = termInMonth / 6;
			break;
		case A:
			numberOfPeriods = termInMonth / 12;
			break;
		default:
			break;
		}
		return numberOfPeriods;
	}
	
	/**
	 * Get number of months
	 * @param frequency
	 * @return
	 */
	public static int getNbOfMonth(Frequency frequency) {
		int nbOfMonths = 0;
		switch (frequency) {
		case M:
			nbOfMonths = 1;
			break;
		case Q:
			nbOfMonths = 3;
			break;
		case H:
			nbOfMonths = 6;
			break;
		case A:
			nbOfMonths = 12;
			break;
		case D:
		case W:
		default:
			throw new IllegalArgumentException("Operation not allowed");
		}
		return nbOfMonths;
	}
}
