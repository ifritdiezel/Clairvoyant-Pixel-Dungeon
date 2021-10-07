package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class SpellCircleBuff extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    private int circleTier;
    public int circleTier() { return circleTier; }



    public void incCircleTier(int count){
        circleTier += count;
    }

    public void incCircleTier(){
        circleTier++;
    }

    public void setCircleTier(int count){
        circleTier = count;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.MAGICCIRCLE);
        else target.sprite.remove(CharSprite.State.MAGICCIRCLE);
    }
    @Override
    public int icon() {
        return BuffIndicator.BLESS;
    }
}
