package org.survival.glorpus.craftingChaos;

import java.util.Vector;

public class AttackPattern {

    // the higher the rarity, the more unlikely to do something
    int rarity = 0;
    Vector<MoveType> moves = new Vector<>();

    // chance for it to happen every second
    public AttackPattern(double rarity) {
        this.rarity = (int) (20.0 / rarity);
    }

    public void add_move(MoveType move) {
        this.moves.add(move);
    }

}
