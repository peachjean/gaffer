package com.example;

/**
 * TODO: Document this class
 */
public class Two {
  private String name;
  private int size;
  private One oneRef;
  private One oneNested;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getSize() {
    return size;
  }

  public void setSize(final int size) {
    this.size = size;
  }

  public One getOneRef() {
    return oneRef;
  }

  public void setOneRef(final One oneRef) {
    this.oneRef = oneRef;
  }

  public One getOneNested() {
    return oneNested;
  }

  public void setOneNested(final One oneNested) {
    this.oneNested = oneNested;
  }
}
