package smtplearning;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public abstract class Action
  implements sut.interfaces.Action
{
  private String methodName;
  private ArrayList<Parameter> parameters;

  public String getMethodName()
  {
    return this.methodName;
  }

  public List<Parameter> getParameters() {
    return new ArrayList(this.parameters);
  }

  public List<sut.interfaces.Parameter> getParams() {
    ArrayList localArrayList = new ArrayList();
    for (Parameter localParameter : this.parameters) {
      localArrayList.add(localParameter);
    }
    return localArrayList;
  }

  public Parameter getParam(int paramInt) {
    return (Parameter)this.parameters.get(paramInt);
  }

  public Parameter getParam(Integer paramInteger) {
    return (Parameter)this.parameters.get(paramInteger.intValue());
  }

  public Action(String paramString, List<Parameter> paramList) {
    this.methodName = paramString;
    this.parameters = new ArrayList();
    for (Parameter localParameter1 : paramList) {
      Parameter localParameter2 = new Parameter(localParameter1.getValue(), localParameter1.getParameterIndex());
      localParameter2.setAction(this);
      this.parameters.add(localParameter2);
    }
  }

  public Action(Action paramAction) {
    this(paramAction.getMethodName(), paramAction.getParameters());
  }

  public Action(sut.interfaces.Action paramAction) {
    this.methodName = paramAction.getMethodName();
    this.parameters = new ArrayList();
    for (sut.interfaces.Parameter localParameter : paramAction.getParams()) {
      Parameter localParameter1 = new Parameter(localParameter.getValue(), localParameter.getParameterIndex());
      localParameter1.setAction(this);
      this.parameters.add(localParameter1);
    }
  }

  public Action(String paramString) {
    String[] arrayOfString = paramString.split("_");

    if (arrayOfString.length < 1) {
      System.out.println(new StringBuilder().append("Error handling abstract input: ").append(paramString).toString());
      throw new RuntimeException(new StringBuilder().append("Error handling abstract input: ").append(paramString).toString());
    }

    this.methodName = arrayOfString[0];
    this.parameters = new ArrayList();

    if (arrayOfString.length > 1) {
      int i = 0;
      for (int j = 1; j < arrayOfString.length; j++) {
        String str = arrayOfString[j];
        Integer localInteger;
        try {
          localInteger = new Integer(str);
        } catch (NumberFormatException localNumberFormatException) {
          System.out.println(new StringBuilder().append("Error parsing abstract input value: ").append(str).append(" in action: ").append(paramString).toString());
          throw new RuntimeException(new StringBuilder().append("Error parsing abstract input value: ").append(str).append(" in action: ").append(paramString).toString());
        }

        this.parameters.add(new Parameter(localInteger.intValue(), i, this));

        i++;
      }
    }
  }

  public String toString() {
    StringBuilder localStringBuilder = new StringBuilder(this.methodName);

    if (this.parameters.size() != 0) {
      localStringBuilder.append(" ");
      for (Parameter localParameter : this.parameters) {
        localStringBuilder.append(localParameter.toString()).append(" ");
      }
    }

    return localStringBuilder.toString().trim();
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof Action)) {
      return false;
    }
    Action localAction = (Action)paramObject;

    if (!this.methodName.equals(localAction.methodName)) {
      return false;
    }

    if (!this.parameters.equals(localAction.parameters)) {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return this.methodName.hashCode() + this.parameters.hashCode();
  }

  public String getValuesAsString() {
    String str = getMethodName();

    if (getParameters().size() > 0) {
      for (Parameter localParameter : getParameters()) {
        str = new StringBuilder().append(str).append("_").append(localParameter.getValue()).toString();
      }
    }
    return str;
  }
}