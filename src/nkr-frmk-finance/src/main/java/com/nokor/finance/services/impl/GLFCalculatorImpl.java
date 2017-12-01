package com.nokor.finance.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.type.Month;

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
	 * @param startDate
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
			
			installmentDate = LoanUtils.getNextInstallementDate(firstInstallmentDate, i, calculationParameter.getFrequency());
			periodStartDate = LoanUtils.getNextInstallementDate(startDate, i, calculationParameter.getFrequency());
			periodEndDate = LoanUtils.getNextInstallementDate(startDate, (i + 1), calculationParameter.getFrequency());
			
			schedule.setN(i + 1);
			schedule.setOriginNCap(schedule.getN());
			schedule.setOriginNIap(schedule.getN());
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(DateUtils.addDaysDate(periodEndDate, -1));
			schedule.setInterestAmount(initialPrincipal * periodicInterestRate);
			schedule.setInstallmentPayment(schedule.getInterestAmount());
			schedule.setPrincipalAmount(0d);
			schedule.setBalanceAmount(initialPrincipal);
			schedules.add(schedule);		
		}
		
		for (int i = numberOfPrincipalGracePeriods; i < numberOfPeriods; i++) {
			Schedule schedule = new Schedule();
			
			installmentDate = LoanUtils.getNextInstallementDate(firstInstallmentDate, i, calculationParameter.getFrequency());
			periodStartDate = LoanUtils.getNextInstallementDate(startDate, i, calculationParameter.getFrequency());
			periodEndDate = LoanUtils.getNextInstallementDate(startDate, (i + 1), calculationParameter.getFrequency());
			
			double interestAmount =  initialPrincipal * irrRate;
			double principalAmount = installmentPayment - interestAmount;
			double balanceAmount = initialPrincipal - principalAmount;
			if (balanceAmount < 0 || (i == numberOfPeriods - 1)) {
				balanceAmount = 0.0;
			}
			schedule.setN(i + 1);
			schedule.setOriginNCap(schedule.getN());
			schedule.setOriginNIap(schedule.getN());
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(DateUtils.addDaysDate(periodEndDate, -1));
			schedule.setInstallmentPayment(installmentPayment);
			schedule.setInterestAmount(interestAmount);
			schedule.setPrincipalAmount(principalAmount);
			schedule.setBalanceAmount(balanceAmount);
			initialPrincipal = balanceAmount;
			schedules.add(schedule);
		}
		
		amortizationSchedules.setIrrRate(irrRate);
		amortizationSchedules.setSchedules(schedules);
		return recalculateHybridAmortizationSchedules(amortizationSchedules);
	}
	
	/**
	 * 
	 * @param months
	 * @param oldSchedules
	 * @return
	 */
	private List<Schedule> convertScheduleByMonthOfPayments(List<Month> months, List<Schedule> oldSchedules) {
		List<Schedule> newSchedules = new ArrayList<Schedule>();
		if (months != null && !months.isEmpty()) {
			if (!oldSchedules.isEmpty()) {
				int monthIndex = 0;
				Date firstInstallmentDate = null;
				Date installmentDate = null;
				for (int i = 0; i < oldSchedules.size(); i++) {
					Schedule newSchedule = (Schedule) SerializationUtils.clone(oldSchedules.get(i));
					newSchedule.setOriginNCap(oldSchedules.get(i).getN());
					newSchedule.setOriginNIap(oldSchedules.get(i).getN());
					newSchedules.add(newSchedule);
					
					if (i == 0) {
						firstInstallmentDate = oldSchedules.get(0).getInstallmentDate();
						newSchedule.setInstallmentDate(firstInstallmentDate);
					} else {
						installmentDate = DateUtils.addMonthsDate(firstInstallmentDate, getNbMonths(oldSchedules.get(0).getInstallmentDate(), months).get(monthIndex));
						newSchedule.setInstallmentDate(installmentDate);
						firstInstallmentDate = installmentDate;
						
						if (monthIndex == getNbMonths(oldSchedules.get(0).getInstallmentDate(), months).size() -1) {
							monthIndex = 0;
						} else {
							monthIndex++;
						}
					}
					
					int nbBetwenTwoDate = 1;
					if ((i + 1) <= oldSchedules.size()) {
						if (i == 0) {
							nbBetwenTwoDate = checkInstallmentDate(newSchedules.get(0).getPeriodStartDate(), 
									newSchedules.get(0).getInstallmentDate());
							nbBetwenTwoDate--;
						} else {
							nbBetwenTwoDate = checkInstallmentDate(newSchedules.get(i - 1).getInstallmentDate(), 
									newSchedule.getInstallmentDate());
						}
					}
					
					newSchedule.setPeriodStartDate(DateUtils.addMonthsDate(newSchedule.getInstallmentDate(), -1 * (nbBetwenTwoDate)));
					newSchedule.setPeriodEndDate(DateUtils.addDaysDate(newSchedule.getInstallmentDate(), -1));
				}
			}
			
		}
		return newSchedules;
	}
	
	/**
	 * 
	 * @param firstInstallmentDate
	 * @param months
	 * @return
	 */
	private List<Integer> getNbMonths(Date firstInstallmentDate, List<Month> months) {
		List<Integer> nbMonths = new ArrayList<Integer>();
		List<Month> sortMonths = new ArrayList<Month>();
		int monthOfFirstInstallment = Integer.parseInt(DateUtils.getDateLabel(firstInstallmentDate, "M"));
		for (int index = 0; index < months.size(); index++) {
			if (monthOfFirstInstallment == months.get(index).getId() + 1) {
				if (index == 0) {
					sortMonths.addAll(months);
				} else if (index == 1) {
					if (months.size() == 4) {
						sortMonths.add(months.get(index));
						sortMonths.add(months.get(index + 1));
						sortMonths.add(months.get(index + 2));
						sortMonths.add(months.get(index - 1));
					} else if (months.size() == 2) {
						sortMonths.add(months.get(1));
						sortMonths.add(months.get(0));
					}
				} else if (index == 2) {
					sortMonths.add(months.get(index));
					sortMonths.add(months.get(index + 1));
					sortMonths.add(months.get(index - 2));
					sortMonths.add(months.get(index - 1));
				} else {
					sortMonths.add(months.get(index));
					sortMonths.add(months.get(index - 3));
					sortMonths.add(months.get(index - 2));
					sortMonths.add(months.get(index - 1));
				}
			}
		}
		Date nextYear = null;
		for (int i = 0; i < sortMonths.size(); i++) {
			int nbMonthCurrent = sortMonths.get(i).getId() + 1;
			int nbMonthNext = 0;
			if (i + 1 == sortMonths.size()) {
				nbMonthNext = sortMonths.get(0).getId() + 1;
			} else {
				nbMonthNext = sortMonths.get(i + 1).getId() + 1;	
			}
			if (nbMonthNext <= nbMonthCurrent) {
				String day = DateUtils.getDateLabel(firstInstallmentDate, "dd");
				String year = DateUtils.getDateLabel(firstInstallmentDate, DateUtils.YEAR_FORMAT);
				String month = String.valueOf(nbMonthCurrent);
				Date currentMonth = DateUtils.getDate(day + "/" + month + "/" + year, DateUtils.FORMAT_DDMMYYYY_SLASH);
				
				String dayNext = DateUtils.getDateLabel(firstInstallmentDate, "dd");
				String yearNext = DateUtils.getDateLabel(firstInstallmentDate, DateUtils.YEAR_FORMAT);
				String monthNext = String.valueOf(nbMonthNext);
				Date nextMonth = DateUtils.getDate(dayNext + "/" + monthNext + "/" + yearNext, DateUtils.FORMAT_DDMMYYYY_SLASH);
				nextYear = DateUtils.addYearsDate(nextMonth, 1);
				for (int j = 1; j < 13; j++) {
					if (DateUtils.isSameDay(DateUtils.addMonthsDate(currentMonth, j), nextYear)) {
						nbMonths.add(j);
						break;
					}
				}
			} else {
				nbMonths.add(nbMonthNext - nbMonthCurrent);	
			}
		}
		return nbMonths;
	}
	
	/**
	 * 
	 * @param firstInstallmentDate
	 * @param secondInstallmentDate
	 * @return
	 */
	private int checkInstallmentDate(Date firstInstallmentDate, Date secondInstallmentDate) {
		int nbBetween = 1;
		for (int i = 1; i < 13; i++) {
			if (DateUtils.isSameDay(DateUtils.addMonthsDate(firstInstallmentDate, i), secondInstallmentDate)) {
				nbBetween = i;
				break;
			}
		}
		return nbBetween;
	}
	
	/**
	 * @param amortizationSchedules
	 * @param calculationParameter
	 * @param calculationParameter
	 * @return
	 */
	private AmortizationSchedules recalculateHybridAmortizationSchedules(AmortizationSchedules amortizationSchedules) {
		CalculationParameter calculationParameter = amortizationSchedules.getCalculationParameter();
		double minPaymentHybridAmount = 0d;
		if (calculationParameter.getMinPaymentHybridAmount() != null) {
			minPaymentHybridAmount = calculationParameter.getMinPaymentHybridAmount();
		}
		List<Month> months = calculationParameter.getMonths();
		if (calculationParameter.isHybrid() && (calculationParameter.getFrequency() == Frequency.H ||
				calculationParameter.getFrequency() == Frequency.Q) && minPaymentHybridAmount > 0) {
			
			// List schedule after converted
			List<Schedule> convertedSchedules = amortizationSchedules.getSchedules();
			if (months != null && !months.isEmpty()) {
				convertedSchedules = convertScheduleByMonthOfPayments(months, amortizationSchedules.getSchedules());
			}
			
			List<Schedule> schedules = new ArrayList<Schedule>();
			
			int nbMonths = Frequency.getNbMonth(calculationParameter.getFrequency());
			int installmentN = 1;
			int monthIndex = 0;
			for (Schedule oldSchedule : convertedSchedules) {
				if (months != null && !months.isEmpty()) {
					Date firstInstallmentDate = amortizationSchedules.getSchedules().get(0).getInstallmentDate();
					if (installmentN == 1) {
						Date firstContraStartDate = amortizationSchedules.getSchedules().get(0).getPeriodStartDate();
						for (int j = 1; j < 13; j++) {
							if (DateUtils.isSameDay(DateUtils.addMonthsDate(firstContraStartDate, j), firstInstallmentDate)) {
								nbMonths = j;
								break;
							}
						}
					} else {
						nbMonths = getNbMonths(firstInstallmentDate, months).get(monthIndex);
						if (monthIndex == getNbMonths(firstInstallmentDate, months).size() -1) {
							monthIndex = 0;
						} else {
							monthIndex++;
						}
					}	
				}
				
				double totalPaidInterestAmount = 0d;
				double totalPaidPrincipal = 0d;
				for (int i = 0; i < nbMonths; i++) {
					Schedule schedule = new Schedule();
					schedule.setN(installmentN);
					schedule.setOriginNCap(oldSchedule.getN());
					schedule.setOriginNIap(oldSchedule.getN());
					schedule.setInstallmentDate(DateUtils.addMonthsDate(oldSchedule.getInstallmentDate(), -1 * (nbMonths - (i + 1))));
					schedule.setPeriodStartDate(DateUtils.addMonthsDate(schedule.getInstallmentDate(), -1));
					schedule.setPeriodEndDate(DateUtils.addDaysDate(schedule.getInstallmentDate(), -1));

					if (i == nbMonths - 1) {
						double diffInterestAmount = oldSchedule.getInterestAmount() - totalPaidInterestAmount;
						if (diffInterestAmount < 0) {
							diffInterestAmount = 0d;
						}
						double diffPrincipalAmount = oldSchedule.getPrincipalAmount() - totalPaidPrincipal;
						schedule.setBalanceAmount(oldSchedule.getBalanceAmount());
						schedule.setPrincipalAmount(diffPrincipalAmount);
						schedule.setInterestAmount(diffInterestAmount);
					} else {
						double interestAmount = 0d;
						double diff = oldSchedule.getInterestAmount() - totalPaidInterestAmount;
						if (diff < 0) {
							diff = 0;
						}
						
						if (diff > calculationParameter.getMinPaymentHybridAmount()) {
							interestAmount = calculationParameter.getMinPaymentHybridAmount();
						} else {
							interestAmount = diff;
						}
						
						double principalAmount = 0d;
						double totalBalanceAmount = oldSchedule.getBalanceAmount() + oldSchedule.getPrincipalAmount();
						if (checkMonthOfPayment(Integer.parseInt(DateUtils.getDateLabel(schedule.getInstallmentDate(), "M")), months)) {
							principalAmount = oldSchedule.getPrincipalAmount();
							totalPaidPrincipal += principalAmount;
						}
						schedule.setBalanceAmount(totalBalanceAmount - totalPaidPrincipal);
						
						totalPaidInterestAmount += interestAmount;
						schedule.setInterestAmount(interestAmount);
						schedule.setPrincipalAmount(principalAmount);
					}
					schedule.setInstallmentPayment(schedule.getPrincipalAmount() + schedule.getInterestAmount());
					schedules.add(schedule);
					installmentN++;
				}
			}
			amortizationSchedules.setSchedules(schedules);
		}
		return amortizationSchedules;
	}
	
	/**
	 * 
	 * @param scheduleInstallmentMonth
	 * @param months
	 * @return
	 */
	private boolean checkMonthOfPayment(int scheduleInstallmentMonth, List<Month> months) {
		if (months != null && !months.isEmpty()) {
			for (Month month : months) {
				if (scheduleInstallmentMonth == month.getId() + 1) {
					return true;
				}
			}
		}
		return false;
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
