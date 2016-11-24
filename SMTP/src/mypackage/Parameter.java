package mypackage;

public class Parameter implements sut.interfaces.Parameter {
private int value;
private int parameterIndex;
private Action action;

public int getValue(){
	return this.value;
}
public int getParameterIndex(){
	return this.parameterIndex;
}
public Action getAction()
{
	return this.action;
}
public void setAction(Action action){
	this.action = action;
}
	
public Parameter(int value, int parameterIndex)
{
  this.value = value;
  this.parameterIndex = parameterIndex;
}

}
