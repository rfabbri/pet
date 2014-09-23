package .pulapirata.core.utils;

// A simple class hierarchy for character attributes,
public class PetAttributes {

  public void populateFromJson();

  public PetAttribute<int> fome() { return fome_; }
  private PetAttribute<int> fome_;
}

public abstract class PetAttribute<T> {
  public T get() { return val_; }
  public T getPassive() { return passive_; }
  public T getPassiveBeats() { return passiveBeats_; }

  public void set(T v) { val_ = v; }
  public void setPassive(T p) { passive_ = p; }
  public void setPassiveBeats(T b) { passiveBeats_ = b; }

  protected T value_;
  protected T passive_;
  protected T passiveBeats_;
}

fome().get()
