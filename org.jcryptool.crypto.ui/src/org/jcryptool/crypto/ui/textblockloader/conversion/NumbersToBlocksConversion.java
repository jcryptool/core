//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.textblockloader.conversion;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.crypto.ui.textblockloader.ConversionCharsToNumbers;
import org.jcryptool.crypto.ui.textblockloader.ConversionNumbersToBlocks;

public class NumbersToBlocksConversion extends ConversionNumbersToBlocks{
	private int defaultNumberForBlockTail = 0;
	private int numbersPerBlock;
	private int base;
	private List<Integer> posWeights;
	
	public NumbersToBlocksConversion(int numbersPerBlock, int base) {
		super();
		this.numbersPerBlock = numbersPerBlock;
		this.base = base;
		this.posWeights = generatePosWeights();
	}

	@Override
	public List<Integer> convert(List<Integer> input) {
		if(numbersPerBlock == 1) return input;
		
		int nrBlocks = (int) Math.ceil((double) (input.size()) / (double) numbersPerBlock);
		List<Integer> blocks = new LinkedList<Integer>();
		
		for(int i=0; i<nrBlocks; i++) {
			LinkedList<Integer> nrsForBlock = new LinkedList<Integer>();
			int startingIndex = i*numbersPerBlock;
			for(int k=0; k<numbersPerBlock; k++) {
				int idx = startingIndex+k;
				if(idx < input.size()) {
					nrsForBlock.add(input.get(idx));
				} else {
					nrsForBlock.add(getDefaultNumberForBlockTail());
				}
			}
			Integer block = calcNumbersToBlock(nrsForBlock, posWeights);
			blocks.add(block);
		}
		
		return blocks;
	}

	private List<Integer> generatePosWeights() {
		List<Integer> posWeights = new LinkedList<Integer>();
		for (int i = numbersPerBlock-1; i >= 0; i--) {
			Integer posWeight = (int) Math.round(Math.pow(base, i));
			posWeights.add(posWeight);
		}
		return posWeights;
	}
	
	private Integer calcNumbersToBlock(List<Integer> nrs, List<Integer> posWeights) {
		Integer result = 0;
		for (int i = 0; i < nrs.size(); i++) {
			Integer nr = nrs.get(i);
			Integer posWeight = posWeights.get(i);
			
			result += nr*posWeight;
		}
		return result;
	}

	@Override
	public List<Integer> revert(List<Integer> blocks) {
		if(numbersPerBlock == 1) return blocks;
		
		List<Integer> numbers = new LinkedList<Integer>();
		for(Integer block: blocks) {
			List<Integer> numbersForBlock = calcNumbersFromBlock(block, posWeights);
			
			numbers.addAll(numbersForBlock);
		}
		
		return numbers;
	}

	private List<Integer> calcNumbersFromBlock(Integer block,
			List<Integer> posWeights) {
		List<Integer> result = new LinkedList<Integer>();
		Integer currentBlock = block;
		for(Integer weight: posWeights) {
			Integer rest = currentBlock % weight;
			Integer pos = (currentBlock-rest) / weight;
			currentBlock = rest;
			
			result.add(pos);
		}
		
		return result;
	}
	
	public int getDefaultNumberForBlockTail() {
		return defaultNumberForBlockTail;
	}
	
	public static Integer getDefaultBaseForNumberToBlockConversion(ConversionCharsToNumbers ctn) {
		return ctn.getMaxNumberValue()+1;
	}
	
	private static double logb(double a, double b) {
		return Math.log(a) / Math.log(b);
	}
	
	public static Integer getMaxBlockCount(Integer base) {
		return (int) Math.floor(logb(1 << 30, base));
		// base ^ count < MAX => count = floor(log_base(MAX))
	}

	@Override
	public Integer getMaxBlockValue() {
		return (int) (Math.round(Math.pow(this.base, this.numbersPerBlock))-1);
	}
	
	public int getBase() {
		return base;
	}
	
	public int getNumbersPerBlock() {
		return numbersPerBlock;
	
	}
}
