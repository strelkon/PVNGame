package com.pvn;

/**
 * Created by strelkon on 2/9/2017.
 */
public class Player {
    private int[] priorities;
    private Strategy strategy;
    private int timeFrame;
    public String id;

    public Player(String id){
        this.strategy = new Strategy();
        this.priorities = new int[]{0, 0, 0, 0, 0, 0, 0};
        this.timeFrame = 5;
        this.id = id;
    }

    public void setPriorities(int[] choice) {
        for (int i=0; i<priorities.length; i++) {
            priorities[i] = choice[i];
        }
    }

    public void increasePriority(int p){
        priorities[p]++;
    }

    public void decreasePriority(int p){
        priorities[p]--;
    }

    public int getPriority(int p){
        return priorities[p];
    }

    public int [] getPriorities(){
        return priorities;
    }

    public void setStrategy(Strategy s){
        strategy = s;
    }

    public Strategy getStrategy(){
        return strategy;
    }

}
