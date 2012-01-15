package com.jirge.server;

import javax.jdo.annotations.*;

import com.jirge.shared.PlayerValue;

import java.io.Serializable;

@SuppressWarnings("serial")
@PersistenceCapable(identityType= IdentityType.APPLICATION)
public class Player implements Serializable {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
  private String key;

  @Persistent
  private String name;

  public String getKey() {
    return key;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PlayerValue toValue() {
      return new PlayerValue(key, name);   
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Player player = (Player) o;

    if (key != null ? !key.equals(player.key) : player.key != null) return false;
    if (name != null ? !name.equals(player.name) : player.name != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }
}
