/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class SpellHand extends TargetedSpell {
	private static final String AC_CHANNELLER_CHARGE = "CHANNELLER_CHARGE";
	private static final String AC_EDIT_SPELLS = "EDIT_SPELLS";

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.subClass == HeroSubClass.CHANNELLER && false) {
			actions.add(AC_CHANNELLER_CHARGE);
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_CHANNELLER_CHARGE)) {
			ArcaneCatalyst catalyst = hero.belongings.getItem( ArcaneCatalyst.class );
			if (hero.belongings.getItem( ArcaneCatalyst.class )!=null) {
				//TODO special attack
				execute(hero);
				catalyst.detach( hero.belongings.backpack );
			} else  {
				GLog.w( Messages.get(this, "no_catalyst") );
			}
		}
		if (action.equals(AC_EDIT_SPELLS)){
			GameScene.show(new WndOptions(Messages.titleCase("edit spells"),
					"Edit spells",
					"button",
					"another",
					"yes",
					"cancel"){

				@Override
				protected void onSelect(int index) {
					if (index < 3) {
						GLog.i("you chose" + index);
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing, reader has to cancel
				}
			});
		}
	}

		{
			image = ItemSpriteSheet.SPELL_HAND;
			usesTargeting = true;
			unique = true;
			bones = false;
		}




	@Override
		protected void affectTarget(Ballistica bolt, Hero hero) {
			int cell = bolt.collisionPos;
			quantity++;

			Char target = Actor.findChar(cell);
			if (target instanceof Mob) {
				if (target.alignment == Char.Alignment.ALLY) {
					Emitter e = target.sprite.emitter();
					e.burst(Speck.factory(Speck.HEALING), 3);
					target.HP += Random.Int(5)+1;
				} else {
					switch (Random.Int(4-hero.pointsInTalent(Talent.ARCANE_STABILIZATION))) {
						case 0:
							target.damage(Random.NormalIntRange(0, 2+hero.lvl/2), this);
							break;
						case 1:
							Buff.affect(target, Vertigo.class, 1);
							break;
						case 2:

							break;
					}
				}

			}
			else if (target instanceof Hero){
				Buff.affect(hero, Light.class, 8);
				Emitter e = hero.sprite.emitter();
				e.burst(Speck.factory(Speck.EVOKE), 1);
				if (hero.hasTalent(Talent.MAGIC_SHIELD))
				{
					Barrier barrier = Buff.affect(target, Barrier.class);
					barrier.setShield((int) (hero.pointsInTalent(Talent.MAGIC_SHIELD)*1.5f));
				}
			}
			else {
				int terr = Dungeon.level.map[cell];
				switch (Random.Int(9)-hero.pointsInTalent(Talent.ARCANE_STABILIZATION)) {
					case 0: default:
						CellEmitter.get( cell ).burst(ShadowParticle.MISSILE, 6 );
						break;
					case 1:
						if (terr == Terrain.HIGH_GRASS) {
							Dungeon.level.plant((Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED), cell);
							CellEmitter.get( cell ).burst( LeafParticle.GENERAL, 6 );
						}
						break;
					case 2:
						Splash.at(cell, 0x9999FF, 5);
						GameScene.add(Blob.seed(cell, 3, Freezing.class));
						break;
					case 3:
						Dungeon.level.pressCell(cell);
						break;
					case 4:
						if (terr == Terrain.EMPTY_DECO|| hero.hasTalent(Talent.RUNE_IMBUEMENT)) {
							if (Random.Int(4-hero.pointsInTalent(Talent.RUNE_IMBUEMENT)) ==0 )
							Dungeon.level.drop(Generator.random(Generator.Category.STONE), cell).sprite.drop();
							break;
						}
					case 5:
						if (terr == Terrain.WATER) {
						GameScene.add(Blob.seed(cell, 10, StormCloud.class));
						}
				}
			}


	}

	public ArrayList<SpellHandSpell> spells = new ArrayList<>();

}


