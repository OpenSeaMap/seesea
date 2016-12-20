package net.sf.seesea.track.api.exception;

public class RawDataEventException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6175561607705680215L;

	/**
	 * 
	 */
	public RawDataEventException() {
		// 
	}

	/**
	 * @param arg0
	 */
	public RawDataEventException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public RawDataEventException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public RawDataEventException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
