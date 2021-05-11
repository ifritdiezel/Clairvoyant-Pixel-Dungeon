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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StableElementalSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class StableElemental extends Mob {

	{
		HP = HT = 60;
		defenseSkill = 20;
		spriteClass = StableElementalSprite.class;
		properties.add(Property.ELECTRIC);
		alignment = Alignment.ALLY;
		intelligentAlly = true;
		actPriority = MOB_PRIO + 1;
		state = HUNTING;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20, 35);
	}

	@Override
	public int attackSkill(Char target) {
		return 25;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}

	private int rangedCooldown = Random.NormalIntRange(3, 5);

	@Override
	protected boolean act() {
		if (state == HUNTING) {
			rangedCooldown--;
		}

		return super.act();
	}


	protected boolean canAttack( Char enemy ) {
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;}



	protected boolean doAttack(Char enemy) {

		if (Dungeon.level.adjacent(pos, enemy.pos) || rangedCooldown > 0) {

			return super.doAttack(enemy);

		} else {

			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap(enemy.pos);
				return false;
			} else {
				zap();
				return true;
			}
		}
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);
		meleeProc(enemy, damage);

		return damage;
	}

	private void zap() {
		spend(1f);

		if (hit(this, enemy, true)) {

			rangedProc(enemy);

		} else {
			enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
		}

		rangedCooldown = Random.NormalIntRange(3, 5);
	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public void add(Buff buff) {
			damage(Random.NormalIntRange(HT / 2, HT * 3 / 5), buff);
	}

	protected ArrayList<Class<? extends Buff>> harmfulBuffs = new ArrayList<>();

	private static final String COOLDOWN = "cooldown";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COOLDOWN, rangedCooldown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(COOLDOWN)) {
			rangedCooldown = bundle.getInt(COOLDOWN);
		}
	}

	protected void meleeProc(Char enemy, int damage) {
		ArrayList<Char> affected = new ArrayList<>();
		ArrayList<Lightning.Arc> arcs = new ArrayList<>();
		Shocking.arc(this, enemy, 2, affected, arcs);
		affected.remove(Dungeon.hero);

		if (!Dungeon.level.water[enemy.pos]) {
			affected.remove(enemy);
		}

		for (Char ch : affected) {
			ch.damage(Math.round(damage * 0.4f), this);
		}

		boolean visible = sprite.visible || enemy.sprite.visible;
		for (Char ch : affected) {
			if (ch.sprite.visible) visible = true;
		}

		if (visible) {
			sprite.parent.addToFront(new Lightning(arcs, null));
			Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
		}
	}

	protected void rangedProc(Char enemy) {
		if (enemy == Dungeon.hero) {
			GameScene.flash(0x80FFFFFF);
		}
	}


	public static void spawnAround(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			spawnAt(pos + n);
		}
	}

	public static StableElemental spawnAt(int pos) {
		if (!Dungeon.level.solid[pos] && Actor.findChar(pos) == null) {

			StableElemental w = new StableElemental();
			w.pos = pos;
			w.state = w.HUNTING;
			GameScene.add(w, 2);
			w.sprite.alpha(0);
			w.sprite.parent.add(new AlphaTweener(w.sprite, 1, 1.5f));

			w.sprite.emitter().burst(EnergyParticle.FACTORY, 5);

			return w;
		} else {
			return null;
		}
	}
}
