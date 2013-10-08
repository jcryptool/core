package org.jcryptool.crypto.ui.textblockloader;

public enum Repr {
	DECIMAL(true), BINARY(true), HEX(true), STRING(false);
	
	private boolean numeric;

	private Repr(boolean numeric) {
		this.numeric = numeric;
	}
	
	public boolean isNumeric() {
		return numeric;
	}
	
	public String numberToString(Integer number) {
		if(this.isNumeric()) {
			if(this == DECIMAL) {
				return number.toString(); 
			} else if(this == BINARY) {
				return Integer.toBinaryString(number);
			} else if(this == HEX) {
				return Integer.toHexString(number);
			} else {
				throw new RuntimeException("unknown numeric representation");
			}
		} else {
			throw new RuntimeException("String conversion not supported here");
		}
	}
	
	public Integer getRadix() {
		if(this.isNumeric()) {
			if(this == DECIMAL) {
				return 10; 
			} else if(this == BINARY) {
				return 2;
			} else if(this == HEX) {
				return 16;
			} else {
				throw new RuntimeException("unknown numeric representation");
			}
		} else {
			throw new RuntimeException("radix not supported here");
		}
	}
}