package ch.cern.eam.wshub.core.tools;


import jakarta.xml.ws.WebFault;

import java.util.Arrays;

@WebFault(name="ExceptionInfoList")
public class EAMException extends Exception {

	private static final long serialVersionUID = 2888535819761291339L;
	private ExceptionInfo[] ExceptionInfoList;

	public EAMException(String msg, Throwable cause, ExceptionInfo[] details) {
		super(msg);
		ExceptionInfoList = details;
	}

	public ExceptionInfo[] getExceptionInfoList() {
		return ExceptionInfoList;
	}

	public void setExceptionInfoList(ExceptionInfo[] exceptionInfoList) {
		ExceptionInfoList = exceptionInfoList;
	}

	@Override
	public String toString() {
		return "EAMException{" +
				"Message: " + getMessage() + ", " +
				"ExceptionInfoList=" + Arrays.toString(ExceptionInfoList) +
				'}';
	}
}
