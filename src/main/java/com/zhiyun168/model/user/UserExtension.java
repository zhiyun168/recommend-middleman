package com.zhiyun168.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Haitao on 15/10/31.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserExtension implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long uid;

    private List<Map> sport;

    private List<String> sports;

    private Integer height;

    private Double weight;

    private Map university;

    private Map industry;

    private Map relationship;

    /**
     * 运动目标
     */
    private Integer target;

    /**
     * 目标体重
     */
    private Double target_weight;

    /**
     * 运动频率
     */
    private Integer sport_frequency;

    /**
     * 每日卡路里
     */
    private Integer daily_calorie;

    /**
     * 每日步数
     */
    private Integer daily_steps;
    /**
     * 健身计划
     */
    private Map fitness_plan;
    /**
     * 健身级别
     */
    private Map fitness_level;

    /**
     * 饮水
     */
    private Double drinks;

    /**
     * 睡眠
     */
    private Integer sleep_hours;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public List<String> getSports() {
        return sports;
    }

    public void setSports(List<String> sports) {
        this.sports = sports;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Map getRelationship() {
        return relationship;
    }

    public void setRelationship(Map relationship) {
        this.relationship = relationship;
    }

    public Map getIndustry() {
        return industry;
    }

    public void setIndustry(Map industry) {
        this.industry = industry;
    }

    public Map getUniversity() {
        return university;
    }

    public void setUniversity(Map university) {
        this.university = university;
    }

    public List<Map> getSport() {
        return sport;
    }

    public void setSport(List<Map> sport) {
        this.sport = sport;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Double getTarget_weight() {
        return target_weight;
    }

    public void setTarget_weight(Double target_weight) {
        this.target_weight = target_weight;
    }

    public Integer getSport_frequency() {
        return sport_frequency;
    }

    public void setSport_frequency(Integer sport_frequency) {
        this.sport_frequency = sport_frequency;
    }

    public Integer getDaily_calorie() {
        return daily_calorie;
    }

    public void setDaily_calorie(Integer daily_calorie) {
        this.daily_calorie = daily_calorie;
    }

    public Integer getDaily_steps() {
        return daily_steps;
    }

    public void setDaily_steps(Integer daily_steps) {
        this.daily_steps = daily_steps;
    }

    public Map getFitness_plan() {
        return fitness_plan;
    }

    public void setFitness_plan(Map fitness_plan) {
        this.fitness_plan = fitness_plan;
    }

    public Map getFitness_level() {
        return fitness_level;
    }

    public void setFitness_level(Map fitness_level) {
        this.fitness_level = fitness_level;
    }

    public Integer getSleep_hours() {
        return sleep_hours;
    }

    public void setSleep_hours(Integer sleep_hours) {
        this.sleep_hours = sleep_hours;
    }

    public Double getDrinks() {
        return drinks;
    }

    public void setDrinks(Double drinks) {
        this.drinks = drinks;
    }
}
