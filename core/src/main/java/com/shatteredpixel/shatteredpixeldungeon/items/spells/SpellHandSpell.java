package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

/*
0 - damage projectiles
1 - modifiers
2 - tile
3 - passive
 */
public enum SpellHandSpell {
BOLT(0), DMGT1(1), GRASS(2), SPEEDBOOST(3);

int spellClass;
SpellHandSpell (int spellClass){
    this.spellClass = spellClass;
}
public int spellClass() {
    return spellClass();
}

public String title(){
        return Messages.get(this, name() + ".title");
    }
public String desc(){
        return Messages.get(this, name() + ".desc");
    }
}
