package org.survival.glorpus.craftingChaos;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import static org.bukkit.Bukkit.getServer;

public class AttackPatterns {

    Vector<AttackPattern> attack_patterns = new Vector<>();
    Vector<MoveType> moves = new Vector<>();
    Random random = new Random();

    Vector<Player> all_known_players = new Vector<>();
    Player focused_player;
    UUID boss_id;



    public AttackPatterns(UUID boss_id, Player player) {
        this.boss_id = boss_id;
        this.focused_player = player;
        this.all_known_players.add(player);
    }

    public void add_pattern(AttackPattern attack) {
        this.attack_patterns.add(attack);
    }

    public Entity get_boss() {
        return getServer().getEntity(this.boss_id);
    }

    public void processCurrentMove(double speed) {
        Entity boss_entity = get_boss();
        boolean finished = false;
        if (!this.moves.isEmpty()) {
            finished = switch (this.moves.firstElement()) {
                case MoveType.LEAP_BOMB -> move.leapBomb(boss_entity, focused_player, speed);

                case MoveType.LEAP -> move.leapRandom(boss_entity, focused_player, speed * 1.2);

                case MoveType.THROW_HEALING -> move.throwHeal(boss_entity);

                case MoveType.THROW_INSTANT_DAMAGE -> move.throwInstantDamage(boss_entity);

                case MoveType.THROW_SPEED -> move.throwSpeed(boss_entity);

                case MoveType.WAIT -> move.wait(boss_entity, random.nextInt((int)(100.0 / speed)));
            };
        }

        if (finished) {
            moves.removeFirst();
        }
    }

    BossMove move = new BossMove();

    // run every tick
    public void patternTick() {
        Entity boss_entity = get_boss();

        double boss_health = ((LivingEntity) boss_entity).getHealth();
        double speed = 1.0 / (boss_health / 10);

        // makes sure we don't que up a ton of nonsense
        if (this.moves.size() < 3) {
            // roll the dice to add a new attack pattern sequence
            AttackPattern attack_pattern = this.attack_patterns.get(random.nextInt(this.attack_patterns.size()));
            if (random.nextInt((int)(attack_pattern.rarity / (speed*1.5))) == 0) {
                this.moves.addAll(attack_pattern.moves);
            }
        }

        this.processCurrentMove(speed);

    }

}
