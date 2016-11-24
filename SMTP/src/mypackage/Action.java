package mypackage;

import java.util.ArrayList;
import java.util.List;

public class Action implements sut.interfaces.Action {

	private String methodName;
	private ArrayList<Parameter> parameter;

	@Override
	public String getMethodName() {
		return this.methodName;
	}

	public List<Parameter> getParameter() {
		// TODO Auto-generated method stub
		return new ArrayList<>(this.parameter);
	}

	public List<sut.interfaces.Parameter> getParams() {
		ArrayList localArrayList = new ArrayList();
		for (Parameter localParameter : this.parameter) {
			localArrayList.add(localParameter);
		}
		return localArrayList;
	}

	public Parameter getParams(int param) {
		return this.parameter.get(param);

	}

	public Action(Action paramAction) {
		this(paramAction.getMethodName(), paramAction.getParameter());
	}

	public Action(String paramAction, List<Parameter> list) {
		this.methodName = paramAction;
		this.parameter = new ArrayList();
		for (Parameter localParameter : list) {
			Parameter localParameter1 = new Parameter(localParameter.getValue(), localParameter.getParameterIndex());
			localParameter1.setAction(this);
			this.parameter.add(localParameter1);
		}
	}

	public Action(String paramAction) {
		String[] arrayOfStr = paramAction.split(".");

		if (arrayOfStr.length < 1) {
			System.out.println("Error");
		}
		this.methodName = arrayOfStr[0];
		this.parameter = new ArrayList();

		if (arrayOfStr.length > 1) {
			for (int i = 1; i < arrayOfStr.length; i++) {
				String str = arrayOfStr[i];

			}
		}

	}


	public boolean equals(Object paramObject) {
		if (this == paramObject) {
			return true;
		}
		if (!(paramObject instanceof Action)) {
			return false;
		}
		Action localAction = (Action) paramObject;

		if (!this.methodName.equals(localAction.methodName)) {
			return false;
		}
		if (!this.parameter.equals(localAction.parameter)) {
			return false;
		}

		return true;
	}

}
