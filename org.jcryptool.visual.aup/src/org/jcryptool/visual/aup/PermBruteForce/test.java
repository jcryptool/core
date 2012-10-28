package org.jcryptool.visual.aup.PermBruteForce;

public class test {

	/**
	 * Simple program for brute forcing the number of permutations for the Android unlock pattern.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		UnlockPattern a = new UnlockPattern();
		try {
			int tmp=bruteForce(a);
			System.out.println(tmp+" possible combinations!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	static public int bruteForce(UnlockPattern a) throws Exception{
		int tmp=0;
		int[] tmpArray=new int[10];
		tmpArray[9]=0;
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				for(int k=0;k<10;k++){
					for(int l=0;l<10;l++){
						for(int m=0;m<10;m++){
							for(int n=0;n<10;n++){
								for(int o=0;o<10;o++){
									for(int p=0;p<10;p++){
										for(int q=0;q<10;q++){
											tmpArray[8]=i;
											tmpArray[7]=j;
											tmpArray[6]=k;
											tmpArray[5]=l;
											tmpArray[4]=m;
											tmpArray[3]=n;
											tmpArray[2]=o;
											tmpArray[1]=p;
											tmpArray[0]=q;
											a.setOrder(tmpArray);
											if(a.verify()){
												tmp++;
											}
										}	
									}	
								}	
							}
						}	
					}	
				}
				System.out.println("change"+i+j+"tmp= "+tmp);
			}
			
		}
		
		
		
		
		return tmp;
	}

}
