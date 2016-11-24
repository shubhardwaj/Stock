package sut.interfaces;

import java.util.List;

public abstract interface Action
{
  public abstract String getMethodName();

  public abstract List<Parameter> getParams();
}
