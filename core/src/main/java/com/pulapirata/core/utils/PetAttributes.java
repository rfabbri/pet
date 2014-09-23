package com.pulapirata.core.utils;

import com.pulapirata.core.utils.PetAttribute;

// A simple set of classes for character attributes
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
  private PetAttribute alcool_;
  public PetAttribute alcool() { return alcool_; }
  private PetAttribute fome_;
  public PetAttribute fome() { return fome_; }

  public PetAttributes(float beatsCoelhoHora) {
    alcool_ = new PetAttribute("Alcool", 3, 0, 10, -1, (int)beatsCoelhoHora);
    fome_ = new PetAttribute("Fome", 13, -20, 120, 10, (int)beatsCoelhoHora);
    // for i in attribute_list, set

    // TODO populateFromJson();
  }

// TODO  public void populateFromJson();


//  TODO needed? private ArrayList<Attribute> attribute =
//        new ArrayList<Attribute>(Arrays.asList(
//        alcool_
//        ));
}
