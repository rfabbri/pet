package .pulapirata.core.utils;

// A simple class hierarchy for character attributes
//
// This is a simple class holding and managing a list of attributes, which is
// basically the game state.
//
// - this takes attribute listing and handling off of the main update() loop
//
// - this also validates sets
//
// - after each "set" in the attribute, the attribute updates
// its state to a qualitative one based on set boundaries,
// eg, for alcool the qualitative state can be "coma", "vomiting",
// "dizzy" or "normal".
//
// - when updating the game sprite, we follow sprite update logic in
// update(), e.g., we check the status of all attributes and follow a table of
// priority to set the current sprite based on that.
//   - based on such priority we also determine which attributes should be
//   listed on the statusbar.
//   - determine also if an alert should be placed.
//   - each attribute can claim a certain priority level, and the policy to
//   decide between more than one w same priority can be handled externally,
//   eg, show alternating messages. But a final external table should order up each
//   attribute in case of conflicts.
//   - the state of the game is determined by the collection of attributes
//   - based on the attributes we determine which actions are allowed or not
//
public class PetAttributes {

  public PetAttribute<int> fome() { return fome_; }

  private PetAttribute<int> fome_ = new PetAttribute("Fome", 13, -20, 120, 10, 60*60);

  public PetAttributes(beatsCoelhoSegundo) {

    // TODO populateFromJson();
  }

  public void populateFromJson();



//  private alcool_ = new Attribute("Alcool", 3, 0, 10, -1,  (int) Math.max(beats_coelhosegundo*60.*60.,1));
// public Attribute alcool() { return alcool_; }

//  private ArrayList<Attribute> attribute =
//        new ArrayList<Attribute>(Arrays.asList(
//        alcool_
//        ));
}

public abstract class PetAttribute<T> {
  public PetAttribute(String name, T startVal, T min, T max, T passive, T passiveBeats) {
    name_ = name;
    val_ = startVal;
    min_ = min;
    max_ = max;
    passive_ = passive;
    passiveBeats_ = passiveBeats;
  }

  public T val() { return val_; }
  public T min() { return min_; }
  public T max() { return max_; }
  public T passive() { return passive_; }
  // the speed in 'beats'
  public T passiveBeats() { return passiveBeats_; }

  public void set(T v) { val_ = v; }
  public void setMin(T v) { min_ = v; // TODO do some checking }
  public void setMax(T v) { max_ = v; }
  public void setPassive(T p) { passive_ = p; }
  public void setPassiveBeats(T b) { passiveBeats_ = b; }
  // TODO updatePassivo();

  protected String name_;
  protected T val_;
  protected T min_;
  protected T max_;
  protected T passive_;
  protected T passiveBeats_;
}
