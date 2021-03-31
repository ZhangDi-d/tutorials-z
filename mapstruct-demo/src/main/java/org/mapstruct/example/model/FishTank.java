/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.example.model;


/**
 * 鱼缸
 */
public class FishTank {

    private Fish fish;  // 鱼
    private WaterPlant plant;  //水生植物
    private String name; //名字
    private MaterialType material;  // 材质
    private Interior interior; //内部
    private WaterQuality quality;  //水质

    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }

    public WaterPlant getPlant() {
        return plant;
    }

    public void setPlant(WaterPlant plant) {
        this.plant = plant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MaterialType getMaterial() {
        return material;
    }

    public void setMaterial(MaterialType material) {
        this.material = material;
    }

    public Interior getInterior() {
        return interior;
    }

    public void setInterior(Interior interior) {
        this.interior = interior;
    }

    public WaterQuality getQuality() {
        return quality;
    }

    public void setQuality(WaterQuality quality) {
        this.quality = quality;
    }

}
