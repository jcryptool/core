package org.jcryptool.games.numbershark.optStrat;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * Starts the calculation Thread
 * @author Johannes Spaeth
 * @version 0.9.5
 */
public class CalculationThread implements IRunnableWithProgress {
	private int min = 1;
	private int max = 100;
	private int stoppedAt;
	private String[][] outputTable;
	
	public CalculationThread(int min, int max){
		this.min = min;
		this.max = max;
	}	


public void run(IProgressMonitor monitor) throws InvocationTargetException,
      InterruptedException {
    monitor.beginTask(Messages.ProgressDialog_0, IProgressMonitor.UNKNOWN);
    try {
		ZahlenhaiBestwerte.main(min, max, monitor);
		outputTable = ZahlenhaiBestwerte.getOutput();
		stoppedAt = ZahlenhaiBestwerte.getStoppedAt();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    monitor.done();

    if (monitor.isCanceled()){
        throw new InterruptedException("The long running operation was cancelled");
    }
  }

	public String[][] getSharkOutput(){
		return outputTable;
	}
	
	public int getStoppedAt(){
		return stoppedAt;
	}
}