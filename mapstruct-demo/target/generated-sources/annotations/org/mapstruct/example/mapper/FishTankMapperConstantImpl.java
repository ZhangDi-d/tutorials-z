package org.mapstruct.example.mapper;

import javax.annotation.processing.Generated;
import org.mapstruct.example.dto.FishDto;
import org.mapstruct.example.dto.FishTankDto;
import org.mapstruct.example.dto.MaterialDto;
import org.mapstruct.example.dto.MaterialTypeDto;
import org.mapstruct.example.dto.WaterPlantDto;
import org.mapstruct.example.model.Fish;
import org.mapstruct.example.model.FishTank;
import org.mapstruct.example.model.MaterialType;
import org.mapstruct.example.model.WaterPlant;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-17T10:36:45+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
public class FishTankMapperConstantImpl implements FishTankMapperConstant {

    @Override
    public FishTankDto map(FishTank source) {
        if ( source == null ) {
            return null;
        }

        FishTankDto fishTankDto = new FishTankDto();

        fishTankDto.setFish( fishToFishDto( source.getFish() ) );
        fishTankDto.setMaterial( fishTankToMaterialDto( source ) );
        fishTankDto.setPlant( waterPlantToWaterPlantDto( source.getPlant() ) );
        fishTankDto.setName( source.getName() );

        return fishTankDto;
    }

    protected FishDto fishToFishDto(Fish fish) {
        if ( fish == null ) {
            return null;
        }

        FishDto fishDto = new FishDto();

        fishDto.setKind( fish.getType() );

        fishDto.setName( "Nemo" );

        return fishDto;
    }

    protected MaterialTypeDto materialTypeToMaterialTypeDto(MaterialType materialType) {
        if ( materialType == null ) {
            return null;
        }

        MaterialTypeDto materialTypeDto = new MaterialTypeDto();

        materialTypeDto.setType( materialType.getType() );

        return materialTypeDto;
    }

    protected MaterialDto fishTankToMaterialDto(FishTank fishTank) {
        if ( fishTank == null ) {
            return null;
        }

        MaterialDto materialDto = new MaterialDto();

        materialDto.setMaterialType( materialTypeToMaterialTypeDto( fishTank.getMaterial() ) );

        materialDto.setManufacturer( "MMM" );

        return materialDto;
    }

    protected WaterPlantDto waterPlantToWaterPlantDto(WaterPlant waterPlant) {
        if ( waterPlant == null ) {
            return null;
        }

        WaterPlantDto waterPlantDto = new WaterPlantDto();

        waterPlantDto.setKind( waterPlant.getKind() );

        return waterPlantDto;
    }
}
