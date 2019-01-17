package ch.cern.eam.wshub.core.tools;

import javax.xml.ws.WebFault;

@WebFault(name="ExceptionInfoList")
public class InforException extends Exception {

	private static final long serialVersionUID = 2888535819761291339L;
	private ExceptionInfo[] ExceptionInfoList;

	public InforException(String msg, Throwable cause, ExceptionInfo[] details) {
		super(msg, cause);
		ExceptionInfoList = details;
	}

	public ExceptionInfo[] getExceptionInfoList() {
		return ExceptionInfoList;
	}

	public void setExceptionInfoList(ExceptionInfo[] exceptionInfoList) {
		ExceptionInfoList = exceptionInfoList;
	}
		
}
