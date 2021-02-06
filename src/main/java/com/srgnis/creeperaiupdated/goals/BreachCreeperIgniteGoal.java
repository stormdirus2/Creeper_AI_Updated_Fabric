package com.srgnis.creeperaiupdated.goals;

import com.srgnis.creeperaiupdated.CreeperAIUpdated;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.CreeperEntity;

import java.util.EnumSet;

public class BreachCreeperIgniteGoal extends Goal {
    private final CreeperEntity creeper;
    private LivingEntity target;

    public BreachCreeperIgniteGoal(CreeperEntity creeper) {
        this.creeper = creeper;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    public boolean canStart() {
        LivingEntity livingEntity = this.creeper.getTarget();
        return this.creeper.getFuseSpeed() > 0 || livingEntity != null && (this.creeper.squaredDistanceTo(livingEntity) < 9.0D || preBreakWall(livingEntity));
    }

    private boolean preBreakWall(LivingEntity livingEntity){
        if (breakWall(livingEntity)){
            System.out.println("PUM!!");
            Path p = creeper.getNavigation().findPathTo(livingEntity, 0);
            if( p!=null && p.getLength() > 6 ){
                creeper.getNavigation().startMovingAlong(p, 1.0);
                return false;
            } else{
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean breakWall(LivingEntity livingEntity) {
        float result = livingEntity.getPos().getY() - creeper.getPos().getY();
        return creeper.age > 60 && !creeper.isNavigating() && result*result < 25;
    }

    public void start() {
        this.creeper.getNavigation().stop();
        this.target = this.creeper.getTarget();
    }

    public void stop() {
        this.target = null;
    }

    public void tick() {
        if (this.target == null) {
            this.creeper.setFuseSpeed(-1);
        } else if (this.creeper.squaredDistanceTo(this.target) > 36.0D && !this.breakWall(target)) {
            this.creeper.setFuseSpeed(-1);
        } else {
            this.creeper.setFuseSpeed(1);
        }
    }
}
