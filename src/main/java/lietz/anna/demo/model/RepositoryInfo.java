package lietz.anna.demo.model;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RepositoryInfo {
  private final String name;
  private final String ownerName;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RepositoryInfo that = (RepositoryInfo) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(ownerName, that.ownerName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, ownerName);
  }
}