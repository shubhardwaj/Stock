package smtplearning;

public class Parameter
  implements sut.interfaces.Parameter
{
  private int value;
  private int parameterIndex;
  private Action action;

  public Parameter(int paramInt1, int paramInt2)
  {
    this.value = paramInt1;
    this.parameterIndex = paramInt2;
  }

  public Parameter(int paramInt1, int paramInt2, Action paramAction)
  {
    this.value = paramInt1;
    this.parameterIndex = paramInt2;
    this.action = paramAction;
  }

  public int getValue() {
    return this.value;
  }

  public int getParameterIndex() {
    return this.parameterIndex;
  }

  public Action getAction() {
    return this.action;
  }

  public void setAction(Action paramAction) {
    this.action = paramAction;
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }

    if (!(paramObject instanceof Parameter)) {
      return false;
    }

    Parameter localParameter = (Parameter)paramObject;

    if (this.value != localParameter.value) {
      return false;
    }

    if (this.parameterIndex != localParameter.parameterIndex) {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return this.parameterIndex;
  }

  public String toString()
  {
    return Integer.toString(this.value);
  }
}