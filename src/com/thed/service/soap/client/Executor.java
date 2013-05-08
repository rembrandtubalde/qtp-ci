package com.thed.service.soap.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Executor {
	private static final Logger logger = Logger.getLogger(Executor.class.getName());
	public static int executeSequentially(String executable, String path) throws InterruptedException, IOException{
		Process process = Runtime.getRuntime().exec(executable + " -source " + path);
		new ProcessStreamReader(process.getErrorStream()).start();
		new ProcessStreamReader(process.getInputStream()).start();
		int status = process.waitFor();
	    
	    /* real value: by convention 0 indicates successful completion of script */
	    if (status == 0) {
	    	logger.log(Level.INFO, "Test exited normally");
	    } else {
	    	logger.log(Level.SEVERE, "Test exited NOT normally");
	    }
	    return status;
	}
	
	public static void main(String args[]) throws InterruptedException, IOException{
		Executor.executeSequentially("c:\\Program Files (x86)\\HP\\Unified Functional Testing\\bin\\UFTBatchRunnerCMD.exe", 
				"c:\\qtp\\ZephyrSearchPass\\ZephyrSearchPass.mtb");
	}
	
	static class ProcessStreamReader extends Thread {
		BufferedReader br;
		private final Logger logger = Logger.getLogger(this.getClass().getName());
		public ProcessStreamReader(InputStream in){
			super();
			br = new BufferedReader(new InputStreamReader(in));
		}

		@Override
		public void run() {
			String line;
			try {
				while ((line = br.readLine()) != null) {
					logger.info("\t" + Thread.currentThread().getName() + " | Read output..." + line);
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error in reading process steams \n", e);
			}finally{
				if(br != null){
					try { br.close(); } catch (IOException e) {}
				}
			}
			logger.info(Thread.currentThread().getName() + " | Done reading process log input stream");
		}
	}
}
