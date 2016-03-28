package com.nokor.finance.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.finance.services.Calculator;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;
import com.nokor.finance.services.shared.system.Frequency;
import com.nokor.finance.services.tools.LoanUtils;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFCalculatorImpl implements Calculator {
	
	/**
	 * Calculate periodic installment amount
	 * @param calculationParameter
	 * @return
	 */
	@Override
	public double calculateInstallmentPayment(CalculationParameter calculationParameter) {
		
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		double periodicInterestRate = calculationParameter.getPeriodicInterestRate();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods() - calculationParameter.getNumberOfPrincipalGracePeriods();
		
		double totalInterest = Frequency.getNbMonth(calculationParameter.getFrequency()) * initialPrincipal * periodicInterestRate * numberOfPeriods;
		
		double installmentPayment = (initialPrincipal + totalInterest) / numberOfPeriods;
		
		return installmentPayment;
	}
	
	/**
	 * Get amortization schedule
	 * @param startDate start date
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @return
	 */
	public AmortizationSchedules getAmortizationSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter) {
		
		AmortizationSchedules amortizationSchedules = new AmortizationSchedules(startDate, firstInstallmentDate, calculationParameter);
		List<Schedule> schedules = new ArrayList<Schedule>();
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		double periodicInterestRate = calculationParameter.getPeriodicInterestRate();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
		int numberOfPrincipalGracePeriods = calculationParameter.getNumberOfPrincipalGracePeriods();
		int numberOfPeriodsInclPrincipalGracePeriods = calculationParameter.getNumberOfPeriods() - numberOfPrincipalGracePeriods;
		
		double installmentPayment = calculateInstallmentPayment(calculationParameter);		
		double[] cashflows = new double[numberOfPeriods + 1];
		cashflows[0] = -1 * initialPrincipal;
		for (int i = 1; i <= numberOfPeriodsInclPrincipalGracePeriods; i++) {
			cashflows[i] = installmentPayment;
		}		
		double irrRate = calculateIRR(cashflows);
		Date installmentDate = null;
		Date periodStartDate = null;
		Date periodEndDate = null;
		
		for (int i = 0; i < numberOfPrincipalGracePeriods; i++) {
			Schedule schedule = new Schedule();
			if (i == 0) {
				installmentDate = firstInstallmentDate;
				periodStartDate = startDate;
			} else {
				installmentDate = DateUtils.addMonthsDate(installmentDate, 
						LoanUtils.getNbOfMonth(calculationParameter.getFrequency()));
			}
			
			periodEndDate =  DateUtils.addMonthsDate(periodStartDate, 
					LoanUtils.getNbOfMonth(calculationParameter.getFrequency()));
			
			schedule.setN(i + 1);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(DateUtils.addDaysDate(periodEndDate, -1));
			schedule.setInterestAmount(initialPrincipal * periodicInterestRate);
			schedule.setInstallmentPayment(schedule.getInterestAmount());
			schedule.setPrincipalAmount(0d);
			schedule.setBalanceAmount(initialPrincipal);
			schedules.add(schedule);			
			periodStartDate = periodEndDate;
		}
		
		for (int i = numberOfPrincipalGracePeriods; i < numberOfPeriods; i++) {
			Schedule schedule = new Schedule();
			
			if (i == 0) {
				installmentDate = firstInstallmentDate;
				periodStartDate = startDate;
			} else {
				installmentDate = DateUtils.addMonthsDate(installmentDate, 
						LoanUtils.getNbOfMonth(calculationParameter.getFrequency()));
			}
			
			periodEndDate =  DateUtils.addMonthsDate(periodStartDate, 
					LoanUtils.getNbOfMonth(calculationParameter.getFrequency()));
			
			double interestAmount =  initialPrincipal * irrRate;
			double principalAmount = installmentPayment - interestAmount;
			double balanceAmount = initialPrincipal - principalAmount;
			if (balanceAmount < 0 || (i == numberOfPeriods - 1)) {
				balanceAmount = 0.0;
			}
			schedule.setN(i + 1);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(DateUtils.addDaysDate(periodEndDate, -1));
			schedule.setInstallmentPayment(installmentPayment);
			schedule.setInterestAmount(interestAmount);
			schedule.setPrincipalAmount(principalAmount);
			schedule.setBalanceAmount(balanceAmount);
			initialPrincipal = balanceAmount;
			schedules.add(schedule);
			
			periodStartDate = periodEndDate;
		}
		
		amortizationSchedules.setIrrRate(irrRate);
		amortizationSchedules.setSchedules(schedules);
		return amortizationSchedules;
	}
	
	/**
	 * Calculate 
	 * @param cashflows
	 * @return
	 */
	private double calculateIRR(final double[] cashflows) {
		final int MAX_ITER = 10000;
		double EXCEL_EPSILON = 0.0000001;
		double irr = 0.1;
		int iter = 0;
		while (iter++ < MAX_ITER) {
			final double x1 = 1.0 + irr;
			double fx = 0.0;
			double dfx = 0.0;
			for (int i = 0; i < cashflows.length; i++) {
				final double v = cashflows[i];
				final double x1_i = Math.pow(x1, i);
				fx += v / x1_i;
				final double x1_i1 = x1_i * x1;
				dfx += -i * v / x1_i1;
			}
			final double new_irr = irr - fx / dfx;
			final double epsilon = Math.abs(new_irr - irr);

			if (epsilon <= EXCEL_EPSILON) {
				if (irr == 0.0 && Math.abs(new_irr) <= EXCEL_EPSILON) {
					return 0.0;
				} else {
					return new_irr;
				}
			}
			irr = new_irr;
		}
		return irr;
	}
}
