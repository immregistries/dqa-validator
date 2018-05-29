package org.immregistries.dqa.validator.report.codes;

import java.util.Objects;
import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.immregistries.dqa.vxu.VxuField;

public class CollectionBucket {

  private String type;
  private String source;
  private String attribute;
  private String value;
  private int count;
  private String status;
  private String label;

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public CollectionBucket(VxuField field, String attribute, String value) {
    this(field.toString(), attribute, value);
  }

  public CollectionBucket(String type, String attribute, String value) {
    this.type = type;
    this.attribute = attribute;
    this.value = value;
  }

  public CollectionBucket(String type, String attribute, String value, int count) {
    this.type = type;
    this.attribute = attribute;
    this.value = value;
    this.count = count;
  }

  @Override
  public String toString() {
    return "CollectionBucket{type:" + type + " attribute:" + getAttribute() + " /value: "
        + getValue() + " /count: " + getCount() + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectionBucket that = (CollectionBucket) o;

    return Objects.equals(this.type, that.type)
        && Objects.equals(this.attribute, that.attribute)
        && Objects.equals(this.source, that.source)
        && Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, source, attribute, value);
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAttribute() {
    return attribute;
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }
}
