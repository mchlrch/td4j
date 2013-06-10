/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2009 Michael Rauch

  td4j is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  td4j is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with td4j.  If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************/

package org.td4j.examples.currencyconverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.td4j.swing.workbench.Workbench;

import ch.miranet.commons.TK;


// TODO: BigDecimal-String Converter
// TODO: use RateFactory and RateRepository
// TODO: throw MoneyLaundryException on attemp to convert between the same currencies
// TODO: create CompositeRates 

public class CurrencyConverterExample {

	public static void main(String[] args) throws Exception {
		
		// reference currency is CHF
		final Currency chf = new Currency("CHF");
		
		// targetCurrencies ;
		final Currency eur = new Currency("EUR");
		final Currency usd = new Currency("USD");
		final Currency mex = new Currency("MEX");
		
		// create simple conversion rates, based on the reference currency
		final Rate eur2chf = new SimpleRate(eur, chf, new BigDecimal("1.7"));
		final Rate chf2eur = eur2chf.createInversion();

		final Rate usd2chf = new SimpleRate(usd, chf, new BigDecimal("1.2"));
		final Rate chf2usd = usd2chf.createInversion();
		
		final Rate mex2chf = new SimpleRate(mex, chf, new BigDecimal("0.1"));
		final Rate chf2mex = mex2chf.createInversion();
		
		final Map<RateKey, Rate> rateMap = new HashMap<RateKey, Rate>();
		rateMap.put(eur2chf.key, eur2chf);
		rateMap.put(chf2eur.key, chf2eur);
		rateMap.put(usd2chf.key, usd2chf);
		rateMap.put(chf2usd.key, chf2usd);
		rateMap.put(mex2chf.key, mex2chf);
		rateMap.put(chf2mex.key, chf2mex);
		
		// TODO inject rateRepository
		final Converter converter = new Converter();
		Workbench.start(converter, Converter.class);
	}
	
	// --------------------------------------

	public static class Money {
		public final BigDecimal amount;
		public final Currency currency;
		
		public Money(BigDecimal amount, Currency currency) {
			this.amount = TK.Objects.assertNotNull(amount, "amount");
			this.currency = TK.Objects.assertNotNull(currency, "currency");
		}
		
		@Override
		public String toString() {
			return "" + currency + " " + amount;
		}
	}
	
	
	public static class Currency {
		public final String code; // 3 chars
		
		public Currency(String code) {
			if (code.trim().length() != 3) throw new IllegalArgumentException("currency code has to be exactly three characters long");
			this.code = code;
		}
		
		@Override
		public String toString() {
			return code;
		}
		
		@Override
		public boolean equals(Object obj) {
			if ( ! (obj instanceof Currency)) return false;
			
			final Currency that = (Currency) obj;
			return this.code.equals(that.code);
		}
		
		@Override
		public int hashCode() {
			return code.hashCode();
		}
	}
	
	
	// --------------------------------------
	
	
	public static class RateKey {
		public final Currency fromCurrency;
		public final Currency toCurrency;
	
		public RateKey(Currency fromCurrency, Currency toCurrency) {
			this.fromCurrency = TK.Objects.assertNotNull(fromCurrency, "fromCurrency");
			this.toCurrency = TK.Objects.assertNotNull(toCurrency, "toCurrency");
		}
		
		@Override
		public String toString() {
			return "" + toCurrency + " / " + fromCurrency;
		}
		
		@Override
		public boolean equals(Object obj) {
			if ( ! (obj instanceof RateKey)) return false;
			
			final RateKey that = (RateKey) obj;
			return this.fromCurrency.equals(that.fromCurrency)
					&& this.toCurrency.equals(that.toCurrency);
		}
		
		@Override
		public int hashCode() {
			return 31 * fromCurrency.hashCode() + toCurrency.hashCode();
		}		
	}
	
		
	public static abstract class Rate {
		public final RateKey key;
		
		protected Rate(Currency fromCurrency, Currency toCurrency) {
			this(new RateKey(fromCurrency, toCurrency));
		}
		
		private Rate(RateKey key) {
			this.key = TK.Objects.assertNotNull(key, "key");
		}
		
		public abstract BigDecimal exchange(BigDecimal amount);
		
		abstract Rate createInversion();
	}
	
	
	public static class SimpleRate extends Rate {
		public final BigDecimal conversionRate;
		
		public SimpleRate(Currency fromCurrency, Currency toCurrency, BigDecimal conversionRate) {
			super(fromCurrency, toCurrency);
			this.conversionRate = TK.Objects.assertNotNull(conversionRate, "conversionRate");
		}

		public BigDecimal exchange(BigDecimal amount) {
			return amount.multiply(conversionRate);
		}
		
		@Override
		Rate createInversion() {
			return new SimpleRate(key.toCurrency, key.fromCurrency, BigDecimal.ONE.divide(conversionRate, 2, RoundingMode.HALF_UP));
		}
		
		@Override
		public String toString() {
			return "" + key.toCurrency + " / " + key.fromCurrency + " = " + conversionRate;
		}
	}
	
	
	public static class CompositeRate extends Rate {
		public final Rate firstRate;
		public final Rate secondRate;
		
		public CompositeRate(Rate firstRate, Rate secondRate) {
			super(firstRate.key.fromCurrency, secondRate.key.toCurrency);
			this.firstRate = firstRate;
			this.secondRate = secondRate;
		}

		public BigDecimal exchange(BigDecimal amount) {
			return secondRate.exchange(firstRate.exchange(amount));
		}
		
		@Override
		Rate createInversion() {
			final Rate firstRateInverted = firstRate.createInversion();
			final Rate secondRateInverted = secondRate.createInversion();
			return new CompositeRate(secondRateInverted, firstRateInverted);
		}
		
		@Override
		public String toString() {
			return "" + firstRate + " -> " + secondRate;
		}
	}
	
		
	// --------------------------------------
	
	public static class Converter {
		
		public Currency from;
		public Currency to;
		public BigDecimal amountFrom;		
		
		public List<Currency> getSelectableFrom() {
			return Collections.emptyList();
		}
		
		public List<Currency> getSelectableTo() {
			return Collections.emptyList();
		}
		
		public BigDecimal getAmountTo() {
			// TODO
			return amountFrom != null ? BigDecimal.TEN.multiply(amountFrom) : null;
		}		
	}
	
//	public static class Project {
//
//		public String name;
//		public File licenseFile;
//		public URL website;
//		
//		@Executable(paramNames = { "projectName", "licenseFilePath", "websiteURL" })
//		public Project(String projectName, String licenseFilePath, String websiteURL) throws MalformedURLException, FileNotFoundException {
//			this.name = projectName;
//			editDetails(licenseFilePath, websiteURL);
//		}
//		
//		@Executable(paramNames = {"licenseFilePath", "websiteURL" })
//		public void editDetails(String licenseFilePath, String websiteURL) throws MalformedURLException, FileNotFoundException {
//			this.licenseFile = new File(licenseFilePath);
//			if ( ! licenseFile.exists()) throw new FileNotFoundException(licenseFile.getAbsolutePath());
//			
//			this.website = new URL(websiteURL);			
//		}
//
//		@Override
//		public String toString() {
//			return "" + name;
//		}
//	}

}
