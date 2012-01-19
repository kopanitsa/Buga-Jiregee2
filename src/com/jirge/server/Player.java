package com.jirge.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.jirge.shared.PlayerValue;


@SuppressWarnings("serial")
@PersistenceCapable(identityType= IdentityType.APPLICATION)
public class Player implements Serializable {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
  private String key;

  @Persistent
  private String name;

  @Persistent
  private List<BugaJiregeePiece> pieces;

  public Player(int type) {
      switch (type) {
      case BugaJiregeePiece.TYPE_DEER:
          this.pieces = new ArrayList<BugaJiregeePiece>(2);
          break;
      case BugaJiregeePiece.TYPE_DOG:
          this.pieces = new ArrayList<BugaJiregeePiece>(24);
          break;
      }
  }

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
 
  public List<BugaJiregeePiece> getPieces() {
	  return this.pieces;
  }

}
