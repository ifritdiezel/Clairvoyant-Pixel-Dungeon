package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.apprentice;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;
import com.watabou.utils.Random;

public class WildMagic extends ArmorAbility {
    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        switch (Random.Int(4)){


            case 0:
                //Affect self
                switch (Random.Int(4)){
                    case 0:
                        Buff.affect(hero, Healing.class).setHeal(10, 0.25f,0);
                        break;
                    case 1:
                        Potion potion = (Potion) Generator.randomUsingDefaults(Generator.Category.POTION);
                        potion.apply(hero);
                        break;
                    case 2:
                        int teleportTo = Random.Int(Statistics.deepestFloor);
                        InterlevelScene.mode = (teleportTo > Dungeon.depth) ? InterlevelScene.Mode.ASCEND : InterlevelScene.Mode.DESCEND;
                        Dungeon.depth = teleportTo;
                        Game.switchScene(InterlevelScene.class);
                        break;
                    case 3:
                        if (hero.belongings.weapon != null){
                            ((Weapon)hero.belongings.weapon).enchant();
                        }
                        break;

                }
                break;


            case 1:
                //Affect nearest enemy
                switch (Random.Int(4)){
                    case 0:
                        break;
                }
                break;


            case 2:
                //Affect all visible enemies
                switch (Random.Int(4)){
                    case 0:
                        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                            if (Dungeon.level.heroFOV[mob.pos] && Random.Boolean()) {
                            Buff.affect(mob, Burning.class).reignite(mob);
                            }
                        }
                        Buff.affect(hero, Burning.class).reignite(hero);
                        break;
                }
                break;


            case 3:
                //Affect terrain
                switch (Random.Int(4)) {
                    case 0:
                        Item seed = hero.belongings.getItem(Generator.random(Generator.Category.SEED).getClass());
                        break;
                }
                break;
        }
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.BLAST_RADIUS, Talent.ELEMENTAL_POWER, Talent.REACTIVE_BARRIER, Talent.HEROIC_ENERGY};
    }
}
