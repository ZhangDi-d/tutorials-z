package org.example.factorybean;

import org.example.entity.Car;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author dizhang
 * @date 2021-04-23
 */
public class CarFactoryBean implements FactoryBean<Car> {

    private String carInfo;

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    @Override
    public Car getObject() throws Exception {
        String[] strings = carInfo.split(",");
        if (strings.length<3) throw new RuntimeException("carInfo 参数错误");
        Car car = new Car();
        car.setMaxSpeed(Integer.parseInt(strings[0]));
        car.setBrand(strings[1]);
        car.setPrice(Double.parseDouble(strings[2]));
        return car;
    }

    @Override
    public Class<?> getObjectType() {
        return Car.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
